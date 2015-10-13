package server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import controller.CallbackInteface;
import peer.ProtocolConsummer;
import protocol.ProtocolInterface;
import protocol.ProtocolParser;
import protocol.ProtocolReturn;

public class ServerConnectionReader implements Runnable {

	InputStream in;
	LinkedList<ProtocolConsummer> consummers;
	LinkedList<CallbackInteface> callbacks;
	Map<Integer, CallbackInteface> CallbackConsummers;
	public void addConsummer(ProtocolConsummer consummer){
		synchronized (consummers) {
			consummers.add(consummer);
		}
	}
	
	public void addCallback(CallbackInteface callback){
		synchronized (callbacks) {
			callbacks.add(callback);
		}
	}
	/**
	 * Add callback for a method with specific id
	 * @param callback
	 * @param id
	 */
	public void addCallback(CallbackInteface callback, int id){
		synchronized (CallbackConsummers) {
			CallbackConsummers.put(new Integer(id), callback);
			System.out.println(CallbackConsummers.size());
		}
	}
	
	/**
	 * Contructor for Server Connection Reader.
	 * @param client
	 * @throws IOException
	 */
	public ServerConnectionReader(ServerConnection client) throws IOException {
		in = client.getInputStream();
		consummers = new LinkedList<ProtocolConsummer>();
		callbacks = new LinkedList<CallbackInteface>();
		CallbackConsummers = new HashMap<Integer, CallbackInteface>();
		
	}
	@Override
	public void run() {
		ProtocolParser parser = new ProtocolParser(in);
		// Read protocol 
		while(true){
			ProtocolInterface c = parser.read();
			
			for (CallbackInteface callbackInteface : callbacks) {
				callbackInteface.onResponse(c);
				if(c.getType() == "return") {
					ProtocolReturn ret = (ProtocolReturn)c;
					int code  = ret.error.code;
					if(code >= 0) callbackInteface.onSuccess(ret.result);
					else callbackInteface.onFail(code, ret.error.message);
				}
			}
			callbacks.clear();
			ArrayList<Integer> temp = new ArrayList<Integer>();
			if(c.getType().equals("return")){
				ProtocolReturn ret = (ProtocolReturn) c;
				for (Entry<Integer, CallbackInteface> entry : CallbackConsummers.entrySet()) {
					//System.out.println(entry.getKey());
					if(entry.getKey().intValue() == ret.Id){
						temp.add(entry.getKey());
						entry.getValue().onResponse(ret);
						int code = ret.error.code;
						if(code >= 0) entry.getValue().onSuccess(ret.result);
						else {
							entry.getValue().onFail(ret.error.code, ret.error.message);
						}
					}
				}
			}
			for (Integer integer : temp) {
				CallbackConsummers.remove(integer);
			}
		}

	}

}
