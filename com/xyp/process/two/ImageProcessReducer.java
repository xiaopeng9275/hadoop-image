package com.xyp.process.two;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.xyp.bundle.BundleFile;
import com.xyp.bundleIO.BundleWriter;

/**
 * 用了两种写法，第一种是把map和reduce写在一个类里面，第二种写法是把它们分开写，这种写法主要是为了做实验方便记录测试数据
 */
public class ImageProcessReducer extends
		Reducer<Text, LongWritable, Text, LongWritable> {
	private Configuration conf;
	private static BundleWriter bw = null;
	private static BundleFile bf = null;
	private static String temp = "";

	protected void setup(Context context) throws IOException, InterruptedException {
		temp = "/tttt";
		this.conf = context.getConfiguration();
		bf = new BundleFile(new Path("/processedImage.seq"), this.conf);
		bw = bf.getBundleWriter();
	}

	protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
		for (LongWritable s : values) {
			context.write(key, s);
		}
		FileSystem fs = FileSystem.get(this.conf);
		FileStatus[] status = fs.listStatus(new Path(temp));
		for (int i = 0; i < status.length; i++) {
			Path temp_path = status[i].getPath();
			bw.appendBundle(temp_path, this.conf);
		}
		bw.close();
	}

	public void cleanup(Context context) throws IOException, InterruptedException {
		removeDir(temp, this.conf);
		context.write(new Text("EndTime:"),new LongWritable(System.currentTimeMillis()));
	}

	private void removeDir(String path, Configuration conf) throws IOException {
		Path output_path = new Path(path);

		FileSystem fs = FileSystem.get(this.conf);
		if (fs.exists(output_path)) {
			fs.delete(output_path, true);
		}
		Path tmp = new Path("/tmp");
		if (fs.exists(tmp))
			fs.delete(tmp, true);
	}
}
