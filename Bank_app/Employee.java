import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Employee extends PersonalData {

	private String salary, professions_f_id, employee_id, branch_id;
	static Map<Integer, String> profession_names = new HashMap<Integer, String>();

	public String getSalary() {
		return salary;
	}
	
	public String getBranch_id() {
		return branch_id;
	}

	public Branch getBranch() throws SQLException, WrongId {
		return new Branch(branch_id);
	}
	
	public String getProfessions_f_id() {
		return professions_f_id;
	}

	public String getEmployees_id() {
		return employee_id;
	}
	
	public List<Client> getClients() throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT PERSONAL_DATA_DATA_ID from clients where employees_employee_id = " + employee_id;
		ResultSet results = statement.executeQuery(query);
		List<Client> accounts = new ArrayList<Client>();
		while (results.next()) {
			accounts.add(new Client(results.getString(1)));
		}
		return accounts;
	}

	public String getProfessionName() throws SQLException {
		int i = Integer.parseInt(professions_f_id);
		if (!profession_names.containsKey(i)) {
			Statement statement = Main.conn.createStatement();
			String query = "SELECT profession_id, name FROM professions";
			ResultSet results = statement.executeQuery(query);
			while (results.next()) profession_names.put(results.getInt(1), results.getString(2));
		}
		return profession_names.get(i);
	}

	public static Employee getEmployeeFromPersonalDataId(String personalDataId) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT employee_id from employees where personal_data_data_id = "+personalDataId);
		if (results.next()) {
			return new Employee(results.getString(1));
		}
		return null;
	}

	public Employee(String employee_id) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT personal_data_data_id, salary, professions_profession_id, branch_id from employees where employee_id = "+employee_id);
		if (results.next()) { // if not empty
			String data_id = results.getString(1);
			this.employee_id = employee_id;
			this.salary = results.getString(2);
			this.professions_f_id = results.getString(3);
			this.branch_id = results.getString(4);
			results = statement.executeQuery("SELECT name, surname, pesel, phone_no, addresses_address_id from personal_data where personal_data_id = "+data_id);
			if (results.next()) { // if not empty
				this.data_id = data_id;
				this.name = results.getString(1);
				this.surname = results.getString(2);
				this.pesel=results.getString(3);
				this.phone_no = results.getString(4); 
				this.addresses_f_id = results.getString(5);
			} else
				throw new WrongId(data_id);
		} else
			throw new WrongId(employee_id);
	}
	public Employee(String name,String surname,String pesel,String phone_no
			,String addresses_address_id, String salary, String professions_f_id, String branch_id) {
		super(name,surname,pesel,phone_no,addresses_address_id);
		this.employee_id = null;
		this.salary = salary;
		this.professions_f_id = professions_f_id;
		this.branch_id = branch_id;
	}
	public void insert(String unhashed_password) throws SQLException { //for id = null
		super.insert(unhashed_password);
		Statement statement = Main.conn.createStatement();
		String query1 = "INSERT INTO employees(personal_data_data_id, salary, professions_profession_id, branch_id) VALUES(" + data_id+ ","+salary+ ","+professions_f_id+ ","+branch_id + ")";
		statement.executeQuery(query1);
		String query2 = "SELECT employee_id FROM employees WHERE personal_data_data_id =" + data_id;
		ResultSet results = statement.executeQuery(query2);
		results.next();
		this.employee_id = results.getString(1);
	}

	public void removeClient(String client_id) throws SQLException {
		CallableStatement cs = Main.conn.prepareCall("call P_DELETE_CLIENT (?)");
		cs.setInt(1, Integer.parseInt(client_id));
		cs.execute();
	}

	public static String getProfessionId(String professionName) throws SQLException {
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT profession_id from professions where name = '"+professionName+"'");
		if (results.next()) {
			return results.getString(1);
		}
		return null;
	}

	public static List<String> getProfessionNames() throws SQLException {
		List<String> names = new ArrayList<String>();
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT name from professions");
		while (results.next()) {
			names.add(results.getString(1));
		}
		return names;
	}

	@Override
	public String toString() {
		return "Employee [salary=" + salary + ", professions_f_id=" + professions_f_id + ", employee_id="
				+ employee_id +", branch_id="+ branch_id +"]";
	}
	
}
