// AddressBean.java
// Bean for interacting with the AddressBook database
//package addressbook;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

// define the data source
@DataSourceDefinition(
        name = "java:global/jdbc/pizzadb",
        className = "org.apache.derby.jdbc.ClientDataSource",
        url = "jdbc:derby://localhost:1527/pizzadb",
        databaseName = "pizzadb",
        user = "app",
        password = "app")


@ManagedBean(name = "addressBean")
@SessionScoped

public class AddressBean implements Serializable {
    // instance variables that represent one address
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private int orderID;
    private String orderTime;
    private int drinkId = 1;
    private int pizzaId = 1;
    private int sidesID = 1;
    private int pizzaQn;
    private int sidesQn;
    private int drinkQn;
    private String pizzaType;
    private String drinkType;
    private String sideType;


    // allow the server to inject the DataSource
    @Resource(lookup = "java:global/jdbc/pizzadb")
    DataSource dataSource;


    // get drink type
    public String getDrinkType() {
        return drinkType;
    }

    // set drink type
    public void setDrinkType(String drinkType) {
        this.drinkType = drinkType;
    }

    // get side type
    public String getSideType() {
        return sideType;
    }

    // set side typ
    public void setSideType(String sideType) {
        this.sideType = sideType;
    }

    //get pizza type
    public String getPizzaType() {
        return pizzaType;
    }

    // set pizza type
    public void setPizzaType(String pizzaType) {
        this.pizzaType = pizzaType;
    }


    // get the first name
    public String getFirstName() {
        return firstName;
    }

    // set the first name
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // get the last name
    public String getLastName() {
        return lastName;
    }

    // set the last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // get the email
    public String getEmailAddress() {
        return emailAddress;
    }

    // set the email
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    // get the phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // set the phoneNumber
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // get the order id
    public int getOrderID() {
        return orderID;
    }

    // set the order id
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    // get the order time
    public String getOrderTime() {
        return orderTime;
    }

    // set the order time
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    // get drink id
    public int getDrinkId() {
        return drinkId;
    }

    // set drink id
    public void setDrinkId(int drinkId) {
        this.drinkId = drinkId;
    }

    // get pizza id
    public int getPizzaId() {
        return pizzaId;
    }

    //set pizza id
    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    // get pizza quantity
    public int getPizzaQn() {
        return pizzaQn;
    }

    // set pizza quantity
    public void setPizzaQn(int pizzaQn) {
        this.pizzaQn = pizzaQn;
    }


    // get sides id
    public int getSidesID() {
        return sidesID;
    }

    //set sides id
    public void setSidesID(int sidesID) {
        this.sidesID = sidesID;
    }

    //get sides quantity
    public int getSidesQn() {
        return sidesQn;
    }

    //set set ides quantity
    public void setSidesQn(int sidesQn) {
        this.sidesQn = sidesQn;
    }

    // get drink quantity
    public int getDrinkQn() {
        return drinkQn;
    }


    //set drink quantity
    public void setDrinkQn(int drinkQn) {
        this.drinkQn = drinkQn;
    }


    // mehtod to return a ResultSet of entries
    public ResultSet getAddresses() throws SQLException {
        // check whether dataSource was injected by the server
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        // obtain a connection from the connection pool
        Connection connection = dataSource.getConnection();

        // check whether connection was successful
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {

            PreparedStatement getAddresses = connection.prepareStatement(
                    "SELECT ORDERID, PHONENUMBER, ORDERTIME, TOTALPRICE " +
                            "FROM ORDERS ORDER BY 3 DESC ");

            CachedRowSet rowSet =
                    RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(getAddresses.executeQuery());
            return rowSet;
        } finally {
            connection.close(); // return this connection to pool
        }
    }

