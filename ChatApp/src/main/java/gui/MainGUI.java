package gui;

import java.awt.Event;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import server.ChatServer;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JTextField;

import peer.ChatClient;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class MainGUI implements MouseListener, ConsumerInterface{
	private JFrame frame;
	private Thread thread = null;
	private JTextField tb_ip;
	private JTextField tb_port;
	JLabel lblNa;
	JButton btnOnline;
	private JButton btnOffline;
	// Server
	ChatServer chatServer;
	private JButton btnConnect;
	private ChatClient chatClient;
	private JTextField message;
	JButton btnSend;
	JTextArea tb_log;
	InputStream in;
	MyInputStream mInputStream = new MyInputStream();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
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
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 368, 509);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		btnOnline = new JButton("Online");
		btnOnline.addMouseListener(this);
		btnOnline.setBounds(66, 140, 89, 23);
		frame.getContentPane().add(btnOnline);
		
		tb_ip = new JTextField();
		tb_ip.setBounds(66, 46, 236, 20);
		frame.getContentPane().add(tb_ip);
		tb_ip.setColumns(10);
		
		tb_port = new JTextField();
		tb_port.setColumns(10);
		tb_port.setBounds(66, 77, 236, 20);
		frame.getContentPane().add(tb_port);
		
		JLabel lblIp = new JLabel("ip");
		lblIp.setBounds(10, 49, 46, 14);
		frame.getContentPane().add(lblIp);
		
		JLabel lblPort = new JLabel("port");
		lblPort.setBounds(10, 80, 46, 14);
		frame.getContentPane().add(lblPort);
		
		lblNa = new JLabel("N/A");
		lblNa.setBounds(10, 11, 414, 23);
		frame.getContentPane().add(lblNa);
		
		btnOffline = new JButton("Offline");
		btnOffline.addMouseListener(null);
		btnOffline.setBounds(165, 140, 137, 23);
		frame.getContentPane().add(btnOffline);
		btnOffline.addMouseListener(this);
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(66, 108, 236, 23);
		frame.getContentPane().add(btnConnect);
		
		tb_log = new JTextArea();
		tb_log.setBounds(66, 174, 236, 248);
		frame.getContentPane().add(tb_log);
		
		message = new JTextField();
		message.setBounds(66, 439, 137, 20);
		frame.getContentPane().add(message);
		message.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(213, 438, 89, 23);
		frame.getContentPane().add(btnSend);
		
		btnConnect.addMouseListener(this);
		btnSend.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == btnOnline && btnOnline.isEnabled()) onClickOnline(e);
		if(e.getSource() == btnOffline && btnOffline.isEnabled()) onClickOffline(e);
		if(e.getSource() == btnConnect && btnConnect.isEnabled()) onClickConnect(e);
		if(e.getSource() == btnSend && btnSend.isEnabled()) onClickSend(e);
	}

	private void onClickSend(MouseEvent e) {
		String text = message.getText();
		mInputStream.sendText(text);
	}

	private void onClickOffline(MouseEvent e) {
		if(chatServer != null)
			chatServer.close();
		btnOffline.setEnabled(false);
		btnOnline.setEnabled(true);
	}

	public void onClickOnline(MouseEvent e)
	{
		// Create server
		int port = 5000;
		ChatServer s = null;
		MyOutputStream os = new MyOutputStream(this);
		for(int i = 0; i < 5; i++)
		{
			try {
				s = new ChatServer(os, port + i);
				break;
			} catch (IOException ew) {
				// Do nothing
				System.out.print("try " + i);
			}
		}
		if(s == null) JOptionPane.showMessageDialog(null, "Can't create connect","Error", JOptionPane.ERROR_MESSAGE );
		s.setInputStream(mInputStream);
		Thread t = new Thread(s);
		t.start();
		lblNa.setText(s.getServerInfo());
		btnOnline.setEnabled(false);
		btnOffline.setEnabled(true);
	}
	
	public void onClickConnect(MouseEvent e)
	{
		MyOutputStream os = new MyOutputStream(this);
		chatClient = new ChatClient(os, tb_ip.getText(), Integer.parseInt(tb_port.getText()));
		chatClient.setInputStream(mInputStream);
		Thread t = new Thread(chatClient);
		t.start();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getText(String text) {
		// Show new message
		tb_log.setText(tb_log.getText() + text);
		frame.repaint();
	}
	
	public void sendText(String text){
	}
}
