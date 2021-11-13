import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonalData {
	String name, surname, pesel, phone_no, addresses_f_id;
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
	public PersonalData(ResultSet results) throws SQLException {
		this.name = results.getString(1);
		this.surname = results.getString(2);
		this.pesel=results.getString(3);
		this.phone_no = results.getString(4); 
		this.addresses_f_id = results.getString(5);
	}
}
