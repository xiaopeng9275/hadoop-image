package com.xyp.bundle;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import com.xyp.bundleIO.BundleReader;
import com.xyp.bundleIO.BundleWriter;
import com.xyp.bundleIO.impl.SequenceBundleWriter;
import com.xyp.util.Config;

/**
 * 图像簇，用来在管理多张小图像
 * @author 大象
 *
 */

public class BundleFile {
	private Path _filepath;
	private Configuration _conf;
	private int type=0;
	private String str;
	public static String SEQUENCE_FILE="seq";
	public BundleWriter bw;
	public BundleReader br;
	
	public BundleFile(Path temp_path, Configuration conf) {
		_filepath = temp_path;
		_conf = conf;
		str = temp_path.toString();
	}

	public BundleWriter getBundleWriter() {
		if(bw == null) {
			bw = new SequenceBundleWriter(this);
		}
		return bw;
	}

	public Config getHConfig() {
		return new Config( _conf,_filepath);
	}

	public Path getPath() {
		return _filepath;
	}

	public Configuration getConfiguration() {
		return _conf;
	}
}
