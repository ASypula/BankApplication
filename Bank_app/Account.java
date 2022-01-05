import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Account {
	protected String client_id, currency_id, currency_abbr;
	protected int interest_rate, accum_period, balance;
	protected Date start_date;
	public int getInterest_rate() {
		return interest_rate;
	}
	public int getAccum_period() {
		return accum_period;
	}
	public int getBalance() {
		return balance;
	}
	public Date getStart_date() {
		return start_date;
	}
	public String getClient_id() {
		return client_id;
	}
	public String getCurrency_id() { return currency_id; }
	public String getCurrency_abbr() { return currency_abbr; }

	public static List<String> getCurrencyAbbreviations() throws SQLException {
		List<String> abbrs = new ArrayList<String>();
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT abbreviation from currencies");
		while (results.next()) {
			abbrs.add(results.getString(1));
		}
		return abbrs;
	}
}
