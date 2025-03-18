package progetto_help;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Profilo extends JFrame implements ActionListener {
    private JTable table;
    private Connection conn;
    private Utente utente;
    private JButton button1;
    private JButton button2;
    private boolean editingEnabled;

    public Profilo(Utente utente) {
        this.utente = utente;
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
            String cfUtente = utente.getCF();
            String query = "SELECT cf, nome, cognome, username, password_utente FROM utenti WHERE cf = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, cfUtente);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String cf = result.getString("cf");
                String nome = result.getString("nome");
                String cognome = result.getString("cognome");
                String username = result.getString("username");
                String password = result.getString("password_utente");

                String[][] data = {
                        {"CF", cf},
                        {"Nome", nome},
                        {"Cognome", cognome},
                        {"Username", username},
                        {"Password", password}
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
        button1 = new JButton("Modifica Dati");
        button2 = new JButton("Chiudi");

        // Creazione di un pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button1);
        buttonPanel.add(button2);

        button1.addActionListener(this);
        button2.addActionListener(this);

        // Aggiunta del pannello dei pulsanti nella parte inferiore
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Creazione della finestra principale
        this.setTitle("PROFILO");
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            editingEnabled = true;
            table.setModel(table.getModel()); // Aggiorna la visualizzazione della tabella
            button1.setEnabled(false); // Disabilita il pulsante "Modifica Dati"
            button2.setText("Salva"); // Cambia il testo del pulsante "Chiudi" in "Salva"
        } else if (e.getSource() == button2) {
            if (button2.getText().equals("Salva")) {
                saveData();
                JOptionPane.showMessageDialog(null, "Modifiche salvate con successo");
            }
            this.dispose();
        }
    }

    private void saveData() {
        try {
            String updateQuery = "UPDATE utenti SET cf = ?, nome = ?, cognome = ?, username = ?, password_utente = ? WHERE cf = ?";
            PreparedStatement updateStatement = conn.prepareStatement(updateQuery);

            for (int row = 0; row < table.getRowCount(); row++) {
                String columnName = (String) table.getValueAt(row, 0);
                String newValue = (String) table.getValueAt(row, 1);

                switch (columnName) {
                    case "CF":
                        // Il CF viene modificato solo se è diverso dal CF originale
                        if (!newValue.equals(utente.getCF())) {
                            updateStatement.setString(1, newValue);
                        } else {
                            updateStatement.setString(1, utente.getCF());
                        }
                        break;
                    case "Nome":
                        updateStatement.setString(2, newValue);
                        break;
                    case "Cognome":
                        updateStatement.setString(3, newValue);
                        break;
                    case "Username":
                        updateStatement.setString(4, newValue);
                        break;
                    case "Password":
                        updateStatement.setString(5, newValue);
                        break;
                }
            }

            updateStatement.setString(6, utente.getCF());
            updateStatement.executeUpdate();
            updateStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante il salvataggio dei dati");
        }
    }

}
