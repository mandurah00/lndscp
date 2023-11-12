<a name="readme-top"></a>
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/mandurah00/lndscp">
    <img src="https://danwilson.dev/meta/ReadMe_lndscpHdr.gif" alt="banner">
  </a>

  <h1 align="center">Landscaping Application</h1>
  <h2 align="center">lndscp</h2>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-school-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
        <li><a href="#objectives">Objectives</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About the (School) Project

This is a school solo project with creating a Java application that works with a database.

This is a simple application that is managed by a customer salesperson for landscaping work orders.



### Objectives

* Write data from the GUI to a database
* Read data from the database and display the data on the GUI application
* Use a Three-Tier Architecture to handle large development projects

[![Product Name Screen Shot][product-screenshot]](https://danwilson.dev)



### Built With

* [![Java][Java-shield]][Java-url] 
* [![MySQL][MySQL-shield]][MySQL-url]
* [![NetBeans][Apache NetBeans IDE]][NetBeans-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

To run this application after downloading, run the project from the command line. Go to the dist folder and
type the following:

java -jar "CIS355A_-_WILSON_Landscaping.jar" 

To distribute this project, zip up the dist folder (including the lib folder)
and distribute the ZIP file. 

**DO KNOW, you will require a database to work in conjunction with this application to retain data.

### Steps to add db

#### (circa 2020)

1. We need to have the database driver in the root folder of our project.  You can get the database driver searching online and download it from the database company.  Since we are using MySQL, google “mysql java driver”.  It may change, but my top search result is “MySQL Connector/J” located at https://www.mysql.com/products/connector/   Then, click on the Download link next to “JDBC Driver for MySQL”.  Choose the operating system as “Platform Independent” and then click on the download for the “Zip Archive”. On the next page, I click the link “No thanks, just start my download.”  Once the file downloads, extract it and find the mysql-connector-java-xxxx.jar file (xxxx part will change).

2. Open your project’s folder.  Copy the mysql-connector-java-xxxx.jar file to the root folder (base folder).  Once the file is there, open your NetBeans project.  Click the  +  sign next to the project name.  Then, right-click on Libraries.  Choose “Add Jar/Folder”.  Browse and find your project’s folder.  Once you find the folder, you will see the mysql-connector-java-xxxx.jar file listed.  Double-click on the file or select it and then click Open to add the MySQL driver to your project library.

3. Open your Visio Class diagram from our design phase.  We need to update the DataIO class to match our Visio Class diagram.  Remember, DataIO stands for “Data Input and Output”.  Since we are connecting with a database, we will need to add our connection variables as constants to the top of the class.

4. If you made the password “github123” and called your schema/database “lndscp”, then your constants should be:
 ```sh
 private final String DATABASE_NAME = "lndscp";
 private final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;
 private final String USER_NAME = "root";
 private final String PASSWORD = "github123";
 ```

5. The DataIO class is set up to read and write to files.  Let’s update it so it reads and writes to the database.  Delete the import statements at the top and replace them with import statements for database access. The asterisk (‘*’) tells Java to import all classes that are referenced from the java.sql package.  We also need to return an ArrayList, so let’s import that class as well from the java.util package.
  ```sh
  import java.sql.*;
  import java.util.ArrayList;
  ```

6. Delete the code in your methods so we can add the database input/output code, including the throws statements at the end of the method headers.

7. Now, we need to set up our MySQL database so it can hold our data.  Open your MySQL Workbench.  Click on the “Local instance MySQL80” icon.

8. Type in your password, which should be “github123” (without the quotes) if you set it up following the default directions.  Otherwise, please enter your chosen password.  Click on the Schemas tab so you can see your databases, which are called Schemas in MySQL.

9. You should have a database (Schema) called lndscp.  If you do not have one, click the barrel icon (“Create new schema in the connected server”) that is located on the Taskbar at the top to create it.  Open the lndscp database/schema by clicking the triangle next to it.  Open the Tables by clicking the triangle next to it so you can see the existing tables.  Right-click on Tables and choose “Create Table”.  Create the following table and call it landscape.  Notice that CustomerID is a primary key and it has Auto Increment turned on.

10. Go back to your DataIO class.  In the addCustomer( ) method, write code to:
    a. Check for the database driver
    b. Connect to the database
    c. Add the Customer record to the landscape table
    d. Close the database connection

    Remember to throw the exceptions so the GUI class can report any possible issues!  Also, remember to use a PreparedStatement to help prevent hacking.

```sh
public int addCustomer(Customer cust) throws ClassNotFoundException, SQLException
{
	//check for driver
	Class.forName("com.mysql.cj.jdbc.Driver");

	//connect to database 
	Connection conn = DriverManager.getConnection(CONNECTION_STRING,
		USER_NAME, PASSWORD);

	//add record 
	String strSQL = "INSERT INTO landscape (CustomerName, CustomerAddress, "
		+ "LandscapeType, YardLength, YardWidth, LandscapeCost) "
		+ "VALUES(?, ?, ?, ?, ?, ?)";
	PreparedStatement pstmt = conn.prepareStatement(strSQL,
		Statement.RETURN_GENERATED_KEYS);
	pstmt.setString(1, cust.getName());
	pstmt.setString(2, cust.getAddress());
	pstmt.setString(3, cust.getYardType());
	pstmt.setDouble(4, cust.getLength());
	pstmt.setDouble(5, cust.getWidth());
	pstmt.setDouble(6, cust.getTotalCost());

	// execute the prepared statement
	pstmt.executeUpdate();

	// get the generated primary key
	ResultSet results = pstmt.getGeneratedKeys();
	int generatedID = -1;
       
	// get generated id if possible
	if (results.next())
		generatedID = results.getInt(1);
       
	//close connection 
	conn.close();

	return generatedID;
}
```
    
11. Go to your LandscapeGUI.java file.  Go to the top of your code window and replace the IOException import with an import for java.sql.*.

12. Update you submitOrder( ) method so that it looks for SQLExceptions instead of IOExceptions.  In addition, we need to look for the ClassNotFoundException in case we do not have the database driver installed correctly.

```sh
private void submitOrder()
{
	if (validateInputs() == false)
	{
	return;    // end the method if validation failed
	}

	Customer cust = createCustomer();

	try
	{
		DataIO data = new DataIO(); // create DataIO object
		data.addCustomer(cust);
		loadCustomers();  // load all customers
		lstCustomers.setSelectedIndex( customerList.size() - 1 );  // select last item

		// reset for the next customer
		reset();

		//move to the client orders tab
		tabMain.setSelectedIndex(2);
	}
	catch (SQLException ex)
	{
		JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
		"Database Error", JOptionPane.ERROR_MESSAGE);
	}
	catch (ClassNotFoundException ex)
	{
		JOptionPane.showMessageDialog(this, "Driver Not Found Error: " + ex.getMessage(),
		"Database Driver Error", JOptionPane.ERROR_MESSAGE);
	}
}
```
    
13. If you use a block comment on your btnDeleteActionPerformed( ) method and your loadCustomers( ) method, you can test your submitOrder( ) method.  For example, put a  \*  as the first line of your loadCustomers method and a  *\  as the last line.

```sh
private void loadCustomers()
{
	/*
	// the code will be “commented out” and ignored by Java
	*/
} 
```

14. Go to your DataIO class.  In order to read data from the database, we need to connect to the database, read the records, and then close the connection. As we read the records, let’s create Customer objects and then add them to the ArrayList.  Finally, we need to return the ArrayList.

```sh
public ArrayList<Customer> getCustomers() throws SQLException
{
	// create the ArrayList so we have something to return
	ArrayList<Customer> list = new ArrayList<Customer>();

	//connect to database 
	Connection conn = DriverManager.getConnection(CONNECTION_STRING,
	USER_NAME, PASSWORD);
    
	Statement statement = conn.createStatement();
	String SQL = "Select * FROM landscape";
	ResultSet rs = statement.executeQuery(SQL);
    
	while (rs.next())
	{
		// create Customer object and load the attributes
		Customer client = new Customer();
		client.setCustomerID(rs.getInt(1));
		client.setName(rs.getString(2));
		client.setAddress(rs.getString(3));
		client.setYardType(rs.getString(4));
		client.setLength(rs.getDouble(5));
		client.setWidth(rs.getDouble(6));
		client.setTotalCost(rs.getDouble(7));
              
		// add the Customer object to our list
		list.add(client);			
	}
    
	// close the database connection
	conn.close();
    
	// return the ArrayList
	return list;
}    
```

15. Save All and then go to your LandscapeGUI.java file.  Update you loadCustomers( ) method so that it looks for SQLExceptions instead of IOExceptions.

```sh
private void loadCustomers()
{
	try
	{
		DataIO data = new DataIO(); // create DataIO object
		ArrayList<Customer> customers = data.getCustomers();

		// clear out the DefaultListModel and textarea
		customerList.clear();
		txaOrderInfo.setText("");

		// copy objects from the ArrayList over to the DefaultListModel
		customerList.addAll(customers);
	}
	catch (SQLException ex)
	{
		JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
			"Database Error", JOptionPane.ERROR_MESSAGE);
	}
}
```

16. Run your application.  Click on the Customer List tab.  Click on the Load List button.  Ensure it works.

17. We need to be able to delete a customer.  Go to your DataIO class.  Change the method header so we can delete the customer based on the customer’s ID number.  In your deleteCustomer( ) method, connect to the database. Then, issue the DELETE FROM command.  Finally, close the connection to the database.

```sh
public void deleteCustomer(int customerID) throws SQLException
{
	// connect to the database
	Connection conn = DriverManager.getConnection(CONNECTION_STRING, USER_NAME, PASSWORD);
        
	// delete the record
	String SQL = "DELETE FROM landscape WHERE CustomerID = ?";
	PreparedStatement pstmt = conn.prepareStatement(SQL);
	pstmt.setInt(1, customerID);
	pstmt.execute();

	// close the database connection
	conn.close();
}
```

18. Let’s update our Delete Customer button event code in the GUI.  Save All and then go to the code and get the selected Customer object.  Then, create a DataIO object and delete the customer based on the customer’s ID number.  Finally, load the current customers using the loadCustomers( ) method.

```sh
try
{
	// get the selected object
	Customer old = lstCustomers.getSelectedValue();

	// if something is selected, delete it and clear the details textarea
	if (old != null)
	{
		DataIO data = new DataIO();
		data.deleteCustomer(old.getCustomerID());   // get the name only
		txaCustomerInfo.setText("");
		loadCustomers();
	}
}
catch (SQLException ex)
{
	JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
		"Database Error", JOptionPane.ERROR_MESSAGE);
}
```

19. Run your application and test it, including the menu.  You can now read and write to databases.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

This app is ideally for small businesses in live applications. Much can be improved on for those interested or motivated to. 

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ROADMAP -->
## Roadmap

- [x] Upload to repository
- [ ] Review application

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->
## Contributing

No Contributions are needed as this is for portfolio purposes.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Dan Wilson - [@_DanWilson](https://www.linkedin.com/in/idanwilson/) - hello@danwilson.dev

Project Link: [https://github.com/mandurah00/al26menu](https://github.com/mandurah00/al26menu)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [Best-README-Template](https://github.com/othneildrew/Best-README-Template)
* [Basic Syntax Markdown Guide](https://www.markdownguide.org/basic-syntax/)
* [Choose an Open Source License](https://choosealicense.com)
* [Img Shields](https://shields.io)
* [GitHub Pages](https://pages.github.com)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://github.com/mandurah00/al26menu/blob/main/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/idanwilson/
[product-screenshot]: https://danwilson.dev/meta/ReadMe-lndscpWDD.gif
[Java-shield]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.java.com/
[MySQL-shield]: https://img.shields.io/static/v1?style=for-the-badge&message=MySQL&color=4479A1&logo=MySQL&logoColor=FFFFFF&label=
[MySQL-url]: https://www.mysql.com/
[Apache NetBeans IDE]: https://img.shields.io/static/v1?style=for-the-badge&message=Apache+NetBeans+IDE&color=1B6AC6&logo=Apache+NetBeans+IDE&logoColor=FFFFFF&label=
[NetBeans-url]: https://netbeans.apache.org/
