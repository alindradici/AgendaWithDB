/**
 * Created by alin on 6/2/2016.
 */


import java.sql.*;
import java.util.*;

public class Main {

    private static int count = 0;

    public static void main(String[] args) {

        do {
            System.out.println();
            System.out.println("1-Insert user\n2-Show user names\n3-Delete user\n4-Update user\n5-Exit Agenda");
            System.out.println();
            System.out.println("enter number : ");
            count = new Scanner(System.in).nextInt();
            switch (count) {
                case 1:
                    String name = enterData("enter first name :");
                    String lastName = enterData("enter last name: ");
                    try {
                        demoCDU(usersQuerys(count, name, lastName, null));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        demoRead(usersQuerys(count, null, null, null));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    subMenu();
                    break;
                case 3:
                    String delete = enterData("enter name of contact to delete :");
                    try {
                        demoCDU(usersQuerys(count, delete, null, null));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    String search = enterData("enter name you want to update :");
                    String newFirstName = enterData("enter new name :");
                    String newLastName = enterData("enter new lastname :");
                    try {
                        demoCDU(usersQuerys(count, newFirstName, newLastName, search));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

            }
        } while (count != 5);
    }

    private static void demoCDU(String query) throws ClassNotFoundException, SQLException {

        // 1. load driver
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/4_alin";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        PreparedStatement pSt = conn.prepareStatement(query);

        // 5. execute a prepared statement
        int rowsInserted = pSt.executeUpdate();

        // 6. close the objects
        pSt.close();
        conn.close();

    }

    public static String usersQuerys(int m, String name, String password, String newName) {
        String query = "";

        switch (m) {
            case 1:
                query = "INSERT INTO agenda_db (user_name,parola) VALUES ('" + name + "','" + password + "')";
                break;
            case 2:
                query = "SELECT user_name,parola FROM agenda_db ";
                break;
            case 3:
                query = "DELETE FROM agenda_users WHERE ida=(SELECT id FROM agenda_db WHERE user_name='" + name + "' OR prenume='" + name + "' );DELETE FROM agenda_db WHERE user_name='" + name + "'";
                break;
            case 4:
                query = "UPDATE agenda_db SET user_name = '" + name + "',parola='" + password + "' WHERE user_name = '" + newName + "' OR parola = '" + newName + "'";
                break;
        }

        return query;

    }

    public static String agendaQuerys(int m, String nume, String prenume, int telefon, int ida, String existing) {
        String query = "";

        switch (m) {

            case 1:
                query = "INSERT into agenda_users (nume,prenume,telefon,ida) VALUES ('" + nume + "','" + prenume + "','" + telefon + "',(SELECT id FROM agenda_db WHERE user_name='" + existing + "' OR parola='" + existing + "' ))";
                break;
            case 2:
                query = "SELECT nume,prenume,telefon FROM agenda_users INNER JOIN agenda_db ON agenda_users.ida=agenda_db.id WHERE agenda_db.user_name='" + nume + "'";
                break;
            case 3:
                query = "DELETE FROM agenda_users USING agenda_db ON agenda_users.ida=agenda_db.id WHERE agenda_db.user_name='" + nume + "'";
        }
        return query;
    }


    public static String enterData(String text) {
        System.out.println(text);
        String data = new Scanner(System.in).nextLine();
        return data;
    }

    public static int enterIntData(String text) {
        System.out.println(text);
        int num = new Scanner(System.in).nextInt();
        return num;
    }

    public static void subMenu() {

        int num;
        do {
            System.out.println();
            System.out.println("1-create new contact\n2-view contacts\n3-delete contacts\n4-exit menu");
            System.out.println();
            num = enterIntData("enter number: ");
            switch (num) {
                case 1:
                    String name = enterData("enter new name: ");
                    String lastName = enterData("enter new last name: ");
                    int number = enterIntData("enter phone number : ");
                    String existing = enterData("this contact belongs to (enter name): ");
                    try {
                        demoCDU(agendaQuerys(num, name, lastName, number, 0, existing));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                case 2:
                    String name1 = enterData("enter user name to see his/hers contacts:");
                    try {
                        demoReadFromAgenda(agendaQuerys(num, name1, null, 0, 0, null), null);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

            }
        } while (num != 4);

    }

    private static void demoRead(String query) throws ClassNotFoundException, SQLException {
        int number = 1;
        // 1. load driver
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/4_alin";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        Statement st = conn.createStatement();

        // 5. execute a query
        ResultSet rs = st.executeQuery(query);

        // 6. iterate the result set and print the values
        while (rs.next()) {
            System.out.print(rs.getString("user_name").trim());
            System.out.print("---");
            System.out.println(rs.getString("parola").trim());
            number++;
        }

        // 7. close the objects
        rs.close();
        st.close();
        conn.close();
    }

    private static void demoReadFromAgenda(String query, String name) throws ClassNotFoundException, SQLException {
        int number = 1;
        // 1. load driver
        Class.forName("org.postgresql.Driver");

        // 2. define connection params to db
        final String URL = "jdbc:postgresql://54.93.65.5:5432/4_alin";
        final String USERNAME = "fasttrackit_dev";
        final String PASSWORD = "fasttrackit_dev";

        // 3. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 4. create a query statement
        Statement st = conn.createStatement();

        // 5. execute a query
        ResultSet rs = st.executeQuery(query);

        // 6. iterate the result set and print the values
        while (rs.next()) {
            System.out.print(rs.getString("nume").trim());
            System.out.print("---");
            System.out.println(rs.getString("prenume").trim());
            System.out.print("---");
            System.out.println(rs.getInt("telefon"));
            number++;
        }

        // 7. close the objects
        rs.close();
        st.close();
        conn.close();
    }

}
