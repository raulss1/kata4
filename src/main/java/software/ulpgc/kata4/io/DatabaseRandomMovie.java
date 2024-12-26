package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Movie;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRandomMovie implements RandomMovie {
    private final Connection connection;

    public DatabaseRandomMovie(String connection) throws SQLException {
        this(DriverManager.getConnection(connection));
    }

    public DatabaseRandomMovie(Connection connection) throws SQLException {
        this.connection = connection;
        this.stopAutoCommit();
    }

    public static DatabaseRandomMovie open(File file) throws SQLException {
        return new DatabaseRandomMovie("jdbc:sqlite:" + file.getAbsolutePath());
    }

    private void stopAutoCommit() throws SQLException {
        this.connection.setAutoCommit(false);
    }

    @Override
    public List<Movie> select(int number) throws SQLException {
        String SIMPLE_QUESTION = "SELECT title, year FROM movies LIMIT " + number;
        String i = "0";
        List<Movie> movies = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SIMPLE_QUESTION)) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                int year = resultSet.getInt("year");
                movies.add(new Movie(i, title, year)); // Crear objeto Movie
                int i1 = Integer.parseInt(i) + 1;
                i = String.valueOf(i1);
            }
        }
        return movies;
    }

    @Override
    public void close() throws Exception {
        this.connection.commit();
        this.connection.close();
    }
}
