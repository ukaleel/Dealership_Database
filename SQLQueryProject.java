import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLQueryProject {

	public static void main(String[] args) {
		Connection conn;
		
		try {
			// 1. driver loading and DB connection
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root", "password");
			System.out.println("DB Connection Success!!");
			
			// 2. Write SQL queries to create DB table
			/*
			 Show all completed rental payments, who’s rental end date are prior to June, 
			 and are currently available to rent (might need to make a more practical query). 
			 Display payment amount, status, date, and transaction id utilizing a stored procedure,
			*/
			
			String u_sql1 =  "CREATE PROCEDURE Completed_Rentals_Before_June() "+ 
				"BEGIN " + 
					"SELECT p.PaymentAmount, p.PaymentStatus, p.PaymentDate, p.TransactionID " +
					"FROM Rental r " + 
					"JOIN Payment p " + 
					" ON r.TransactionID = p.TransactionID " + 
					" JOIN Inventory i " + 
					" ON r.VINNumber = i.VINNumber " + 
					" JOIN rental_info ri " +
					" ON r.RentalID = ri.RentalID " +
					"WHERE ri.IsReservation = 0 AND " + 
					" ri.EndDate < '2023-06-01' AND " + 
					"i.Availability = 1; " + 
				"END";
			
			/* 
			Show all vehicles available that are located in addresses that contain “St”. 
			Display the vehicle make, model, year, rate and VIN using a view. 
			*/
			
			String u_sql2 = "CREATE OR REPLACE VIEW Available_Vehicles_With_No_Payments AS " + 
			"SELECT i.Make, i.Model, i.Year, i.DailyRate, i.VINNumber, li.Address AS Location_Address " + 
					"FROM Inventory i JOIN Location l on i.LocationID = l.LocationID " +
					"JOIN location_info li on l.Name = li.Name " + 
			"WHERE li.Address LIKE '%St%'";
			
			/*
			Select all cash transactions that have payment amount above 200 and a daily rate greater 
			than 50 that started prior to July. Display the payment amount, daily rate, and start date.
			*/
			
			String u_sql3 = "CREATE OR REPLACE VIEW High_Value_Cash_Transactions AS " +
			"SELECT p.PaymentAmount, i.DailyRate, ri.StartDate " +
					"FROM Payment p JOIN Rental r ON p.TransactionID = r.TransactionID " +
		              "JOIN Inventory i ON r.VINNumber = i.VINNumber " +
		              "JOIN rental_info ri ON r.RentalID = ri.RentalID " +
					"WHERE p.PaymentMethod = 'Cash' AND " +
		              "p.PaymentAmount > 200 AND " +
		              "i.DailyRate > 50 AND " +
		              "ri.StartDate < '2023-07-01'";
			
			// Displays the average length of rental by location
			String b_sql1 = "SELECT location.Name, "
					+ "round(AVG(DATEDIFF(rental_info.EndDate, rental_info.StartDate)),1) AS AvgRentalDuration "
					+ "FROM location "
					+ "JOIN inventory ON location.LocationID = inventory.LocationID "
					+ "JOIN rental ON inventory.VINNumber = rental.VINNumber "
					+ "JOIN rental_info ON rental.RentalID = rental_info.RentalID "
					+ "GROUP BY location.Name;";
			
			// Displays the first 2 reservations along with customer data
			String b_sql2 = "SELECT rental.RentalID, rental.VINNumber, rental_info.StartDate, "
					+ "rental_info.EndDate, rental_info.IsReservation, "
					+ "       customer.FirstName, customer.LastName, customer.Email, customer.Phone "
					+ "FROM customer "
					+ "LEFT JOIN rental ON customer.CustomerID = rental.TransactionID "
					+ "LEFT JOIN rental_info ON rental.RentalID = rental_info.RentalID AND "
					+ "rental_info.IsReservation = 1 "
					+ "ORDER BY rental.RentalID DESC, customer.CustomerID DESC "
					+ "LIMIT 2;";
			
			// Displays how many cars are at a given location given a certain month (1-12)
			String b_sql3 = "SELECT location.Name, COUNT(DISTINCT inventory.VINNumber) AS CarsOnLocation "
					+ "FROM location "
					+ "LEFT JOIN inventory ON location.LocationID = inventory.LocationID "
					+ "LEFT JOIN rental ON inventory.VINNumber = rental.VINNumber "
					+ "LEFT JOIN rental_info ON rental.RentalID = rental_info.RentalID "
					+ "WHERE MONTH(COALESCE(rental_info.StartDate, rental_info.EndDate)) = 2 "
					+ "   OR (rental_info.StartDate IS NULL AND rental_info.EndDate IS NULL) "
					+ "GROUP BY location.Name;";
			
			// Retrieves the vehicles that are being reserved for rental by customers at their respective locations
			String a_sql1 = "CREATE PROCEDURE reserved_inventory() "
					+ "BEGIN "
					+ " SELECT C.FirstName, C.LastName, I.VINNumber, "
					+ " CONCAT(LI.Name, \", \", LI.Address) AS Location, StartDate, EndDate "
					+ "    FROM customer C, payment P, rental R, inventory I, location L, "
					+ "		location_info LI, rental_info RI "
					+ "    WHERE C.CustomerID = P.CustomerID "
					+ "		AND P.TransactionID = R.TransactionID "
					+ "		AND R.VINNumber = I.VINNumber "
					+ "		AND I.LocationID = L.LocationID "
					+ "		AND L.Name = LI.Name "
					+ "		AND R.RentalID = RI.RentalID "
					+ "    AND IsReservation = 1; "
					+ "END";
			
			String a_sql4 = "call project.reserved_inventory()";
			
			// Retrieves all of the rented cars at every location, with the renter's full name
			String a_sql2 = "CREATE OR REPLACE VIEW rented_inventory AS "
					+ "SELECT CONCAT(LI.Name, \", \", LI.Address) AS Location, "
					+ " CONCAT(Year, \" \", Make, \" \", Model) AS Vehicle, "
					+ "	CONCAT(C.LastName, \", \", C.FirstName) AS \"Rented By\" "
					+ "FROM location L, inventory I, rental R, payment P, customer C, location_info LI "
					+ "WHERE L.LocationID = I.LocationID "
					+ "	AND I.VINNumber = R.VINNumber "
					+ " AND R.TransactionID = P.TransactionID "
					+ "	AND P.CustomerID = C.CustomerID "
					+ " AND L.Name = LI.Name "
					+ "AND Availability = 0 "
					+ "ORDER BY L.LocationID;";
			
			String a_sql5 = "SELECT * FROM project.rented_inventory;";
			
			// Calculates the daily revenue based on availability of the vehicles and their daily rates
			String a_sql3 = "CREATE FUNCTION potential_revenue (availability_var int) RETURNS VARCHAR(45) "
					+ "BEGIN "
					+ "	DECLARE revenue DECIMAL(9, 2); "
					+ "    DECLARE total_rentals DECIMAL(9, 2); "
					+ "    DECLARE total_daily_rate DECIMAL(9, 2); "
					+ ""
					+ "    SELECT COUNT(RentalID), SUM(DailyRate) "
					+ "    INTO total_rentals, total_daily_rate "
					+ "    FROM rental R, inventory I "
					+ "    WHERE R.VINNumber = I.VINNumber AND Availability = availability_var; "
					+ ""
					+ "    SET revenue = ROUND(total_rentals * total_daily_rate, 2); "
					+ "RETURN CONCAT(\"$\", revenue); "
					+ "END";
			
			String a_sql6 = "SELECT project.potential_revenue(1)";
			String a_sql7 = "SELECT project.potential_revenue(0)";
			
			// 3. Create the statement object to execute SQL queries.
			Statement stmt = conn.createStatement();
			
			// 4. Execute SQL query using the execute method of the statement object
			// SQL Query Creation
			stmt.execute(u_sql1);	// creates Completed_Rentals_Before_June()
			stmt.execute(u_sql2);	// creates available_vehicles_with_no_payments
			stmt.execute(u_sql3);	// creates high_value_cash_transactions
			
			stmt.execute(a_sql1);	// creates reserved_inventory()
			stmt.execute(a_sql2);	// creates rented_inventory
			stmt.execute(a_sql3);	// creates potential_revenue()
			
			String u_sql4 = "call project.Completed_Rentals_Before_June()";
			String u_sql5 = "SELECT * " + "FROM project.available_vehicles_with_no_payments;";
			String u_sql6 = "SELECT * "+" FROM project.high_value_cash_transactions;";
			
			// Output for Completed_Rentals_Before_June()
			ResultSet rs1 = stmt.executeQuery(u_sql4);

			System.out.println("\n===Procedure Call for Completed_Rentals_Before_June===");
			while (rs1.next()) {
				Double PaymentAmount = rs1.getDouble("PaymentAmount");
				String PaymentStatus = rs1.getString("PaymentStatus");
				String PaymentDate = rs1.getString("PaymentDate");
				int TransactionID = rs1.getInt("TransactionID");

				System.out.println("PaymentAmount: " + PaymentAmount + "\n" + "PaymentStatus: " + PaymentStatus + "\n" + "PaymentDate: " + PaymentDate + "\n"
						+ "TransactionID: " + TransactionID + "\n");
			}

			// Output for available_vehicles_with_no_payments
			ResultSet rs2 = stmt.executeQuery(u_sql5);

			System.out.println("===View Call for available_vehicles_with_no_payments===");
			while (rs2.next()) {
				String Make = rs2.getString("Make");
				String Model = rs2.getString("Model");
				int Year = rs2.getInt("Year");
				Double DailyRate = rs2.getDouble("DailyRate");
				String VINNumber = rs2.getString("VINNumber");
				String Location_Address = rs2.getString("Location_Address");

				System.out.println("Make: " + Make + "\n" + "Model: " + Model + 
						"\n" + "Year: " + Year + "\n" +  "DailyRate: " + DailyRate + 
						"\n" + "VINNumber: " + VINNumber + "\n" + "Location_Address: " + 
						Location_Address + "\n");
			}
			
			// Output for high_value_cash_transactions
			ResultSet rs3 = stmt.executeQuery(u_sql6);
			
			System.out.println("===View Call for high_value_cash_transactions===");
			while (rs3.next()) {
				Double PaymentAmount = rs3.getDouble("PaymentAmount");
				Double DailyRate = rs3.getDouble("DailyRate");
				String StartDate = rs3.getString("StartDate");

				System.out.println("PaymentAmount: " + PaymentAmount + "\n" + 
						"DailyRate: " + DailyRate + "\n" + "StartDate: " + StartDate + "\n");
			}
			
			// Output for the average length of rental by location
			ResultSet rs4 = stmt.executeQuery(b_sql1);
			
			System.out.println("=== Average Length of Rental By Location ===");
			while(rs4.next()) {
				String Name = rs4.getString("Name");
				String AvgRentalDuration = rs4.getString("AvgRentalDuration");
				
				System.out.println("Name: " + Name + "\n" + 
						"Average Rental Duration: " + AvgRentalDuration + "\n");
			}
			
			// Output for the 2 current reservations along with customer data
			ResultSet rs5 = stmt.executeQuery(b_sql2);
			
			System.out.println("=== First 2 Current Reservations ===");
			while(rs5.next()) {
				int RentalID = rs5.getInt("RentalID");
				String VINNumber = rs5.getString("VINNumber");
				String StartDate = rs5.getString("StartDate");
				String EndDate = rs5.getString("EndDate");
				int isReservation = rs5.getInt("IsReservation");
				String FirstName = rs5.getString("FirstName");
				String LastName = rs5.getString("LastName");
				String Email = rs5.getString("Email");
				String Phone = rs5.getString("Phone");
				
				System.out.println("RentalID: " + RentalID + "\n" + 
						"VIN Number: " + VINNumber + "\n" + 
						"Start Date: " + StartDate + "\n" +
						"End Date: " + EndDate + "\n" + 
						"Reservation Status: " + isReservation + "\n" +
						"First Name: " + FirstName + "\n" +
						"Last Name: " + LastName + "\n" +
						"Email: " + Email + "\n" +
						"Phone: " + Phone + "\n");
			}
			
			// Output for how many cars at a given location in a certain month
			ResultSet rs6 = stmt.executeQuery(b_sql3);
			
			System.out.println("=== Cars At a Location in a given Month ===");
			while(rs6.next()) {
				String Name = rs6.getString("Name");
				int CarsOnLocation = rs6.getInt("CarsOnLocation");
				
				System.out.println("Location: " + Name + "\n" +
						"# Cars on Location: " + CarsOnLocation + "\n");
			}
			
			// Output for reserved_inventory()
			ResultSet a_rs1 = stmt.executeQuery(a_sql4);
			
			System.out.println("=== Procedure Call for reserved_inventory() ===");
			while(a_rs1.next()) {
				String FirstName = a_rs1.getString("FirstName");
				String LastName = a_rs1.getString("LastName");
				String VINNumber = a_rs1.getString("VINNumber");
				String Location = a_rs1.getString("Location");
				String StartDate = a_rs1.getString("StartDate");
				String EndDate = a_rs1.getString("EndDate");
				
				System.out.println("First Name: " + FirstName + "\n" + "Last Name: " + LastName + "\n" 
						+ "VIN Number: "+ VINNumber + "\n" + "Location: " + Location + "\n" 
						+ "Start Date: " + StartDate + "\n" + "End Date: "+ EndDate + "\n");
			}
			
			// Output for rented_inventory()
			ResultSet a_rs2 = stmt.executeQuery(a_sql5);
			
			System.out.println("\n=== View Call for rented_inventory ===");
			while(a_rs2.next()) {
				String Location = a_rs2.getString("Location");
				String Vehicle = a_rs2.getString("Vehicle");
				String Customer = a_rs2.getString("Rented By");
				
				System.out.println("Location: " + Location + "\n"
						+ "Vehicle: " + Vehicle + "\n"
						+ "Rented By: " + Customer + "\n");
			}
			
			// Output for potential_revenue()
			ResultSet a_rs3 = stmt.executeQuery(a_sql6);
			
			System.out.println("\n=== Function Call for potential_revenue(1) ===");
			while(a_rs3.next()) {
				String Revenue = a_rs3.getString("project.potential_revenue(1)");
				
				System.out.println("Potential Daily Revenue for Available Rentals: " + Revenue +  "\n");
			}
			
			ResultSet a_rs4 = stmt.executeQuery(a_sql7);
			
			System.out.println("\n=== Function Call for potential_revenue(0) ===");
			while(a_rs4.next()) {
				String Revenue = a_rs4.getString("project.potential_revenue(0)");
				
				System.out.println("Daily Revenue of Active Rentals: " + Revenue +  "\n");
			}
			
			// 5. Close
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}
}
