package org.BigDataFramework.learningHadoop;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsUtil {
	protected static final Log logger = LogFactory.getLog(HdfsUtil.class);
	private static HdfsUtil util;
	
 	private static  FileSystem fs = null;

	private HdfsUtil() { }
	
 	public static HdfsUtil getInstance() throws IOException {
		if(fs==null) {
		  util = new HdfsUtil();
		  Configuration	conf = new Configuration();
		  conf.addResource("core-site.xml");
		  conf.addResource("hdfs-site.xml");
		  fs = FileSystem.get(conf);
		}
		return util;
	}
 	 
    //创建新文件
    public void createFile(String dist , byte[] contents) throws IOException {
        Path dstPath = new Path(dist); //目标路径
        //打开一个输出流
        FSDataOutputStream outputStream = fs.create(dstPath,true);
        try{
	        outputStream.write(contents);
	        logger.info("dist:"+ dist +"文件创建成功！");
        }finally{
        	outputStream.close();
         }
    }
    
    //上传本地文件
    public void uploadFile(String src,String dist) throws IOException {
         try{
	        Path srcPath = new Path(src); //原路径
	        Path dstPath = new Path(dist); //目标路径
	        //调用文件系统的文件复制函数,前面参数是指是否删除原文件，true为删除，默认为false
	        fs.copyFromLocalFile(false,srcPath, dstPath);
        }finally{
         }
        
    }
    
    //列出目录下的信息
    public List<String> listFiles(String dir) throws IOException {
        Path f = new Path(dir);
        FileStatus[] status = fs.listStatus(f);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i< status.length; i++) {
        	list.add(status[i].getPath().toString());
        }
        return list;
    }
    
    //创建目录
    public void mkdir(String dir) throws IOException {
         try{
	        Path path = new Path(dir);
	        boolean isok = fs.mkdirs(path);
	        if(isok){
	        	logger.info(dir+ " create dir ok!");
	        }else{
	        	logger.error(dir+ " create dir failure!");
	        }
        }finally{ 
        }
    }
    
    //读取文件内容返回byte[]
	public byte[] readHDFSFile(String dst) throws IOException {
		Path path = new Path(dst);
		if (fs.exists(path)) {
			FSDataInputStream is = fs.open(path);
			FileStatus stat = fs.getFileStatus(path);
			try{
				byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
				is.readFully(0, buffer);
				return buffer;
			}finally{
				is.close(); 
			}
		} else {
			return null;
		}
	}
    
	public FSDataInputStream readHDFSFile2(String dst) throws IOException {
		Path path = new Path(dst);
		if (fs.exists(path)) {
			FSDataInputStream is = fs.open(path);
			return is;
		}
		else return null;
	}
	
    //删除文件
    public void rmdir(String dir) throws IOException {
         try{
	        Path path = new Path(dir);
	        boolean isok = fs.delete(path,true);
	        if(isok){
	        	logger.info(dir + " delete ok!");
	        }else{
	        	logger.error(dir + " delete failure!");
	        }
        }finally{ 
        }
    }
    
    //文件重命名
    public void rename(String oldName,String newName) throws IOException {
          try{
	        Path oldPath = new Path(oldName);
	        Path newPath = new Path(newName);
	        boolean isok = fs.rename(oldPath, newPath);
	        if(isok){
	        	logger.info("oldName:" + oldName + "newName:" + newName + " rename ok!");
	        }else{
	        	logger.error("oldName:" + oldName + "newName:" + newName + " rename failure!");
	        }
        }finally{
             
        }
    }
    
    /**
     * 下载hdfs文件到本地
     * @param src
     * @param dest
     * @throws URISyntaxException 
     */
    public void downFile(String src, String dest) throws IOException { 
    	  try{
 	        Path srcPath = new Path(src);
 	        Path destPath = new Path(dest);
 	        fs.copyToLocalFile(false, srcPath, destPath);
         }finally{
              
         }
    }
     
}
