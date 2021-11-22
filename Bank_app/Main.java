

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

enum appState {
	LOGIN, ACCOUNT, INFO, MENU, EXIT
}

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
			runApplication(); //text app with options

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done");
	}

	private static void runApplication() throws SQLException {
		Scanner scan = new Scanner(System.in); //temp user imput. Will be replaced in the future
		appState state = appState.LOGIN;
		PersonalData client = null;
		while (state != appState.EXIT) { //basic menu using switch
			switch (state) {
			case LOGIN:
				String login = null, password = null;
				System.out.println("enter login");//at the moment data_id is login
				login = scan.nextLine();
				System.out.println("enter passworld");
				password = scan.nextLine();
				client = login(login, password);
				if (client == null)
					System.out.println("wrong login or passworld");
				else
					state = appState.MENU;
				break;
			case MENU:
				String s = "";
				System.out.println("Choose one :\ninfo\naccount\nlogout\nexit");
				s = scan.nextLine();
				if (s.equals("info"))
					state = appState.INFO;
				if (s.equals("account"))
					state = appState.ACCOUNT;
				if (s.equals("logout"))
					state = appState.LOGIN;
				if (s.equals("exit"))
					state = appState.EXIT;
				break;
			case INFO:
				System.out.println("U R awesome "+client.getName());
				scan.nextLine();
				state = appState.MENU;
				break;
			case ACCOUNT:
				scan.nextLine();
				state = appState.MENU;
				break;
			default:
				break;
			}
		}
	}

	public static PersonalData login(String login, String password) throws SQLException {
		password = hash(password);
		Statement statement = Main.conn.createStatement();
		//searches for personal data with given login and password
		ResultSet results = statement.executeQuery("SELECT name, surname, pesel, phone_no, addresses_f_id from personal_data where data_id = "+login+" and hashed_pswd = '"+password+"'");
		if (results.next()) // if not empty
			return new PersonalData(results);
		else
			return null;
	}
	public static String hash(String password) {
		return password;//here be hash func
	}
}