package de.inger.latex2png;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.inger.io.InputStreamToString;
import de.inger.latex2png.runner.DviPngExecutor;
import de.inger.latex2png.runner.LatexExecutor;

public class LatexToPng {

	private static Log log = LogFactory.getLog(LatexToPng.class);

	private String tempDirPath = System.getProperty("java.io.tmpdir");

	private LatexExecutor latexExecutor;
	private DviPngExecutor dviPngExecutor;

	public LatexToPng() {
		latexExecutor = new LatexExecutor();
		dviPngExecutor = new DviPngExecutor();
	}

	public void setTransparentBackground(boolean transparent) {
		dviPngExecutor.setTransparentBackground(transparent);
	}

	public void setResolution(int resolution) {
		dviPngExecutor.setResolution(resolution);
	}

	public void setTightImageSize(boolean tight) {
		dviPngExecutor.setTightImageSize(tight);
	}


	private void executeDviPng(File file) {
		String absolutePath = file.getAbsolutePath();
		String baseName = FilenameUtils.getBaseName(absolutePath);
		String fullPath = FilenameUtils.getFullPath(absolutePath);

		String dviAbsolutePath = FilenameUtils.concat(fullPath, baseName.concat(".dvi"));
		String pngAbsolutePath = FilenameUtils.concat(fullPath, baseName.concat(".png"));

		dviPngExecutor.setInputFile(dviAbsolutePath);
		dviPngExecutor.setOutputFile(pngAbsolutePath);

		log.debug(dviPngExecutor.getCommandLine());
		try {
			dviPngExecutor.execute();
		} catch (ExecuteException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

	private void executeLatex(File file) {
		latexExecutor.setFile(file);
		latexExecutor.setOutputDirectory(tempDirPath);
		log.debug(latexExecutor.getCommandLine());
		try {
			latexExecutor.execute();
		} catch (ExecuteException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

	private void createFile(File file, String tex) {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("template.tex");
		String template = null;
		try {
			template = InputStreamToString.read(is, "UTF-8");
		} catch (IOException e) {
			log.error(e);
			return;
		}

		template = template.replace("#content#", tex);
		template = template.replace("#backgroundcolor#", "255, 255, 255");
		template = template.replace("#textcolor#", "0, 0, 0");

		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(template);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			log.error(e);
		}
	}

	public byte[] convertString(String tex) throws IOException {
		File file = File.createTempFile("dil2p", ".tex");
		createFile(file, tex);
		executeLatex(file);
		executeDviPng(file);
		String pngFilePath = String.format("%s%s", tempDirPath, file.getName().replace(".tex", ".png"));
		byte[] picture = FileUtils.readFileToByteArray(new File(pngFilePath));

		return picture;
	}

	public LatexExecutor getLatexExecutor() {
		return latexExecutor;
	}

}
