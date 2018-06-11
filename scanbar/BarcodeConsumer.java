package com.zcnest.qrcode.consumer;

import java.util.ArrayList;
import java.util.List;

import com.zcnest.common.utils.LogUtils;
import com.zcnest.qrcode.producer.BarcodeBuffer;

/**
 * 此消费者线程会从此缓冲区中获取数据并执行数据的保存操作 数据的保存调用BarcodeSaveService接口定义的save方法
 * 
 */
public class BarcodeConsumer {
	// 消费者线程
	private Thread thread;
	// 数据保存服务（可有多个）
	private List<BarcodeSaveService> barcodeSaveServices = new ArrayList<BarcodeSaveService>();
	private boolean quit;

	/**
	 * 停止消费者线程 此方法在tomcat关闭的时候被调用
	 */
	public void stopConsume() {
		if (thread != null) {
			thread.interrupt();
			// 释放资源
			for (BarcodeSaveService barcodeSaveService : barcodeSaveServices) {
				barcodeSaveService.finish();
			}
		}
	}

	/**
	 * 启动消费者线程 此方法在tomcat启动的时候被调用
	 */
	public void startConsume() {
		// 防止重复启动
		if (thread != null && thread.isAlive()) {
			return;
		}
		LogUtils.info("qrcode comsumer begin");
		registerBarcodeSaveServcie();
		thread = new Thread() {
			@Override
			public void run() {
				while (!quit) {
					try {
						// 当缓冲区没有数据的时候，此方法会阻塞
						String barcode = BarcodeBuffer.consume();
						System.out.println(barcode);

						for (BarcodeSaveService barcodeSaveService : barcodeSaveServices) {
							barcodeSaveService.save(barcode);
						}
					} catch (InterruptedException e) {
						quit = true;
					}
				}
				LogUtils.info("client quit now");
			}
		};
		thread.setName("consumer");
		thread.start();
	}

	/**
	 * 消费者线程从缓冲区获取到数据后需要调用保存服务对数据进行处理
	 */
	private void registerBarcodeSaveServcie() {
		try {
			BarcodeSaveService barcodeSaveService = new BarcodeToWebsocket();
			barcodeSaveServices.add(barcodeSaveService);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
