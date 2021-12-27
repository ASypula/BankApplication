import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Transaction {
	private String transaction_id, transaction_type_id, bank_account_id, target_acc_no;
	java.sql.Date date;
	private int amount;
	static Map<Integer, String> transaction_types = new HashMap<Integer, String>();
	public Transaction(ResultSet results) throws SQLException {
		this.transaction_id = results.getString(1);
		this.amount = results.getInt(2);
		this.date = results.getDate(3);
		this.bank_account_id = results.getString(4);
		this.transaction_type_id = results.getString(5);
		this.target_acc_no = results.getString(6);
	}
	
	public Transaction(String transaction_id, String transaction_type_id, String bank_account_id, int amount, Date date, String target_acc_no) {
		this.transaction_id = transaction_id;
		this.date = date;
		this.transaction_type_id = transaction_type_id;
		this.bank_account_id = bank_account_id;
		this.amount = amount;
		this.target_acc_no = target_acc_no;
	}

	public Transaction(String transaction_id, String transaction_type_id, String bank_account_id,
			int amount, String target_acc_no) {
		this.transaction_id = transaction_id;
		java.util.Date date = new java.util.Date();
		this.date = new java.sql.Date(date.getTime());
		this.transaction_type_id = transaction_type_id;
		this.bank_account_id = bank_account_id;
		this.amount = amount;
		this.target_acc_no = target_acc_no;
	}
	
	public Transaction(String transaction_type_id, String bank_account_id,
			int amount, String target_acc_no) {
		this.transaction_id = null;
		java.util.Date date = new java.util.Date();
		this.date = new java.sql.Date(date.getTime());
		this.transaction_type_id = transaction_type_id;
		this.bank_account_id = bank_account_id;
		this.amount = amount;
		this.target_acc_no = target_acc_no;
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
	
	public void insert() throws SQLException { //insert current
		Statement statement = Main.conn.createStatement();
		if (transaction_id != null)
		statement.executeQuery("INSERT INTO transaction_history VALUES ("+transaction_id+", "+amount+", DATE '"+date+"', "+bank_account_id+","+transaction_type_id+","+target_acc_no+" )");	
		else //INSERT INTO transaction_history(amount, "Date",BANK_ACCOUNTS_ACCOUNT_ID,TRANSACTION_TYPE_TYPE_ID) VALUES (21,TO_DATE('06-07-2021', 'DD-MM-YYYY'), 1, 2);
		statement.executeQuery("INSERT INTO transaction_history(amount, \"Date\",BANK_ACCOUNT_ID,TRANSACTION_TYPE_TYPE_ID, TARGET_ACC_NO) VALUES ("+amount+", DATE '"+date+"', "+bank_account_id+","+transaction_type_id+target_acc_no+" )");	
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
	public String getTarget_acc_no() {
		return target_acc_no;
	}
	@Override
	public String toString() {
		return "Transaction [transaction_id=" + transaction_id + ", date=" + date + ", transaction_type_id="
				+ transaction_type_id + ", bank_account_id=" + bank_account_id + ", amount=" + amount
				+ "target_acc_no=" + target_acc_no + "]";
	}
	
}
