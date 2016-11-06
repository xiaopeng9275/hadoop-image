package com.xyp.bundleIO;


import java.io.File;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import com.xyp.bundle.BundleFile;
import com.xyp.entity.HImage;
import com.xyp.util.Config;


public interface BundleWriter {
	void openToWrite();

	void appendImage(HImage himage);

	void appendImage(InputStream inputstream);

	void appendImage(File file);

	void appendBundle(Path path, Configuration conf);

	Config getConfiguration();

	long getImageCount();

	void close();

	BundleFile getBundleFile();
}
