

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zcnest.common.utils.LogUtils;
import com.zcnest.dba.SftpClient;


@Component
public class OrderImportSchedule {

	@Autowired
	private SftpClient client;
	
	@Scheduled(fixedDelay = 5000)
	public void index() {
		LogUtils.info("begin sftp sync ");
		client.connect();
		try {
			client.uploadFile("pom.xml", "/uploaded.txt");
			client.retrieveFile("/uploaded.txt", "target/downloaded.txt");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.disconnect();
		}
	}
}
