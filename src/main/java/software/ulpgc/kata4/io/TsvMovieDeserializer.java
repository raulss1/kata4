package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Movie;

import java.util.Arrays;

public class TsvMovieDeserializer implements MovieDeserializer{
    @Override
    public Movie deserialize(String text) {
        return deserialize_array(text.split("\t"));
    }

    private Movie deserialize_array(String[] split) {
        return new Movie(split[0], split[1], toInt(split[5]));
    }

    private int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
