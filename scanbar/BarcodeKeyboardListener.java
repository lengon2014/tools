import java.util.HashMap;
import java.util.Map;

public class BarcodeKeyboardListener {
	// 条形码数据缓充区
	private StringBuilder barcode;
	// 扫描开始时间
	private long start;
	private Map<Integer, Character> keyToLetter = new HashMap<Integer, Character>();
	// 一次扫描的最长时间
	private static int maxScanTime = 300;

	// 条形码的最短长度
	private static int barcodeMinLength = 4;

	/**
	 * 初始键盘代码和字母的对于关系
	 */
	public BarcodeKeyboardListener() {
		for (int i = 48; i < 58; i++) {
			keyToLetter.put(i, (char) ('0' + i - 48));
		}

		// 新增a-z的代码
		for (int i = 65; i < 91; i++) {
			keyToLetter.put(i, (char) ('a' + i - 65));
		}
	}

	/**
	 * 此方法响应扫描枪事件
	 * 
	 * @param keyCode
	 */
	public void onKey(int keyCode) {

		// 回车键
		if (keyCode == 13) {
			if (null != barcode && barcode.length() >= barcodeMinLength) {
				// 将数据加入缓存阻塞队列
				BarcodeBuffer.product(barcode.toString());
			}
			// 清空原来的缓冲区
			barcode = new StringBuilder();
			return;
		}

		// 获取输入的是那个数字
		Character letter = keyToLetter.get(keyCode);
		if (null == letter) {
			System.err.println(keyCode);
			return;
		}
		// System.out.println("find code: " + letter);
		if (barcode == null) {
			// 开始进入扫描状态
			barcode = new StringBuilder();
			// 记录开始扫描时间
			start = System.currentTimeMillis();
		}
		// 需要判断时间
		long cost = System.currentTimeMillis() - start;
		if (cost > maxScanTime) {
			// 开始进入扫描状态
			barcode = new StringBuilder();
			// 记录开始扫描时间
			start = System.currentTimeMillis();
		}
		barcode.append(letter);
	}
}
