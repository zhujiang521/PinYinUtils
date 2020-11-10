package com.zj;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        //1、待转换文件所在文件夹
        File file = new File("/Users/zhujiang/Lenovo/Image资源/节日切图汇总/节日切图-横屏深色");
        //2、转换之后文件生成的文件夹位置
        File filecopy = new File("/Users/zhujiang/Desktop/Images/drawable-night-land    ");

        if (!filecopy.exists()) {
            filecopy.mkdirs();
        }

        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        File[] filelist = file.listFiles();
        for (File fi : filelist) {
            //1、获取到文件名
            String substring = fi.getName().substring(0, fi.getName().indexOf('.'));
            //2、文件名中文转换为拼音首字母
            String newPrefix = getPinYin(substring);
            //3、获取文件的后缀
            String suffix = fi.getName().substring(fi.getName().lastIndexOf('.'));
            //4、将需要处理的文件输入到流中
            in = FileUtil.getInputStream(fi);
            //5、新文件的名称及地址：filecopy + newPrefix + suffix
            String newFilePath = filecopy + File.separator + newPrefix + suffix;

            File newfile = new File(newFilePath);
            if (!newfile.exists()) {
                try {
                    newfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            out = FileUtil.getOutputStream(newFilePath);
            //6、使用Hutool进行复制
            long copyTime = IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE);
            System.out.printf("转换耗时: %d \n", copyTime);
        }

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将字符串中的中文转化为拼音，其他字符不变
     *
     * @param inputString
     * @return
     */


    public static String getPinYin(String inputString) {

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        StringBuilder output = new StringBuilder();

        try {
            for (char c : input) {
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    output.append(temp[0]);
                } else
                    output.append(c);
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变
     *
     * @param chinese
     * @return
     */

    public static String getFirstSpell(String chinese) {
        StringBuilder pybf = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        for (char c : arr) {
            if (c > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(c);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim().toUpperCase();
    }

    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese
     * @return
     */
    public static String getFullSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : arr) {
            if (c > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(c);
            }
        }
        return pybf.toString();
    }
}
