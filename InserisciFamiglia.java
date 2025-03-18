package progetto_help;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InserisciFamiglia extends JFrame {
	private Utente utente;
    private JTextField idFamigliaTextField;
    private JTextField iseeTextField;
    private JButton inserisciMembroButton;
    private JButton inserisciButton;
    private JButton indietro;
    private Connection conn;

    public InserisciFamiglia(Utente utente) {
    	
    	this.utente=utente;
    	
        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
            dispose();
        }

        getContentPane().setLayout(null);

        JLabel idFamigliaLabel = new JLabel("ID Famiglia:");
        idFamigliaLabel.setBounds(50, 50, 100, 14);
        getContentPane().add(idFamigliaLabel);

        idFamigliaTextField = new JTextField();
        idFamigliaTextField.setBounds(160, 47, 200, 20);
        getContentPane().add(idFamigliaTextField);
        idFamigliaTextField.setColumns(10);

        JLabel iseeLabel = new JLabel("ISEE:");
        iseeLabel.setBounds(50, 80, 100, 14);
        getContentPane().add(iseeLabel);

        iseeTextField = new JTextField();
        iseeTextField.setBounds(160, 77, 200, 20);
        getContentPane().add(iseeTextField);
        iseeTextField.setColumns(10);

        inserisciMembroButton = new JButton("Aggiungi Membro");
        inserisciMembroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String famigliaId = idFamigliaTextField.getText();
                InserisciMembro insmem = new InserisciMembro(famigliaId);
            }
        });
        inserisciMembroButton.setBounds(160, 110, 200, 23);
        getContentPane().add(inserisciMembroButton);

        inserisciButton = new JButton("Inserisci");
        inserisciButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String famigliaId = idFamigliaTextField.getText();
                String isee = iseeTextField.getText();
                String poloId = getPoloIdFromCF(utente.getCF());
                if(famigliaId.equals("") || isee.equals("")) {
                	JOptionPane.showMessageDialog(null, "Devi inserire sia l'id sia l'ISEE");
                }
                else {
                	inserisciFamiglia(famigliaId, poloId, isee);
                }
            }
        });
        inserisciButton.setBounds(160, 158, 200, 23);
        getContentPane().add(inserisciButton);

        
        indietro = new JButton("Indietro");
        indietro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                GestioneTerritorioPolo gestpolo = new GestioneTerritorioPolo(utente);
            }
        });
        indietro.setBounds(160, 192, 200, 23);
        getContentPane().add(indietro);
        
        
        
        this.setTitle("Inserisci Famiglia");
        this.setSize(424, 267);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void inserisciFamiglia(String famigliaId, String poloId, String isee) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Famiglie (id_famiglia, polo, ISEE) VALUES (?, ?, ?)");
            stmt.setString(1, famigliaId);
            stmt.setString(2, poloId);
            stmt.setString(3, isee);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Famiglia inserita con successo");
            stmt.close();
            dispose();
            GestioneTerritorioPolo gestpolo = new GestioneTerritorioPolo(utente);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Errore durante l'inserimento della famiglia");
        }
        
        
    }

    private String getPoloIdFromCF(String cf) {
        String poloId = null;
        try {
            String query = "SELECT id_polo FROM poli WHERE cf_gestore = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, cf);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                poloId = result.getString("id_polo");
            }

            result.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'ottenimento dell'ID del polo");
        }
        return poloId;
    }
}
