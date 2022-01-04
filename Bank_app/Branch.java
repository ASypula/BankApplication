import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch {
	static String[] days = {"MON","TUE","WED","THU","FRI","SAT","SUN"};
	Map<String, String> Opening_hours = new HashMap<String, String>();
	String bank_branch_id, addresses_address_id;
	int employees_no;
	public Map<String, String> getOpening_hours() {
		return Opening_hours;
	}
	public String getOpening_hours(String day) {
		return Opening_hours.get(day);
	}
	public String getBank_branch_id() {
		return bank_branch_id;
	}
	public String getAddresses_address_id() {
		return addresses_address_id;
	}
	public int getEmployees_no() {
		return employees_no;
	}
	public Address getAddress() throws SQLException, WrongId {
		return new Address(addresses_address_id);
	}
	public Branch(String bank_branch_id)  throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT OPENING_HOURS_ID, addresses_address_id, employees_no from BANK_BRANCHES where bank_branch_id = "+bank_branch_id);
		if (results.next()) { // if not empty
			String opening_hours_id = results.getString(1);
			this.bank_branch_id = bank_branch_id;
			this.addresses_address_id = results.getString(2);
			this.employees_no = results.getInt(3);
			results = statement.executeQuery("SELECT MON_HOURS, TUE_HOURS, WED_HOURS,"
			+ "THU_HOURS, FRI_HOURS,SAT_HOURS,SUN_HOURS from OPENING_HOURS where OPENING_HOURS_ID = "+opening_hours_id);
			if (results.next()) { // if not empty
				int i = 1;
				for (String day : days) {
					Opening_hours.put(day, results.getString(i++));
				}
			} else
				throw new WrongId(opening_hours_id);
		} else
			throw new WrongId(bank_branch_id);
	}

	public static List<String> getBranches() throws SQLException {
		List<String> branches = new ArrayList<String>();
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT bank_branch_id from bank_branches");
		while (results.next()) {
			branches.add(results.getString(1));
		}
		return branches;
	}
	
	@Override
	public String toString() {
		return "Branch [Opening_hours=" + Opening_hours + ", bank_branch_id=" + bank_branch_id
				+ ", addresses_address_id=" + addresses_address_id + ", employees_no=" + employees_no + "]";
	}

}
