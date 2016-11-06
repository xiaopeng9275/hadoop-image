package com.xyp.method.self_define.CannyEdgeFilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import com.xyp.entity.HImage;
import com.xyp.method.GenericAlgorithm;

public class CannyEdgeFilter extends AbstractBufferedImageOp implements
		GenericAlgorithm {
	private HImage himage;
	private float gaussianKernelRadius = 2.0F;
	private int gaussianKernelWidth = 16;
	private float lowThreshold = 2.5F;
	private float highThreshold = 7.5F;
	private int width;
	private int height;
	private float[] data;
	private float[] magnitudes;

	public void run() {
		BufferedImage src = this.himage.getBufferedImage();
		if (src == null) {
			return;
		}
		BufferedImage dest = createCompatibleDestImage(src, null);
		this.width = src.getWidth();
		this.height = src.getHeight();

		int[] inPixels = new int[this.width * this.height];
		int[] outPixels = new int[this.width * this.height];
		getRGB(src, 0, 0, this.width, this.height, inPixels);
		int index = 0;
		for (int row = 0; row < this.height; row++) {
			int ta = 0;
			int tr = 0;
			int tg = 0;
			int tb = 0;
			for (int col = 0; col < this.width; col++) {
				index = row * this.width + col;
				ta = inPixels[index] >> 24 & 0xFF;
				tr = inPixels[index] >> 16 & 0xFF;
				tg = inPixels[index] >> 8 & 0xFF;
				tb = inPixels[index] & 0xFF;
				int gray = (int) (0.299D * tr + 0.587D * tg + 0.114D * tb);
				inPixels[index] = (ta << 24 | gray << 16 | gray << 8 | gray);
			}
		}

		float[][] kernel = new float[this.gaussianKernelWidth][this.gaussianKernelWidth];
		for (int x = 0; x < this.gaussianKernelWidth; x++) {
			for (int y = 0; y < this.gaussianKernelWidth; y++) {
				kernel[x][y] = gaussian(x, y, this.gaussianKernelRadius);
			}
		}

		int krr = (int) this.gaussianKernelRadius;
		for (int row = 0; row < this.height; row++) {
			for (int col = 0; col < this.width; col++) {
				index = row * this.width + col;
				double weightSum = 0.0D;
				double redSum = 0.0D;
				for (int subRow = -krr; subRow <= krr; subRow++) {
					int nrow = row + subRow;
					if ((nrow >= this.height) || (nrow < 0)) {
						nrow = 0;
					}
					for (int subCol = -krr; subCol <= krr; subCol++) {
						int ncol = col + subCol;
						if ((ncol >= this.width) || (ncol <= 0)) {
							ncol = 0;
						}
						int index2 = nrow * this.width + ncol;
						int tr1 = inPixels[index2] >> 16 & 0xFF;
						redSum += tr1 * kernel[(subRow + krr)][(subCol + krr)];
						weightSum += kernel[(subRow + krr)][(subCol + krr)];
					}
				}
				int gray = (int) (redSum / weightSum);
				outPixels[index] = gray;
			}

		}

		this.data = new float[this.width * this.height];
		this.magnitudes = new float[this.width * this.height];
		for (int row = 0; row < this.height; row++) {
			for (int col = 0; col < this.width; col++) {
				index = row * this.width + col;

				float xg = (getPixel(outPixels, this.width, this.height, col,
						row + 1)
						- getPixel(outPixels, this.width, this.height, col, row)
						+ getPixel(outPixels, this.width, this.height, col + 1,
								row + 1) - getPixel(outPixels, this.width,
						this.height, col + 1, row)) / 2.0F;
				float yg = (getPixel(outPixels, this.width, this.height, col,
						row)
						- getPixel(outPixels, this.width, this.height, col + 1,
								row)
						+ getPixel(outPixels, this.width, this.height, col,
								row + 1) - getPixel(outPixels, this.width,
						this.height, col + 1, row + 1)) / 2.0F;

				this.data[index] = hypot(xg, yg);
				if (xg == 0.0F) {
					if (yg > 0.0F) {
						this.magnitudes[index] = 90.0F;
					}
					if (yg < 0.0F)
						this.magnitudes[index] = -90.0F;
				} else if (yg == 0.0F) {
					this.magnitudes[index] = 0.0F;
				} else {
					this.magnitudes[index] = ((float) (Math.atan(yg / xg) * 180.0D / 3.141592653589793D));
				}

				this.magnitudes[index] += 90.0F;
			}
		}

		Arrays.fill(this.magnitudes, 0.0F);
		for (int row = 0; row < this.height; row++) {
			for (int col = 0; col < this.width; col++) {
				index = row * this.width + col;
				float angle = this.magnitudes[index];
				float m0 = this.data[index];
				this.magnitudes[index] = m0;
				if ((angle >= 0.0F) && (angle < 22.5D)) {
					float m1 = getPixel(this.data, this.width, this.height,
							col - 1, row);
					float m2 = getPixel(this.data, this.width, this.height,
							col + 1, row);
					if ((m0 < m1) || (m0 < m2))
						this.magnitudes[index] = 0.0F;
				} else if ((angle >= 22.5D) && (angle < 67.5D)) {
					float m1 = getPixel(this.data, this.width, this.height,
							col + 1, row - 1);
					float m2 = getPixel(this.data, this.width, this.height,
							col - 1, row + 1);
					if ((m0 < m1) || (m0 < m2))
						this.magnitudes[index] = 0.0F;
				} else if ((angle >= 67.5D) && (angle < 112.5D)) {
					float m1 = getPixel(this.data, this.width, this.height,
							col, row + 1);
					float m2 = getPixel(this.data, this.width, this.height,
							col, row - 1);
					if ((m0 < m1) || (m0 < m2))
						this.magnitudes[index] = 0.0F;
				} else if ((angle >= 112.5D) && (angle < 157.5D)) {
					float m1 = getPixel(this.data, this.width, this.height,
							col - 1, row - 1);
					float m2 = getPixel(this.data, this.width, this.height,
							col + 1, row + 1);
					if ((m0 < m1) || (m0 < m2))
						this.magnitudes[index] = 0.0F;
				} else if (angle >= 157.5D) {
					float m1 = getPixel(this.data, this.width, this.height,
							col, row + 1);
					float m2 = getPixel(this.data, this.width, this.height,
							col, row - 1);
					if ((m0 < m1) || (m0 < m2)) {
						this.magnitudes[index] = 0.0F;
					}
				}
			}
		}

		float min = 255.0F;
		float max = 0.0F;
		for (int i = 0; i < this.magnitudes.length; i++)
			if (this.magnitudes[i] != 0.0F) {
				min = Math.min(min, this.magnitudes[i]);
				max = Math.max(max, this.magnitudes[i]);
			}
		System.out.println("Image Max Gradient = " + max + " Mix Gradient = "
				+ min);

		Arrays.fill(this.data, 0.0F);
		int offset = 0;
		for (int row = 0; row < this.height; row++) {
			for (int col = 0; col < this.width; col++) {
				if ((this.magnitudes[offset] >= this.highThreshold)
						&& (this.data[offset] == 0.0F)) {
					edgeLink(col, row, offset, this.lowThreshold);
				}
				offset++;
			}

		}

		for (int i = 0; i < inPixels.length; i++) {
			int gray = clamp((int) this.data[i]);
			outPixels[i] = (gray > 0 ? -1 : -16777216);
		}
		setRGB(dest, 0, 0, this.width, this.height, outPixels);
		this.himage = new HImage(dest);
	}

	private float hypot(float x, float y) {
		return (float) Math.hypot(x, y);
	}

	private int getPixel(int[] inPixels, int width, int height, int col, int row) {
		if ((col < 0) || (col >= width))
			col = 0;
		if ((row < 0) || (row >= height))
			row = 0;
		int index = row * width + col;
		return inPixels[index];
	}

	private float getPixel(float[] input, int width, int height, int col,
			int row) {
		if ((col < 0) || (col >= width))
			col = 0;
		if ((row < 0) || (row >= height))
			row = 0;
		int index = row * width + col;
		return input[index];
	}

	private float gaussian(int x, int y, float sigma) {
		float xDistance = x * x;
		float yDistance = y * y;
		float sigma22 = 2.0F * sigma * sigma;
		float sigma22PI = 3.141593F * sigma22;
		return (float) Math.exp(-(xDistance + yDistance) / sigma22) / sigma22PI;
	}

	private void edgeLink(int x1, int y1, int index, float threshold) {
		int x0 = x1 == 0 ? x1 : x1 - 1;
		int x2 = x1 == this.width - 1 ? x1 : x1 + 1;
		int y0 = y1 == 0 ? y1 : y1 - 1;
		int y2 = y1 == this.height - 1 ? y1 : y1 + 1;

		this.data[index] = this.magnitudes[index];
		for (int x = x0; x <= x2; x++)
			for (int y = y0; y <= y2; y++) {
				int i2 = x + y * this.width;
				if (((y != y1) || (x != x1)) && (this.data[i2] == 0.0F)
						&& (this.magnitudes[i2] >= threshold)) {
					edgeLink(x, y, i2, threshold);
					return;
				}
			}
	}

	public int clamp(int value) {
		return value < 0 ? 0 : value > 255 ? 255 : value;
	}

	public HImage getProcessedImage() {
		return this.himage;
	}

	public void setHImage(HImage himage) {
		this.himage = himage;
	}

	public static void main(String[] args) throws Exception {
		HImage himage = new HImage(new File("E:/images/2.jpg"));
		CannyEdgeFilter c = new CannyEdgeFilter();
		c.setHImage(himage);
		c.run();
		HImage cimage = c.getProcessedImage();
		FileOutputStream fos = new FileOutputStream("E:/canny.jpg");
		fos.write(cimage.imagebytes);
		fos.close();
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		// TODO Auto-generated method stub
		return null;
	}
}
