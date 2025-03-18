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

public class GestioneReport extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private Connection conn;
    private Utente utente;
    private JButton button4, button5;

    public GestioneReport(Utente utente) {
        this.utente = utente;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }
        button4 = new JButton("Crea Nuovo Report");
        button5 = new JButton("Indietro");

        // Creazione del pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
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
            String cfUtente = utente.getCF(); 
            String query = "SELECT id_report FROM Report where id_creatore = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, cfUtente);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                
            	String idReport = result.getString("id_report");

                JLabel famigliaLabel = new JLabel("Report:");
                constraints.gridx = 0;
                mainPanel.add(famigliaLabel, constraints);

                JLabel idLabel = new JLabel(idReport);
                constraints.gridx = 1;
                mainPanel.add(idLabel, constraints);

                JButton visualizzaButton = new JButton("Visualizza Report");
                visualizzaButton.setActionCommand(idReport);
                visualizzaButton.addActionListener(new ReportButtonActionListener());
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

        this.setTitle("Gestione Report");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);
        button4.addActionListener(this);
        button5.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button5) {
            this.dispose();
            Homepage homepage = new Homepage(utente);
        }
        
        if (e.getSource() == button4) {
            //this.dispose();
            CreaNuovoReport crewnewreport = new CreaNuovoReport(utente);
        }

    }

    private class ReportButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String idReport = e.getActionCommand();
            if (idReport != null) {
            	VisualizzaReport viewreport = new VisualizzaReport(utente, idReport);
            }
            
           
        }
        
        
    }

   
    
    
}
