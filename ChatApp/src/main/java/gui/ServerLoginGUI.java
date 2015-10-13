package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;

import controller.CallbackInteface;
import controller.ServerService;
import peer.ProtocolConsummer;
import protocol.ProtocolInterface;

import server.ServerConnection;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.UnknownHostException;


public class ServerLoginGUI implements ProtocolConsummer{

	private JFrame frame;
	private JTextField tb_username;
	private JTextField tb_password;
	public ServerLoginGUI instance;
	ServerConnection connection;
	ServerService serverService;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerLoginGUI window = new ServerLoginGUI();
					window.instance = window;
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerLoginGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		tb_username = new JTextField();
		tb_username.setBounds(217, 59, 163, 20);
		frame.getContentPane().add(tb_username);
		tb_username.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("User name : ");
		lblNewLabel.setBounds(78, 62, 87, 14);
		frame.getContentPane().add(lblNewLabel);
		
		tb_password = new JTextField();
		tb_password.setBounds(217, 106, 163, 20);
		frame.getContentPane().add(tb_password);
		tb_password.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Password :");
		lblNewLabel_1.setBounds(78, 109, 87, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnLogin = new JButton("Login");
		
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					createServerConnection();
					
					//ServerService serverService = new ServerService(connection);
					serverService.login(
							tb_username.getText(), 
							tb_password.getText(), new CallbackInteface() {
								
								@Override
								public void onResponse(ProtocolInterface protocolInterface) {
									//JOptionPane.showMessageDialog(null, protocolInterface.toString());
									
								}

								@Override
								public void onSuccess(Object[] result) {
									JOptionPane.showMessageDialog(null, "Success");
									
								}

								@Override
								public void onFail(int errorCode, String message) {
									JOptionPane.showMessageDialog(null, "Error "+message);
									
								}

								@Override
								public void onTimeout() {
									// TODO Auto-generated method stub
									
								}
							});
				} catch (InterruptedException interruptedException) {
					// TODO: handle exception
				}
				
			}
		});
		btnLogin.setBounds(214, 161, 166, 23);
		frame.getContentPane().add(btnLogin);
	}

	@Override
	public void consume(ProtocolInterface p) {
		JOptionPane.showMessageDialog(null, p.toString());
		
	}
	
	public void createServerConnection(){
		if(connection != null) return;
		try {
			connection = new ServerConnection();
			//Thread connThread = new Thread(connection);
			//connThread.start();
			serverService = new ServerService(connection);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
