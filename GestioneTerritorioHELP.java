package progetto_help;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestioneTerritorioHELP extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private Connection conn;
    private Utente utente;
    private JButton button1, button2;

    public GestioneTerritorioHELP(Utente utente) {
        this.utente = utente;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        // Creazione dei pulsanti
        button1 = new JButton("Schema Di Distribuzione");
        button2 = new JButton("Indietro");

        // Creazione del pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button1);
        buttonPanel.add(button2);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        try {
            String query = "SELECT id_diocesi FROM Diocesi";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String idDiocesi = result.getString("id_diocesi");

                JLabel diocesiLabel = new JLabel("Diocesi:");
                constraints.gridx = 0;
                mainPanel.add(diocesiLabel, constraints);

                JLabel idLabel = new JLabel(idDiocesi);
                constraints.gridx = 1;
                mainPanel.add(idLabel, constraints);

                JButton visualizzaButton = new JButton("Visualizza Poli");
                visualizzaButton.setActionCommand(idDiocesi);
                visualizzaButton.addActionListener(new VisualizzaPoliButtonActionListener());
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

        this.setTitle("Gestione Territorio");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);

        // Aggiunta dei listener ai pulsanti
        button1.addActionListener(this);
        button2.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            SchemaDistribuzione schemdistr = new SchemaDistribuzione(utente);
        }

        if (e.getSource() == button2) {
            this.dispose();
            Homepage homepage = new Homepage(utente);
        }

        // Implementa le azioni per gli altri pulsanti qui
    }

    private class VisualizzaPoliButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String idDiocesi = e.getActionCommand();
            if (idDiocesi != null) {
            	dispose();
                VisualizzaPoli vispoli = new VisualizzaPoli(utente,idDiocesi);
            }
        }
    }
}
