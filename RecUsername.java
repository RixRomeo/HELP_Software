package progetto_help;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RecUsername extends JFrame implements ActionListener {
    private static JLabel label;
    private static JTextField email_textbox;
    private static JButton btnTrova;

    private Connection conn;

    public RecUsername() {
        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        JPanel panel = new JPanel();
        panel.setLayout(null);

        this.setTitle("RECUPERA USERNAME");
        this.setLocation(new Point(500, 300));
        getContentPane().add(panel);
        this.setSize(new Dimension(400, 200));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        label = new JLabel("Email");
        label.setBounds(100, 30, 70, 20);
        panel.add(label);

        email_textbox = new JTextField();
        email_textbox.setBounds(100, 50, 200, 28);
        panel.add(email_textbox);
        

        btnTrova = new JButton("Trova");
        btnTrova.setBounds(100, 100, 90, 25);
        btnTrova.setForeground(Color.WHITE);
        btnTrova.setBackground(Color.BLACK);
        btnTrova.addActionListener(this);
        panel.add(btnTrova);

        
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnTrova) {
            String Email = email_textbox.getText();

            try {
                String query = "SELECT username FROM utenti WHERE email = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, Email);
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                	
                    String username_rps = result.getString("username");
                    JOptionPane.showMessageDialog(null, "Username: "+username_rps);
                    this.dispose();
                    Login login = new Login();

                } 
                
                else {
                    JOptionPane.showMessageDialog(null, "Email non presente nel database!");
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
