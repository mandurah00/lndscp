
import java.sql.*;
import java.util.ArrayList;

/**************************************************** 
Program Name: DataIO.java 
Programmer's Name: Dan Wilson 
Program Description: Database Input/Output connection 
***********************************************************/

public class DataIO {
    
    private final String DATABASE_NAME = "cis355a";
    private final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;
    private final String USER_NAME = "root";
    private final String PASSWORD = "devry123";
    
    public int addCustomer(Customer cust) throws ClassNotFoundException, SQLException {
        // ALWAYS use PreparedStatement to write to databases when we get input from users
        // to help prevent hacking. Injection attacks are very common against databases.
        // http://sqlzoo.net/hack

        // check for the driver
        Class.forName("com.mysql.cj.jdbc.Driver");  // connect to db
        
        // connect to db
        Connection conn = DriverManager.getConnection(CONNECTION_STRING, USER_NAME, PASSWORD);
        
        // add record entry
        String strSQL = "INSERT INTO landscape (CustomerName, CustomerAddress, "
                + "LandscapeType, YardLength, YardWidth, LandscapeCost) "
                + "VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, cust.getName());
        pstmt.setString(2, cust.getAddress());
        pstmt.setString(3, cust.getYardType());
        pstmt.setDouble(4, cust.getLength());
        pstmt.setDouble(5, cust.getWidth());
        pstmt.setDouble(6, cust.getTotalCost());
        
        // Execute "Prepared Statement"
        pstmt.executeUpdate();
        
        // get the generated primary key
        ResultSet results = pstmt.getGeneratedKeys();
        int generatedID = -1;
        
        // get generated ID if possible
        if(results.next())
            generatedID = results.getInt(1);
        
        // close db connection
        conn.close();
        
        return generatedID;
    }
    
    public void deleteCustomer(int customerID) throws SQLException {
        // connect to db
        Connection conn = DriverManager.getConnection(CONNECTION_STRING, USER_NAME, PASSWORD);
        
        // delete the record
        String strSQL = "DELETE FROM landscape WHERE CustomerID = ?";
        PreparedStatement pstmt = conn.prepareStatement(strSQL);
        pstmt.setInt(1, customerID);
        pstmt.execute();
        
        // close the db connection
        conn.close();
    }
    
    public ArrayList<Customer> getCustomers() throws SQLException {
        // create ArrayList so there's something to return
        ArrayList<Customer> list = new ArrayList<Customer>();
        
        // connect to db
        Connection conn = DriverManager.getConnection(CONNECTION_STRING, USER_NAME, PASSWORD);
        
        Statement stmt = conn.createStatement();
        String strSQL = "SELECT * FROM landscape";
        ResultSet rs = stmt.executeQuery(strSQL);
        
        while(rs.next()) {
            // create Customer object using row of data
            Customer cust = new Customer();
            cust.setCustomerID(rs.getInt(1));
            cust.setName(rs.getString(2));
            cust.setAddress(rs.getString(3));
            cust.setYardType(rs.getString(4));
            cust.setLength(rs.getDouble(5));
            cust.setWidth(rs.getDouble(6));
            cust.setTotalCost(rs.getDouble(7));
            
            // add the Customer object to the arrayList
            list.add(cust);
        }
        
        // close the db connection AFTER the While Loop
        conn.close();
        
        // return the ArrayList
        return list;
    }
}