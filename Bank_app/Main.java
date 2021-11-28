import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.Scanner;

public class Main {
	static Connection conn;
	
	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
				"username", "password")) {
			if (conn != null) {
				Main.conn = conn;
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

	/*private static void testtransfer() throws SQLException, WrongId {
		Client cl = new Client("7001");
			System.out.println(cl);
			BankAccount b =cl.getBankAccounts().get(0);
			BankAccount b2 = new BankAccount("9003");
			System.out.println(b2);
			b.transfer("10008", "4002", 101, "9003");
			System.out.println(b);
	}*/

	public static PersonalData login(String login, String password) throws SQLException {
		password = hash(password);
		Statement statement = Main.conn.createStatement();
		//searches for personal data with given login and password
		ResultSet results = statement.executeQuery("SELECT data_id, name, surname, pesel, phone_no, addresses_address_id from personal_data where data_id = "+login+" and hashed_pswd = '"+password+"'");
		if (results.next()) {
			try {
					return new Client(login);
				} catch (WrongId e) {;}
			//here can be employee..
		}
		return null;
	}
	public static String hash(String password) {
		return password;//here be hash func
	}
}
