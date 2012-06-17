package com.gigaspaces.poc.utils;

import java.util.List;

public interface FileMonitorListener {

	void onFileModified(List<String> newContent);
	
}
