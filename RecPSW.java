package progetto_help;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RecPSW extends JFrame implements ActionListener {
	private static JLabel label_email, label_cf;
	private static JTextField cf_textbox;
	private static JButton button;
	private static JTextField email_textbox;

    private Connection conn;

    public RecPSW() {
        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        JPanel panel = new JPanel();
        panel.setLayout(null);

   
        this.setTitle("RECUPERA PASSWORD");
        this.setLocation(new Point(500, 300));
        this.add(panel);
        this.setSize(new Dimension(400, 200));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        label_cf = new JLabel("CF");
        label_cf.setBounds(100, 8, 70, 20);
        panel.add(label_cf);

        cf_textbox = new JTextField();
        cf_textbox.setBounds(100, 27, 193, 28);
        panel.add(cf_textbox);
        
        label_email = new JLabel("Email");
        label_email.setBounds(100, 55, 70, 20);
        panel.add(label_email);
        
        email_textbox = new JTextField();
        email_textbox.setBounds(100, 75, 193, 28);
        panel.add(email_textbox);
        

        button = new JButton("trova");
        button.setBounds(100, 110, 90, 25);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.addActionListener(this);
        panel.add(button);

        
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button) {
        	String cf = cf_textbox.getText();
            String Email = email_textbox.getText();

            try {
                String query = "SELECT password_utente FROM utenti WHERE cf = ? AND email = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, cf);
                statement.setString(2, Email);
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                	
                    String psw_rps = result.getString("password_utente");
                    JOptionPane.showMessageDialog(null, "Password: "+psw_rps);
                    this.dispose();
                    Login login = new Login();

                } 
                
                else {
                    JOptionPane.showMessageDialog(null, "Email o CF non presente nel database!");
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

