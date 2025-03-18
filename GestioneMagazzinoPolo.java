package progetto_help;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestioneMagazzinoPolo extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private Connection conn;
    private Utente utente;
    private int idMagazzino;
    private JButton carichiEntrataButton;
    private JButton visualizzaGiacenzaButton;
    private JButton indietro;
    private String direzione;

    public GestioneMagazzinoPolo(Utente utente, int idMagazzino) {
        this.utente = utente;
        this.idMagazzino = idMagazzino;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        // Creazione dei pulsanti
        carichiEntrataButton = new JButton("Carichi in Entrata");
        visualizzaGiacenzaButton = new JButton("Visualizza Giacenza");
        indietro = new JButton("Indietro");

        // Aggiunta del listener ai pulsanti
        carichiEntrataButton.addActionListener(this);
        visualizzaGiacenzaButton.addActionListener(this);
        indietro.addActionListener(this);

        // Creazione del pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(carichiEntrataButton);
        buttonPanel.add(visualizzaGiacenzaButton);
        buttonPanel.add(indietro);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        // Esegui le query per ottenere i carichi relativi al magazzino dell'utente
        try {
            String query = "SELECT * FROM Carichi WHERE destinatario = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idMagazzino);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                // Ottieni i dati del carico
                int idCarico = result.getInt("id_carico");
                String stato = result.getString("stato");
                int idSchema = result.getInt("id_schema");
                int mittente = result.getInt("mittente");
                int destinatario = result.getInt("destinatario");
                
                if(mittente==idMagazzino) {
                	direzione = "uscita";
                }
                
                else if(destinatario==idMagazzino) {
                	direzione = "entrata";
                }

                // Aggiungi i dati del carico alla lista nel pannello principale
                JLabel caricoLabel = new JLabel("Carico:");
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = mainPanel.getComponentCount() / 3;
                constraints.anchor = GridBagConstraints.WEST;
                constraints.insets = new Insets(5, 5, 5, 5);
                mainPanel.add(caricoLabel, constraints);

                JLabel idCaricoLabel = new JLabel(Integer.toString(idCarico));
                constraints.gridx = 1;
                mainPanel.add(idCaricoLabel, constraints);
                
                JLabel DirezioneLabel = new JLabel(direzione);
                constraints.gridx = 2;
                mainPanel.add(DirezioneLabel, constraints);

                JLabel StatoLabel = new JLabel(stato);
                constraints.gridx = 3;
                mainPanel.add(StatoLabel, constraints);
                
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

        this.setTitle("Gestione Magazzino ");
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == indietro) {
            this.dispose();
            Homepage homepage = new Homepage(utente);	
        } 
        
        else if (e.getSource() == carichiEntrataButton) {
        	this.dispose();
            CarichiEntrata carichientrata = new CarichiEntrata(utente,idMagazzino);
        }
        
        if (e.getSource() == visualizzaGiacenzaButton) {
        	VisualizzaGiacenza visgiac = new VisualizzaGiacenza(utente,idMagazzino);
        }

        
    }

    private class VisualizzaFamigliaButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int idCarico = Integer.parseInt(e.getActionCommand());
            // Visualizza la famiglia associata al carico con l'id specificato
        }
    }
}
