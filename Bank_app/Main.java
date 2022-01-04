import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
	static Connection conn;
	
	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
				"z08", "tcpzjh")) {
			if (conn != null) {
				Main.conn = conn;
				// to be passed as arguments: recipient and mail_info
				
//				String recipient = "casa.de.papel.pap@gmail.com";
//				HashMap<String, String> mail_info = new HashMap<String, String>();
//				mail_info.put("title", "This is a title");
//				mail_info.put("msg", "This is a message");
//				Mail mail = new Mail();
//				mail.send(recipient, mail_info, true);
//				System.out.println("Mail sent");
				
				runApplication();
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void runApplication() throws SQLException {
		AppFrame frame = new AppFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		while (true) {}
	}

/*
//this func is propably too long to exist but it shows how to create new client with address
	private static void addClient(String city_name, String street, int apartment_no,
			String name,String surname,String pesel,String phone_no
			,String addresses_address_id) throws SQLException, WrongId {
		Address b = new Address(city_name, street, apartment_no);
		b.insert();
		Client c1 = new Client(name, surname, pesel, phone_no, b.getAddress_id());
		c1.insert(c1.getName());//<-password is name
	}*/

	public static PersonalData login(String login, String password) throws SQLException {
		password = hash(password);
		Statement statement = Main.conn.createStatement();
		//searches for personal data with given login and password
		ResultSet results = statement.executeQuery("SELECT personal_data_id, name, surname, pesel, phone_no, addresses_address_id "
				+ "from personal_data where personal_data_id = "+login+" and hashed_pswd = '"+password+"'");
		if (results.next()) {
			try {
					return new Client(login);
			} catch (WrongId e) {
				try {
					return Employee.getEmployeeFromPersonalDataId(login);
				} catch (WrongId ignored) {}
			}
		}
		return null;
	}

	public static String hash(String password) {
		// 
		password += "bd1";
		String hashedPassword = null;
		try {
		  MessageDigest md = MessageDigest.getInstance("SHA-256");
		  md.update(password.getBytes());
		  byte[] bytes = md.digest();
		  StringBuilder sb = new StringBuilder();
		  for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		  }
		  hashedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
		  e.printStackTrace();
		}
		return hashedPassword.toUpperCase();
	}
	public static String capitalize(String str) {
		if (str.length()>1)
		str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
		else
		str = str.toUpperCase();
		return str;
	}
}
