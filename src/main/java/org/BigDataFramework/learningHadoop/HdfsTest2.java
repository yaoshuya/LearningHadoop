package org.BigDataFramework.learningHadoop;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsTest2 { 

	// 上传本地文件
	public void test() throws IOException, URISyntaxException,
			InterruptedException {
		Configuration conf = new Configuration();
		conf.addResource("core-site.xml");
		conf.addResource("hdfs-site.xml");
		FileSystem fs = FileSystem.get(conf); 

		List<String> ls = listFiles(fs, "hdfs://elkcluster/tmp/");
		for (String str : ls)
			System.out.println(str);
	}
 
	public List<String> listFiles(FileSystem fs, String dir)
			throws IOException, URISyntaxException, InterruptedException {

		Path f = new Path(dir);
		FileStatus[] status = fs.listStatus(f); 
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < status.length; i++) {
			list.add(status[i].getPath().toString());
		}
		return list;
	}

	public static void main(String[] args) throws IOException,
			URISyntaxException, InterruptedException {
		final HdfsTest2 ht = new HdfsTest2();
		new Thread(new Runnable() {
			public void run() {
				try {
					ht.test();
				} catch (IOException | URISyntaxException
						| InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start(); 

	}
}
