package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class UserTable {
	// Private
	private User mUser;      
	
	public User getLastSelected(){
		return mUser;
	}
	// Initialisation
	static 
	{
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Connection getConnection() throws SQLException
	{
		String un = "root";
		String pw = "";
		String url = "jdbc:mysql://localhost:3306/talkwithme";
		return DriverManager.getConnection(url, un, pw);
	}
	
	@Deprecated
	public boolean validUserAccount(String un, String pw){
		try {
			Connection conn = getConnection();
			String sql = "SELECT * FROM `user` WHERE `UserName` = ? AND "+
						" `Password` = ?";
			PreparedStatement statement = //new PreparedStatement((MySQLConnection) conn, sql);
					conn.prepareStatement(sql);
			statement.setString(1, un);
			statement.setString(2, pw);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				String username = resultSet.getString("UserName");
				String password = resultSet.getString("Password");
				String FullName = resultSet.getString("Fullname");
				String Ip = resultSet.getString("IP");
				int port = Integer.parseInt(resultSet.getString("Port"));
				int Status = resultSet.getInt("Status");
				User user = new User(username, password, FullName, Ip, port, (Status == 0)? true : false);
				this.mUser = user;
				return true;
			} else return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean AddUser(User user){
		try {
			Connection connection = getConnection();
			String sql  = "INSERT INTO `user`(`username`, `password`, `fullname`, `DateOfBirth`, `Sex`, `Avatar`) " +
					"VALUES(?,?,?,?,?,?)";
			PreparedStatement stat = connection.prepareStatement(sql);
			stat.setString(1, user.getUserName());
			stat.setString(2, user.getPassword());
			stat.setString(3, user.getFullName());
			stat.setString(4, user.getDateOfBirth());
			stat.setString(5, user.getSex());
			stat.setString(6, user.getAvatar());
			boolean ret = stat.execute();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public User selectUser(String username){
		Connection conn;
		try {
			conn = getConnection();
			String sql = "SELECT * FROM `user` WHERE `UserName` = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				String password = resultSet.getString("Password");
				String fname = resultSet.getString("FullName");
				boolean status = (resultSet.getInt("Status") == 0) ? true : false;
				String ip = resultSet.getString("IP");
				int port = resultSet.getInt("Port");
				User user = new User(username, password, fname, ip, port, status);
				// Addition information
				String dateOfBirth = resultSet.getString("DateOfBirth");
				String sex = resultSet.getString("Sex");
				String avatar = resultSet.getString("Avatar");
				user.setAvatar(avatar);
				user.setDateOfBirth(dateOfBirth);
				user.setSex(sex);
				return user;
			}
			//return null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}
	
	public java.util.List<User> selectFriend(String username){
		Connection conn;
		try {
			conn = getConnection();
			String sql = "SELECT * FROM `friendrelation` WHERE `UserName1` = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();
			java.util.List<User> ret = new LinkedList<>();
			while(resultSet.next()){
				String password = resultSet.getString("Password");
				String fname = resultSet.getString("FullName");
				boolean status = (resultSet.getInt("Status") == 0) ? true : false;
				String ip = resultSet.getString("IP");
				int port = resultSet.getInt("Port");
				User user = new User(username, password, fname, ip, port, status);
				ret.add(user);
			}
			return ret;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}
	
	public void updateOnlineStatus(String username, boolean status){
		try {
			Connection connection = getConnection();
			String sql  = "UPDATE `talkwithme`.`user` SET `Status`= ? WHERE `UserName`=?";
			PreparedStatement stat = connection.prepareStatement(sql);
			stat.setString(1, (status) ? "1" : "0");
			stat.setString(2, username);
			boolean ret = stat.execute();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void updateOnlineStatus(String username, boolean status, String addr, int port){
		try {
			Connection connection = getConnection();
			String sql  = "UPDATE `talkwithme`.`user` SET `Status`=?, `Port`=?, `IP`=? WHERE `UserName`=?;";
			PreparedStatement stat = connection.prepareStatement(sql);
			stat.setString(1, (status) ? "1" : "0");
			stat.setInt(2, port);
			stat.setString(3, addr);
			stat.setString(4, username);
			boolean ret = stat.execute();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
