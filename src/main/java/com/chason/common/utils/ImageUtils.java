package com.chason.common.utils;

import org.springframework.web.multipart.MultipartFile;
import com.chason.common.utils.gif.AnimatedGifEncoder;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @date 2017/12/18.
 */
public class ImageUtils {
    /***
     * 剪裁图片
     * @param file 图片
     * @param x  起点横坐标
     * @param y  纵坐标
     * @param w  长
     * @param h  高
     * @throws IOException
     * @date
     */
    public static BufferedImage cutImage(MultipartFile file, int x, int y, int w, int h,String prefix) {

        @SuppressWarnings("rawtypes")
		Iterator iterator = ImageIO.getImageReadersByFormatName(prefix);
        try {
            ImageReader reader = (ImageReader)iterator.next();
            //转换成输入流
            InputStream in = file.getInputStream();
            ImageInputStream iis = ImageIO.createImageInputStream(in);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, w,h);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0,param);
            return bi;
        } catch (Exception ignored) {
        }
        return null;
    }
    /***
     * 图片旋转指定角度
     * @param bufferedimage 图像
     * @param degree      角度
     * @return
     * @date
     */
    public static BufferedImage rotateImage(BufferedImage bufferedimage, int degree) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.setPaint(Color.WHITE);
        graphics2d.fillRect(0, 0, w, h);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0,Color.WHITE, null);
        graphics2d.dispose();
        return img;
    }

    /**
     * 把3张图片压缩成一张gif
     * @param urls 三张图片url的字符串数组
     * @param dirName rlId+/img
     * */
    public static String makeGif(String dirName, ArrayList<String> urls,String devName)
    {
    	String pic1Url = urls.get(0);
    	String pic2Url = urls.get(1);
    	String pic3Url = urls.get(2);
    	String strGifName = "";
    	try
    	{
            BufferedImage src  = ImageIO.read(new File(pic1Url)); // 读入文件
            BufferedImage src1 = ImageIO.read(new File(pic2Url)); // 读入文件
            BufferedImage src2 = ImageIO.read(new File(pic3Url)); // 读入文件

            String dir = "d:/var/uploaded_files/"+dirName;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            strGifName = sdf.format(new Date());
            File file = new File(dir);
            if(!file.exists())
            {
            	file.mkdirs();
            }

            System.out.println("start making gif...");
            AnimatedGifEncoder e = new AnimatedGifEncoder();
                       e.setRepeat(0);
                       e.start("d:/var/uploaded_files/"+ dirName + "/" + devName + "." + strGifName + ".gif" );
                       e.setDelay(2000);   //2 sec per frame
                       e.addFrame(src);
                       e.setDelay(2000);
                       e.addFrame(src1);
                       e.setDelay(2000);
                       e.addFrame(src2);
                       e.finish();

             //制作完成删除原图片
             file = new File(pic1Url);
             if(file.exists())
             {
            	 file.delete();
             }
             file = new File(pic2Url);
             if(file.exists())
             {
            	 file.delete();
             }
             file = new File(pic3Url);
             if(file.exists())
             {
            	 file.delete();
             }
        }
    	catch(IOException e)
    	{
    		e.printStackTrace();
            return null;
        }

        return strGifName;
    }




    /**
     * 检查上传的截图完整性
     * @param dirPath 截图的上传文件夹
     * @return Map<String, String>
     */
    public static Map<String, String> handlePic(String dirPath, String rlId)
    {
    	File file = new File(dirPath);

    	String[] pics = file.list();
    	Map<String, String> logUrls = new HashMap<>();

    	if(pics == null || pics.length < 3)  //当文件夹中少于三张图片的时候没有压缩的必要
    	{
    	    System.err.println("pics is not enough！");
    	    logUrls.put("NO3PICS", rlId);
    		return logUrls;
    	}

    	String picSort = "";   //截图的顺序，用来判断是不是一组截屏结束
    	String devName = "";   //设备名称
    	String result = "";
    	ArrayList<String> urls = new ArrayList<>();

    	for(int i = 0; i < pics.length; i++)
    	{
    		picSort = pics[i].split("\\.")[1];      //这里的文件名用.分割所以要转义
    		if("3".equals(picSort))                 //有一台机器3张图片上传完全
    		{
    			devName = pics[i].split("\\.")[0];

    			urls.add(dirPath + devName + ".1.bmp");
    			urls.add(dirPath + devName + ".2.bmp");
    			urls.add(dirPath + devName + ".3.bmp");

    			//给bmp图片添加水印 水印内容为截图时间
    	    	System.out.println("starting add mark...");
    	    	String srcImgPath1 = dirPath + devName + ".1.bmp";
    	    	String srcImgPath2 = dirPath + devName + ".2.bmp";
    	    	String srcImgPath3 = dirPath + devName + ".3.bmp";

    	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	    	String logoText = sdf.format(new Date());

    	    	WaterMarkUtils.waterMarkByText(logoText, srcImgPath1, srcImgPath1, 0, null, null, 1.0f);
    	    	WaterMarkUtils.waterMarkByText(logoText, srcImgPath2, srcImgPath2, 0, null, null, 1.0f);
    	    	WaterMarkUtils.waterMarkByText(logoText, srcImgPath3, srcImgPath3, 0, null, null, 1.0f);

    			result = makeGif(rlId + "/img", urls, devName);

    			if(StringUtils.isNotNull(result))
    			{
    			    //键值对 key: devCode 值这个设备对应的url(文件系统下的url)
    				logUrls.put( devName ,"/files/" +rlId + "/img/" + devName + "." + result + ".gif");
    				urls.clear();          //清空数组
    			}
    			else
    			{
    				System.out.println(rlId + "/img" + " gif making failed!");
    			}
    		}
    	}
    	return logUrls;
    }
}
