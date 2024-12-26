package software.ulpgc.kata4;


import software.ulpgc.kata4.io.DatabaseRandomMovie;
import software.ulpgc.kata4.model.Movie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {
    private JTextField answerField;
    private JLabel questionLabel;
    private DatabaseRandomMovie reader;
    private final List<Movie> movies;
    private int i = 0;

    public MainFrame(DatabaseRandomMovie reader) throws HeadlessException, SQLException {
        this.reader = reader;
        this.setTitle("Kata 3");
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        questionLabel = new JLabel();
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        answerField = new JTextField();

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(BorderLayout.EAST, toolbar());
        bottomPanel.add(BorderLayout.CENTER, answerField);

        this.add(BorderLayout.NORTH, questionLabel);
        this.add(BorderLayout.SOUTH, bottomPanel);
        this.movies = reader.select(5); //cambiar el número de películas que quieras mostrar por pantalla

        updateQuestion();
    }

    private void updateQuestion(){
        questionLabel.setText("En que año se estrenó esta película " + movies.get(i).title());
    }

    private Component toolbar() {
        JPanel panel = new JPanel();
        panel.add(toggle());
        return panel;
    }


    private JButton toggle() {

        JButton button = new JButton("Submit");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String respuesta = answerField.getText();
                JOptionPane.showMessageDialog(MainFrame.this, "Tu respuesta: " + respuesta + "\n" + "La respuesta correcta es " + movies.get(i).year());
                System.out.println("Respuesta para " + movies.get(i) + ": " + respuesta);

                // Pasar a la siguiente pregunta si hay más películas
                i++;
                if (i < movies.size()) {
                    updateQuestion();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "¡Has respondido todas las preguntas!");
                    dispose(); // Cerrar la ventana
                }
                
                answerField.setText("");
            }
        });
        return button;
    }

}
