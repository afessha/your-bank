package uk.co.asepstrath.bank;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class Database {

    DataSource ds;
    Connection connection;

    public Database(DataSource ds) {
        this.ds = ds;
        // Open Connection to DB
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createTable(connection);
        insertAccounts(connection);
        configureQueries(connection);
        closeConnection(connection);
    }

    public void createTable(Connection connection) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS accounts (\n"
                    + " id SERIAL PRIMARY KEY,\n"
                    + " name VARCHAR(20) NOT NULL,\n"
                    + " balance DECIMAL NOT NULL,\n"
                    + " currency VARCHAR(20),\n"
                    + " accountType VARCHAR(30));";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAccounts(Connection connection) {
        try {
            String insertions = "INSERT INTO accounts (name, balance, currency, accountType) "
                    + "VALUES (?,?,?,?);";
            PreparedStatement prep = connection.prepareStatement(insertions);
            Controller controller = new Controller();
            List<Account> accountsList = controller.getList();
            for (int i = 0; i < accountsList.size(); i++) {
                prep.setString(1, accountsList.get(i).getName());
                prep.setBigDecimal(2, accountsList.get(i).getBalance());
                prep.setString(3, accountsList.get(i).getCurrency());
                prep.setString(4, accountsList.get(i).getAccountType());
                prep.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void configureQueries(Connection connection) {
        try {
            String selectAllQuery = "SELECT * from accounts;"; // return all accounts
            String nameQuery = "SELECT * from accounts WHERE name LIKE ?"; // return accounts with certain name, parameter specified below
            PreparedStatement prepQuery = connection.prepareStatement(selectAllQuery);
            //prepQuery.setString(1, "A%"); // define parameter for query
            ResultSet rs = prepQuery.executeQuery();
            //uncomment to print query results
            /*while (rs.next()) {
                System.out.println(rs.getString("id"));
                System.out.println(rs.getString("name"));
                System.out.println(rs.getString("balance"));
                System.out.println(rs.getString("currency"));
                System.out.println(rs.getString("accountType"));
            }
             */
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}