    // method add entries  to order,customers,and order id tables
    public String save() throws SQLException {

        // Check which type and then set pizza id
        if (pizzaType.toLowerCase().contains("pepperoni")) {
            setPizzaId(1);
        }

        if (pizzaType.toLowerCase().contains("sausage")) {
            setPizzaId(2);
        }

        if (pizzaType.toLowerCase().contains("cheese")) {
            setPizzaId(3);
        }


        // Check which type and then set drink id
        if (drinkType.toLowerCase().contains("pepsi")) {
            setDrinkId(1);
        }
        if (drinkType.toLowerCase().contains("water")) {
            setDrinkId(2);
        }


        // Check which type and then set side id
        if (sideType.toLowerCase().contains("cheesesticks")) {
            setSidesID(1);
        }
        if (sideType.toLowerCase().contains("wings")) {
            setSidesID(2);
        }


        // check whether dataSource was injected by the server
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        // obtain a connection from the connection pool
        Connection connection = dataSource.getConnection();

        // check whether connection was successful
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {

            Date orderDate = new Date();
            orderID = (int) (orderDate.getTime() & 0x0000000000ffffffL); // get randome order id
            setOrderID(orderID); // set order id

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderDate); //get string of timestamp
            setOrderTime(timeStamp); // set timestamp

            // prepared statement to insert into customers table
            PreparedStatement addCustomers
                    = connection.prepareStatement("INSERT INTO CUSTOMERS "
                    + "(PHONENUMBER, FIRSTNAME, LASTNAME, EMAIL) \n"
                    + " ( SELECT ?, ?, ?, ? \n"
                    + "From CUSTOMERS \n"
                    + " WHERE PHONENUMBER = ?"
                    + " HAVING count(*)=0 )"
            );


            // specify the PreparedStatement's arguments
            addCustomers.setString(1, getPhoneNumber());
            addCustomers.setString(2, getFirstName());
            addCustomers.setString(3, getLastName());
            addCustomers.setString(4, getEmailAddress());
            addCustomers.setString(5, getPhoneNumber());
            addCustomers.executeUpdate(); // insert the entry


            // create a PreparedStatement to insert into ordereditems table
            PreparedStatement addOrderedItems =
                    connection.prepareStatement("INSERT INTO ORDEREDITEMS " +
                            "(ORDERID,PIZZAID,PIZZAQN,SIDESID,SIDESQN,DRINKID,DRINKQN)" +
                            " VALUES (?, ?, ?, ?,?,?,?)");


            // specify the PreparedStatement's arguments
            addOrderedItems.setInt(1, getOrderID());
            addOrderedItems.setInt(2, getPizzaId());
            addOrderedItems.setInt(3, getPizzaQn());
            addOrderedItems.setInt(4, getSidesID());
            addOrderedItems.setInt(5, getSidesQn());
            addOrderedItems.setInt(6, getDrinkId());
            addOrderedItems.setInt(7, getDrinkQn());


            addOrderedItems.executeUpdate(); // insert the entry


            // create a PreparedStatement to insert to orders table
            PreparedStatement addEntry =
                    connection.prepareStatement("INSERT INTO ORDERS " +
                            "(ORDERID,PHONENUMBER,ORDERTIME,TOTALPRICE)" +
                            " VALUES (?, ?, ?, ?)");

            // specify the PreparedStatement's arguments
            addEntry.setInt(1, getOrderID());
            addEntry.setString(2, getPhoneNumber());
            addEntry.setString(3, getOrderTime());
            addEntry.setDouble(4, getAmount());
            addEntry.executeUpdate(); // insert the entry

            return "index"; // go back to index.xhtml page
        } finally {
            connection.close(); // return this connection to pool
        }

    }

    // method to get total price
    public double getAmount() {

        // intiialize individual prices to 0
        double pizzaPrice = 0;
        double drinkPrice = 0;
        double sidesPrice = 0;

        // check pizza id in order to get correct price per item
        if (pizzaId == 1) {
            pizzaPrice = pizzaQn * 8.0;

        }

        if (pizzaId == 2) {
            pizzaPrice = pizzaQn * 9.0;

        }

        if (pizzaId == 3) {
            pizzaPrice = pizzaQn * 6.0;

        }


        // check drink id in order to get correct price per item
        if (drinkId == 1) {
            drinkPrice = pizzaQn * 2.0;

        }

        if (drinkId == 2) {
            drinkPrice = pizzaQn * 1.0;

        }
        if (sidesID == 1) {
            sidesPrice = pizzaQn * 4.0;

        }


        // check sides id in order to get correct price per item
        if (sidesID == 2) {
            sidesPrice = pizzaQn * 6.0;

        }

        // return sum
        return sidesPrice + pizzaPrice + drinkPrice;
    }

}


