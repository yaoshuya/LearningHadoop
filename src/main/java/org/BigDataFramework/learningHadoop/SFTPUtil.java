package org.BigDataFramework.learningHadoop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class SFTPUtil {
	private static final Log logger = LogFactory.getLog(SFTPUtil.class);
	private static SFTPUtil instance = null;//是否是final的不重要，因为最多只可能实例化一次
	public  static final int DEFAULT_SSH_PORT = 22;
	private static final int TIMEOUT = 30000;

	private SFTPUtil(){
		
	}
	
	public static SFTPUtil getInstance() {
		if (instance == null) {
			// 双重检查加锁，只有在第一次实例化时，才启用同步机制，提高了性能。
			synchronized (SFTPUtil.class) {
				if (instance == null) {
					instance = new SFTPUtil();
				}
			}
		}
		logger.info("start sftp");
		return instance;
	}

	public Session getSession(String hostIp, String userName, String password) throws JSchException{
		return this.getSession(hostIp, DEFAULT_SSH_PORT, userName, password);
	}
	
	public Session getSession(String hostIp, int port, String userName, String password) throws JSchException{
		JSch jsch = new JSch();
		UserInfo userInfo = new CustomUserInfo(password);
		Session session = jsch.getSession(userName, hostIp, port);
		session.setUserInfo(userInfo);
		session.setPassword(password);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect(TIMEOUT);
		return session;
	}
	
	public ChannelSftp getChannelSFTP(Session session) throws JSchException{
		Channel channel = session.openChannel("sftp");// 打开SFTP通道
		channel.connect(TIMEOUT); // 建立SFTP通道的连接
		ChannelSftp channelSftp = (ChannelSftp) channel;
		return channelSftp;
	}
	
	public void execPut(ChannelSftp channelSftp, String localFile, String destPath, String newName) throws SftpException, IOException{
		InputStream instream = null;
		try{
	        instream = new FileInputStream(new File(localFile));
	        channelSftp.cd(destPath);
			channelSftp.put(instream, newName,ChannelSftp.OVERWRITE);
		} finally{
        	if (instream != null){
				instream.close();
        	}
        }
	}
	
	public void execPut(ChannelSftp channelSftp, InputStream instream , String destPath, String newName) throws SftpException, IOException{
		try{
			channelSftp.cd(destPath);
			channelSftp.put(instream, newName,ChannelSftp.OVERWRITE);
		} finally{
        	if (instream != null){
				instream.close();
        	}
        }
	}

	public void closeChannelSftp(ChannelSftp channelSftp) throws JSchException {
		if (channelSftp != null && !channelSftp.isClosed()){ 
			channelSftp.exit();
			channelSftp.quit();
			channelSftp.disconnect();
			channelSftp.getSession().disconnect();
		} 
	}

	public void closeSession(Session session) {
		if (session != null && session.isConnected()){
			session.disconnect();
		}
	}
	
	public static class CustomUserInfo implements UserInfo, UIKeyboardInteractive {
		private String passphrase = "";  
	    public CustomUserInfo(String passphrase ){  
	        this.passphrase = passphrase;  
	    } 
	    
		public String getPassphrase() {   
			return this.passphrase;
	    }

	    public String getPassword() {
	    	return this.passphrase;
	    }

	    public boolean promptPassphrase(String message) {
	        return false;
	    }

	    public boolean promptPassword(String message) {
	        return false;
	    }

	    public boolean promptYesNo(String message) {
	        return true;
	    }

	    public void showMessage(String message) {
	    }

	    public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
	        return null;
	    }
	}
}