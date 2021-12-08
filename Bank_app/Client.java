import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Client extends PersonalData {
	private String client_id, employees_id;
	
	public String getClient_id() {
		return client_id;
	}

	public String getEmployees_id() {
		return employees_id;
	}
	public Employee getEmployee() throws SQLException, WrongId {
		return new Employee(this.employees_id);
	}
	public List<BankAccount> getBankAccounts() throws SQLException {
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT bank_account_id, balance, account_no, start_date, end_date, interest_rate, accum_period, installment_size, account_types_type_id, clients_client_id from bank_accounts where clients_client_id = "+this.client_id);
		List<BankAccount> accounts = new ArrayList<BankAccount>();
		while (results.next()) {
			accounts.add(new BankAccount(results));
		}
		return accounts;
	}
	public Client(String data_id) throws SQLException, WrongId {
		super(data_id);
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT client_id, employees_employee_id from clients where PERSONAL_DATA_DATA_ID = "+data_id);
		if (results.next()) { // if not empty
			this.client_id = results.getString(1);
			this.employees_id = results.getString(2);
		} else
			throw new WrongId(data_id);
	}
	public Client(String name,String surname,String pesel,String phone_no
			,String addresses_address_id) {
		super(name,surname,pesel,phone_no,addresses_address_id);
		this.client_id = null;
		this.employees_id = "1"; //I'm a sad client I don't know how to get my emplotee ;(
	}
	public void insert(String unhashed_password) throws SQLException { //for id = null
		super.insert(unhashed_password);
		Statement statement = Main.conn.createStatement();
		statement.executeQuery("INSERT INTO CLIENTS(PERSONAL_DATA_DATA_ID,EMPLOYEES_EMPLOYEE_ID) VALUES("+data_id+","+employees_id+")");
		ResultSet results = statement.executeQuery("Select CLIENT_ID from CLIENTS where PERSONAL_DATA_DATA_ID =" + data_id);
		results.next();
		this.client_id = results.getString(1);
	}

	@Override
	public String toString() {
		return "Client [client_id=" + client_id + ", employees_id=" + employees_id + "]";
	}
}
