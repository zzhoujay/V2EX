package com.zzhoujay.v2ex.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by 州 on 2015/7/4 0004.
 * 文件操作相关工具类
 */
public class FileUtils {

    public static DecimalFormat decimalFormat = new DecimalFormat(".00");

    /**
     * 判断文件是否存在
     *
     * @param path 文件的全路径
     * @return 文件是否存在
     */
    @SuppressWarnings("unused")
    public static boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    @SuppressWarnings("unused")
    public static boolean isFileExists(File parent, String name) {
        File file = new File(parent, name);
        return file.exists();
    }

    /**
     * 获取文件的后缀名
     *
     * @param path 文件全路径
     * @return 后缀名
     */
    @SuppressWarnings("unused")
    public static String getFileExtension(String path) {
        return path == null ? null : path.substring(path.lastIndexOf(".") + 1);
    }

    /**
     * 去掉路径的后缀名
     *
     * @param path 文件全路径
     * @return 去后缀名后的文件名
     */
    @SuppressWarnings("unused")
    public static String getPathWithoutExtension(String path) {
        return path == null ? null : path.substring(0, path.lastIndexOf("."));
    }

    /**
     * 将对象写入指定路径的文件中
     *
     * @param path 文件路径
     * @param obj  需要被写入的对象
     */
    @SuppressWarnings("unused")
    public static void writeObject(String path, Object obj) {
        File file = new File(path);
        writeObject(file, obj);
    }

    /**
     * 写入对象到文件中
     *
     * @param file 文件对象
     * @param obj  需要写入文件的对象
     */
    @SuppressWarnings("unused")
    public static void writeObject(File file, Object obj) {
        if (null == file || obj == null) {
            return;
        }
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
        } catch (IOException e) {
            Log.d("writeObject", e.getMessage());
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                Log.d("writeObject", e.getMessage());
            }
        }
    }

    /**
     * 从指定路径的文件中读取对象
     *
     * @param path 文件路径
     * @return 读取到的对象
     */
    @SuppressWarnings("unused")
    public static Object readObject(String path) {
        File file = new File(path);
        return readObject(file);
    }

    /**
     * 从文件中读取对象
     *
     * @param file 文件对象
     * @return 读取到的对象
     */
    public static Object readObject(File file) {
        Object obj = null;
        if (file != null && file.exists()) {
            FileInputStream fileInputStream = null;
            ObjectInputStream objectInputStream = null;

            try {
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);

                obj = objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                Log.d("readObject", e.getMessage());
            } finally {
                try {
                    if (objectInputStream != null) {
                        objectInputStream.close();
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException e) {
                    Log.d("readObject", e.getMessage());
                }

            }
        }
        return obj;
    }

    /**
     * 格式化文件大小以字符串输出
     *
     * @param size 大小
     * @return B、KB、MB类型的字符串
     */
    @SuppressWarnings("unused")
    public static String formatSize(int size) {
        if (size < 1024 * 0.6) {
            return size + "B";
        } else if (size < 1024 * 1024 * 0.6) {
            return decimalFormat.format((float) size / 1024) + "KB";
        } else {
            return decimalFormat.format((float) size / (1024 * 1024)) + "MB";
        }
    }

    @SuppressWarnings("unused")
    public static boolean createFolder(File file, String name) {
        if (file.exists()) {
            File f = new File(file, name);
            if (!f.exists()) {
                return f.mkdirs();
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static void deleteSomeCache(File parent, String start, int maxSize) {
        if(parent!=null){
            File[] files = parent.listFiles(new FileNameFilter(start));
            if (files != null && files.length > maxSize) {
                Arrays.sort(files, new FileComparator());
                for (int len = files.length, i = maxSize; i < len; i++) {
                    files[i].delete();
                }
            }
        }
    }
}
