package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Movie;

import java.sql.SQLException;
import java.util.List;

public interface RandomMovie extends AutoCloseable {
    List<Movie> select(int number) throws SQLException;
}
