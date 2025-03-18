package progetto_help;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomepageAzienda extends JFrame implements ActionListener  {

	private Utente utente;
	private static JButton button3;   
	private static JButton button4;
	private static JButton button5;
	private Connection conn;
	
	
	public HomepageAzienda(Utente utente) {
		
		this.utente = utente;
		
		try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		this.setTitle("HOMEPAGE");
		//this.setLocation(new Point(500, 300));
		getContentPane().add(panel);
		this.setSize(new Dimension(400, 400));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(panel);
		this.setVisible(true);
		
		button3 = new JButton("Gestione magazzino");
		button3.setBounds(112, 98, 148, 50);
		button3.setForeground(Color.WHITE);
		button3.setBackground(Color.BLACK);
		button3.addActionListener(this);
		panel.add(button3);
		
		button4 = new JButton("profilo");
		button4.setBounds(112, 187, 148, 50);
		button4.setForeground(Color.WHITE);
		button4.setBackground(Color.BLACK);
		button4.addActionListener(this);
		panel.add(button4);
		
		ImageIcon icon = new ImageIcon("C:\\Users\\romeo\\eclipse-workspace\\ProgettoHelp\\src\\progetto_help\\Mail.png");

        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
		
        button5 = new JButton(scaledIcon);
		button5.setBounds(163, 11, 50, 50);
		button5.setBorder(null); // Rimuovi il bordo del bottone
	    button5.setContentAreaFilled(false); // Rimuovi il riempimento dell'area del contenuto del bottone
	    button5.setFocusPainted(false); // Rimuovi l'effetto di focus sul bottone
	    button5.setOpaque(false); // Rendi il bottone trasparente
		button5.addActionListener(this);
		panel.add(button5);
		
		
		
	}


	
	public void actionPerformed(ActionEvent e) {
	
		if(e.getSource() == button3) { //Gestione Magazzinbo
			this.dispose();
			
			try {
            	String query = "SELECT m.id_magazzino FROM Magazzini m JOIN utenti u ON m.responsabile=u.cf WHERE u.cf=?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, utente.getCF());
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    int idMagazzino = result.getInt("m.id_magazzino");
                    
                    this.dispose();
                    GestioneMagazzinoAzienda gestmagazienda = new GestioneMagazzinoAzienda(utente,idMagazzino);

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
		
		
		if(e.getSource() == button4) { //Profilo
			//this.dispose();
			Profilo profilo = new Profilo(utente);
		}
		
		if(e.getSource() == button5) { //Messaggi
			this.dispose();
			Messaggi messaggi = new Messaggi(utente);
		}
		
	}
	
}

