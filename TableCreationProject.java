import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TableCreationProject {

	public static void main(String[] args) {
		Connection conn;

		try {
			// 1. driver loading and DB connection
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root", "password");
			System.out.println("DB Connection Success!!");

			// 2. Write SQL queries to create DB table
			
			// customer: CustomerID, FirstName, LastName
			String sql1 = "CREATE TABLE customer" + 
					"(CustomerID int AUTO_INCREMENT not null primary key," + 
					"FirstName varchar(45) not null," + 
					"LastName varchar(45) not null," +
					"Email varchar(45) null," + 
					"Phone varchar(45) null," +
					"Address varchar(45) null)";
			
			// location: LocationID, Name; relates to location_info
			String sql2 = "CREATE TABLE location" + 
					"(LocationID int AUTO_INCREMENT not null primary key," + 
					"Name varchar(45) not null unique)";
			
			// location_info: Name, Address, Phone
			String sql7 = "CREATE TABLE location_info" + 
					"(Name varchar(45)," +
					"foreign key (Name) references location(Name)," +
					"Address varchar(45) not null," + 
					"Phone varchar(45) not null)";
			
			// inventory: VINNumber, Make, Model, Year, DailyRate, Availability, LocationID
			String sql3 = "CREATE TABLE inventory" +
				    "(VINNumber varchar(45) not null primary key," +
				    "Make varchar(45) not null," +
				    "Model varchar(45) not null," +
				    "Year int not null," +
				    "DailyRate decimal(10,2) not null," +
				    "Availability tinyint(1) not null," +
				    "LocationID int," +
				    "foreign key (LocationID) references location(LocationID))";
			
			// rental: RentalID, VINNumber, TransactionID; relates to rental_info
			String sql5 = "CREATE TABLE rental" +
				    "(RentalID int AUTO_INCREMENT not null primary key," +
				    "VINNumber varchar(45)," +
				    "TransactionID int," +
				    "foreign key (VINNumber) references inventory(VINNumber)," +
				    "foreign key (TransactionID) references payment(TransactionID))";
			
			// rental_info: RentalID, StartDate, EndDate, IsReservation
			String sql6 = "CREATE TABLE rental_info" +
				    "(RentalID int," +
					"foreign key (RentalID) references rental(RentalID)," +
				    "StartDate date not null," +
				    "EndDate date not null," +
				    "IsReservation tinyint(1) not null)";

			// payment: TransactionID, PaymentDate, PaymentAmount, PaymentMethod, PaymentStatus, CustomerID
			String sql4 = "CREATE TABLE payment" +
					"(TransactionID int AUTO_INCREMENT not null primary key," +
					"PaymentDate DATE not null," +
					"PaymentAmount decimal(10,2) null," +
					"PaymentMethod varchar(45) null," +
					"PaymentStatus varchar(45) null," +
					"CustomerID int," +
					"foreign key (CustomerID) references customer(CustomerID))";
				    
			// 3. Create the statement object to execute SQL queries.
			Statement smt = conn.createStatement();

			// 4. Execute SQL query using the execute method of the statement object
			boolean result1 = smt.execute(sql1);
			boolean result2 = smt.execute(sql2);
			boolean result3 = smt.execute(sql3);
			boolean result4 = smt.execute(sql4);
			boolean result5 = smt.execute(sql5);
			boolean result6 = smt.execute(sql6);
			boolean result7 = smt.execute(sql7);
			
			
			System.out.println("Result: " + result1);
			System.out.println("Result: " + result2);
			System.out.println("Result: " + result3);
			System.out.println("Result: " + result4);
			System.out.println("Result: " + result5);
			System.out.println("Result: " + result6);
			System.out.println("Result: " + result7);

			// 5. Close
			if (smt != null)
				smt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}
}