package server;

import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

import protocol.ProtocolMethod;
import protocol.ProtocolMethodExcutor;
import protocol.ProtocolParser;
import protocol.ProtocolReturn;

public class ServerThread extends Thread {
	public Socket socket;
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			ProtocolParser parser = new ProtocolParser(socket.getInputStream());
			while(true){
				//JSONObject j = ProtocolParser.Parser(socket.getInputStream());
				ProtocolMethod method = parser.readMethod();
				ProtocolReturn r = ProtocolMethodExcutor.excute(method);
				System.out.println(method.toString());
				if(method.method_name == "signout") break;
				socket.getOutputStream().write(r.toString().getBytes());
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
