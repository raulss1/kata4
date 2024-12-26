package software.ulpgc.kata4.control;

import software.ulpgc.kata4.io.*;
import software.ulpgc.kata4.model.Movie;
import software.ulpgc.kata4.ui.ImportDialog;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ImportCommand implements Command {

    private final ImportDialog importDialog;

    public ImportCommand(ImportDialog importDialog) {
        this.importDialog = importDialog;
    }

    @Override
    public void execute() {
        try {
            MovieReader reader = new GzipFileMovieReader(InputFile(), tsvMovieDeserialize());
            MovieWriter writer = DatabaseMovieWriter.open(outputFile());
            doExecute(reader, writer);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void doExecute(MovieReader reader, MovieWriter writer) throws IOException {
        while(true){
            Movie movie = reader.read();
            if (movie == null) break;
            writer.write(movie);
        }
    }

    private File outputFile() {
        return new File("movies.db");
    }

    private MovieDeserializer tsvMovieDeserialize() {
        return new TsvMovieDeserializer();
    }

    private File InputFile() {
        return importDialog.get();
    }
}
