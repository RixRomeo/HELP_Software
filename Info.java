package progetto_help;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Info extends JFrame implements ActionListener {
    private JTable table;
    private Connection conn;
    private Utente utente;
    private JButton button1;

    public Info(Utente utente) {
        this.utente = utente;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        String[] columnNames = {"Campo", "Valore"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rende tutte le celle non modificabili
            }
        };

        try {
            String cfUtente = utente.getCF();
            String query;
            if (utente.getTipo().equals("polo")) {
                query = "SELECT * FROM poli WHERE cf_gestore = ?";
            } else if (utente.getTipo().equals("diocesi")) {
                query = "SELECT * FROM Diocesi WHERE cf_gestore = ?";
            } else {
                JOptionPane.showMessageDialog(null, "Tipo utente non valido");
                return;
            }

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, cfUtente);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                ResultSetMetaData metaData = result.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = result.getString(i);

                    String[] row = {columnName, value};
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

        // Creazione del pannello che contiene la tabella
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Creazione dei pulsanti
        button1 = new JButton("Indietro");

        // Creazione di un pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button1);

        button1.addActionListener(this);

        // Aggiunta del pannello dei pulsanti nella parte inferiore
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Creazione della finestra principale
        this.setTitle("Info");
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            this.dispose();
        }
    }
}
