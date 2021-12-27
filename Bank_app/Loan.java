import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Loan {
	private String loan_id, service_id;
	private Date end_date;
	private int installment, initial_value;
	
	public Loan(ResultSet results) throws SQLException {
		this.loan_id = results.getString(1);
		this.end_date = results.getDate(2);
		this.installment = results.getInt(3);
		this.initial_value = results.getInt(4);
		this.service_id = results.getString(5);
	}
	
	public Loan(String loan_id) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		String query = "SELECT loan_id, end_date, installment, initial_value, service_id FROM loans where loan_id ="
				+ loan_id;
		ResultSet results = statement.executeQuery(query);
		if (results.next()) { // if not empty
			this.loan_id = results.getString(1);
			this.end_date = results.getDate(2);
			this.installment = results.getInt(3);
			this.initial_value = results.getInt(4);
			this.service_id = results.getString(5);
		} else
			throw new WrongId(loan_id);
	}
	
	public String getLoan_id() {
		return loan_id;
	}

	public Date getEndDate() {
		return end_date;
	}

	public String getService_id() {
		return service_id;
	}

	public int getInstallment() {
		return installment;
	}

	public int getInitial_value() {
		return initial_value;
	}

	@Override
	public String toString() {
		return "Loan [loan_id=" + loan_id + ", end_date= " + end_date + ", installment="
				+ installment + "]";
	}
	
}
