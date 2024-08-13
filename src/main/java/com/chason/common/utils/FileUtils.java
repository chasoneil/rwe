package com.chason.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class FileUtils
{

    public static void uploadFile(byte[] file, String filePath, String fileName)
            throws Exception
    {
        File targetFile = new File(filePath);
        if (!targetFile.exists())
        {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static boolean deleteFile(String fileName)
    {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile())
        {
            if (file.delete())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public static String renameToUUID(String fileName)
    {
        return UUID.randomUUID() + "."
                + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 递归删除文件夹（包含文件夹下的所有内容）
     * @param dirFile 要被删除的文件或者目录
     * @return 删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }

        return dirFile.delete();
    }

}
