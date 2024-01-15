package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlManager {

    private Connection getConnection() {
        Connection conn = null;
        try {
            // db parameters
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:/Users/isaac/Downloads/soenProjectFinal/mocktails.db";
            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public void createDrinksTable(){
        String mySql = "CREATE TABLE IF NOT EXISTS drinks(\n"
                + " id text PRIMARY KEY,\n"
                + " drinkName text NOT NULL,\n"
                + " category text NOT NULL,\n"
                + " alcoholic text\n"
                + ");";

        try (Connection connection = this.getConnection()){
            Statement createStatement = connection.createStatement();
            createStatement.execute(mySql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void insertRecord(Drinks data){
        String mySql = "INSERT INTO " +
                "drinks(id, drinkName, category, alcoholic)" +
                "VALUES(?, ?, ?, ?)";

        try(Connection connection = this.getConnection()){
            PreparedStatement prepStmt = connection.prepareStatement(mySql);
            prepStmt.setString(1, data.getId());
            prepStmt.setString(2, data.getDrinkName());
            prepStmt.setString(3, data.getCategory());
            prepStmt.setString(4, data.getAlcoholic());
            prepStmt.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public List<Drinks> selectAllDrinks() {
        List<Drinks> drinks = new ArrayList<>();

        String mySql = "SELECT * FROM drinks";

        try(Connection connection = this.getConnection()){
            PreparedStatement prepStmt = connection.prepareStatement(mySql);
            getDrinksFromSQLQueryResults(drinks, prepStmt);
            return drinks;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return drinks;
    }


    public List<Drinks> searchDrinksByName(String drinkName){
        List<Drinks> drinks = new ArrayList<>();

        String mySql = "SELECT * FROM drinks WHERE drinkName LIKE '%' || ? || '%'";

        try(Connection connection = this.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(mySql);
            System.out.println(preparedStatement);
            preparedStatement.setString(1, drinkName);
            getDrinksFromSQLQueryResults(drinks, preparedStatement);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return drinks;
    }

    private void getDrinksFromSQLQueryResults(List<Drinks> drinks, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            String id = resultSet.getString("id");
            String drinkName = resultSet.getString("DrinkName");
            String category = resultSet.getString("Category");
            String alcoholic = resultSet.getString("Alcoholic");
            drinks.add(
                    new Drinks(
                            id,
                            drinkName,
                            category,
                            alcoholic
                    )
            );
        }
    }
}
