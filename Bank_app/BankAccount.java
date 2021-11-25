import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAccount {
	private String account_id, account_types_f_id, clients_f_id, account_no;
	private Date start_date, end_date;
	private int interest_rate, accum_period, installment_size, balance;
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
			ResultSet results = statement.executeQuery("SELECT * from account_types");	
			while (results.next()) account_types.put(results.getInt(1), results.getString(2));
		}
		return account_types.get(i);
	}
	
	public List<Transaction> getTransactions() throws SQLException {
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT transaction_id, amount, \"Date\", transaction_type_f_id, bank_accounts_f_id from transaction_history where bank_accounts_f_id = "+this.account_id);
		List<Transaction> accounts = new ArrayList<Transaction>();
		while (results.next()) {
			accounts.add(new Transaction(results));
		}
		return accounts;
	}
	
	public List<Card> getCards() throws SQLException {
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT card_id, expiration_date, hashed_ccv, hashed_pin, card_types_f_id, bank_accounts_f_id from cards where bank_accounts_f_id = "+this.account_id);
		List<Card> accounts = new ArrayList<Card>();
		while (results.next()) {
			accounts.add(new Card(results));
		}
		return accounts;
	}
	
	public String getClients_f_id() {
		return clients_f_id;
	}
	public String getAccount_no() {
		return account_no;
	}
	public Date getStart_date() {
		return start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public int getInterest_rate() {
		return interest_rate;
	}
	public int getAccum_period() {
		return accum_period;
	}
	public int getInstallment_size() {
		return installment_size;
	}
	public int getBalance() {
		return balance;
	}
	public void balanseInc(int i) throws SQLException { //database not updating for some reason ?? 
		balance += i;
		Statement statement = Main.conn.createStatement();
		//statement.executeQuery("UPDATE bank_accounts SET balance = "+balance+" WHERE account_id = " +account_id);
	}
	public boolean balanseDec(int i) throws SQLException {
		if (i>balance) return false;
		balance -= i;
		Statement statement = Main.conn.createStatement();
		//statement.executeQuery("UPDATE bank_accounts SET balance = "+balance+" WHERE account_id = " +account_id);
		return true;
	}
	public BankAccount(ResultSet results) throws SQLException {
			this.account_id = results.getString(1);
			this.balance = results.getInt(2);		
			this.account_no = results.getString(3);
			this.start_date = results.getDate(4);
			this.end_date = results.getDate(5);
			this.interest_rate = results.getInt(6);
			this.accum_period = results.getInt(7);
			this.installment_size = results.getInt(8);
			this.account_types_f_id = results.getString(9);
			this.clients_f_id = results.getString(10);
	}
	@Override
	public String toString() {
		return "BankAccount [account_id=" + account_id + ", account_types_f_id=" + account_types_f_id
				+ ", clients_f_id=" + clients_f_id + ", account_no=" + account_no + ", start_date=" + start_date
				+ ", end_date=" + end_date + ", interest_rate=" + interest_rate + ", accum_period=" + accum_period
				+ ", installment_size=" + installment_size + ", balance=" + balance + "]";
	}
	
}
