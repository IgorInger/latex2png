package de.inger.latex2png;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggingStreamHandler implements ExecuteStreamHandler {

	private Log log = LogFactory.getLog(LoggingStreamHandler.class);

	private Log processLog = LogFactory.getLog("de.inger.latex2png.process");

	private InputStream errStream;
	private InputStream outStream;
	private OutputStream inStream;

	private static final int DEFAULT_BUFFER_SIZE = 1024;

	public LoggingStreamHandler() {
		log.trace("LoggingStreamHandler.LoggingStreamHandler()");
	}

	public LoggingStreamHandler(String applicationName) {
		log.trace("LoggingStreamHandler.LoggingStreamHandler()");
		processLog = LogFactory.getLog("de.inger.latex2png.process." + applicationName);
	}

	@Override
	public void setProcessErrorStream(InputStream stream) throws IOException {
		log.trace("LoggingStreamHandler.setProcessErrorStream()");
		errStream = stream;
	}

	@Override
	public void setProcessInputStream(OutputStream stream) throws IOException {
		log.trace("LoggingStreamHandler.setProcessInputStream()");
		inStream = stream;
	}

	@Override
	public void setProcessOutputStream(InputStream stream) throws IOException {
		log.trace("LoggingStreamHandler.setProcessOutputStream()");
		outStream = stream;
	}

	@Override
	public void start() throws IOException {
		log.trace("LoggingStreamHandler.start()");
		byte[] bytes;
		
		bytes = logInputStream(errStream);
		if (bytes.length > 0) {
			processLog.warn(new String(bytes));
		}		
	}

	private byte[] logInputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
		int length;
		while ((length = inputStream.read(bytes, 0, DEFAULT_BUFFER_SIZE)) != -1) {
			byteArray.write(bytes, 0, length);
		}
		return byteArray.toByteArray();
	}

	@Override
	public void stop() {
		log.trace("LoggingStreamHandler.stop()");
		try {
			if (errStream != null) {
				errStream.close();
			}
		} catch (IOException e) {
			log.error(e);
		}
		try {
			if (outStream != null) {
				outStream.close();
			}
		} catch (IOException e) {
			log.error(e);
		}
		try {
			if (inStream != null) {
				inStream.close();
			}
		} catch (IOException e) {
			log.error(e);
		}
	}

}
