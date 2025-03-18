package progetto_help;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VisualizzaFamiglia extends JFrame implements ActionListener {
    private JTable table;
    private Connection conn;
    private String idfamiglia;
    private JButton backButton;
    private JButton editButton;
    private boolean editingEnabled;

    public VisualizzaFamiglia(String idfamiglia) {
        this.idfamiglia = idfamiglia;
        this.editingEnabled = false;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        String[] columnNames = {" ", " "};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return editingEnabled; // Abilita la modifica solo quando editingEnabled è true
            }
        };

        try {
            String query = "SELECT cf_membro, id_famiglia, nome, cognome, eta, ruolo, bisogni_speciali FROM MembriFamiglie WHERE id_famiglia=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, idfamiglia);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String cfmembro = result.getString("cf_membro");
                String nome = result.getString("nome");
                String cognome = result.getString("cognome");
                String eta = result.getString("eta");
                String ruolo = result.getString("ruolo");
                String bisogni_speciali = result.getString("bisogni_speciali");

                String[][] data = {
                        {"CF", cfmembro},
                        {"Id-famiglia", idfamiglia},
                        {"Nome", nome},
                        {"Cognome", cognome},
                        {"Eta'", eta},
                        {"Ruolo", ruolo},
                        {"Bisogni Speciali", bisogni_speciali},
                        {" ", " "},
                        {" ", " "}
                };

                for (String[] row : data) {
                    model.addRow(row);
                }
            }

            result.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
        }

        table = new JTable(model);

        // Creazione dei pulsanti
        backButton = new JButton("Indietro");
        editButton = new JButton("Modifica");

        // Creazione di un pannello per i pulsanti
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(backButton);
        buttonPanel.add(editButton);

        backButton.addActionListener(this);
        editButton.addActionListener(this);

        // Aggiunta del pannello dei pulsanti nella parte superiore
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Creazione della finestra principale
        this.setTitle("Visualizza Famiglia");
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            this.dispose();
        } else if (e.getSource() == editButton) {
            // Aggiungi qui il codice per la gestione del pulsante "Modifica"
        }
    }
}
