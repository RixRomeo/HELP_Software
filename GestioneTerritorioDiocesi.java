package progetto_help;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestioneTerritorioDiocesi extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private Connection conn;
    private Utente utente;
    private JButton button1, button2, button3;

    public GestioneTerritorioDiocesi(Utente utente) {
        this.utente = utente;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        // Creazione dei pulsanti
        button1 = new JButton("I");
        button2 = new JButton("Schema Di Distribuzione");
        button3 = new JButton("Indietro");

        // Creazione del pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        try {
            String cfUtente = utente.getCF(); // Sostituisci con il CF dell'utente desiderato
            String query = "SELECT id_polo, cf_gestore FROM poli WHERE diocesi = (SELECT id_diocesi FROM diocesi WHERE cf_gestore = ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, cfUtente);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String idPolo = result.getString("id_polo");
                String cfGestorePolo = result.getString("cf_gestore");

                JLabel poloLabel = new JLabel("Polo:");
                constraints.gridx = 0;
                mainPanel.add(poloLabel, constraints);

                JLabel idLabel = new JLabel(idPolo);
                constraints.gridx = 1;
                mainPanel.add(idLabel, constraints);
                
                JLabel cfLabel = new JLabel("Gestore: "+ cfGestorePolo);
                constraints.gridx = 2;
                mainPanel.add(cfLabel, constraints);

                constraints.gridy++;
            }

            result.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        this.setTitle("Gestione Territorio");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);

        // Aggiunta dei listener ai pulsanti
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            Info info = new Info(utente);
        }
        
        
        if(e.getSource() == button2) {
        	SchemaDistribuzione schemdistr = new SchemaDistribuzione(utente);
        }
        
        if (e.getSource() == button3) {
        	this.dispose();
            Homepage homepage = new Homepage(utente);
        }

        // Implementa le azioni per gli altri pulsanti qui
    }
}
