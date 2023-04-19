package com.liuxe.iword.utils

import android.util.Log
import java.io.*

/**
 *  author : liuxe
 *  date : 2023/3/28 14:38
 *  description :
 */
object FileUtils {
    // 将字符串写入到文本文件中
    fun writeTxtToFile(strcontent: String, filePath: String, fileName: String) {
//生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName)
        val strFilePath = filePath + fileName
        // 每次写入时，都换行写
        val strContent = strcontent + "rn"
        try {
            val file = File(strFilePath)
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:$strFilePath")
                file.getParentFile().mkdirs()
                file.createNewFile()
            }
            val raf = RandomAccessFile(file, "rwd")
            raf.seek(file.length())
            raf.write(strContent.toByteArray())
            raf.close()
        } catch (e: Exception) {
            Log.e("TestFile", "Error on write File:$e")
        }
    }

    //生成文件
    fun makeFilePath(filePath: String, fileName: String): File? {
        var file: File? = null
        makeRootDirectory(filePath)
        try {
            file = File(filePath + fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    //生成文件夹
    fun makeRootDirectory(filePath: String?) {
        var file: File? = null
        try {
            file = File(filePath)
            if (!file.exists()) {
                file.mkdir()
            }
        } catch (e: Exception) {
            Log.i("error:", e.toString() + "")
        }
    }

    //读取指定目录下的所有TXT文件的文件内容
    fun getFileContent(file: File): String {
        var content = ""
        if (!file.isDirectory()) { //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) { //文件格式为""文件
                try {
                    val instream: InputStream = FileInputStream(file)
                    if (instream != null) {
                        val inputreader = InputStreamReader(instream, "UTF-8")
                        val buffreader = BufferedReader(inputreader)
                        var line = ""
                        //分行读取
                        while (buffreader.readLine().also { line = it } != null) {
                            content += line + "n"
                        }
                        instream.close() //关闭输入流
                    }
                } catch (e: FileNotFoundException) {
                    Log.d("TestFile", "The File doesn't not exist.")
                } catch (e: IOException) {
                    Log.d("TestFile", ""+e.message)
                }
            }
        }
        return content
    }
}