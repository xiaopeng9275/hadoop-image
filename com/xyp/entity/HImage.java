package com.xyp.entity;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;

public class HImage {

	public byte[] imagebytes = null;
	private BufferedImage bufferedImage = null;
	private Mat mat = null;
	private String ext = "jpg";
	private HImageHeader imageHeader = null;// 用来存储图像元信息

	public byte[] getImageBytes() {
		return imagebytes;
	}

	public HImage(byte[] by) {
		this.imagebytes = by;
	}

	public HImage(Mat mat) {
		this.mat = mat;
		byte[] data = new byte[mat.rows() * mat.cols() * (int) (mat.elemSize())];
		mat.get(0, 0, data);

		this.bufferedImage = new BufferedImage(mat.cols(), mat.rows(),
				BufferedImage.TYPE_BYTE_GRAY);
		this.bufferedImage.getRaster().setDataElements(0, 0, mat.cols(),
				mat.rows(), data);
		generateImageBytes(this.bufferedImage);
	}

	public HImage(BufferedImage bf) {
		generateImageBytes(bf);
	}

	public HImage(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			imagebytes = new byte[(int) file.length()];
			fis.read(imagebytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HImage(InputStream inputstream) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte buffer[] = new byte[1024];
			int read = 0;
			while ((read = inputstream.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, read);
			}
			baos.flush();
			imagebytes = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getBufferedImage() {
		if (bufferedImage == null) {
			generateBufferedImage();
		}
		return bufferedImage;
	}

	private void generateBufferedImage() {
		InputStream in = new ByteArrayInputStream(imagebytes);
		try {
			bufferedImage = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void generateImageBytes(BufferedImage bf) {
		if (this.imagebytes == null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bf, ext, baos);
				baos.flush();
				this.imagebytes = baos.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public HImageHeader getImageHeader() {
		return imageHeader;
	}

	public void setImageHeader(HImageHeader imageHeader) {
		this.imageHeader = imageHeader;
	}

	public static void main(String[] args) throws Exception {

	}
}
