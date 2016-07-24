package org.BigDataFramework.learningHadoop;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.fs.FSDataInputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class App {

	static void syncClient() throws JSchException, IOException, SftpException,
			URISyntaxException {
		SFTPUtil sftpUtil = SFTPUtil.getInstance();
		Session session = null;
		ChannelSftp channelSftp = null;

		session = sftpUtil.getSession("127.0.0.1", 22, "monitor", "monitor");
		channelSftp = sftpUtil.getChannelSFTP(session);
  
		FSDataInputStream isfile = HdfsUtil.getInstance().readHDFSFile2("/jdk1.7.tar.gz");
		
		sftpUtil.execPut(channelSftp, isfile, "/home/monitor/", "jdk1.7.tar.gz");

		if (null != isfile) {
			isfile.close();
		}
		
		sftpUtil.closeChannelSftp(channelSftp);
	}

	public static void main(String[] args) throws IOException,
			URISyntaxException, JSchException, SftpException {
		syncClient();
	}
}
