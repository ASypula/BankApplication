import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Transaction {
	private String transaction_id, transaction_type_id, bank_account_id, target_acc_id, currency_abbr;
	java.sql.Date date;
	private int amount;
	static Map<Integer, String> transaction_types = new HashMap<Integer, String>();
	public Transaction(ResultSet results) throws SQLException {
		this.transaction_id = results.getString(1);
		this.amount = results.getInt(2);
		this.date = results.getDate(3);
		this.bank_account_id = results.getString(4);
		this.transaction_type_id = results.getString(5);
		this.target_acc_id = results.getString(6);
		this.currency_abbr = results.getString(7);
	}
	
	public Transaction(String transaction_id, String transaction_type_id, String bank_account_id, int amount, Date date, String target_acc_no, String currency_abbr) {
		this.transaction_id = transaction_id;
		this.date = date;
		this.transaction_type_id = transaction_type_id;
		this.bank_account_id = bank_account_id;
		this.amount = amount;
		this.target_acc_id = target_acc_no;
		this.currency_abbr = currency_abbr;
	}

	public Transaction(String transaction_id, String transaction_type_id, String bank_account_id,
			int amount, String target_acc_no, String currency_abbr) {
		this.transaction_id = transaction_id;
		java.util.Date date = new java.util.Date();
		this.date = new java.sql.Date(date.getTime());
		this.transaction_type_id = transaction_type_id;
		this.bank_account_id = bank_account_id;
		this.amount = amount;
		this.target_acc_id = target_acc_no;
		this.currency_abbr = currency_abbr;
	}
	
	public Transaction(String transaction_type_id, String bank_account_id,
			int amount, String target_acc_no) {
		this.transaction_id = null;
		java.util.Date date = new java.util.Date();
		this.date = new java.sql.Date(date.getTime());
		this.transaction_type_id = transaction_type_id;
		this.bank_account_id = bank_account_id;
		this.amount = amount;
		this.target_acc_id = target_acc_no;
	}


	public String getTransaction_type() throws SQLException {
		int i = Integer.parseInt(transaction_type_id);
		if (!transaction_types.containsKey(i)) {
			Statement statement = Main.conn.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM transaction_type");	
			while (results.next()) transaction_types.put(results.getInt(1), results.getString(2));
		}
		return transaction_types.get(i);
	}

	public String getTargetAccNo() throws SQLException {
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT account_no FROM bank_accounts WHERE bank_account_id = " + target_acc_id);
		if (results.next())
			return results.getString(1);
		else
			return "";
	}
	
	public void insert() throws SQLException { //insert current
		Statement statement = Main.conn.createStatement();
		if (transaction_id != null)
		statement.executeQuery("INSERT INTO transaction_history VALUES ("+transaction_id+", "+amount+", DATE '"+date+"', "+bank_account_id+","+transaction_type_id+","+ target_acc_id +" )");
		else
		statement.executeQuery("INSERT INTO transaction_history(amount, \"Date\",BANK_ACCOUNT_ID,TRANSACTION_TYPE_TYPE_ID, TARGET_ACC_NO) VALUES ("+amount+", DATE '"+date+"', "+bank_account_id+","+transaction_type_id+","+ target_acc_id +" )");
	}
	
	public String getTransaction_id() {
		return transaction_id;
	}
	public Date getDate() {
		return date;
	}
	public String getTransaction_type_id() {
		return transaction_type_id;
	}
	public String getBank_account_id() {
		return bank_account_id;
	}

	public int getAmount() {
		return amount;
	}
	public String getTarget_acc_id() {
		return target_acc_id;
	}
	public String getCurrency_abbr() {
		return currency_abbr;
	}
	@Override
	public String toString() {
		return "Transaction [transaction_id=" + transaction_id + ", date=" + date + ", transaction_type_id="
				+ transaction_type_id + ", bank_account_id=" + bank_account_id + ", amount=" + amount
				+ "target_acc_no=" + target_acc_id + "]";
	}
	
}
