package de.inger.latex2png.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.inger.io.InputStreamToString;

public class LatexTemplate {

	private String content;

	public void setContent(String content) {
		this.content = content;
	}

	public void setContentInputStream(InputStream inputStream) throws IOException {
		this.content = InputStreamToString.read(inputStream);
	}

	public void setContentInputStream(InputStream inputStream, String charSetName) throws IOException {
		this.content = InputStreamToString.read(inputStream, charSetName);
	}

	public void setContentFile(File file) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		this.content = InputStreamToString.read(fileInputStream);

	}

	public void setContentFile(File file, String charSetName) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		this.content = InputStreamToString.read(fileInputStream, charSetName);
	}

	public String getContent() {
		return content;
	}

}
