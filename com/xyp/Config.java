package com.xyp.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Config {
	private Configuration _conf;
	private Path _path;
	private FileSystem _fs;
	
	public Config() {
		super();
	}

	public Config(Configuration _conf, Path _path) {
		this._conf = _conf;
		this._path = _path;
		generate();
	}
	
	public Config(Configuration _conf, String _path) {
		this._conf = _conf;
		this._path = new Path(_path);
	}
	
	private void generate() {
		try {
			this._fs = FileSystem.get(this._path.toUri(), this._conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Path getPath() {
		return _path;
	}

	public Configuration getConfiguration() {
		return _conf;
	}

	public FileSystem getFileSystem() {
		return _fs;
	}
	
	
}
