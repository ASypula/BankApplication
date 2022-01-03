import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	}
	
	public Loan(String loan_id) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT loan_id, end_date, installment, initial_value, balance, start_date, interest, accum_period, client_id FROM v_loans where loan_id ="
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
		String query = "SELECT F_NEXT_INSTALLMENT(" + loan_id + ") from dual;";
		ResultSet results = statement.executeQuery(query);
		nextInstallment = results.getDate(1);
		return nextInstallment;
	}

	@Override
	public String toString() {
		return "Loan [loan_id=" + loan_id + ", end_date= " + end_date + ", installment="
				+ installment + "]";
	}
	
}
