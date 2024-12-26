package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Movie;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class GzipFileMovieReader implements MovieReader{
    private final BufferedReader reader;
    private final MovieDeserializer deserializer;

    public GzipFileMovieReader(File file, MovieDeserializer deserializer) throws IOException {
        this.reader = readerOf(file);
        this.deserializer = deserializer;
        this.skipHeader();
    }

    private String skipHeader() throws IOException {
        return this.reader.readLine();
    }

    @Override
    public Movie read() throws IOException {
        return deserialize(reader.readLine());
    }

    private Movie deserialize(String line) {
        return line != null ? deserializer.deserialize(line) : null;
    }

    private static BufferedReader readerOf(File file) throws IOException {
        return new BufferedReader(new InputStreamReader(gzipInputStreamOf(file)));
    }

    private static GZIPInputStream gzipInputStreamOf(File file) throws IOException {
        return new GZIPInputStream(new FileInputStream(file));
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}
