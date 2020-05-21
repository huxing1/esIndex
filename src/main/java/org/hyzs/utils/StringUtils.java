package org.hyzs.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: AiHBase
 * @description: String工具类
 * @author: hux
 * @create: 2020-01-08 09:32
 **/
public class StringUtils {

    /**
    * @Description: 正则匹配string中.点的个数
    * @Param: [str]
    * @return: java.lang.Integer
    * @Author: hux
    * @Date: 2020/1/8
    */
    public static Integer strCount(String str) {
        return str.length()-str.replaceAll("[.]", "").length();
    }

    /**
    * @Description: 将调用接口失败的文书信息持久化到文件中
    * @Param: [s]
    * @return: void
    * @Author: hux
    * @Date: 2020/1/21
    */
    public static void saveFailedInfo(String pathName, String s) {

        File f = new File(pathName);
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(s);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String stringSplit(String s){
        String[] strs = s.split("[:]");
        if (strs.length>1) {
            String result = strs[1];
            return result;
        }return null;

    }
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String docid = "a" + i;
            saveFailedInfo("FailedDocid.txt",docid);
        }
        System.out.println("执行完成");
    }
}
