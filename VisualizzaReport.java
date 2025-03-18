package progetto_help;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VisualizzaReport extends JFrame implements ActionListener {
    private JTable table;
    private Connection conn;
    private Utente utente;
    private JButton button2;
    private boolean editingEnabled;
    private String idReport;

    public VisualizzaReport(Utente utente, String idReport) {
        this.utente = utente;
        this.idReport=idReport;
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
                return editingEnabled; 
            }
        };

        try {
            
            String query = "SELECT * FROM Report WHERE id_report = ? ";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, idReport);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String idreport = result.getString("id_report");
                String id_creatore = result.getString("id_creatore");
                String testo = result.getString("testo");
                String tipo = result.getString("tipo");
                String periodo = result.getString("periodo");

                String[][] data = {
                        {"ID Report", idreport},
                        {"ID Creatore", id_creatore},
                        {"Testo", testo},
                        {"Tipo", tipo},
                        {"Periodo", periodo}
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
        button2 = new JButton("Chiudi");

        // Creazione di un pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button2);
        button2.addActionListener(this);

        // Aggiunta del pannello dei pulsanti nella parte inferiore
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Creazione della finestra principale
        this.setTitle("PROFILO");
        this.setSize(501, 316);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == button2) {
			this.dispose();
		}
		
	}

   

}
