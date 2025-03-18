package progetto_help;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarichiEntrata extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private Connection conn;
    private Utente utente;
    private int idMagazzino;
    private JButton indietro;
    private String direzione;
    private int idCarico;
    private int idSchema;
    private int SchemaIdPrelevato;
    private int luogo;

    public CarichiEntrata(Utente utente, int idMagazzino) {
        this.utente = utente;
        this.idMagazzino = idMagazzino;

        direzione = "entrata";

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

            String query = "SELECT * FROM Carichi WHERE destinatario = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idMagazzino);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                // Ottieni i dati del carico
                idCarico = result.getInt("id_carico");
                String stato = result.getString("stato");
                idSchema = result.getInt("id_schema");
                int mittente = result.getInt("mittente");
                int destinatario = result.getInt("destinatario");

                // Aggiungi i dati del carico alla lista nel pannello principale solo se lo stato è "incompleto"
                if (!stato.equals("arrivato")) {
                    // Aggiungi i componenti per il carico
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

                    JLabel direzioneLabel = new JLabel(direzione);
                    constraints.gridx = 2;
                    mainPanel.add(direzioneLabel, constraints);

                    JButton confermaButton = new JButton("Conferma");
                    confermaButton.setActionCommand(String.valueOf(idCarico));
                    confermaButton.addActionListener(new ConfermaCaricoButtonActionListener());
                    constraints.gridx = 2;
                    mainPanel.add(confermaButton, constraints);

                    JButton problemaCaricoButton = new JButton("Problema nel Carico");
                    problemaCaricoButton.setActionCommand(String.valueOf(idCarico));
                    problemaCaricoButton.addActionListener(new ProblemaCaricoButtonActionListener());
                    constraints.gridx = 3;
                    mainPanel.add(problemaCaricoButton, constraints);
                }
            
                
                else {
                    // Aggiungi i componenti per il carico
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

                    JLabel direzioneLabel = new JLabel(direzione);
                    constraints.gridx = 2;
                    mainPanel.add(direzioneLabel, constraints);

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
            if (utente.getTipo().equals("diocesi") || utente.getTipo().equals("help")) {
                GestioneMagazzino gestmag = new GestioneMagazzino(utente, idMagazzino);
            } else if (utente.getTipo().equals("polo")) {
                GestioneMagazzinoPolo gestmagpolo = new GestioneMagazzinoPolo(utente, idMagazzino);
            } else if (utente.getTipo().equals("azienda")) {
                GestioneMagazzinoAzienda gestmagazienda = new GestioneMagazzinoAzienda(utente, idMagazzino);
            }
        }
    }

    private class ConfermaCaricoButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int idCarico = Integer.parseInt(e.getActionCommand());
            if(utente.getTipo().equals("help")) {
                try {
                    String query = "UPDATE Carichi SET stato = 'arrivato' WHERE destinatario = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, idMagazzino);
                    statement.executeUpdate();
                    
                    
                    
                    
                    String DiocesiRandomQuery = "SELECT m.id_magazzino, m.responsabile FROM Magazzini m JOIN utenti u ON m.responsabile=u.cf where u.tipo='diocesi' ORDER BY RAND() LIMIT 1";
                    PreparedStatement randomDioceseStatement = conn.prepareStatement(DiocesiRandomQuery);
                    ResultSet randomDioceseResult = randomDioceseStatement.executeQuery();
                    if (randomDioceseResult.next()) {
                        int randomDioceseId = randomDioceseResult.getInt("m.id_magazzino");
                        String ResponsabileDiocesiRandom = randomDioceseResult.getString("m.responsabile");
                        
                        String Schemaquery = "INSERT INTO SchemaDistribuzione (id_schema, destinatario, data) VALUES (?, ?, ?)";
                        PreparedStatement SchemaStatement = conn.prepareStatement(Schemaquery);
                        SchemaStatement.setInt(1,32);
                        SchemaStatement.setString(2,ResponsabileDiocesiRandom);
                        java.util.Date date = new java.util.Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                        SchemaStatement.setDate(3, sqlDate);
                        
                        SchemaStatement.executeUpdate();
                        
                        
                        
                        SchemaStatement.close();
                        
                        //Preleviamo l'id schema appena creato:
                        String PrelevaSchemaQuery = "SELECT id_schema FROM SchemaDistribuzione WHERE data = ?";
                        PreparedStatement PrelevaSchemaStatement = conn.prepareStatement(PrelevaSchemaQuery);
                        java.util.Date date1 = new java.util.Date();
                        java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());

                        PrelevaSchemaStatement.setDate(1, sqlDate1);

                        ResultSet PrelevaSchemaResult = PrelevaSchemaStatement.executeQuery();
                        
                        while(PrelevaSchemaResult.next()){
                            SchemaIdPrelevato = PrelevaSchemaResult.getInt("id_schema");
                        }
                        PrelevaSchemaStatement.close();

                        String insertQuery = "INSERT INTO Carichi (stato, id_schema, mittente, destinatario, id_carico) VALUES (?, ?, ?, ?, ?)";
                        int idcarico = idCarico+1;
                        PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                        insertStatement.setString(1, "incompleto");
                        insertStatement.setInt(2, SchemaIdPrelevato);
                        insertStatement.setInt(3, idMagazzino);
                        insertStatement.setInt(4, randomDioceseId);
                        insertStatement.setInt(5, idcarico);
                        insertStatement.executeUpdate();

                        
                        insertStatement.close();
                        
                        String PrelevaQuantitaquery = "SELECT quantita FROM Prodotti";
                        PreparedStatement PrelevaQuantitaStatement = conn.prepareStatement(PrelevaQuantitaquery);
                        ResultSet PrelevaQuantitaResult = PrelevaQuantitaStatement.executeQuery();
                        if (PrelevaQuantitaResult.next()) {
                            
                            int quantita = PrelevaQuantitaResult.getInt("quantita");
                            
                            String Prodottiquery = "UPDATE Prodotti SET luogo_di_conservazione = ?, id_carico = ? , quantita = ? WHERE id_carico = ?";
                            PreparedStatement ProdottiStatement = conn.prepareStatement(Prodottiquery);
                            ProdottiStatement.setInt(1, idMagazzino);
                            ProdottiStatement.setInt(2, idcarico);
                            ProdottiStatement.setInt(3, quantita-40);
                            ProdottiStatement.setInt(4, idCarico);
                            ProdottiStatement.executeUpdate();
                            
                            
                            
                            ProdottiStatement.close();
                        }
                        
                        PrelevaQuantitaStatement.close();
                    }
                    
                   statement.close();
                   
                }catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
                }
                
            }
            
            
            if(utente.getTipo().equals("diocesi")) {
                try {
                    String query = "UPDATE Carichi SET stato = 'arrivato' WHERE destinatario = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, idMagazzino);
                    statement.executeUpdate();
                    
                    
                    
                    
                    String PrendiDiocesiQuery = "SELECT id_diocesi FROM Diocesi WHERE cf_gestore=?";
                    PreparedStatement PrendiDiocesistatement = conn.prepareStatement(PrendiDiocesiQuery);
                    PrendiDiocesistatement.setString(1,utente.getCF());
                    ResultSet PrendiDiocesiResult = PrendiDiocesistatement.executeQuery();
                    
                    while(PrendiDiocesiResult.next()) {
                        luogo=PrendiDiocesiResult.getInt("id_diocesi");
                    }
                    
                    
                    
                    String RandomQuery = "SELECT m.id_magazzino, m.responsabile FROM Magazzini m JOIN utenti u ON m.responsabile=u.cf WHERE u.tipo='polo' AND u.cf=(SELECT cf_gestore FROM poli WHERE diocesi = ?) ORDER BY RAND() LIMIT 1";
                    PreparedStatement randomPoloStatement = conn.prepareStatement(RandomQuery);
                    randomPoloStatement.setInt(1, luogo);

                    ResultSet randomPoloResult = randomPoloStatement.executeQuery();
                    if (randomPoloResult.next()) {
                        int randomPoloId = randomPoloResult.getInt("m.id_magazzino");
                        String ResponsabilePoloRandom = randomPoloResult.getString("m.responsabile");
                        
                        String Schemaquery = "INSERT INTO SchemaDistribuzione (id_schema, destinatario, data) VALUES (?, ?, ?)";
                        PreparedStatement SchemaStatement = conn.prepareStatement(Schemaquery);
                        SchemaStatement.setInt(1,33);
                        SchemaStatement.setString(2,ResponsabilePoloRandom);
                        java.util.Date date = new java.util.Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                        SchemaStatement.setDate(3, sqlDate);
                        
                        SchemaStatement.executeUpdate();
                        
                        
                        
                        SchemaStatement.close();
                        
                        //Preleviamo l'id schema appena creato:
                        String PrelevaSchemaQuery = "SELECT id_schema FROM SchemaDistribuzione WHERE data = ?";
                        PreparedStatement PrelevaSchemaStatement = conn.prepareStatement(PrelevaSchemaQuery);
                        java.util.Date date1 = new java.util.Date();
                        java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());

                        PrelevaSchemaStatement.setDate(1, sqlDate1);

                        ResultSet PrelevaSchemaResult = PrelevaSchemaStatement.executeQuery();
                        
                        while(PrelevaSchemaResult.next()){
                            SchemaIdPrelevato = PrelevaSchemaResult.getInt("id_schema");
                        }
                        PrelevaSchemaStatement.close();

                        String insertQuery = "INSERT INTO Carichi (stato, id_schema, mittente, destinatario, id_carico) VALUES (?, ?, ?, ?, ?)";
                        int idcarico = idCarico+1;
                        PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                        insertStatement.setString(1, "incompleto");
                        insertStatement.setInt(2, SchemaIdPrelevato);
                        insertStatement.setInt(3, idMagazzino);
                        insertStatement.setInt(4, randomPoloId);
                        insertStatement.setInt(5, idcarico);
                        insertStatement.executeUpdate();

                        
                        insertStatement.close();
                        
                        String PrelevaQuantitaquery = "SELECT quantita FROM Prodotti";
                        PreparedStatement PrelevaQuantitaStatement = conn.prepareStatement(PrelevaQuantitaquery);
                        ResultSet PrelevaQuantitaResult = PrelevaQuantitaStatement.executeQuery();
                        if (PrelevaQuantitaResult.next()) {
                            
                            int quantita = PrelevaQuantitaResult.getInt("quantita");
                            
                            String Prodottiquery = "UPDATE Prodotti SET luogo_di_conservazione = ?, id_carico = ? , quantita = ? WHERE id_carico = ?";
                            PreparedStatement ProdottiStatement = conn.prepareStatement(Prodottiquery);
                            ProdottiStatement.setInt(1, idMagazzino);
                            ProdottiStatement.setInt(2, idcarico);
                            ProdottiStatement.setInt(3, quantita-40);
                            ProdottiStatement.setInt(4, idCarico);
                            ProdottiStatement.executeUpdate();
                            
                            
                            
                            ProdottiStatement.close();
                        }
                        
                        PrelevaQuantitaStatement.close();
                        
                        
                    }
                    
                    
                   
                   statement.close();
                   
                }catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
                }
                
            }
            
            
            if(utente.getTipo().equals("polo")) {
                try {
                    String query = "UPDATE Carichi SET stato = 'arrivato' WHERE destinatario = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, idMagazzino);
                    statement.executeUpdate();
                    
                    
          
                    
                    String Prodottiquery = "UPDATE Prodotti SET luogo_di_conservazione = ? WHERE id_carico = ?";
                    PreparedStatement ProdottiStatement = conn.prepareStatement(Prodottiquery);
                    ProdottiStatement.setInt(1, idMagazzino);
                    ProdottiStatement.setInt(2, idCarico);
                    ProdottiStatement.executeUpdate();
                    
                    
                    
                    ProdottiStatement.close();
                    
                    
                   statement.close();
                   
                }catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
                }
            }
            
            
            dispose();
            CarichiEntrata carichientrata = new CarichiEntrata(utente,idMagazzino);
            
        }
    }

    private class ProblemaCaricoButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int idCarico = Integer.parseInt(e.getActionCommand());
            ProblemaCarico probcar = new ProblemaCarico(utente, idCarico);
        }
    }
}
