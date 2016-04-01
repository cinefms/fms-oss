package com.openfms.core.connector.base.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import javax.net.SocketFactory;

public class SocketUtil {
	
	public Socket createSocket(int timeout, String host, int port) throws SocketException, IOException {
		OpenSocketRunnable osr = new OpenSocketRunnable(host, port);
		Thread t = new Thread(osr);
		t.start();
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis()<(start+timeout)) {
			try {
				Thread.sleep(20);
				if(osr.getE()!=null) {
					throw osr.getE();
				}
				if(osr.isSuccess()) {
					return osr.getSocket();
				}
			} catch (InterruptedException e) {
				throw new IOException(e);
			} finally {
				try {
					t.interrupt();
				} catch (Exception e2) {
				}
			}
		}
		throw new IOException("connection timed out");
	}
	
	private class OpenSocketRunnable implements Runnable {
		
		private String host;
		private int port;
		private Socket socket;
		private IOException e;
		private boolean success = false;

		public OpenSocketRunnable(String host, int port) {
			super();
			this.host = host;
			this.port = port;
		}
		
		@Override
		public void run() {
			try {
				this.socket = SocketFactory.getDefault().createSocket(host,port);
				this.success = true;
			} catch (IOException e) {
				this.e = e;
			}
		}

		public Socket getSocket() {
			return socket;
		}


		public IOException getE() {
			return e;
		}

		public boolean isSuccess() {
			return success;
		}

	}
	

}
