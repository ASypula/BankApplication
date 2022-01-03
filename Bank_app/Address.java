import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Address {
	String address_id, street, city_name;
	int apartment_no;
	
	public String getAddress_id() {
		return address_id;
	}
	public String getStreet() {
		return street;
	}
	public String getCity_name() {
		return city_name;
	}
	public int getApartment_no() {
		return apartment_no;
	}
	@Override
	public String toString() {
		return "Address [address_id=" + address_id + ", street=" + street + ", city_name=" + city_name
				+ ", apartment_no=" + apartment_no + "]";
	}
	public Address(String address_id) throws SQLException, WrongId {
		this.address_id = address_id;
		String query = "SELECT  street, city_name, apartment_no FROM v_addresses WHERE address_id = " + address_id;
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery(query);
		if (results.next()) { // if not empty
			this.street=results.getString(1);
			this.city_name= results.getString(2);
			this.apartment_no = results.getInt(3);
		} else
			throw new WrongId(address_id);
	}
	public Address(String city_name, String street, int apartment_no) {
		address_id=null; 
		this.street=Main.capitalize(street);
		this.city_name=Main.capitalize(city_name);
		this.apartment_no =apartment_no;
	}
	
	public void insert() throws SQLException { //for id = null
		String city_id;
		Statement statement = Main.conn.createStatement();
		ResultSet results = statement.executeQuery("select CITY_ID from cities where CITY_NAME = '"+city_name+"'");
		if (!results.next()){
			statement.executeQuery("INSERT INTO CITIES(city_name) VALUES ('"+city_name+"')");
			results = statement.executeQuery("select CITY_ID from cities where CITY_NAME = '"+city_name+"'");
			results.next();
		}
		city_id = results.getString(1);
		results = statement.executeQuery("SELECT ADDRESS_ID FROM addresses where street ='"+street+"' AND apartment_no = "+apartment_no+" AND CITIES_CITY_ID ="+city_id);
		if (!results.next()) {
			statement.executeQuery("INSERT INTO addresses(STREET,APARTMENT_NO,CITIES_CITY_ID) VALUES ('" + street + "'," + apartment_no + "," + city_id + ")");
			results = statement.executeQuery("SELECT ADDRESS_ID FROM addresses where street ='" + street + "' AND apartment_no = " + apartment_no + " AND CITIES_CITY_ID =" + city_id);
			results.next();
		}
		address_id = results.getString(1);
	}

}
