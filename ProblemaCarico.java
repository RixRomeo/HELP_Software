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

public class ProblemaCarico extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private Connection conn;
    private Utente utente;
    private JButton button3;
    private int idCarico;
    private JTextField textField;

    public ProblemaCarico(Utente utente, int idCarico) {
        this.utente = utente;
        this.idCarico = idCarico;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        button3 = new JButton("Conferma");

        // Creazione del pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button3);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        try {

            String query = "SELECT id_prodotto, lotto, quantita FROM Prodotti WHERE id_carico = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idCarico);
            ResultSet result = statement.executeQuery();

            while (result.next()) {

                int idprod = result.getInt("id_prodotto");
                String lotto = result.getString("lotto");
                int quantita = result.getInt("quantita");

                JLabel prodottoLabel = new JLabel("Prodotto:");
                constraints.gridx = 0;
                mainPanel.add(prodottoLabel, constraints);

                JLabel idLabel = new JLabel(String.valueOf(idprod));
                constraints.gridx = 1;
                mainPanel.add(idLabel, constraints);

                JLabel lottoLabel = new JLabel(lotto);
                constraints.gridx = 2;
                mainPanel.add(lottoLabel, constraints);

                JLabel quantitaLabel = new JLabel(String.valueOf(quantita));
                constraints.gridx = 3;
                mainPanel.add(quantitaLabel, constraints);

                JCheckBox checkBox = new JCheckBox();
                constraints.gridx = 4;
                mainPanel.add(checkBox, constraints);

                textField = new JTextField(10);
                constraints.gridx = 5;
                mainPanel.add(textField, constraints);
                textField.setVisible(false); // Nascondo il JTextField all'inizio

                checkBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (checkBox.isSelected()) {
                            textField.setVisible(true);
                            textField.setEditable(true);
                            textField.requestFocus();
                        } else {
                            textField.setVisible(false);
                        }
                    }
                });

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

        this.setTitle("Problema Carico");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setVisible(true);
        button3.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button3) {
            this.dispose();
            String testo=textField.getText();
            JOptionPane.showMessageDialog(null, "Nuova quantita' : "+testo);
        }
    }
}
