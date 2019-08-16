package util;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import com.hsic.qp.sz.R;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class MySFTP {

	private static String defaultFtpTime = "30";

	private String host;
	private int time;
	private int port;
	private String username;
	private String password;

	private String fTime;

	public MySFTP(Context context) {
		this.host = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("APKServer", context.getResources().getString(R.string.xml_default, "47.100.12.195"));

		this.fTime = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("overTime", defaultFtpTime);

		try{
			time = Integer.parseInt(fTime);
		}catch(Exception e){
			time = 30;
		}

		Log.i(host, fTime);

		this.port = 22;

		this.username = "szlpg";
		this.password = "szlpg";

		Log.i(username, password);
	}

	/**
	 * 连接sftp服务器
	 *
	 * @param host
	 *            主机
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	private SftpProgressMonitor sftpProgressMonitor = null;

	public ChannelSftp connect() {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			System.out.println("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setTimeout(time * 1000);
			// sshSession.setServerAliveInterval(92000);
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			// System.out.println("Session connected.");
			System.out.println("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			System.out.println("Connected to " + host + ".");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sftp;
	}

	public ChannelSftp connect(SftpProgressMonitor monitor) {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.setTimeout(time * 1000);
			// sshSession.setServerAliveInterval(92000);
			sshSession.connect();
			// System.out.println("Session connected.");
			// System.out.println("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			this.sftpProgressMonitor = monitor;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sftp;
	}

	public boolean addDirs(String directory, List<String> dirList, ChannelSftp sftp){
		if(dirList==null || dirList.size()<=0) return false;

		String rootString = directory;

		for(int i=0; i<dirList.size(); i++){
			if(mkdir(rootString, dirList.get(i).toString() + "/", sftp)){
				rootString = rootString + dirList.get(i).toString() + "/";
				Log.i("i="+i, rootString);
			}
		}

		return true;
	}

	public boolean mkdir(String directory, String dir, ChannelSftp sftp){
		try{
			sftp.mkdir(directory+dir);
			Log.i("mkdir", directory+dir);
			return true;
		}catch (Exception e) {
			Log.i("已存在", directory+dir);
			return true;
		}
	}

	/**
	 * 上传文件
	 *
	 * @param directory
	 *            上传的目录
	 * @param uploadFile
	 *            要上传的文件
	 * @param sftp
	 */
	public boolean upload(String directory, String uploadFile, ChannelSftp sftp) {

		try {

			sftp.cd(directory);
			File file = new File(uploadFile);
			FileInputStream fis = new FileInputStream(file);

			if (null != sftpProgressMonitor) {
				sftp.put(fis, file.getName(),
						sftpProgressMonitor);
			} else {
				sftp.put(fis, file.getName());
			}
			fis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 下载文件
	 *
	 * @param directory
	 *            下载目录
	 * @param downloadFile
	 *            下载的文件
	 * @param saveFile
	 *            存在本地的路径
	 * @param sftp
	 */
	public boolean download(String directory, String downloadFile,
							String saveFile, ChannelSftp sftp) {
		try {
			// Log.d("sftp", directory);
			// Log.d("sftp", host);
			// Log.d("sftp", String.valueOf(port));
			// Log.d("sftp", username);
			// Log.d("sftp", password);
			sftp.cd(directory);
			File file = new File(saveFile);
			if (null != sftpProgressMonitor) {
				sftp.get(downloadFile, new FileOutputStream(file),
						sftpProgressMonitor);
			} else {
				sftp.get(downloadFile, new FileOutputStream(file));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除文件
	 *
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 * @param sftp
	 */
	public void delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列出目录下的文件
	 *
	 * @param directory
	 *            要列出的目录
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	@SuppressWarnings("rawtypes")
	public Vector listFiles(String directory, ChannelSftp sftp)
			throws SftpException {
		return sftp.ls(directory);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getfTime() {
		return fTime;
	}

	public void setfTime(String fTime) {
		this.fTime = fTime;
	}


}