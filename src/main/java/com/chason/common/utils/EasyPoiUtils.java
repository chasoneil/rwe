/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.chason.common.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.*;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;

/**
 * Excel处理工具类
 *
 */
public class EasyPoiUtils
{
    /**
     * Excel导出
     * */
    public static void exportExcel(List<?> list,
            String title,
            String sheetName,
            Class<?> pojoClass,
            String fileName,
            boolean isCreateHeader,
            HttpServletResponse response)
        throws IOException
    {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * Excel导出
     * */
    public static void exportExcel(List<?> list,
            String title,
            String sheetName,
            Class<?> pojoClass,
            String fileName,
            HttpServletResponse response)
        throws IOException
    {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }

    /**
     * Excel导出
     * */
    public static void exportExcel(List<Map<String, Object>> list,
            String fileName,
            HttpServletResponse response)
        throws IOException
    {
        defaultExport(list, fileName, response);
    }

    private static void defaultExport(List<?> list,
            Class<?> pojoClass,
            String fileName,
            HttpServletResponse response,
            ExportParams exportParams)
        throws IOException
    {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null)
        {
            downLoadExcel(fileName, response, workbook);
        }
    }

    public static void downLoadExcel(String fileName,
            HttpServletResponse response,
            Workbook workbook)
        throws IOException
    {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        workbook.write(response.getOutputStream());
    }

    private static void defaultExport(List<Map<String, Object>> list,
            String fileName,
            HttpServletResponse response)
        throws IOException
    {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (workbook != null)
        {
            downLoadExcel(fileName, response, workbook);
        }
    }

    /**
     * Excel文件导入
     * @param filePath 文件路径
     * @param titleRows  表格标题行数,默认0
     * @param headerRows 表头行数,默认1
     * @param pojoClass
     * */
    public static <T> List<T> importExcel(String filePath,
            Integer titleRows,
            Integer headerRows,
            Class<T> pojoClass)
        throws NoSuchElementException
    {
        if (StringUtils.isBlank(filePath))
        {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
    }

    /**
     * Excel文件导入
     * @param file 文件句柄
     * @param titleRows  表格标题行数,默认0
     * @param headerRows 表头行数,默认1
     * @param pojoClass
     * */
    public static <T> List<T> importExcel(MultipartFile file,
            Integer titleRows,
            Integer headerRows,
            Class<T> pojoClass)
        throws NoSuchElementException, Exception
    {
        if (file == null)
        {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);

        return ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
    }

}
