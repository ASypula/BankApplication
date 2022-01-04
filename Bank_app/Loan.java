import java.sql.*;

public class Loan extends Account{
	private String loan_id;
	private Date end_date;
	private int installment, initial_value;
	
	public Loan(ResultSet results) throws SQLException {
		this.loan_id = results.getString(1);
		this.end_date = results.getDate(2);
		this.installment = results.getInt(3);
		this.initial_value = results.getInt(4);
		this.balance = results.getInt(5);
		this.start_date = results.getDate(6);
		this.interest_rate = results.getInt(7);
		this.accum_period = results.getInt(8);
		this.client_id = results.getString(9);
		this.currency_id = results.getString(10);
		this.currency_abbr = results.getString(11);
	}
	
	public Loan(String loan_id) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT loan_id, end_date, installment, initial_value, balance, start_date, interest, accum_period, client_id, currency_id, abbreviation FROM v_loans where loan_id ="
				+ loan_id;
		ResultSet results = statement.executeQuery(query);
		if (results.next()) { // if not empty
			this.loan_id = results.getString(1);
			this.end_date = results.getDate(2);
			this.installment = results.getInt(3);
			this.initial_value = results.getInt(4);
			this.balance = results.getInt(5);
			this.start_date = results.getDate(6);
			this.interest_rate = results.getInt(7);
			this.accum_period = results.getInt(8);
			this.client_id = results.getString(9);
			this.currency_id = results.getString(10);
			this.currency_abbr = results.getString(11);
		} else
			throw new WrongId(loan_id);
	}
	
	public String getLoan_id() {
		return loan_id;
	}

	public Date getEndDate() {
		return end_date;
	}

	public int getInstallment() {
		return installment;
	}

	public int getInitial_value() {
		return initial_value;
	}

	public Date nextInstallmentDate() throws SQLException {
		Date nextInstallment;
		Statement statement = Main.conn.createStatement();
		String query = "SELECT F_NEXT_INSTALLMENT(" + loan_id + ") from dual";
		ResultSet results = statement.executeQuery(query);
		if (results.next()) {
			nextInstallment = results.getDate(1);
			return nextInstallment;
		}
		return null;
	}

	public void changeCurrency(String new_curr_abbr) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT currency_id FROM currencies WHERE abbreviation=" + new_curr_abbr;
		ResultSet results = statement.executeQuery(query);
		int new_curr_id = 1;
		if (results.next()) {
			new_curr_id = results.getInt(1);
		} else {
			throw new WrongId(new_curr_abbr);
		}

		query = "SELECT service_info_id FROM loans WHERE loan_id=" + loan_id;
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
			throw new WrongId(loan_id);
		}
	}

	@Override
	public String toString() {
		return "Loan [loan_id=" + loan_id + ", end_date= " + end_date + ", installment="
				+ installment + "]";
	}
	
}
