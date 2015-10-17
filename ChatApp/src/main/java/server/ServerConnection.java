package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import global.GlobalEnv;
import gui.ConsumerInterface;
import peer.ProtocolConsummer;
import protocol.ProtocolInterface;
import protocol.ProtocolMethod;

public class ServerConnection implements Runnable, ProtocolConsummer {

	String host =  GlobalEnv.ip;
	int port = GlobalEnv.port;
	ServerConnectionReader ireader;
	BlockingQueue<ProtocolInterface> method_queue;
	Socket socket;
	public ServerConnection() throws UnknownHostException, IOException {
		method_queue = new LinkedBlockingQueue<ProtocolInterface>();
		socket = new Socket(host, port);
	}
	
	public void write(ProtocolInterface p) throws InterruptedException{
		method_queue.put(p);
	}
	public InputStream getInputStream() throws IOException{
		return socket.getInputStream();
	}

	@Override
	public void run() {
		try {
			
			OutputStream os = socket.getOutputStream();
			while(true){
				try {
					ProtocolInterface method = method_queue.take();
					byte[] data = method.toString().getBytes();
					os.write(data);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		} catch (UnknownHostException e) {
			// Can not connection to server.
			System.out.println("Can not connect with server");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void consume(ProtocolInterface p) {
		System.out.println(p.toString());
		
	}

	public void logout() {
		// TODO Logout
		
	}

}
