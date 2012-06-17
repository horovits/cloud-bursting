package com.gigaspaces.poc.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FileMonitor {

	private static final Logger logger = Logger.getLogger("FileMonitor");
	
	private final String filename;
	private final FileMonitorListener listener;
	private FileModificationPolling fileModificationPolling;
	private Thread pollingThread;
	private volatile boolean closed;

	public FileMonitor(String filename, FileMonitorListener listener) throws IOException {
		this.filename = filename;
		if (!new File(filename).exists())
			throw new IOException("file: " + filename + " does not exist");
		this.listener = listener;
		this.fileModificationPolling = new FileModificationPolling();
		this.pollingThread = new Thread(fileModificationPolling);
		this.pollingThread.start();
		logger.info("Monitoring [" + filename + "] for changes");
	}

	public void close() {
		closed = true;
	}
	
	private class FileModificationPolling implements Runnable {

		private long previousLength;

		public FileModificationPolling() {
			File f = new File(filename);
			previousLength = f.length();
		}
		
		@Override
		public void run() {
			while (!closed) {
				try {
					File f = new File(filename);
					if (previousLength < f.length())
						if (listener != null) {
							RandomAccessFile raf = new RandomAccessFile(f, "r");
							byte[] bytes = new byte[(int)(f.length() - previousLength)];
							raf.seek(previousLength);
							raf.readFully(bytes);
							raf.close();
							BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
							String line = null;
							List<String> content = new ArrayList<String>();
							while ((line = br.readLine()) != null) {
								content.add(line);
							}
							br.close();
							logger.info(content.size() + " lines added to file [" + filename + "]");
							listener.onFileModified(content);
						}
					previousLength = f.length();
					Thread.sleep(100);
				} catch (Exception e) {
					Logger.getLogger("FileMonitor").warning("Exception: " + e.getMessage());
				}
			}
		}
		
	}
	

}
