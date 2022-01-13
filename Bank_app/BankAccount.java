import java.sql.*;
import java.util.*;

public class BankAccount extends Account {
	private String account_id, account_types_f_id, account_no, service_name;
	static Map<Integer, String> account_types = new HashMap<Integer, String>();
	public String getAccount_id() {
		return account_id;
	}
	public String getAccount_types_f_id() {
		return account_types_f_id;
	}
	public String getAccount_type() throws SQLException {
		int i = Integer.parseInt(account_types_f_id);
		if (!account_types.containsKey(i)) {
			Statement statement = Main.conn.createStatement();
			String query = "SELECT * FROM account_types";
			ResultSet results = statement.executeQuery(query);	
			while (results.next()) account_types.put(results.getInt(1), results.getString(2));
		}
		return account_types.get(i);
	}

	public static List<String> getNotDepositAccountTypes() throws SQLException {
		List<String> types = new ArrayList<String>();
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT name FROM account_types WHERE name NOT LIKE '%deposit'");
		while (results.next()) {
			types.add(results.getString(1));
		}
		return types;
	}
	
	public List<Transaction> getTransactions() throws SQLException {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT transaction_id, amount, \"Date\", bank_account_id, transaction_type_type_id, target_acc_no, currency FROM transaction_history" +
				" WHERE bank_account_id = " + this.account_id + "ORDER BY transaction_id DESC";
		ResultSet results = statement.executeQuery(query);
		List<Transaction> accounts = new ArrayList<Transaction>();
		while (results.next()) {
			accounts.add(new Transaction(results));
		}
		return accounts;
	}
	
	public List<Card> getCards() throws SQLException {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT card_id, expiration_date, hashed_ccv, hashed_pin, card_types_type_id, bank_accounts_account_id FROM cards WHERE bank_accounts_account_id = "+ this.account_id;
		ResultSet results = statement.executeQuery(query);
		List<Card> accounts = new ArrayList<Card>();
		while (results.next()) {
			accounts.add(new Card(results));
		}
		return accounts;
	}
	public String getAccount_no() {
		return account_no;
	}
	public String getServiceName() {
		return service_name;
	}

