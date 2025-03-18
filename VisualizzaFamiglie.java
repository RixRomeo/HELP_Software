package progetto_help;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VisualizzaFamiglie extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private Connection conn;
    private Utente utente;
    private String idPolo;
    private String idDiocesi;
    private JButton backButton;

    public VisualizzaFamiglie(Utente utente, String idPolo, String idDiocesi) {
        this.utente = utente;
        this.idPolo = idPolo;
        this.idDiocesi = idDiocesi; 
        		
        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        // Creazione del pulsante "Indietro"
        backButton = new JButton("Indietro");

        // Creazione del pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        try {
            String query = "SELECT id_famiglia FROM Famiglie WHERE polo = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, idPolo);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String idFamiglia = result.getString("id_famiglia");

                JLabel famigliaLabel = new JLabel("Famiglia:");
                constraints.gridx = 0;
                mainPanel.add(famigliaLabel, constraints);

                JLabel idLabel = new JLabel(idFamiglia);
                constraints.gridx = 1;
                mainPanel.add(idLabel, constraints);

                JButton visualizzaFamigliaButton = new JButton("Visualizza Famiglia");
                visualizzaFamigliaButton.setActionCommand(idFamiglia);
                visualizzaFamigliaButton.addActionListener(new VisualizzaFamigliaButtonActionListener());
                constraints.gridx = 2;
                mainPanel.add(visualizzaFamigliaButton, constraints);

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

        this.setTitle("Visualizza Dettagli HELP");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);

        // Aggiunta del listener al pulsante "Indietro"
        backButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            this.dispose();
            VisualizzaPoli visualizzaPoli = new VisualizzaPoli(utente, idDiocesi);
        }

        // Implementa le azioni per gli altri pulsanti qui
    }

    class VisualizzaFamigliaButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String idFamiglia = e.getActionCommand();
            if (idFamiglia != null) {
                VisualizzaFamiglia visfam = new VisualizzaFamiglia(idFamiglia);
            }
        }
    }
}
