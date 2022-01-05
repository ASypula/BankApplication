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
	public void addBankAccount(String currency_abbr, String account_no, String Acc_Type_name, int interest_rate, int accum_period) throws SQLException, WrongId {
		BankAccount a1 = new BankAccount(currency_abbr, account_no, Acc_Type_name, client_id, interest_rate, accum_period, 0/*balance*/);
		a1.insert();
	}
	public List<Loan> getLoans() throws SQLException {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT loan_id, end_date, installment, initial_value, balance, start_date, interest, accum_period, client_id, currency_id, abbreviation FROM v_loans where client_id = "+this.client_id;
		ResultSet results = statement.executeQuery(query);
		List<Loan> accounts = new ArrayList<Loan>();
		while (results.next()) {
			accounts.add(new Loan(results));
		}
		return accounts;
	}
	public List<BankAccount> getBankAccounts() throws SQLException {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT bank_account_id, balance, account_no, start_date, interest, accum_period, account_types_type_id, client_id, name, currency_id, abbreviation FROM v_bank_accounts where client_id = "+this.client_id;
		ResultSet results = statement.executeQuery(query);
		List<BankAccount> accounts = new ArrayList<BankAccount>();
		while (results.next()) {
			BankAccount a = new BankAccount(results);
			if (a.getAccount_types_f_id().equals("4") || a.getAccount_types_f_id().equals("5"));
			else
			accounts.add(a);
		}
		return accounts;
	}
	public List<BankAccount> getDeposits() throws SQLException {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT bank_account_id, balance, account_no, start_date, interest, accum_period, account_types_type_id, client_id, name, currency_id, abbreviation FROM v_bank_accounts where client_id = "+this.client_id;
		ResultSet results = statement.executeQuery(query);
		List<BankAccount> accounts = new ArrayList<BankAccount>();
		while (results.next()) {
			BankAccount a = new BankAccount(results);
			if (a.getAccount_types_f_id().equals("4") || a.getAccount_types_f_id().equals("5"))
			accounts.add(a);
			else;
		}
		return accounts;
	}
	public Client(String data_id) throws SQLException, WrongId {
		super(data_id);
		Statement statement = Main.conn.createStatement();
		String query = "SELECT client_id, employees_employee_id from clients where PERSONAL_DATA_DATA_ID = " + data_id;
		ResultSet results = statement.executeQuery(query);
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
		this.employees_id = "1"; //I'm a sad client I don't know how to get my employee ;(
//		TODO: Should choose employee with the least clients
	}

	public void insert(String unhashed_password) throws SQLException { //for id = null
		super.insert(unhashed_password);
		Statement statement = Main.conn.createStatement();
		String query1 = "INSERT INTO clients(personal_data_data_id, employees_employee_id) VALUES(" + data_id + ","
				+ employees_id + ")";
		statement.executeQuery(query1);
		String query2 = "SELECT client_id, employees_employee_id FROM clients WHERE personal_data_data_id =" + data_id;
		ResultSet results = statement.executeQuery(query2);
		results.next();
		this.client_id = results.getString(1);
		this.employees_id = results.getString(2);
	}

	@Override
	public String toString() {
		return "Client [client_id=" + client_id + ", employees_id=" + employees_id + "]";
	}
}
