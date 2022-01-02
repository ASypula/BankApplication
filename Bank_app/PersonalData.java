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
	public Address getAddress() throws SQLException, WrongId {
		return new Address(addresses_f_id);
	}
	public PersonalData() {
	}
	public void updateAddress(String addresses_address_id) throws SQLException, WrongId {
		addresses_f_id = addresses_address_id;
		Statement statement = Main.conn.createStatement();
		String query = "UPDATE PERSONAL_DATA SET ADDRESSES_ADDRESS_ID = " + addresses_address_id + " WHERE PERSONAL_DATA_ID = " + data_id;
		statement.executeQuery(query);
	}
	public void updatePhone(String phone) throws SQLException, WrongId {
		phone_no = phone;
		Statement statement = Main.conn.createStatement();
		String query = "UPDATE PERSONAL_DATA SET phone_no = '" + phone + "' WHERE PERSONAL_DATA_ID = " + data_id;
		statement.executeQuery(query);
	}
	public void updatePassword(String unhashed_password) throws SQLException, WrongId {
		String password = Main.hash(unhashed_password);
		Statement statement = Main.conn.createStatement();
		String query = "UPDATE PERSONAL_DATA SET HASHED_PSWD = '" + password + "' WHERE PERSONAL_DATA_ID = " + data_id;
		statement.executeQuery(query);
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
	public PersonalData(String name,String surname,String pesel,String phone_no
			,String addresses_address_id) {
			this.data_id = null;
			this.name = Main.capitalize(name);
			this.surname = Main.capitalize(surname);
			this.pesel=pesel;
			this.phone_no = phone_no; 
			this.addresses_f_id = addresses_address_id;
	}
	
	public void insert(String unhashed_password) throws SQLException {
		String password = Main.hash(unhashed_password);
		Statement statement = Main.conn.createStatement();
		statement.executeQuery("INSERT INTO personal_data(NAME,SURNAME,PESEL,PHONE_NO,HASHED_PSWD,ADDRESSES_ADDRESS_ID) VALUES ('"+name+"','"+surname+"','"+pesel+"','"+phone_no+"','"+password+"',"+addresses_f_id+")");
		ResultSet results = statement.executeQuery("Select PERSONAL_DATA_ID from personal_data where name='"+name+"' and surname='"+surname+"' and pesel = '"+pesel+"' and phone_no = '"+phone_no+"' and hashed_pswd = '"+password+"' and addresses_address_id ="+addresses_f_id);
		results.next();
		this.data_id = results.getString(1);
	}
	
	@Override
	public String toString() {
		return "PersonalData [name=" + name + ", surname=" + surname + ", pesel=" + pesel + ", phone_no=" + phone_no
				+ ", addresses_f_id=" + addresses_f_id + ", personal_data_id=" + data_id + "]";
	}
}

