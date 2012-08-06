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
	
	private final String logfilename;
	private final String indexfilename;
	private final FileMonitorListener listener;
	private FileModificationPolling fileModificationPolling;
	private Thread pollingThread;
	private volatile boolean closed;

	public FileMonitor(String logfilename, String indexfilename, FileMonitorListener listener) throws IOException {
		this.logfilename = logfilename;
		if (!new File(logfilename).exists())
			throw new IOException("file: " + logfilename + " does not exist");
		this.indexfilename = indexfilename;
		new File(logfilename).createNewFile();
		this.listener = listener;
		this.fileModificationPolling = new FileModificationPolling();
		this.pollingThread = new Thread(fileModificationPolling);
		this.pollingThread.start();
		logger.info("Monitoring [" + logfilename + "] for changes");
	}

	public void close() {
		closed = true;
	}
	
	private class FileModificationPolling implements Runnable {

		private long previousLength;

		public FileModificationPolling() {
			if (isEmptyIndexFile()) {
				logger.info("index file doesn't exist. creating ...");
				File f = new File(logfilename);
				previousLength = f.length();
				updateIndexFile(previousLength);
			}
		}
		
		@Override
		public void run() {
			while (!closed) {
				try {
					previousLength = readFromIndexFile();
					File f = new File(logfilename);
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
							logger.info(content.size() + " lines added to file [" + logfilename + "]");
							listener.onFileModified(content);
							previousLength = f.length();
							updateIndexFile(previousLength);
						}
					Thread.sleep(100);
				} catch (Exception e) {
					Logger.getLogger("FileMonitor").warning("Exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
	}
	
	void updateIndexFile(long idx) {
	    try {
	    	logger.config("updating index file: " + idx);
	        java.io.BufferedWriter out = new java.io.BufferedWriter(new java.io.FileWriter(indexfilename));
	        out.write(Long.toString(idx));
	        out.close();
	    } catch (IOException e) {
	    	throw new RuntimeException("failed to update index file "+indexfilename);
	    }

	}
	
	long readFromIndexFile() {
		String indexStr;
		try {
	        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(indexfilename));
	        indexStr = in.readLine();
	        in.close();
	    	logger.config("reading from index file: " + indexStr);
	    } catch (IOException e) {
	    	throw new RuntimeException("failed to read from index file "+indexfilename);
	    }
	    long idx = Long.parseLong(indexStr);
	    return idx;
	}
	
	boolean isEmptyIndexFile() {
		return (new File(indexfilename)).length() == 0;
	}

}
