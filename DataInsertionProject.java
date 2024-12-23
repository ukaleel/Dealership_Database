import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataInsertionProject {

    public static void main(String[] args) {
        Connection conn;

        try {
            // 1. driver loading and DB connection
            conn = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/project", "root", "password");
            System.out.println("DB Connection Success!!");

            // 2. Write SQL queries to create DB table
            // CustomerID, FirstName, LastName, Email, Phone, Address
            String sql1 = "Insert INTO customer "
            		+ "VALUE(1, 'John', 'Doe', 'john.doe@email.com', '555-1234', '123 Main St'),"
            		+ "(2, 'Jane', 'Smith', 'jane.smith@email.com', '555-5678', '456 Oak St'),"
            		+ "(3, 'Mike', 'Johnson', 'mike.johnson@email.com', '555-9876', '789 Pine St'),"
            		+ "(4, 'Sarah', 'Williams', 'sarah.williams@email.com', '555-4321', '321 Elm St'),"
            		+ "(5, 'David', 'Brown', 'david.brown@email.com', '555-8765', '654 Birch St'),"
            		+ "(6, 'Emily', 'Davis', 'emily.davis@email.com', '555-2345', '234 Cedar St'),"
            		+ "(7, 'Anna', 'Ziegler', 'anna.ziegler@email.com', '444-1234', '245 Somerset St'),"
            		+ "(8, 'Chris', 'Taylor', 'chris.taylor@email.com', '444-5678', '678 Maple St'),"
            		+ "(9, 'Laura', 'White', 'laura.white@email.com', '444-9876', '789 Oak St'),"
            		+ "(10, 'Mark', 'Miller', 'mark.miller@email.com', '444-4321', '432 Pine St'),"
            		+ "(11, 'Alex', 'Johnson', 'alex.johnson@email.com', '555-1111', '111 Main St'),"
            		+ "(12, 'Sophia', 'Clark', 'sophia.clark@email.com', '555-2222', '222 Oak St'),"
            		+ "(13, 'Ryan', 'Anderson', 'ryan.anderson@email.com', '555-3333', '333 Pine St'),"
            		+ "(14, 'Olivia', 'Moore', 'olivia.moore@email.com', '555-4444', '444 Birch St'),"
            		+ "(15, 'Daniel', 'Hall', 'daniel.hall@email.com', '555-5555', '555 Cedar St')";
            
            //Insert into location_info
            // Name, Address, Phone
            String sql7 = "INSERT INTO location_info "
                    + "VALUES ('Downtown', '123 City Blvd', '555-1111'),"
                    + "('Suburb', '456 Suburb Ln', '555-2222'),"
                    + "('Airport', '789 Terminal Ave', '555-3333'),"
                    + "('Shopping Mall', '101 Shopper St', '555-4444'),"
                    + "('Beachfront', '555 Beach Rd', '555-5555'),"
                    + "('Mountain View', '777 Summit Ln', '555-6666'),"
                    + "('Business District', '321 Corporate St', '555-7777'),"
                    + "('Industrial Area', '444 Factory Rd', '555-8888'),"
                    + "('Residential Zone', '789 Home Ave', '555-9999'),"
                    + "('Historical Quarter', '888 Heritage St', '555-0000'),"
                    + "('Tech Park', '123 Tech St', '555-1122'),"
                    + "('Parkside', '456 Park Ave', '555-2233'),"
                    + "('Riverfront', '789 River Rd', '555-3344'),"
                    + "('Garden Heights', '101 Garden Blvd', '555-4455'),"
                    + "('Countryside', '555 Country Ln', '555-5566')";
            
            // LocationID, Name
            String sql2 = "INSERT INTO location "
                    + "VALUES (001, 'Downtown'),"
                    + "(002, 'Suburb'),"
                    + "(010, 'Airport'),"
                    + "(011, 'Shopping Mall'),"
                    + "(012, 'Beachfront'),"
                    + "(020, 'Mountain View'),"
                    + "(021, 'Business District'),"
                    + "(022, 'Industrial Area'),"
                    + "(100, 'Residential Zone'),"
                    + "(101, 'Historical Quarter'),"
                    + "(102, 'Tech Park'),"
                    + "(110, 'Parkside'),"
                    + "(111, 'Riverfront'),"
                    + "(112, 'Garden Heights'),"
                    + "(120, 'Countryside')";

            // VINNumber, Make, Model, Year, DailyRate, Availability, LocationID
            String sql3 = "INSERT INTO inventory "
                    + "VALUES ('1ABCD12345A112233', 'Toyota', 'Camry', 2020, 50.00, 1, 001),"
                    + "('2DCBA54321B445566', 'Honda', 'Accord', 2019, 55.00, 1, 002),"
                    + "('3AABB66666C987654', 'Ford', 'Mustang', 2021, 70.00, 0, 010),"
                    + "('4CCDD22222D001122', 'Chevrolet', 'Malibu', 2022, 60.00, 1, 011),"
                    + "('5EFGH99887E557799', 'Nissan', 'Altima', 2021, 55.00, 1, 012),"
                    + "('6TUVW13579F654321', 'Jeep', 'Wrangler', 2020, 75.00, 0, 020),"
                    + "('7XYZ12345G789012', 'Tesla', 'Model S', 2023, 120.00, 1, 021),"
                    + "('8ABC98765H123456', 'BMW', 'X5', 2022, 90.00, 1, 022),"
                    + "('9LMN45678I789012', 'Mercedes', 'C-Class', 2022, 80.00, 1, 100),"
                    + "('10PQR78901J234567', 'Audi', 'Q7', 2023, 100.00, 1, 101),"
                    + "('11STU98765K876543', 'Hyundai', 'Elantra', 2022, 45.00, 1, 102),"
                    + "('12VWX12345L654321', 'Kia', 'Sorento', 2021, 65.00, 1, 110),"
                    + "('13YZA45678M789012', 'Subaru', 'Outback', 2022, 55.00, 1, 111),"
                    + "('14BCD98765N123456', 'Mazda', 'CX-5', 2023, 70.00, 1, 112),"
                    + "('15CDE12345O654321', 'Volvo', 'XC90', 2022, 85.00, 1, 120)";

           
            // RentalID, VINNumber, TransactionID
            String sql5 = "INSERT INTO rental "
                    + "VALUES (1,'1ABCD12345A112233', 0001),"
                    + "(2,'2DCBA54321B445566', 0010),"
                    + "(3,'3AABB66666C987654', 0011),"
                    + "(4,'4CCDD22222D001122', 0100),"
                    + "(5,'5EFGH99887E557799', 0101),"
                    + "(6,'6TUVW13579F654321', 0110),"
                    + "(7,'7XYZ12345G789012', 0111),"
                    + "(8,'8ABC98765H123456', 1000),"
                    + "(9,'9LMN45678I789012', 1001),"
                    + "(10,'10PQR78901J234567', 1010),"
                    + "(11,'11STU98765K876543', 1011),"
                    + "(12,'12VWX12345L654321', 1100),"
                    + "(13,'13YZA45678M789012', 1101),"
                    + "(14,'14BCD98765N123456', 1110),"
                    + "(15,'15CDE12345O654321', 1111)";
            
            // RentalID, StartDate, EndDate, IsReservation,
            String sql6 = "INSERT INTO rental_info "
                    + "VALUES (1,'2023-01-01', '2023-01-05', 0),"
                    + "(2, '2023-02-10', '2023-02-15', 1),"
                    + "(3, '2023-03-20', '2023-03-25', 0),"
                    + "(4, '2023-04-01', '2023-04-10', 1),"
                    + "(5, '2023-05-10', '2023-05-20', 0),"
                    + "(6, '2023-06-20', '2023-06-25', 1),"
                    + "(7, '2023-07-05', '2023-07-15', 0),"
                    + "(8, '2023-08-15', '2023-08-20', 1),"
                    + "(9, '2023-09-25', '2023-09-30', 0),"
                    + "(10, '2023-10-10', '2023-10-20', 1),"
                    + "(11, '2023-11-05', '2023-11-10', 0),"
                    + "(12, '2023-12-15', '2023-12-20', 1),"
                    + "(13, '2024-01-25', '2024-01-30', 0),"
                    + "(14, '2024-02-10', '2024-02-20', 1),"
                    + "(15, '2024-03-05', '2024-03-15', 0)";

            // TransactionID, PaymentDate, PaymentAmount, PaymentMethod, PaymentStatus, CustomerID
            String sql4 = "INSERT INTO payment "
                    + "VALUES (0001, '2023-01-10', 150.00, 'Credit Card', 'Completed', 1),"
                    + "(0010, '2023-02-20', 200.00, 'Cash', 'Completed', 2),"
                    + "(0011, '2023-03-30', 180.00, 'Online', 'Pending', 3),"
                    + "(0100, '2023-04-05', 250.00, 'Credit Card', 'Completed', 4),"
                    + "(0101, '2023-05-15', 220.00, 'Cash', 'Completed', 5),"
                    + "(0110, '2023-06-25', 300.00, 'Online', 'Pending', 6),"
                    + "(0111, '2023-07-10', 180.00, 'Credit Card', 'Pending', 7),"
                    + "(1000, '2023-08-20', 250.00, 'Cash', 'Completed', 8),"
                    + "(1001, '2023-09-30', 200.00, 'Online', 'Pending', 9),"
                    + "(1010, '2023-10-15', 280.00, 'Credit Card', 'Completed', 10),"
                    + "(1011, '2023-11-05', 150.00, 'Credit Card', 'Completed', 11),"
                    + "(1100, '2023-12-20', 220.00, 'Cash', 'Pending', 12),"
                    + "(1101, '2024-01-30', 190.00, 'Online', 'Completed', 13),"
                    + "(1110, '2024-02-20', 270.00, 'Credit Card', 'Pending', 14),"
                    + "(1111, '2024-03-15', 240.00, 'Cash', 'Completed', 15)";
            
            System.out.println("All Data Points Added");

            // 3. Create the statement object to execute SQL queries.
            Statement stmt = conn.createStatement();

            // 4. Execute SQL query using the execute method of the statement object
            boolean result1 = stmt.execute(sql1);
            boolean result2 = stmt.execute(sql2);
            boolean result3 = stmt.execute(sql3);
            boolean result4 = stmt.execute(sql4);
            boolean result5 = stmt.execute(sql5);
            boolean result6 = stmt.execute(sql6);
            boolean result7 = stmt.execute(sql7);

            System.out.println("Result: " + result1);
            System.out.println("Result: " + result2);
            System.out.println("Result: " + result3);
            System.out.println("Result: " + result4);
            System.out.println("Result: " + result5);
            System.out.println("Result: " + result6);
            System.out.println("Result: " + result7);

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
