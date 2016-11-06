package com.xyp.bundleIO;

import com.xyp.entity.HImage;
import com.xyp.util.Config;


public interface BundleReader {
void openToRead();
	
	boolean hasNext();
	
	HImage next();
	
	long getReturnCount();
	
	Config getConfiguration();
	
	void close();
}
