package com.xyp.process.two;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;

import com.xyp.bundle.BundleFile;
import com.xyp.bundleIO.BundleWriter;
import com.xyp.entity.HImage;
import com.xyp.method.GenericAlgorithm;
import com.xyp.method.selef_define.ColorToGray.ColorToGray;
import com.xyp.util.Random;
/**
 * 用了两种写法，第一种是把map和reduce写在一个类里面，第二种写法是把它们分开写，这种写法主要是为了做实验方便记录测试数据
 */
public class ImageProcessMapper extends Mapper<IntWritable, BytesWritable, Text, LongWritable> {
	private static Configuration conf;
	private static BundleFile bf;
	private static BundleWriter bw = null;

	protected void setup(Context context) throws IOException, InterruptedException {
		conf = context.getConfiguration();
		String temp = "/tttt";
		createDir(temp, conf);
		bf = new BundleFile(new Path(temp + "/" + Random.random() + "tempImage.seq"), conf);
		bw = bf.getBundleWriter();
		context.write(new Text("StartTime:"), new LongWritable(System.currentTimeMillis()));
	}

	private void createDir(String path, Configuration conf) throws IOException {
		Path output_path = new Path(path);

		FileSystem fs = FileSystem.get(conf);

		if (!fs.exists(output_path))
			fs.mkdirs(output_path);
	}

	protected void map(IntWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
		if (value != null) {
			HImage him = new HImage(value.getBytes());
			him = runNormal(him);
			bw.appendImage(him);
		}
	}

	private HImage runNormal(HImage him) {
		GenericAlgorithm pi = new ColorToGray();
		pi.setHImage(him);
		pi.run();
		return pi.getProcessedImage();
	}

	protected void cleanup(Context context) throws IOException, InterruptedException {
		bw.close();
	}
}
