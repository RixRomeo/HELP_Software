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

public class GestioneTerritorioPolo extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private Connection conn;
    private Utente utente;
    private JButton button1, button2, button3, button4, button5;

    public GestioneTerritorioPolo(Utente utente) {
        this.utente = utente;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        // Creazione dei pulsanti
        button1 = new JButton("I");
        button2 = new JButton("Inserisci Famiglia");
        button3 = new JButton("Schema di Distribuzione");
        button4 = new JButton("Autosospensione");
        button5 = new JButton("Indietro");

        // Creazione del pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);
        buttonPanel.add(button5);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        try {
            String cfUtente = utente.getCF(); // Sostituisci con il CF dell'utente desiderato
            String query = "SELECT famiglie.id_famiglia FROM famiglie JOIN poli ON famiglie.polo = poli.id_polo WHERE poli.cf_gestore = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, cfUtente);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String idFamiglia = result.getString("id_famiglia");

                JLabel famigliaLabel = new JLabel("Famiglia:");
                constraints.gridx = 0;
                mainPanel.add(famigliaLabel, constraints);

                JLabel idLabel = new JLabel(idFamiglia);
                constraints.gridx = 1;
                mainPanel.add(idLabel, constraints);

                JButton visualizzaButton = new JButton("Visualizza Famiglia");
                visualizzaButton.setActionCommand(idFamiglia);
                visualizzaButton.addActionListener(new FamigliaButtonActionListener());
                constraints.gridx = 2;
                mainPanel.add(visualizzaButton, constraints);

                JButton eliminaButton = new JButton("Elimina Famiglia");
                eliminaButton.setActionCommand(idFamiglia);
                eliminaButton.addActionListener(new EliminaFamigliaButtonActionListener());
                constraints.gridx = 3;
                mainPanel.add(eliminaButton, constraints);

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
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);

        // Aggiunta dei listener ai pulsanti
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            Info info = new Info(utente);
        }

        if (e.getSource() == button2) {
        	this.dispose();
            InserisciFamiglia insfam = new InserisciFamiglia(utente);
        }
        
        if(e.getSource() == button3) {
        	SchemaDistribuzione schemdistr = new SchemaDistribuzione(utente);
        }

        if (e.getSource() == button5) {
            this.dispose();
            Homepage homepage = new Homepage(utente);
        }

    }

    private class FamigliaButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String idFamiglia = e.getActionCommand();
            if (idFamiglia != null) {
                VisualizzaFamiglia visfam = new VisualizzaFamiglia(idFamiglia);
            }
            
           
        }
        
        
    }

    private class EliminaFamigliaButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String idFamiglia = e.getActionCommand();
            if (idFamiglia != null) {
                ConfermaDialog confermaDialog = new ConfermaDialog(GestioneTerritorioPolo.this, "Conferma Eliminazione","Vuoi confermare l'eliminazione");
                confermaDialog.setVisible(true);

                if (confermaDialog.isConfermato()) {
                    try {
                        // Elimina tutti i membri della famiglia dalla tabella "MembriFamiglie"
                        String deleteMembriQuery = "DELETE FROM MembriFamiglie WHERE id_famiglia = ?";
                        PreparedStatement deleteMembriStmt = conn.prepareStatement(deleteMembriQuery);
                        deleteMembriStmt.setString(1, idFamiglia);
                        deleteMembriStmt.executeUpdate();
                        deleteMembriStmt.close();

                        // Elimina la famiglia dalla tabella "Famiglie"
                        String deleteFamigliaQuery = "DELETE FROM Famiglie WHERE id_famiglia = ?";
                        PreparedStatement deleteFamigliaStmt = conn.prepareStatement(deleteFamigliaQuery);
                        deleteFamigliaStmt.setString(1, idFamiglia);
                        deleteFamigliaStmt.executeUpdate();
                        deleteFamigliaStmt.close();

                        // Rimuovi la famiglia dalla visualizzazione
                        Component[] components = mainPanel.getComponents();
                        for (Component component : components) {
                            if (component instanceof JButton) {
                                JButton button = (JButton) component;
                                if (button.getActionCommand().equals(idFamiglia)) {
                                    mainPanel.remove(component);
                                }
                            }
                        }

                        mainPanel.revalidate();
                        mainPanel.repaint();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Errore durante l'eliminazione della famiglia");
                    }
                }
            }
            
            dispose();
            GestioneTerritorioPolo gestpolo = new GestioneTerritorioPolo(utente);
            
        }
    }
}
