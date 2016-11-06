package com.xyp.bundleIO.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;

import com.xyp.bundle.BundleFile;
import com.xyp.bundleIO.BundleWriter;
import com.xyp.entity.HImage;
import com.xyp.util.Config;


public class SequenceBundleWriter implements BundleWriter {
	private SequenceFile.Writer _seqWriter;
	private Config _hConf;
	private BundleFile _file;
	private int _seqTotal = 0;

	public SequenceBundleWriter(BundleFile file) {
		this._file = file;
		this._hConf = file.getHConfig();
		openToWrite();
	}

	public void openToWrite() {
		SequenceFile.Writer.Option[] opts = new SequenceFile.Writer.Option[3];
		opts[0] = SequenceFile.Writer.file(this._hConf.getPath());
		opts[1] = SequenceFile.Writer.keyClass(IntWritable.class);
		opts[2] = SequenceFile.Writer.valueClass(BytesWritable.class);
		try {
			this._seqWriter = SequenceFile.createWriter(
					this._hConf.getConfiguration(), opts);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this._seqTotal = 1;
	}

	public void appendImage(HImage himage) {
		try {
			if (himage.getImageBytes() != null) {
				this._seqWriter.append(new IntWritable(this._seqTotal),
						new BytesWritable(himage.getImageBytes()));
				this._seqTotal += 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void appendImage(InputStream inputstream) {
		HImage temp = new HImage(inputstream);
		appendImage(temp);
	}

	public void appendImage(File file) {
		HImage temp = new HImage(file);
		appendImage(temp);
	}
	
	public Config getConfiguration() {
		return this._hConf;
	}

	public long getImageCount() {
		return this._seqTotal;
	}

	public void close() {
		if (this._seqWriter != null)
			try {
				this._seqWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void appendBundle(Path path, Configuration conf) {
		SequenceBundleReader reader = new SequenceBundleReader(path, conf);
		try {
			while (reader.hasNext()) {
				this._seqWriter.append(new IntWritable(this._seqTotal),
						reader.getValue());

				this._seqTotal += 1;
			}
			reader.close();
			FileSystem fs = FileSystem.get(conf);
			fs.delete(path, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void appendBundle(BundleFile file) {
		appendBundle(file.getPath(), file.getConfiguration());
	}

	public BundleFile getBundleFile() {
		return this._file;
	}

}
