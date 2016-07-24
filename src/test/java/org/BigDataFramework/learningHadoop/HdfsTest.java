package org.BigDataFramework.learningHadoop;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.Test;

public class HdfsTest {
	 
	@Test
	public void test01() throws IOException, URISyntaxException{
			List<String> list = HdfsUtil.getInstance().listFiles("/tmp");
			for (String str  : list){
				System.out.println(str);
			}
	}
	
	@Test
	public void test02() throws IOException, URISyntaxException{		 
			HdfsUtil.getInstance().mkdir("/tmp/test");	 
	}
	
	@Test
	public void test03() throws IOException, URISyntaxException{		 
			HdfsUtil.getInstance().mkdir("/tmp/test");
			HdfsUtil.getInstance().createFile("/tmp/test/logstash.conf", "XXXX".getBytes());
	}
	
	@Test
	public void test04() throws IOException, URISyntaxException{
		String initshellUri = "/tmp/test/logstash.conf";
		byte[]  bytes = HdfsUtil.getInstance().readHDFSFile(initshellUri);
		System.err.println(bytes.length);	 
	}
		
}
