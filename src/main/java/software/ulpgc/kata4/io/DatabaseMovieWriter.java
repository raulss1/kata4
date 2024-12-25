package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Movie;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

import static java.sql.Types.INTEGER;
import static java.sql.Types.NVARCHAR;

public class DatabaseMovieWriter implements MovieWriter{
    private final Connection connection;
    private final PreparedStatement insertMoviePreparedStatement;

    public static DatabaseMovieWriter open(File file) throws SQLException {
        return new DatabaseMovieWriter("jdbc:sqlite:" + file.getAbsolutePath());
    }

    public DatabaseMovieWriter(String connection) throws SQLException {
        this(DriverManager.getConnection(connection));
    }

    public DatabaseMovieWriter(Connection connection) throws SQLException {
        this.connection = connection;
        this.stopAutoCommit();
        this.createTables();
        this.insertMoviePreparedStatement = this.createInsertStatement();
    }

    private final static String InsertMovieStatement = """
            INSERT INTO movies (id,title,year)
            VALUES (?,?,?)
            """;
    private PreparedStatement createInsertStatement() throws SQLException {
        return connection.prepareStatement(InsertMovieStatement);
    }

    private void stopAutoCommit() throws SQLException {
        this.connection.setAutoCommit(false);
    }

    private final static String CreateMoviesTableStatement = """
            CREATE TABLE IF NOT EXISTS movies (
                id TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                year INTEGER,
                duration INTEGER)
            """;
    private void createTables() throws SQLException {
        connection.createStatement().execute(CreateMoviesTableStatement);
    }

    @Override
    public void write(Movie movie) throws IOException {
        try {
            insertMovieStatementFor(movie).execute();
        } catch (SQLException e) {
            throw new IOException(e.getMessage());
        }
    }

    private PreparedStatement insertMovieStatementFor(Movie movie) throws SQLException {
        insertMoviePreparedStatement.clearParameters();
        parametersOf(movie).forEach(this::define);
        return insertMoviePreparedStatement;
    }

    private void define(Parameter parameter) {
        try {
            if (parameter.value == null)
                insertMoviePreparedStatement.setNull(parameter.index, parameter.type);
            else
                insertMoviePreparedStatement.setObject(parameter.index, parameter.value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Parameter> parametersOf(Movie movie) {
        return List.of(
                new Parameter(1, movie.id(), NVARCHAR),
                new Parameter(2, movie.title(), NVARCHAR),
                new Parameter(3, movie.year() != -1 ? movie.year() : null, INTEGER)
        );

    }

    private record Parameter(int index, Object value, int type) {}

    @Override
    public void close() throws Exception {
        connection.commit();
        connection.close();
    }
}
