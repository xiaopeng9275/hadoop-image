package com.xyp.method.self_define.ColorToGray;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xyp.entity.HImage;
import com.xyp.method.GenericAlgorithm;

public class ColorToGray implements GenericAlgorithm {
	public static int GRAY = 0;
	private HImage himage;

	public void run() {
		BufferedImage bf = this.himage.getBufferedImage();
		if (bf == null) {
			return;
		}
		int width = bf.getWidth();
		int height = bf.getHeight();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				Color c = new Color(bf.getRGB(j, i));
				int red = (int) (c.getRed() * 0.299D);
				int green = (int) (c.getGreen() * 0.587D);
				int blue = (int) (c.getBlue() * 0.114D);
				Color newColor = new Color(red + green + blue, red + green
						+ blue, red + green + blue);
				bf.setRGB(j, i, newColor.getRGB());
			}
		try {
			ImageIO.write(bf, "jpg", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HImage temp = new HImage(baos.toByteArray());

		this.himage = temp;
	}

	public HImage getProcessedImage() {
		return this.himage;
	}

	public void setHImage(HImage himage) {
		this.himage = himage;
	}
}
