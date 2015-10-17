package controller;

import java.util.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import protocol.ProtocolMethod;
import server.ServerConnectionReader;
import server.ServerConnection;

public class ServerService {
	private ServerConnection connection;
	private ServerConnectionReader reader;
	public int method_ip_counter = 1;
	public Session session;
	
	public class Session {
		public String username = null;
		private List<SessionListener> sessionListeners = new LinkedList<SessionListener>();
		public void addSessionListener(SessionListener listener){
			sessionListeners.add(listener);
		}
		public void setSession(String username){
			this.username = username;
			for (SessionListener sessionListener : sessionListeners) {
				sessionListener.onLogin(username);
			}
		}
		
		public void remove(){
			
			for (SessionListener sessionListener : sessionListeners) {
				sessionListener.onLogout(username);	
			}
			username = null;
		}
	}
	private int getTransactionId(){
		return method_ip_counter++;
	}
	private static ServerService mService = null;
	
	/**
	 * Get resource server. If there are a server service return it. otherwise 
	 * create new service and return it
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static ServerService GetResource() throws UnknownHostException, IOException{
		if(mService == null)
		{
			// create res
			ServerConnection conn = new ServerConnection();
			ServerService serverService = new ServerService(conn);
			mService = serverService;
		}
		return mService;
	}
	/**
	 * Create a server communicate with server
	 * @param conn Server connection
	 * @throws IOException Something went wrong
	 */
	public ServerService(ServerConnection conn) throws IOException {
		session = new Session();
		connection = conn;
		reader = new ServerConnectionReader(conn);
		
		// Set resource
		mService = this;
		// Start connection
		Thread serverThread = new Thread(conn);
		serverThread.start();
		
		Thread readThread = new Thread(reader);
		readThread.start();
	}
	/**
	 * Login exist account
	 * @param username User name
	 * @param password Password 
	 * @param callback which called when server has return
	 * @throws InterruptedException
	 */
	public void login(String username, String password, int port, CallbackInteface callback) throws InterruptedException{
		Object[] param = new Object[3];
		param[0] = username;
		param[1] = password;
		param[2] = port;
		int transactionId = getTransactionId();
		ProtocolMethod p = new ProtocolMethod("login", param, transactionId);
		System.out.println(p.toString());
		reader.addCallback(callback, transactionId);
		
		connection.write(p);
			
	}

	/**
	 * Create a new account
	 * @param username User name
	 * @param password Password
	 * @param fullname Full name
	 * @param callback which called when server has response
	 * @throws InterruptedException 
	 */
	public void signup(String username, String password, String fullname, CallbackInteface callback) throws InterruptedException{
		String[] param = new String[3];
		param[0] = username;
		param[1] = password;
		param[2] = fullname;
		int transactionId = getTransactionId();
		ProtocolMethod p = new ProtocolMethod("signup", param, transactionId);
		System.out.println(p.toString());
		reader.addCallback(callback, transactionId);	
		connection.write(p);
	}
	
	/**
	 * Request get friend by keyword. By default, if keyword null it will load all friends.
	 * @param keyword Friend user name. Nullable.
	 * @param callbackwhich called when server has response
	 * @throws InterruptedException 
	 */
	public void getContacts(String keyword, CallbackInteface callback) throws InterruptedException{
		String[] param = new String[1];
		param[0] = keyword;
		int transactionId = getTransactionId();
		ProtocolMethod p = new ProtocolMethod("getContacts", param, transactionId);
		System.out.println(p.toString());
		reader.addCallback(callback, transactionId);	
		System.out.println(p.toString());
		connection.write(p);
	}
	
	/**
	 * Get user profiles
	 * @param username user name.
	 * @param callback which called when server has response
	 * @throws InterruptedException 
	 */
	public void getProfile(String username, CallbackInteface callback) throws InterruptedException{
		String[] param = new String[1];
		param[0] = username;
		int transactionId = getTransactionId();
		ProtocolMethod p = new ProtocolMethod("getProfile", param, transactionId);
		System.out.println(p.toString());
		reader.addCallback(callback, transactionId);	
		connection.write(p);
	}
	
	/**
	 * Confirm this client still online. And request update new online list.
	 * @param callback server return new online list
	 * @throws InterruptedException 
	 */
	public void online(String current_user_name, CallbackInteface callback) throws InterruptedException{
		String[] param = new String[1];
		param[0] = current_user_name;
		int transactionId = getTransactionId();
		ProtocolMethod p = new ProtocolMethod("online", param, transactionId);
		//System.out.println(p.toString());
		reader.addCallback(callback, transactionId);	
		connection.write(p);
	}
	
	public void search(String keyword, CallbackInteface callback) throws InterruptedException{
		String[] param = new String[1];
		param[0] = keyword;
		int transactionId = getTransactionId();
		ProtocolMethod p = new ProtocolMethod("search", param, transactionId);
		System.out.println(p.toString());
		reader.addCallback(callback, transactionId);	
		connection.write(p);
	}
	
	/**
	 * Log out
	 * @param callback
	 */
	public void logout(CallbackInteface callback){
		connection.logout();
	}

}
