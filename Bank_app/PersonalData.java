import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PersonalData {
	protected String name, surname, pesel, phone_no, addresses_f_id, data_id;
	public String getData_id() {
		return data_id;
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public String getPesel() {
		return pesel;
	}
	public String getPhone_no() {
		return phone_no;
	}
	public String getAddresses_f_id() {
		return addresses_f_id;
	}
	public PersonalData() {
	}
	
	public PersonalData(ResultSet results) throws SQLException {
		this.data_id = results.getString(1);
		this.name = results.getString(2);
		this.surname = results.getString(3);
		this.pesel=results.getString(4);
		this.phone_no = results.getString(5); 
		this.addresses_f_id = results.getString(6);
	}
	public PersonalData(String data_id) throws SQLException, WrongId {
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("SELECT name, surname, pesel, phone_no, addresses_address_id from personal_data where personal_data_id = "+data_id);
		if (results.next()) { // if not empty
			this.data_id = data_id;
			this.name = results.getString(1);
			this.surname = results.getString(2);
			this.pesel=results.getString(3);
			this.phone_no = results.getString(4); 
			this.addresses_f_id = results.getString(5);
		} else
			throw new WrongId(data_id);
	}
	@Override
	public String toString() {
		return "PersonalData [name=" + name + ", surname=" + surname + ", pesel=" + pesel + ", phone_no=" + phone_no
				+ ", addresses_f_id=" + addresses_f_id + ", personal_data_id=" + data_id + "]";
	}
}

