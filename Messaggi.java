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

public class Messaggi extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private Connection conn;
    private Utente utente;
    private JButton button5;

    public Messaggi(Utente utente) {
        this.utente = utente;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }
        button5 = new JButton("Indietro");

        // Creazione del pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button5);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        try {
            String cfUtente = utente.getCF(); 
            String query = "SELECT id_mess FROM Messaggi WHERE destinatario = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, cfUtente);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                
            	String idMessaggi = result.getString("id_mess");

                JLabel famigliaLabel = new JLabel("Messaggio:");
                constraints.gridx = 0;
                mainPanel.add(famigliaLabel, constraints);

                JLabel idLabel = new JLabel(idMessaggi);
                constraints.gridx = 1;
                mainPanel.add(idLabel, constraints);

                JButton visualizzaButton = new JButton("Visualizza Messaggio");
                visualizzaButton.setActionCommand(idMessaggi);
                visualizzaButton.addActionListener(new MessaggioButtonActionListener());
                constraints.gridx = 2;
                mainPanel.add(visualizzaButton, constraints);

                

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

        this.setTitle("Messaggi");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);
        button5.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button5) {
            this.dispose();
            Homepage homepage = new Homepage(utente);
        }

    }

    private class MessaggioButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String idMessaggi = e.getActionCommand();
            if (idMessaggi != null) {
            	try {
                    String cfUtente = utente.getCF(); 
                    String query = "SELECT tipo_mess, testo FROM Messaggi WHERE destinatario = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, cfUtente);
                    ResultSet result = statement.executeQuery();

                    while (result.next()) {
                        
                    	String tipomess = result.getString("tipo_mess");
                    	String testomess = result.getString("testo");

                        if(tipomess.equals("testo")) {
                        	JOptionPane.showMessageDialog(null, testomess);
                        }
                        
                        else if(tipomess.equals("richiesta")) {
                        	ConfermaDialog confermaDialog = new ConfermaDialog(Messaggi.this,"Conferma Richiesta",testomess);
                        	confermaDialog.setVisible(true);
                        }
                    	
                    }

                    result.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
                }
            }
            
           
        }
        
        
    }

   
    
    
}
