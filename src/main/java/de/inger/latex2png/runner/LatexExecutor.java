package de.inger.latex2png.runner;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.inger.latex2png.LoggingStreamHandler;

public class LatexExecutor implements Executor {

	private static Log log = LogFactory.getLog(LatexExecutor.class);

	private String executable = "latex";
	private String executableName = executable;
	private long timeout = Executor.DEFAULT_TIMEOUT;

	private File file;
	private String outputDirectory;

	private boolean quiet = true;

	public void setFile(File file) {
		log.trace("LatexRunner.setFile()");
		this.file = file;
	}

	public void setFile(String path) {
		log.trace("LatexRunner.SetFile()");
		file = new File(path);
	}

	public void setQuiet(boolean quiet) {
		log.trace("LatexRunner.setQuiet()");
		this.quiet = quiet;
	}

	public void setOutputDirectory(String directory) {
		log.trace("LatexRunner.setOutputDirectory()");
		this.outputDirectory = directory;
	}

	@Override
	public void setExecutable(String executable) {
		log.trace("LatexRunner.setExecutable()");
		this.executable = executable;
	}

	@Override
	public void execute() throws IOException {
		log.trace("LatexRunner.execute()");
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new LoggingStreamHandler(executableName));
		executor.setWatchdog(new ExecuteWatchdog(timeout));
		executor.execute(getCommandLine());
	}

	@Override
	public void setTimeout(long timeout) {
		log.trace("LatexRunner.setTimeout()");
		this.timeout = timeout;
	}

	@Override
	public void setExecutableName(String name) {
		log.trace("LatexRunner.setExecutableName()");
		this.executableName = name;
	}

	@Override
	public CommandLine getCommandLine() {
		log.trace("LatexRunner.getCommandLine()");
		CommandLine line = new CommandLine(executable);
		line.addArgument("--interaction=nonstopmode");
		if (outputDirectory != null) {
			line.addArgument(String.format("--output-directory=%s", outputDirectory));
		}
		if (quiet) {
			line.addArgument("--quiet");
		}
		line.addArgument(file.getAbsolutePath());

		return line;
	}

	@Override
	public void deleteCreatedFiles() {
		String filename = file.getName();
		log.debug(filename.lastIndexOf(".tex"));
	}

	public File getTexFile() {
		return file;
	}

	public File getLogFile() {
		String absolutePath = file.getAbsolutePath();
		int lastIndex = absolutePath.lastIndexOf(".tex");
		absolutePath = absolutePath.substring(0, lastIndex);
		absolutePath = absolutePath + ".log";
		return new File(absolutePath);
	}

	public File getAuxFile() {
		String absolutePath = file.getAbsolutePath();
		int lastIndex = absolutePath.lastIndexOf(".tex");
		absolutePath = absolutePath.substring(0, lastIndex);
		absolutePath = absolutePath + ".aux";
		return new File(absolutePath);
	}

	public File getDviFile() {
		String absolutePath = file.getAbsolutePath();
		int lastIndex = absolutePath.lastIndexOf(".tex");
		absolutePath = absolutePath.substring(0, lastIndex);
		absolutePath = absolutePath + ".dvi";
		return new File(absolutePath);
	}

	public void removeInputFiles() {
		if (getTexFile().exists()) {
			getTexFile().delete();
		}
	}

	public void removeLogFiles() {
		if (getLogFile().exists()) {
			getLogFile().delete();
		}
		if (getAuxFile().exists()) {
			getAuxFile().delete();
		}
	}

	public void removeOutputFiles() {
		if (getDviFile().exists()) {
			getDviFile().delete();
		}
	}

	public void removeAllFiles() {
		removeInputFiles();
		removeLogFiles();
		removeOutputFiles();
	}

}
