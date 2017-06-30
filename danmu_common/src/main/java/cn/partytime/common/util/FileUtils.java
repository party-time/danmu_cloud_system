package cn.partytime.common.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lENOVO on 2016/11/28.
 */
public class FileUtils {

    public static String getMd5ByFile(File file) throws FileNotFoundException, NoSuchAlgorithmException {

        FileInputStream fileInputStream = null;
        MessageDigest  MD5 = MessageDigest.getInstance("MD5");
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String txt2String(String filPath){
        StringBuilder result = new StringBuilder();
        try{
            File file = new File(filPath);
            if(!file.exists()){
                return "";
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while((s = br.readLine())!=null){
                result.append(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void outputFile(String filePath,String content)  {
            FileOutputStream fos=null;
            try {
                File f = new File(filePath);
                if (f.exists()==false) {
                    f.createNewFile();//create file if not exist
                }
                //create FileOutputStream with file
                fos = new FileOutputStream(f);
                //output file
                fos.write(content.getBytes());
                //flush this stream
                fos.flush();
                //close this stream
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(fos!=null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void appendContentToFile(String fileName, String content) {
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
