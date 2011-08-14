package de.inger.latex2png.runner;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;

import de.inger.latex2png.LoggingStreamHandler;

public class DviPngExecutor implements Executor {

	private String executable = "dvipng";
	private String executableName = executable;
	private File inputFile;
	private File outputFile;
	private long timeout = Executor.DEFAULT_TIMEOUT;
	private int resolution = 0;

	private boolean verbose = false;
	private boolean tightImageSize = false;
	private boolean trasparentBackground = false;

	@Override
	public void setExecutable(String executable) {
		this.executable = executable;
	}

	@Override
	public void execute() throws IOException {
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new LoggingStreamHandler(executableName));
		executor.setWatchdog(new ExecuteWatchdog(timeout));
		executor.execute(getCommandLine());
	}

	@Override
	public void setExecutableName(String name) {
		this.executableName = name;
	}

	@Override
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	@Override
	public CommandLine getCommandLine() {
		CommandLine line = new CommandLine(executable);
		if (verbose) {
			line.addArgument("-v");
		}
		if (resolution != 0) {
			line.addArgument(String.format("-D %d", resolution));
		}
		if (tightImageSize) {
			line.addArgument("-Ttight");
		}
		if (trasparentBackground) {
			line.addArgument("-bgtransparent");
		}
		if (outputFile != null) {
			line.addArgument(String.format("-o%s", outputFile.getAbsoluteFile()));
		}
		// line.addArgument("-fg rgb 1.0 0.5 0.0");
		line.addArgument(inputFile.getAbsolutePath());
		return line;
	}

	public void setInputFile(File file) {
		this.inputFile = file;
	}

	public void setInputFile(String path) {
		this.inputFile = new File(path);
	}

	public void setOutputFile(File file) {
		this.outputFile = file;
	}

	public void setOutputFile(String path) {
		this.outputFile = new File(path);
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	public void setTightImageSize(boolean tight) {
		this.tightImageSize = tight;
	}

	public void setTransparentBackground(boolean transparent) {
		this.trasparentBackground = transparent;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	@Override
	public void deleteCreatedFiles() {
		// TODO Auto-generated method stub

	}

	public File getPngFile() {
		return outputFile;
	}

	public void removeOutputFiles() {
		if (getPngFile().exists()) {
			getPngFile().delete();
		}
	}

}
