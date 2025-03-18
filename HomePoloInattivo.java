package progetto_help;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomePoloInattivo extends JFrame implements ActionListener  {

	private Utente utente;
	private JButton button3;   
	private JButton button5;
	private Connection conn;
	
	
	public HomePoloInattivo(Utente utente) {
		
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
		this.setSize(new Dimension(319, 228));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(panel);
		this.setVisible(true);
		
		button3 = new JButton("RIABILITA POSIZIONE");
		button3.setBounds(63, 66, 171, 62);
		button3.setForeground(Color.WHITE);
		button3.setBackground(Color.BLACK);
		button3.addActionListener(this);
		panel.add(button3);
		
		
	}


	
	public void actionPerformed(ActionEvent e) {
	
		
		if(e.getSource() == button3) { 
			//this.dispose();
			
			try {
	            
	            String query = "SELECT id_magazzino FROM Magazzini WHERE responsabile = ? ";
	            PreparedStatement statement = conn.prepareStatement(query);
	            statement.setString(1, utente.getCF());
	            ResultSet result = statement.executeQuery();

	            while (result.next()) {
	                int idmagazzino = result.getInt("id_magazzino");
	                
	                CarichiEntrataPoloInattivo ciao = new CarichiEntrataPoloInattivo(utente,idmagazzino);

	                
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

