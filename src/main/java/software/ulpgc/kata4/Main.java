package software.ulpgc.kata4;

import software.ulpgc.kata4.control.ImportCommand;
import software.ulpgc.kata4.io.DatabaseRandomMovie;
import software.ulpgc.kata4.ui.ImportDialog;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        new ImportCommand(importDialog()).execute();
        DatabaseRandomMovie reader = DatabaseRandomMovie.open(new File("movies.db"));
        MainFrame mainFrame = new MainFrame(reader);
        mainFrame.setVisible(true);
        reader.close();
    }

    private static ImportDialog importDialog() {
        return () -> new File("title.basics.tsv.gz");
    }

}
