package progetto_help;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarichiUscita extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private Connection conn;
    private Utente utente;
    private int idMagazzino;
    private JButton indietro;
    private String direzione;

    public CarichiUscita(Utente utente, int idMagazzino) {
        this.utente = utente;
        this.idMagazzino = idMagazzino;
        
        direzione = "uscita";

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }
        indietro = new JButton("Indietro");
        indietro.addActionListener(this);

        // Creazione del pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(indietro);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        // Esegui le query per ottenere i carichi relativi al magazzino dell'utente
        try {
        	
        	String query = "SELECT * FROM Carichi WHERE mittente = ?";
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
                

                if(!stato.equals("uscito")) {
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
                
                JButton ConfermaButton = new JButton("Conferma");
                ConfermaButton.setActionCommand(String.valueOf(idCarico));
                ConfermaButton.addActionListener(new ConfermaCaricoButtonActionListener());
                constraints.gridx = 4;
                mainPanel.add(ConfermaButton, constraints);
                
                }
                
                else {
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
                    
                    
                    }
                
                
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

        this.setTitle("Gestione Magazzino");
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
            if(utente.getTipo().equals("diocesi") || utente.getTipo().equals("help")) {
            	GestioneMagazzino gestmag = new GestioneMagazzino (utente,idMagazzino);
            }
            else if(utente.getTipo().equals("polo")) {
            	GestioneMagazzinoPolo gestmagpolo = new GestioneMagazzinoPolo(utente,idMagazzino);
            }
            else if(utente.getTipo().equals("azienda")) {
            	GestioneMagazzinoAzienda gestmagazienda = new GestioneMagazzinoAzienda(utente,idMagazzino);
            }
        }
        
        
   
        
        
    }

    private class ConfermaCaricoButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int idCarico = Integer.parseInt(e.getActionCommand());
            try {
        		String query = "UPDATE Carichi SET stato = 'uscito' WHERE mittente = ?";
        	    PreparedStatement statement = conn.prepareStatement(query);
        	    statement.setInt(1, idMagazzino);
        	    statement.executeUpdate();
        	   
        	    
        	    statement.close();
        	}catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
            }
            
            dispose();
            CarichiUscita carichiusciti = new CarichiUscita(utente,idMagazzino);
            
        }
        
        
    }




}



