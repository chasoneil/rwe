package com.chason.common.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 添加水印的帮助类
 */
public class WaterMarkUtils {
	/**
	 * 给图片添加“图片水印”
	 *
	 * @param iconPath
	 *            作为水印的图片路径
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 */
	public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath) {
		markImageByIcon(iconPath, srcImgPath, targerPath, null);
	}

	/**
	 * 给图片添加“图片水印”、可设置图片水印旋转角度
	 *
	 * @param iconPath
	 *            作为水印的图片路径
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 * @param degree
	 *            水印图片旋转角度
	 */
	public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath, Integer degree) {
		OutputStream os = null;
		try {
			// 1、源图片
			Image srcImg = ImageIO.read(new File(srcImgPath));
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);

			// 2、水印图象：水印一般为gif或者png的，这样可设置透明度
			ImageIcon imgIcon = new ImageIcon(iconPath);
			Image img = imgIcon.getImage();// 得到Image对象。

			// 3、画笔对象
			Graphics2D g2d = buffImg.createGraphics();
			// ---------- 增加下面的代码使得背景透明 -----------------
			buffImg = g2d.getDeviceConfiguration().createCompatibleImage(srcImg.getWidth(null), srcImg.getHeight(null),
					Transparency.TRANSLUCENT);
			g2d.dispose();
			g2d = buffImg.createGraphics();
			// ---------- 背景透明代码结束 -----------------

			// 设置对线段的锯齿状边缘处理
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);// 设置对线段的锯齿状边缘处理

			g2d.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH),
					0, 0, null);

			// 设置水印旋转
			if (null != degree) {
				g2d.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}

			// 透明度
			float alpha = 1.0f;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

			// 表示水印图片的位置
			g2d.drawImage(img, 90, 92, null);

			// 4、释放资源
			g2d.dispose();

			// 5、生成图片
			os = new FileOutputStream(targerPath);
			ImageIO.write(buffImg, "PNG", os);

			System.out.println("图片完成添加Icon印章");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (null != os)
					os.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 辅助方法:获得水印图片的宽
	 *
	 * @param waterMarkContent
	 * @param g
	 * @return
	 */
	public static int getWatermarkLength(String waterMarkContent, Graphics2D g)
	{
		return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
	}

	/**
	 * 给图片添加“文字水印”、可设置旋转角度
	 *
	 * @param logoText水印文字
	 * @param srcImgPath源图片路径
	 * @param targerPath目标图片路径
	 * @param degree水印图片旋转角度
	 * @param width宽度(与左相比)
	 * @param height高度(与顶相比)
	 * @param clarity透明度(小于1的数)越接近0越透明
	 */
	public static void waterMarkByText(String logoText, String srcImgPath, String targerPath, Integer degree,
			Integer width, Integer height, Float clarity) {

		@SuppressWarnings("unused")
		InputStream is = null;
		OutputStream os = null;
		try {
			/** 1、源图片 */
			Image srcImg = ImageIO.read(new File(srcImgPath));
			// 创建BufferedImage对象:new BufferedImage(width,
			// height,BufferedImage.TYPE_INT_RGB);
			// 其实就是以源图片的宽度为宽，以源图片的长度为长
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);

			/** 2、得到画笔对象 */
			Graphics2D g2d = buffImg.createGraphics();

			// ---------- 增加下面的代码使得背景透明 -----------------
			buffImg = g2d.getDeviceConfiguration().createCompatibleImage(srcImg.getWidth(null), srcImg.getHeight(null),
					Transparency.TRANSLUCENT);
			g2d.dispose();
			g2d = buffImg.createGraphics();
			// ---------- 背景透明代码结束 -----------------

			// 设置对线段的锯齿状边缘处理
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			// 把源图片写入
			g2d.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH),
					0, 0, null);

			// 设置水印旋转
			if (null != degree) {
				g2d.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}

			// 设置颜色
			g2d.setColor(Color.red);

			// 设置 Font
			g2d.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 33));

			// 设置透明度:1.0f为透明度 ，值从0-1.0，依次变得不透明
			float alpha = clarity;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));// 透明度设置 结束
			// 文字内容，文字在图片上的坐标位置(x,y)

			//设置水印的坐标 如果没传宽高，则默认左下角
			if(width == null)
			{
				width = 10;
			}

			if(height == null)
			{
				height = buffImg.getHeight() - 10;
			}

			g2d.drawString(logoText, width, height);

			// 释放资源
			g2d.dispose();

			/** 3、生成图片 */
			os = new FileOutputStream(targerPath);
			ImageIO.write(buffImg, "png", os);

			System.out.println("添加水印文字完成!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
