package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Movie;

public interface MovieReader extends AutoCloseable{
    Movie read();
}
