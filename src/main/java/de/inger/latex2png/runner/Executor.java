package de.inger.latex2png.runner;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;


public interface Executor {
	
	long DEFAULT_TIMEOUT = 6000;
	
	void setExecutable(String executable);
	
	void execute() throws IOException;
	
	void setExecutableName(String name);
	
	void setTimeout(long timeout);
	
	CommandLine getCommandLine();
	
	void deleteCreatedFiles();

}
