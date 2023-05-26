import java.sql.*;
import java.util.*;
import java.util.Date;

public class Main {
    //db connect vars

    static Connection connection = null;
    static String databaseName = "a4_mydb402";
    static String url = "jdbc:mysql://localhost:3306/" + databaseName;

    static String username = "root";
    static String password = "200154cjh";

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Which company's user you are (Enter X or Y or Z): ");

        String company = in.nextLine();
        while (!company.equals("X") && !company.equals("Y") && !company.equals("Z")){
            System.out.println("Please reenter company: ");
            company = in.nextLine();
        }

        connection = DriverManager.getConnection(url, username, password);

        System.out.println("You can enter \"1\" to list all the parts.");
        System.out.println("You can enter \"2\" to list specific PO.");
        System.out.println("You can enter \"3\" to list specific line.");
        System.out.println("You can enter \"4\" to purchase an order.");
        System.out.println("You can enter \"5\" to start 2PC coordinate.");
        System.out.println("You can enter \"exit\" to exit the program.");
        System.out.print("You can enter the command now: ");

        String input = in.nextLine();
        while (!input.equals("exit")){
            if (input.equals("1")){
                list_Parts402(company);
            } else if (input.equals("2")) {
                System.out.print("What's your clientID: ");
                int clientID = in.nextInt();

                list_POs402(company, clientID);
            } else if (input.equals("3")) {
                System.out.print("What's your PO number: ");
                int poNum = in.nextInt();

                list_Lines402(company, poNum);
            } else if (input.equals("4")) {
                //get the clientID
                System.out.println("What's your clientID: ");
                int clientID = in.nextInt();
                String query = String.format("SELECT * FROM %s_Clients402 WHERE clientID402 = %s;",company, clientID);
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                //check the clientID
                if (!rs.next() || clientID == 0){
                    System.out.println("Your clientID is invalid.");
                }else {
                    //check the part number
                    System.out.println("What's part number you want to buy: ");
                    int partNo = in.nextInt();
                    boolean part_valid = check_partNo402(company, partNo);

                    if (!part_valid){
                        System.out.println("The part number is invalid.");
                    }else {
                        //get the qty
                        System.out.println("How many parts you want to buy: ");
                        int qty = in.nextInt();

                        //submit the PO
                        submit_a_PO402(company, clientID, partNo, qty);
                    }
                }
            } else if (input.equals("5")) {
                System.out.println("What's your clientID: ");
                int clientID = in.nextInt();
                String query = String.format("SELECT * FROM %s_Clients402 WHERE clientID402 = %s;",company, clientID);
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                //check the clientID
                if (!rs.next() || clientID == 0){
                    System.out.println("Your clientID is invalid.");
                }else {
                    //check the part number
                    System.out.println("What's part number you want to buy: ");
                    int partNo = in.nextInt();
                    boolean part_valid = check_partNo402(company, partNo);

                    if (!part_valid){
                        System.out.println("The part number is invalid.");
                    }else {
                        //get the qty
                        System.out.println("How many parts you want to buy: ");
                        int qty = in.nextInt();
                        boolean available = true;

                        if (company.equals("Z")){
                            //get the qoh
                            query = String.format("SELECT qoh402 FROM X_Parts402 WHERE partNo402 = %s;", partNo);
                            rs = st.executeQuery(query);
                            int qohX = 0;
                            while (rs.next()){
                                qohX = rs.getInt("qoh402");
                            }

                            query = String.format("SELECT qoh402 FROM Y_Parts402 WHERE partNo402 = %s;", partNo);
                            rs = st.executeQuery(query);
                            int qohY = 0;
                            while (rs.next()){
                                qohY = rs.getInt("qoh402");
                            }
                            //check the Availability
                            if (qohX < qty && qohY < qty){
                                available = false;
                                System.out.println("The part on hand is not enough, sorry.");
                            }
                        }else {
                            //if client is from X or Y, get the qoh
                            query = String.format("SELECT qoh402 FROM %s_Parts402 WHERE partNo402 = %s;",company, partNo);
                            rs = st.executeQuery(query);
                            int qoh = 0;
                            while (rs.next()){
                                qoh = rs.getInt("qoh402");
                            }
                            if (qoh < qty){
                                available = false;
                                System.out.println("The part on hand is not enough, sorry.");
                            }
                        }

                        if (available){
                            System.out.println("Prepare to commit.");
                            //get log id
                            query = String.format("SELECT COUNT(idlog402) FROM log402");
                            rs = st.executeQuery(query);
                            int logid = 0;
                            while (rs.next()){
                                logid = rs.getInt("COUNT(idlog402)") + 1;
                            }
                            //add the record to the log
                            query = String.format("INSERT INTO log402 VALUES ('%s', '%s', '%s', '%s', 'wait')", logid, company, qty, clientID);
                            int POsUpdate = st.executeUpdate(query);

                            System.out.println("Do you want to submit the order? You can enter \"1\" to sumbit, enter \"2\" to cancel.");
                            int temp = in.nextInt();
                            if (temp == 1){
                                //submit the PO
                                submit_a_PO402(company, clientID, partNo, qty);
                                query = String.format("INSERT INTO log402 VALUES ('%s', '%s', '%s', '%s', 'confirmed')", logid+1, company, qty, clientID);
                                POsUpdate = st.executeUpdate(query);
                                System.out.println("Order confirmed!");
                            } else if (temp == 2) {
                                query = String.format("INSERT INTO log402 VALUES ('%s', '%s', '%s', '%s', 'cancel')", logid+1, company, qty, clientID);
                                POsUpdate = st.executeUpdate(query);
                                System.out.println("Order canceled.");
                            }else {
                                query = String.format("INSERT INTO log402 VALUES ('%s', '%s', '%s', '%s', 'cancel')", logid+1, company, qty, clientID);
                                POsUpdate = st.executeUpdate(query);
                                System.out.println("Wrong command, PO cancel.");
                            }
                        }
                    }
                }
            }

            System.out.print("You can enter the command now: ");
            input = in.nextLine();
        }
    }

    static void list_Parts402(String company) throws Exception {
        String query;
        if (company.equals("X")){
            query = "SELECT partNo402, partName402, currentPrice402 FROM X_Parts402;";
        } else if (company.equals("Y")) {
            query = "SELECT partNo402, partName402, currentPrice402 FROM Y_Parts402;";
        }else {
            query = "SELECT partNo402, partName402, currentPrice402 FROM X_Parts402 UNION SELECT partNo402, partName402, currentPrice402 FROM Y_Parts402 ORDER BY partNo402;";
        }

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        //print all the parts information
        System.out.println("partNo402\tpartName402\tcurrentPrice402");
        int temp = 0;
        while (rs.next()){
            int poNo402 = rs.getInt("partNo402");

            String partName402 = rs.getString("partName402");
            int currentPrice402 = rs.getInt("currentPrice402");
            //reference: https://stackoverflow.com/questions/9643610/how-to-including-variables-within-strings
            //not print duplicated parts
            if (temp != poNo402){
                System.out.format("%s\t\t\t%s\t\t\t%s\n", poNo402, partName402, currentPrice402);
            }
            temp = poNo402;
        }
        st.close();
    }
    static void list_POs402(String company, int clientID) throws Exception{
        String query;
        query = String.format("SELECT poNo402, clientID402, dateOfPO402, status402 FROM %s_POs402 WHERE clientID402 = %s;",company, clientID);

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        //print the POs
        System.out.println("poNo402\tclientID402\tdateOfPO402\tstatusID");
        while (rs.next()){
            int poNo402 = rs.getInt("poNo402");
            int clientID402 = rs.getInt("clientID402");
            Date date = rs.getDate("dateOfPO402");
            String status = rs.getString("status402");

            System.out.format("%s\t\t%s\t\t\t\t%s\t%s\n", poNo402, clientID402, date, status);
        }
        st.close();
    }
    static void list_Lines402(String company, int poNum) throws Exception{
        String query;
        query = String.format("SELECT poNo402, lineNo402, partNo402, qty402, priceOrdered402 FROM %s_Lines402 WHERE poNo402 = %s;",company, poNum);

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);


        System.out.println("poNo402\tlineNo402\tpartNo402\tqty402\tpriceOrdered402");
        while (rs.next()){
            int poNo402 = rs.getInt("poNo402");
            int lineNo402 = rs.getInt("lineNo402");
            int partNo402 = rs.getInt("partNo402");
            int qty402 = rs.getInt("qty402");
            int priceOrdered402 = rs.getInt("priceOrdered402");
            System.out.format("%s\t\t%s\t\t\t%s\t\t\t%s\t\t%s\n", poNo402, lineNo402, partNo402, qty402, priceOrdered402);
        }
        st.close();

    }
    static void submit_a_PO402(String company, int clientID, int partNo, int qty) throws Exception{
        try {
            String query;
            Statement st = connection.createStatement();

            if (company.equals("Z")){
                //get the qoh
                query = String.format("SELECT qoh402 FROM X_Parts402 WHERE partNo402 = %s;", partNo);
                ResultSet rs = st.executeQuery(query);
                int qohX = 0;
                while (rs.next()){
                    qohX = rs.getInt("qoh402");
                }

                query = String.format("SELECT qoh402 FROM Y_Parts402 WHERE partNo402 = %s;", partNo);
                rs = st.executeQuery(query);
                int qohY = 0;
                while (rs.next()){
                    qohY = rs.getInt("qoh402");
                }
                //check the Availability
                if (qohX < qty && qohY < qty){
                    System.out.println("The part on hand is not enough, sorry.");
                }else {
                    //reference: https://www.w3schools.com/sql/sql_count_avg_sum.asp
                    //get the poNo
                    query = String.format("SELECT COUNT(poNo402) FROM Z_POs402");
                    rs = st.executeQuery(query);
                    int poNO = 0;
                    while (rs.next()){
                        poNO = rs.getInt("COUNT(poNo402)") + 1;
                    }

                    //get the lineNo
                    query = String.format("SELECT COUNT(lineNo402) FROM Z_Lines402");
                    rs = st.executeQuery(query);
                    int lineNo = 0;
                    while (rs.next()){
                        lineNo = rs.getInt("COUNT(lineNo402)") + 1;
                    }

                    //get the price from both companies
                    query = String.format("SELECT currentPrice402 FROM X_Parts402 WHERE partNo402 = %s;", partNo);
                    rs = st.executeQuery(query);
                    int X_partPrice = 999999999;
                    while (rs.next()){
                        X_partPrice = rs.getInt("currentPrice402");
                    }

                    query = String.format("SELECT currentPrice402 FROM Y_Parts402 WHERE partNo402 = %s;", partNo);
                    rs = st.executeQuery(query);
                    int Y_partPrice = 999999999;
                    while (rs.next()){
                        Y_partPrice = rs.getInt("currentPrice402");
                    }
                    //compare the price
                    if (X_partPrice < Y_partPrice && qohX >= qty){
                        //reference: https://www.javatpoint.com/java-get-current-date
                        //submit the POs
                        query = String.format("INSERT INTO Z_POs402 VALUES ('%s', '%s', 'a', '%s')", poNO, java.time.LocalDate.now(), clientID);
                        int POsUpdate = st.executeUpdate(query);

                        //submit the lines
                        query = String.format("INSERT INTO Z_Lines402 VALUES ('%s', '%s', '%s', '%s', '%s', '0', '%s')", lineNo, qty, X_partPrice*qty, poNO, partNo, partNo);
                        int LinesUpdate = st.executeUpdate(query);

                        //update the table
                        query = String.format("UPDATE X_Parts402 SET qoh402 = %s WHERE partNo402 = %s;", qohX-qty, partNo);
                        int partsUpdate = st.executeUpdate(query);

                    }else {
                        //reference: https://www.javatpoint.com/java-get-current-date
                        //submit the POs
                        query = String.format("INSERT INTO Z_POs402 VALUES ('%s', '%s', 'a', '%s')", poNO, java.time.LocalDate.now(), clientID);
                        int POsUpdate = st.executeUpdate(query);

                        //submit the lines
                        query = String.format("INSERT INTO Z_Lines402 VALUES ('%s', '%s', '%s', '%s', '0', '%s', '%s')", lineNo, qty, Y_partPrice*qty, poNO, partNo, partNo);
                        int LinesUpdate = st.executeUpdate(query);

                        //update the table
                        query = String.format("UPDATE Y_Parts402 SET qoh402 = %s WHERE partNo402 = %s;", qohY-qty, partNo);
                        int partsUpdate = st.executeUpdate(query);
                    }

                    System.out.println("PO submitted.");
                }
            }else {
                //if client is from X or Y, get the qoh
                query = String.format("SELECT qoh402 FROM %s_Parts402 WHERE partNo402 = %s;",company, partNo);
                ResultSet rs = st.executeQuery(query);
                int qoh = 0;
                while (rs.next()){
                    qoh = rs.getInt("qoh402");
                }
                if (qoh < qty){
                    System.out.println("The part on hand is not enough, sorry.");
                }else {
                    //reference: https://www.w3schools.com/sql/sql_count_avg_sum.asp
                    //get the poNo
                    query = String.format("SELECT COUNT(poNo402) FROM %s_POs402", company);
                    rs = st.executeQuery(query);
                    int poNO = 0;
                    while (rs.next()){
                        poNO = rs.getInt("COUNT(poNo402)") + 1;
                    }

                    //reference: https://www.javatpoint.com/java-get-current-date
                    //submit the POs
                    query = String.format("INSERT INTO %s_POs402 VALUES ('%s', '%s', 'a', '%s')",company, poNO, java.time.LocalDate.now(), clientID);
                    int POsUpdate = st.executeUpdate(query);

                    //get the lineNo
                    query = String.format("SELECT COUNT(lineNo402) FROM %s_Lines402", company);
                    rs = st.executeQuery(query);
                    int lineNo = 0;
                    while (rs.next()){
                        lineNo = rs.getInt("COUNT(lineNo402)") + 1;
                    }

                    //get the part price
                    query = String.format("SELECT currentPrice402 FROM %s_Parts402 WHERE partNo402 = %s;",company, partNo);
                    rs = st.executeQuery(query);
                    int partPrice = 0;
                    while (rs.next()){
                        partPrice = rs.getInt("currentPrice402");
                    }

                    //submit the lines
                    query = String.format("INSERT INTO %s_Lines402 VALUES ('%s', '%s', '%s', '%s', '%s')",company, lineNo, qty, partPrice*qty, poNO, partNo);
                    int LinesUpdate = st.executeUpdate(query);

                    //update the table
                    query = String.format("UPDATE %s_Parts402 SET qoh402 = %s WHERE partNo402 = %s;", company, qoh-qty, partNo);
                    int partsUpdate = st.executeUpdate(query);

                    System.out.println("PO submitted.");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static boolean check_partNo402(String company, int partNo) throws Exception{
        String query;
        if (company.equals("X")){
            query = "SELECT partNo402, partName402, currentPrice402 FROM X_Parts402;";
        } else if (company.equals("Y")) {
            query = "SELECT partNo402, partName402, currentPrice402 FROM Y_Parts402;";
        }else {
            query = "SELECT partNo402, partName402, currentPrice402 FROM X_Parts402 UNION SELECT partNo402, partName402, currentPrice402 FROM Y_Parts402 ORDER BY partNo402;";
        }

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        if (!rs.next() || partNo == 0){
            return false;
        }else {
            return true;
        }
    }
}