
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Configuration
@ConfigurationProperties(prefix = "sftp")
@PropertySource(value = { "classpath:sftp.properties" })
public class SftpClient {

	private final static Logger logger = LoggerFactory.getLogger(SftpClient.class);
	private String ip;
	private int port;
	private String login;
	private String passwd;

	private JSch jsch = null;
	private Session session = null;
	private Channel channel = null;
	private ChannelSftp c = null;

	/**
	 * Connects to the server and does some commands.
	 */
	public void connect() {
		if(null==ip) {
			logger.warn("do not config the server, ignore sftp task ");
			return;
		}
		try {
			logger.debug("Initializing jsch");
			jsch = new JSch();
			session = jsch.getSession(login, ip, port);
			// Java 6 version
			session.setPassword(passwd);

			logger.debug("Jsch set to StrictHostKeyChecking=no");
			Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			logger.info("Connecting to " + ip + ":" + port);
			session.connect();
			logger.info("Connected !");

			// Initializing a channel
			logger.debug("Opening a channel ...");
			channel = session.openChannel("sftp");
			channel.connect();
			c = (ChannelSftp) channel;
			logger.debug("Channel sftp opened");

		} catch (JSchException e) {
			logger.error("", e);
		}
	}

	/**
	 * Uploads a file to the sftp server
	 * 
	 * @param sourceFile
	 *            String path to sourceFile
	 * @param destinationFile
	 *            String path on the remote server
	 * @throws Exception
	 *             if connection and channel are not available or if an error occurs
	 *             during upload.
	 */
	public void uploadFile(String sourceFile, String destinationFile) throws Exception {
		if (c == null || session == null || !session.isConnected() || !c.isConnected()) {
			throw new Exception("Connection to server is closed. Open it first.");
		}

		try {
			logger.debug("Uploading file to server");
			c.put(sourceFile, destinationFile);
			logger.info("Upload successfull.");
		} catch (SftpException e) {
			throw new Exception(e);
		}
	}

	/**
	 * Retrieves a file from the sftp server
	 * 
	 * @param destinationFile
	 *            String path to the remote file on the server
	 * @param sourceFile
	 *            String path on the local fileSystem
	 * @throws Exception
	 *             if connection and channel are not available or if an error occurs
	 *             during download.
	 */
	public void retrieveFile(String sourceFile, String destinationFile) throws Exception {
		if (c == null || session == null || !session.isConnected() || !c.isConnected()) {
			throw new Exception("Connection to server is closed. Open it first.");
		}

		try {
			logger.debug("Downloading file to server");
			c.get(sourceFile, destinationFile);
			logger.info("Download successfull.");
		} catch (SftpException e) {
			throw new Exception(e.getMessage(), e);
		}
	}

	public void disconnect() {
		if (c != null) {
			logger.debug("Disconnecting sftp channel");
			c.disconnect();
		}
		if (channel != null) {
			logger.debug("Disconnecting channel");
			channel.disconnect();
		}
		if (session != null) {
			logger.debug("Disconnecting session");
			session.disconnect();
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public SftpClient() {
	}
}