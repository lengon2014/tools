public class Main {

	public static void main(String[] args) throws Exception {
		// 注册扫码枪的信息的对外输送。
		BarcodeProducter producter = new BarcodeProducter();
		BarcodeConsumer consumer = new BarcodeConsumer();
		producter.startProduct();
		consumer.startConsume();
	}

}
