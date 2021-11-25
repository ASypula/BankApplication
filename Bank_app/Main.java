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
				System.out.println("Connected to the database!");
			} else {
				System.out.println("Failed to make connection!");
			}
			
			Main.conn = conn;

			runApplication();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done");
	}

	private static void runApplication() throws SQLException {
		AppFrame frame = new AppFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		while (true) {}
	}

	/*private static void testclass() throws SQLException, WrongId {
		// TODO Auto-generated method stub
		PersonalData pd = new PersonalData("321");
		Client cl = new Client("321");
		System.out.println(pd);
		System.out.println(cl);
		System.out.println(cl.getEmployee());
		System.out.println("bnk accounts:");
		for (BankAccount b : cl.getBankAccountes()) {
			System.out.println(b);
			System.out.println(b.getAccount_type());
		} 
		BankAccount b =cl.getBankAccountes().get(0);
		
		System.out.println(b);
		Transaction t1 = new Transaction("345", "1", "82", 1000);
		t1.insert();
		for (Transaction t : b.getTransactions()) {
			System.out.println(t);
			System.out.println(t.getTransaction_type());
		}
		for (Card c : b.getCards()) {
			System.out.println(c);
			System.out.println(c.getCard_type());
		}
		new Scanner(System.in).nextLine();
	}*/

	public static PersonalData login(String login, String password) throws SQLException {
		password = hash(password);
		Statement statement = Main.conn.createStatement();
		//searches for personal data with given login and password
		ResultSet results = statement.executeQuery("SELECT data_id, name, surname, pesel, phone_no, addresses_address_id from personal_data where data_id = "+login+" and hashed_pswd = '"+password+"'");
		if (results.next()) {
//			return new PersonalData(results);
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
