package progetto_help;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    private JLabel password1, label;
    private JTextField username;
    private JButton button_login;
    private JPasswordField Password;
    private JButton btnRecuperaUsername;
    private JButton btnRecuperaPassword;
    private JButton registrati;
    private Connection conn;

    public Login() {
        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        JPanel panel = new JPanel();
        panel.setLayout(null);

        this.setTitle("LOGIN");
        this.setLocation(new Point(500, 300));
        getContentPane().add(panel);
        this.setSize(new Dimension(520, 220));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        label = new JLabel("Username");
        label.setBounds(138, 11, 70, 20);
        panel.add(label);

        username = new JTextField();
        username.setBounds(137, 27, 193, 28);
        panel.add(username);

        password1 = new JLabel("Password");
        password1.setBounds(138, 53, 70, 20);
        panel.add(password1);

        Password = new JPasswordField();
        Password.setBounds(138, 71, 193, 28);
        panel.add(Password);

        button_login = new JButton("Login");
        button_login.setBounds(138, 110, 90, 25);
        button_login.setForeground(Color.WHITE);
        button_login.setBackground(Color.BLACK);
        button_login.addActionListener(this);
        panel.add(button_login);

        registrati = new JButton("Registrati");
        registrati.setBounds(240, 110, 90, 25);
        registrati.setForeground(Color.WHITE);
        registrati.setBackground(Color.BLACK);
        registrati.addActionListener(this);
        panel.add(registrati);

        btnRecuperaUsername = new JButton(" Recupera Username");
        btnRecuperaUsername.setBounds(26, 150, 164, 25);
        btnRecuperaUsername.setForeground(Color.WHITE);
        btnRecuperaUsername.setBackground(Color.BLACK);
        btnRecuperaUsername.addActionListener(this);
        panel.add(btnRecuperaUsername);

        btnRecuperaPassword = new JButton("Recupera Password");
        btnRecuperaPassword.setBounds(311, 150, 164, 25);
        btnRecuperaPassword.setForeground(Color.WHITE);
        btnRecuperaPassword.setBackground(Color.BLACK);
        btnRecuperaPassword.addActionListener(this);
        panel.add(btnRecuperaPassword);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button_login) {
            String Username = username.getText();
            String Password1 = new String(Password.getPassword());

            try {
            	String query = "SELECT u.cf, u.tipo, p.stato FROM utenti u LEFT JOIN poli p ON u.cf = p.cf_gestore AND u.luogo = p.id_polo WHERE u.username = ? AND u.password_utente = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, Username);
                statement.setString(2, Password1);
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    String tipo_rps = result.getString("tipo");
                    String cf_rps = result.getString("cf");
                    String stato_polo = result.getString("stato");

                    Utente utente = new Utente(cf_rps, Username, Password1, tipo_rps);

                    if (tipo_rps.equals("diocesi") || tipo_rps.equals("help")) {
                        this.dispose();
                        Homepage homepage = new Homepage(utente);
                    } else if (tipo_rps.equals("azienda")) {
                        this.dispose();
                        HomepageAzienda homepageazienda = new HomepageAzienda(utente);
                    } else if (tipo_rps.equals("polo")) {
                        if (stato_polo.equals("attivo")) {
                            this.dispose();
                            Homepage homepage = new Homepage(utente);
                        } else if (stato_polo.equals("sospeso")) {
                        	this.dispose();
                            HomePoloInattivo homepoloinvattivo = new HomePoloInattivo(utente);
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Username o Password Errati!");
                }

                result.close();
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
            }
        }

        if (e.getSource() == btnRecuperaUsername) {
        	this.dispose();
        	RecUsername recuperausername = new RecUsername();
        	
        }

       
        if (e.getSource() == btnRecuperaPassword) {
        	this.dispose();
        	RecPSW recuperapassword = new RecPSW();
        	
        }
        
        if(e.getSource() == registrati) {
        	this.dispose();
        	Registrati registrazione = new Registrati();
        }
        
        
    }
}
