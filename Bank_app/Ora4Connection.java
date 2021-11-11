import java.sql.*;

public class Ora4Connection {

    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl", "username", "password")) {

                Statement statement = conn.createStatement();
                ResultSet results = statement.executeQuery("SELECT name FROM employees");

                while (results.next()) {
 
                    // Get the data from the current row using the column index
                    String data = results.getString(1);
                   
                    System.out.println("Fetching data by column index for row " + results.getRow() + " : " + data);
                  }

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {  
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}