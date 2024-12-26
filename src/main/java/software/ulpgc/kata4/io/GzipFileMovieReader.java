package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Movie;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class GzipFileMovieReader implements MovieReader{

    private final MovieDeserializer deserializer;
    private final BufferedReader reader;

    public GzipFileMovieReader(File file, MovieDeserializer deserializer) throws IOException {
        this.deserializer = deserializer;
        this.reader = readerOf(file);
        this.skipHeader();
    }

    private BufferedReader readerOf(File file) throws IOException {
        return new BufferedReader(new InputStreamReader(gzipInputSreamOf(file)));
    }

    private InputStream gzipInputSreamOf(File file) throws IOException {
        return new GZIPInputStream(new FileInputStream(file));
    }

    private void skipHeader() throws IOException {
        this.reader.readLine();
    }

    @Override
    public Movie read() throws IOException {
        return deserialize(reader.readLine());
    }

    private Movie deserialize(String s) {
        return s != null ? deserializer.deserialize(s) : null;
    }

    @Override
    public void close() throws Exception {
        reader.close();

    }
}