	public boolean transfer(int amount, String bank_accounts_receiver_no) throws SQLException, WrongId {
		String trans_type_id;
		Statement statement = Main.conn.createStatement();
		String query = "SELECT transaction_type_id FROM transaction_type WHERE type_name = 'transfer_out'";
		ResultSet results = statement.executeQuery(query);
		if (results.next())
			trans_type_id = results.getString(1);
		else
			throw new WrongId("transfer_out");

		query = "SELECT bank_account_id FROM bank_accounts WHERE account_no = '" + bank_accounts_receiver_no + "'";
		results = statement.executeQuery(query);
		if (results.next())
			return transfer(trans_type_id, amount, results.getString(1));
		return false;
	}
	public boolean payment(int amount) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT transaction_type_id FROM transaction_type WHERE type_name = 'payment'";
		ResultSet results = statement.executeQuery(query);
		if (results.next())
			return transfer(results.getString(1), amount, null);
		else
			throw new WrongId("payment");
	}
	public boolean withdrawal(int amount) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT transaction_type_id FROM transaction_type WHERE type_name = 'withdrawal'";
		ResultSet results = statement.executeQuery(query);
		if (results.next())
			return transfer(results.getString(1), amount, null);
		else
			throw new WrongId("withdrawal");
	}

	public boolean transfer(String transaction_type_f_id,
			int amount, String bank_accounts_receiver_id) throws SQLException {
		if (amount>0 && !Objects.equals(this.getAccount_id(), bank_accounts_receiver_id)) {
			if (Objects.equals(transaction_type_f_id, "2"))
				balanceInc(amount);
			else if (!balanceDec(amount))
				return false;
		} else { return false; }
		Transaction t = new Transaction(transaction_type_f_id, getAccount_id(), amount, bank_accounts_receiver_id);
		t.insert();
		return true;
	}
	
	public BankAccount(String account_id) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT bank_account_id, balance, account_no, start_date, interest, accum_period, account_types_type_id, client_id, name, currency_id, abbreviation FROM v_bank_accounts where bank_account_id ="
				+ account_id;
		ResultSet results = statement.executeQuery(query);
		if (results.next()) { // if not empty
			this.account_id = results.getString(1);
			this.balance = results.getInt(2);		
			this.account_no = results.getString(3);
			this.start_date = results.getDate(4);
			this.interest_rate = results.getInt(5);
			this.accum_period = results.getInt(6);
			this.account_types_f_id = results.getString(7);
			this.client_id = results.getString(8);
			this.service_name = results.getString(9);
			this.currency_id = results.getString(10);
			this.currency_abbr = results.getString(11);
		} else
			throw new WrongId(account_id);
	}
	public BankAccount(String currency_abbr, String account_no, String service_name, String client_id, int interest_rate, int accum_period, int balance) throws SQLException, WrongId{
		Statement statement = Main.conn.createStatement(); 
		ResultSet results;
		this.client_id = client_id;
		this.interest_rate = interest_rate; //
		this.accum_period = accum_period;
		this.balance = balance; //
		java.util.Date date = new java.util.Date();
		this.start_date = new java.sql.Date(date.getTime());;
		this.service_name = service_name;
		results = statement.executeQuery("Select ACCOUNT_TYPE_ID from ACCOUNT_TYPES where NAME = '"+service_name+"'");
		results.next();this.account_types_f_id = results.getString(1);
		this.account_no = account_no;
		this.currency_abbr = currency_abbr;
		results = statement.executeQuery("Select CURRENCY_ID from CURRENCIES where ABBREVIATION = '"+currency_abbr+"'");
		results.next();this.currency_id = results.getString(1);
		this.account_id = null;
	}
	
	public void insert() throws SQLException {
		Statement statement = Main.conn.createStatement();
		statement.executeQuery("INSERT INTO SERVICES_INFO(BALANCE,ACCUM_PERIOD,INTEREST,CLIENT_ID,CURRENCY_ID,STATUS) VALUES ("+balance+","+accum_period+","+interest_rate+","+client_id+","+currency_id+", (null) )");
		ResultSet results = statement.executeQuery("Select service_info_id from SERVICES_INFO where CLIENT_ID = "+client_id+" and BALANCE = "+balance+"  and ACCUM_PERIOD = " + accum_period + "  and INTEREST ="+interest_rate );
		results.next(); String service_info_id = results.getString(1);
		statement.executeQuery("INSERT INTO BANK_ACCOUNTS(ACCOUNT_TYPES_TYPE_ID,SERVICE_INFO_ID) VALUES (" + account_types_f_id + "," + service_info_id +")");
		results = statement.executeQuery("Select BANK_ACCOUNT_ID, ACCOUNT_NO from BANK_ACCOUNTS where SERVICE_INFO_ID = '"+service_info_id+"'");
		results.next();
		this.account_id = results.getString(1);
		this.account_no = results.getString(2);
	}
	public void balanceInc(int i) throws SQLException {
		balance += i;
		Statement statement = Main.conn.createStatement();
		String query = "UPDATE v_bank_accounts SET balance = " + balance + " WHERE client_id = " + client_id + " AND bank_account_id = "+account_id;
		statement.executeQuery(query);
	}
	public boolean balanceDec(int i) throws SQLException {
		if (i>balance) return false;
		balance -= i;
		Statement statement = Main.conn.createStatement();
		String query = "UPDATE v_bank_accounts SET balance = " + balance + " WHERE client_id = " + client_id + " AND bank_account_id = "+account_id;
		statement.executeQuery(query);
		return true;
	}
	public BankAccount(ResultSet results) throws SQLException {
		this.account_id = results.getString(1);
		this.balance = results.getInt(2);		
		this.account_no = results.getString(3);
		this.start_date = results.getDate(4);
		this.interest_rate = results.getInt(5);
		this.accum_period = results.getInt(6);
		this.account_types_f_id = results.getString(7);
		this.client_id = results.getString(8);
		this.service_name = results.getString(9);
		this.currency_id = results.getString(10);
		this.currency_abbr = results.getString(11);
	}

	public void changeCurrency(String new_curr_abbr) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT currency_id FROM currencies WHERE abbreviation='" + new_curr_abbr + "'";
		ResultSet results = statement.executeQuery(query);
		int new_curr_id = 1;
		if (results.next()) {
			new_curr_id = results.getInt(1);
		} else {
			throw new WrongId(new_curr_abbr);
		}

		query = "SELECT service_info_id FROM bank_accounts WHERE bank_account_id=" + account_id;
		results = statement.executeQuery(query);
		if (results.next()) {
			int service_info_id = results.getInt(1);
			CallableStatement cs = Main.conn.prepareCall("call P_CONVERT_CURRENCY (?, ?)");
			cs.setInt(1, service_info_id);
			cs.setInt(2, new_curr_id);
			cs.execute();
			currency_id = Integer.toString(new_curr_id);
			currency_abbr = new_curr_abbr;
		} else {
			throw new WrongId(account_id);
		}

		query = "SELECT balance FROM v_bank_accounts WHERE bank_account_id=" + account_id;
		results = statement.executeQuery(query);
		if (results.next()) {
			balance = results.getInt(1);
		}
	}
	@Override
	public String toString() {
		return "BankAccount [account_id=" + account_id + ", account_types_f_id=" + account_types_f_id + ", account_no="
				+ account_no + ", service_name=" + service_name + ", client_id=" + client_id + ", currency_id="
				+ currency_id + ", currency_abbr=" + currency_abbr + ", interest_rate=" + interest_rate
				+ ", accum_period=" + accum_period + ", balance=" + balance + ", start_date=" + start_date + "]";
	}
}
