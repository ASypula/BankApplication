import java.sql.Date;

public class Account {
	protected String client_id;
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
}
