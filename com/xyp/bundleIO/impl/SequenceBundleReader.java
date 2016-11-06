package com.xyp.bundleIO.impl;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;

import com.xyp.bundle.BundleFile;
import com.xyp.bundleIO.BundleReader;
import com.xyp.entity.HImage;
import com.xyp.util.Config;


public class SequenceBundleReader implements BundleReader {
	private SequenceFile.Reader _seqReader;
	private long _seqTotal = 0L;
	private Config _hConf = null;
	private BundleFile _file = null;
	private long _tempKey;
	private BytesWritable _tempImageBytes;
	private HImage _tempImage;

	public SequenceBundleReader(BundleFile file) {
		this._file = file;
		this._hConf = file.getHConfig();
		openToRead();
	}

	public void openToRead() {
		SequenceFile.Reader.Option[] opts = new SequenceFile.Reader.Option[1];
		opts[0] = SequenceFile.Reader.file(this._hConf.getPath());
		try {
			this._seqReader = new SequenceFile.Reader(
					this._hConf.getConfiguration(), opts);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SequenceBundleReader(Path path, Configuration conf) {
		this._hConf = new Config(conf, path);
		this._file = new BundleFile(path, conf);
		openToRead();
	}

	public boolean hasNext() {
		IntWritable key = new IntWritable();
		BytesWritable image = new BytesWritable();
		try {
			if (this._seqReader == null) {
				return false;
			}
			if (this._seqReader.next(key, image)) {
				this._tempKey = key.get();
				this._tempImageBytes = image;
				this._tempImage = new HImage(image.getBytes());
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public BytesWritable getValue() {
		return this._tempImageBytes;
	}

	public void close() {
		if (this._seqReader != null)
			try {
				this._seqReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public HImage next() {
		_seqTotal++;
        return _tempImage;
	}

	public long getReturnCount() {
		 return _seqTotal;
	}

	public Config getConfiguration() {
		 return _hConf;
	}

}
