import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Transaction {
	private String transaction_id, transaction_type_f_id, bank_accounts_f_id;
	java.sql.Date date;
	private int amount;
	static Map<Integer, String> transaction_types = new HashMap<Integer, String>();
	public Transaction(ResultSet results) throws SQLException {
		this.transaction_id = results.getString(1);
		this.amount = results.getInt(2);
		this.date = results.getDate(3);
		this.transaction_type_f_id = results.getString(4);
		this.bank_accounts_f_id = results.getString(5);
	}
	
	public Transaction(String transaction_id, String transaction_type_f_id, String bank_accounts_f_id, int amount, Date date) {
		this.transaction_id = transaction_id;
		this.date = date;
		this.transaction_type_f_id = transaction_type_f_id;
		this.bank_accounts_f_id = bank_accounts_f_id;
		this.amount = amount;
	}

	public Transaction(String transaction_id, String transaction_type_f_id, String bank_accounts_f_id,
			int amount) {
		this.transaction_id = transaction_id;
		java.util.Date date = new java.util.Date();
		this.date = new java.sql.Date(date.getTime());
		this.transaction_type_f_id = transaction_type_f_id;
		this.bank_accounts_f_id = bank_accounts_f_id;
		this.amount = amount;
	}
	
	public Transaction(String transaction_type_f_id, String bank_accounts_f_id,
			int amount) {
		this.transaction_id = null;
		java.util.Date date = new java.util.Date();
		this.date = new java.sql.Date(date.getTime());
		this.transaction_type_f_id = transaction_type_f_id;
		this.bank_accounts_f_id = bank_accounts_f_id;
		this.amount = amount;
	}


	public String getTransaction_type() throws SQLException {
		int i = Integer.parseInt(transaction_type_f_id);
		if (!transaction_types.containsKey(i)) {
			Statement statement = Main.conn.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM transaction_type");	
			while (results.next()) transaction_types.put(results.getInt(1), results.getString(2));
		}
		return transaction_types.get(i);
	}
	
	public void insert() throws SQLException { //insert current
		Statement statement = Main.conn.createStatement();
		if (transaction_id != null)
		statement.executeQuery("insert into transaction_history VALUES ("+transaction_id+", "+amount+", DATE '"+date+"', "+bank_accounts_f_id+","+transaction_type_f_id+" )");	
		else //INSERT INTO transaction_history(amount, "Date",BANK_ACCOUNTS_ACCOUNT_ID,TRANSACTION_TYPE_TYPE_ID) VALUES (21,TO_DATE('06-07-2021', 'DD-MM-YYYY'), 1, 2);
		statement.executeQuery("insert into transaction_history(amount, \"Date\",BANK_ACCOUNTS_ACCOUNT_ID,TRANSACTION_TYPE_TYPE_ID) VALUES ("+amount+", DATE '"+date+"', "+bank_accounts_f_id+","+transaction_type_f_id+" )");	
	}
	
	public String getTransaction_id() {
		return transaction_id;
	}
	public Date getDate() {
		return date;
	}
	public String getTransaction_type_f_id() {
		return transaction_type_f_id;
	}
	public String getBank_accounts_f_id() {
		return bank_accounts_f_id;
	}
	public int getAmount() {
		return amount;
	}
	@Override
	public String toString() {
		return "Transaction [transaction_id=" + transaction_id + ", date=" + date + ", transaction_type_f_id="
				+ transaction_type_f_id + ", bank_accounts_f_id=" + bank_accounts_f_id + ", amount=" + amount + "]";
	}
	
}
