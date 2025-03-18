package progetto_help;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Registrati extends JFrame implements ActionListener {
    private JTextField cfMembroTextField;
    private JTextField nomeTextField;
    private JTextField cognomeTextField;
    private JTextField etaTextField;
    private JTextField ruoloTextField;
    private JTextField bisogniSpecialiTextField;
    private JButton inserisciButton;
    private JButton indietro;
    private Connection conn;

    public Registrati() {
        

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
            dispose();
        }

        setTitle("Registrazione");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel cfMembroLabel = new JLabel("Codice Fiscale:");
        cfMembroLabel.setBounds(50, 20, 100, 14);
        contentPane.add(cfMembroLabel);

        cfMembroTextField = new JTextField();
        cfMembroTextField.setBounds(160, 17, 200, 20);
        contentPane.add(cfMembroTextField);
        cfMembroTextField.setColumns(10);

        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setBounds(50, 50, 100, 14);
        contentPane.add(nomeLabel);

        nomeTextField = new JTextField();
        nomeTextField.setBounds(160, 47, 200, 20);
        contentPane.add(nomeTextField);
        nomeTextField.setColumns(10);

        JLabel cognomeLabel = new JLabel("Cognome:");
        cognomeLabel.setBounds(50, 80, 100, 14);
        contentPane.add(cognomeLabel);

        cognomeTextField = new JTextField();
        cognomeTextField.setBounds(160, 77, 200, 20);
        contentPane.add(cognomeTextField);
        cognomeTextField.setColumns(10);

        JLabel etaLabel = new JLabel("Età:");
        etaLabel.setBounds(50, 110, 100, 14);
        contentPane.add(etaLabel);

        etaTextField = new JTextField();
        etaTextField.setBounds(160, 107, 200, 20);
        contentPane.add(etaTextField);
        etaTextField.setColumns(10);

        JLabel ruoloLabel = new JLabel("Email:");
        ruoloLabel.setBounds(50, 140, 100, 14);
        contentPane.add(ruoloLabel);

        ruoloTextField = new JTextField();
        ruoloTextField.setBounds(160, 137, 200, 20);
        contentPane.add(ruoloTextField);
        ruoloTextField.setColumns(10);

        JLabel bisogniSpecialiLabel = new JLabel("Ruolo:");
        bisogniSpecialiLabel.setBounds(50, 170, 100, 14);
        contentPane.add(bisogniSpecialiLabel);

        bisogniSpecialiTextField = new JTextField();
        bisogniSpecialiTextField.setBounds(160, 167, 200, 20);
        contentPane.add(bisogniSpecialiTextField);
        bisogniSpecialiTextField.setColumns(10);

        inserisciButton = new JButton("Registrati");
        inserisciButton.addActionListener(this);
        inserisciButton.setBounds(87, 227, 89, 23);
        contentPane.add(inserisciButton);
        
        indietro = new JButton("Indietro");
        indietro.addActionListener(this);
        indietro.setBounds(213, 227, 89, 23);
        contentPane.add(indietro);

        setSize(450, 300);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == inserisciButton) {
            
        }
        
        
        if(e.getSource() == indietro) {
        	this.dispose();
        	Login login = new Login();
        }
    }

    
}
