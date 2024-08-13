package com.chason.common.utils;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ZxingUtils {

	/**
	 * 生成web版本二维码
	 * @param url 要生成二维码的路径
	 * @param response response对象
	 * @param width 二维码宽度
	 * @param height 二维码高度
	 * @throws IOException
	 */

	public static void getQRCode(String url, HttpServletResponse response, int width, int height)
	{
		if (url != null && !"".equals(url))
		{
			ServletOutputStream stream = null;
			try
			{
				stream = response.getOutputStream();
				QRCodeWriter writer = new QRCodeWriter();
				BitMatrix m = writer.encode(url, BarcodeFormat.QR_CODE, height, width);
				MatrixToImageWriter.writeToStream(m, "png", stream);
			}
			catch (WriterException e)
			{
				e.printStackTrace();

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (stream != null)
				{
					try
					{
						stream.flush();
						stream.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}

				}
			}
		}
	}

}
