

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


/**
 * 把条形码数据保存到文件的实现（参考实现） 可根据自己的需求做一个自定义实现 只要实现BarcodeSaveService接口的方法
 * 并在barcode.save.services文件中指定使用的实现类即可

 */
public class BarcodeToWebsocket implements BarcodeSaveService {
	private Writer writer;

	/**
	 * websocket返回。
	 * 
	 * @param barcode
	 */
	@Override
	public void save(String barcode) {
		System.out.println("begin send to websocket " + barcode);
		
	}

	/**
	 * 关闭文件
	 */
	@Override
	public void finish() {
		System.out.println("关闭文件");
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
