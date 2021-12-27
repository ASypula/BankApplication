import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAccount {
	private String account_id, account_types_f_id, client_id, account_no, service_name;
	private Date start_date;
	private int interest_rate, accum_period, balance;
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
	
	public List<Transaction> getTransactions() throws SQLException {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT transaction_id, amount, \"Date\", transaction_type_type_id, bank_accounts_id FROM transaction_history WHERE bank_account_id = "+ this.account_id;
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
	
	public String getClient_id() {
		return client_id;
	}
	public String getAccount_no() {
		return account_no;
	}
	public Date getStart_date() {
		return start_date;
	}
	public int getInterest_rate() {
		return interest_rate;
	}
	public int getAccum_period() {
		return accum_period;
	}

	public int getBalance() {
		return balance;
	}
	public String getServiceName() {
		return service_name;
	}
	public boolean transfer(String transaction_type_f_id,
			int amount, String bank_accounts_receiver_id) throws SQLException {
		return transfer(null, transaction_type_f_id, amount, bank_accounts_receiver_id);
	}
	
	public boolean transfer(String transaction_id, String transaction_type_f_id,
			int amount, String bank_accounts_receiver_id) throws SQLException {
		if (amount<=0 | !balanceDec(amount) | transaction_id == bank_accounts_receiver_id)
			return false;
		Transaction t = new Transaction(transaction_id, transaction_type_f_id, getAccount_id(), -amount, bank_accounts_receiver_id);
		t.insert();
		try {
			BankAccount b = new BankAccount(bank_accounts_receiver_id);
			b.balanceInc(amount);
			t = new Transaction(transaction_id, transaction_type_f_id, bank_accounts_receiver_id, amount, bank_accounts_receiver_id);
			t.insert();
		} catch (WrongId e) {;}
		return true;
	}
	
	public BankAccount(String account_id) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT bank_account_id, balance, account_no, start_date, interest, accum_period, account_types_type_id, client_id, name FROM v_bank_accounts where bank_account_id ="
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
		} else
			throw new WrongId(account_id);
	}
	
	public void balanceInc(int i) throws SQLException {
		balance += i;
		Statement statement = Main.conn.createStatement();
		String query = "UPDATE services_info SET balance = " + balance + " WHERE client_id = " + client_id;
		statement.executeQuery(query);
	}
	public boolean balanceDec(int i) throws SQLException {
		if (i>balance) return false;
		balance -= i;
		Statement statement = Main.conn.createStatement();
		String query = "UPDATE services_info SET balance = " + balance + " WHERE client_id = " + client_id;
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
	}
	@Override
	public String toString() {
		return "BankAccount [bank_account_id=" + account_id + ", account_types_f_id=" + account_types_f_id
				+ ", client_id=" + client_id + ", account_no=" + account_no + ", start_date=" + start_date
				+ ", interest_rate=" + interest_rate + ", accum_period=" + accum_period
				+ ", balance=" + balance + ", service_name=" + service_name + " ]";
	}
	
}
