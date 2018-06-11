/**
 * 启动和关闭条码枪扫描线程
 * 
 * @author ysc
 */
public class BarcodeProducter {
	private boolean quit;
	private Thread thread;
	private ScanBarcodeService scanBarcodeService;

	public BarcodeProducter() {
		scanBarcodeService = new ScanBarcodeService();
	}

	/**
	 * 启动生产者线程 此方法在tomcat启动的时候被调用
	 */
	public void startProduct() {
		// 防止重复启动
		if (thread != null && thread.isAlive()) {
			return;
		}
		LogUtils.info("start barcode listener");
		
		// 启动一个线程用于在tomcat关闭的时候卸载键盘钩子
		thread = new Thread() {
			@Override
			public void run() {
				LogUtils.info("barcode listener start success");
				while (!quit) {
					try {
						Thread.sleep(5000);
					} catch (Exception e) {
						quit = true;
					}
				}

				scanBarcodeService.stopScanBarcodeService();
				LogUtils.info("barcode listener quit");
				System.exit(0);
			}
		};
		thread.start();
		
		new Thread() {
			@Override
			public void run() {
				scanBarcodeService.startScanBarcodeService();
			}
		}.start();

	}

	/**
	 * 关闭生产者线程 此方法在扫码枪关闭的时候被调用
	 */
	public void stopProduct() {
		if (thread != null) {
			thread.interrupt();
			LogUtils.info("barcode listener quit");
		}
	}
}
