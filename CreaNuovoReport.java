package progetto_help;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreaNuovoReport extends JFrame implements ActionListener {
    private JComboBox<String> tipoComboBox;
    private JComboBox<String> meseComboBox;
    private JButton prelevaDatiButton;
    private JButton salvaButton;
    private JTextArea reportTextArea;
    private Connection conn;
    private Utente utente;
    
    private int idCarico;
    
    private String idprodotto;
    private String descrizione;
    
    private String testo;

    public CreaNuovoReport(Utente utente) {
        this.utente = utente;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        tipoComboBox = new JComboBox<>(new String[]{"mensile", "trimestrale", "annuale"});
        meseComboBox = new JComboBox<>(new String[]{"gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre"});
        prelevaDatiButton = new JButton("Preleva Dati");
        prelevaDatiButton.addActionListener(this);
        salvaButton = new JButton("Salva");
        salvaButton.addActionListener(this);
        salvaButton.setEnabled(false);
        reportTextArea = new JTextArea(10, 30);
        reportTextArea.setEditable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Tipo:"));
        inputPanel.add(tipoComboBox);
        inputPanel.add(new JLabel("Mese:"));
        inputPanel.add(meseComboBox);
        inputPanel.add(prelevaDatiButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(reportTextArea), BorderLayout.CENTER);
        mainPanel.add(salvaButton, BorderLayout.SOUTH);

        this.setTitle("Crea Nuovo Report");
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == prelevaDatiButton) {
            String tipo = (String) tipoComboBox.getSelectedItem();
            String mese = (String) meseComboBox.getSelectedItem();

            try {
                String query = "SELECT c.id_carico FROM Carichi c INNER JOIN SchemaDistribuzione s ON c.id_schema = s.id_schema WHERE MONTH(s.data) = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, getMonthNumber(mese));

                ResultSet result = statement.executeQuery();

                StringBuilder reportBuilder = new StringBuilder();
                while (result.next()) {
                    idCarico = result.getInt("c.id_carico");
                    
                    String ProdottiQuery = "SELECT id_prodotto, descrizione FROM Prodotti where id_carico = ?";
                    PreparedStatement ProdottiStatement = conn.prepareStatement(ProdottiQuery);
                    ProdottiStatement.setInt(1,idCarico);
                    ResultSet ProdottiResult = ProdottiStatement.executeQuery();
                    
                    while(ProdottiResult.next()) {
                    	idprodotto = ProdottiResult.getString("id_prodotto");
                    	descrizione = ProdottiResult.getString("descrizione");
                    }
                    
                    reportBuilder.append("Carico: ").append(idCarico).append(" - Idprodotto: ").append(idprodotto).append("  descrizione: ").append(descrizione).append("\n");
                }

                reportTextArea.setText(reportBuilder.toString());
                salvaButton.setEnabled(true);
                testo = reportBuilder.toString();

                result.close();
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
            }
        } else if (e.getSource() == salvaButton) {
               	
            JOptionPane.showMessageDialog(null, "Report salvato con successo!");
            this.dispose();
        }
    }

    private int getMonthNumber(String monthName) {
        switch (monthName) {
            case "gennaio":
                return 1;
            case "febbraio":
                return 2;
            case "marzo":
                return 3;
            case "aprile":
                return 4;
            case "maggio":
                return 5;
            case "giugno":
                return 6;
            case "luglio":
                return 7;
            case "agosto":
                return 8;
            case "settembre":
                return 9;
            case "ottobre":
                return 10;
            case "novembre":
                return 11;
            case "dicembre":
                return 12;
            default:
                return 0;
        }
    }
}
