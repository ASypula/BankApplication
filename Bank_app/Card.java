import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Card {
	private String card_id, hashed_ccv, hashed_pin, card_types_f_id, bank_accounts_f_id;
	private Date expiration_date;
	static Map<Integer, String> card_types = new HashMap<Integer, String>();
	
	public Card(ResultSet results) throws SQLException {
		this.card_id = results.getString(1);
		this.expiration_date= results.getDate(2);
		this.hashed_ccv= results.getString(3);
		this.hashed_pin= results.getString(4);
		this.card_types_f_id= results.getString(5);
		this.bank_accounts_f_id= results.getString(6);
	}
	
	public String getCard_type() throws SQLException {
		int i = Integer.parseInt(card_types_f_id);
		if (!card_types.containsKey(i)) {
			Statement statement = Main.conn.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM card_types");	
			while (results.next()) card_types.put(results.getInt(1), results.getString(2));
		}
		return card_types.get(i);
	}
	
	public String getCard_id() {
		return card_id;
	}

	public String getHashed_ccv() { //empty space becouse of type: 64 char
		return hashed_ccv;
	}

	public String getHashed_pin() { //empty space becouse of type: 64 char
		return hashed_pin;
	}

	public String getCard_types_f_id() {
		return card_types_f_id;
	}

	public String getBank_accounts_f_id() {
		return bank_accounts_f_id;
	}

	public Date getExpiration_date() {
		return expiration_date;
	}

	@Override
	public String toString() {
		return "Card [card_id=" + card_id + ", hashed_ccv=" + hashed_ccv + ", hashed_pin=" + hashed_pin
				+ ", card_types_f_id=" + card_types_f_id + ", bank_accounts_f_id=" + bank_accounts_f_id
				+ ", expiration_date=" + expiration_date + "]";
	}
	
}
