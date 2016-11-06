package test;

import java.io.File;
import java.io.FileOutputStream;

import com.xyp.entity.HImage;
import com.xyp.method.GenericAlgorithm;
import com.xyp.method.selef_define.ColorToGray.ColorToGray;

public class testMethod {
	public static void main(String[] args) throws Exception {
		for (int i = 1; i <= 25000; i++) {
			File f = new File("D:/迅雷下载/mirflickr25k/mirflickr/im" + i + ".jpg");
			HImage h = new HImage(f);
			GenericAlgorithm pi = new ColorToGray();
			pi.setHImage(h);
			pi.run();
			HImage h1 = pi.getProcessedImage();
			FileOutputStream fos = new FileOutputStream("E:/image/" + i
					+ ".jpg");
			fos.write(h1.imagebytes);
			fos.close();
		}
	}
}
