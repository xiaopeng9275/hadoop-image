package test;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer.Option;

import com.xyp.bundle.BundleFile;
import com.xyp.bundleIO.BundleWriter;
import com.xyp.entity.HImage;


public class testBundleFileWrite {
	public static String uri = "hdfs://192.168.139.202:9000";

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf, "root");
		Path path = new Path("/temp.seq");
		Option opts[] = new Option[3];
		opts[0] = SequenceFile.Writer.file(path);
		opts[1] = SequenceFile.Writer.keyClass(LongWritable.class);
		opts[2] = SequenceFile.Writer.valueClass(BytesWritable.class);
		SequenceFile.Writer write = SequenceFile.createWriter(fs,conf, path,IntWritable.class,BytesWritable.class);
		// key.set(1);
		// value.set(new BytesWritable(himage.getImageBytes()));
		for (int i = 1; i <= 50; i++) {
			HImage himage = new HImage(new File("E:/images/"+i+".jpg"));
			write.append(new IntWritable(i),
					new BytesWritable(himage.getImageBytes()));
		}
		IOUtils.closeStream(write);
	}
}
