package com.xyp.method.self_define.NarrowImage;

import java.awt.image.BufferedImage;

import com.xyp.entity.HImage;
import com.xyp.method.GenericAlgorithm;

public class NarrowImage implements GenericAlgorithm {
	private HImage himage;

	public void run() {
		BufferedImage bf = this.himage.getBufferedImage();
		if (bf == null) {
			return;
		}

		int width = bf.getWidth();
		int height = bf.getHeight();

		float resizeTimes = 0.5F;

		int toWidth = (int) (width * resizeTimes);
		int toHeight = (int) (height * resizeTimes);

		BufferedImage result = new BufferedImage(toWidth, toHeight, 1);

		result.getGraphics().drawImage(
				bf.getScaledInstance(toWidth, toHeight, 4), 0, 0, null);
		this.himage = new HImage(result);
	}

	public HImage getProcessedImage() {
		return this.himage;
	}

	@Override
	public void setHImage(HImage himage) {
	}
}
