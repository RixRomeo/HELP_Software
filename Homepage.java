package progetto_help;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Homepage extends JFrame implements ActionListener  {

	private Utente utente;
	private JButton button1;
	private JButton button2;
	private JButton button3;   
	private JButton button4;
	private JButton button5;
	private Connection conn;
	
	
	public Homepage(Utente utente) {
		
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
		this.add(panel);
		this.setSize(new Dimension(700, 400));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(panel);
		this.setVisible(true);
		
		button1 = new JButton("Gestione Territorio");
		button1.setBounds(140, 90, 148, 50);
		button1.setForeground(Color.WHITE);
		button1.setBackground(Color.BLACK);
		button1.addActionListener(this);
		panel.add(button1);
		
		button2 = new JButton("Gestione Report");
		button2.setBounds(140, 180, 148, 50);
		button2.setForeground(Color.WHITE);
		button2.setBackground(Color.BLACK);
		button2.addActionListener(this);
		panel.add(button2);
		
		button3 = new JButton("Gestione magazzino");
		button3.setBounds(400, 90, 148, 50);
		button3.setForeground(Color.WHITE);
		button3.setBackground(Color.BLACK);
		button3.addActionListener(this);
		panel.add(button3);
		
		button4 = new JButton("profilo");
		button4.setBounds(400, 180, 148, 50);
		button4.setForeground(Color.WHITE);
		button4.setBackground(Color.BLACK);
		button4.addActionListener(this);
		panel.add(button4);
		
		ImageIcon icon = new ImageIcon("C:\\Users\\romeo\\eclipse-workspace\\ProgettoHelp\\src\\progetto_help\\Mail.png");

        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
		
        button5 = new JButton(scaledIcon);
		button5.setBounds(600, 20, 50, 50);
		button5.setBorder(null); // Rimuovi il bordo del bottone
	    button5.setContentAreaFilled(false); // Rimuovi il riempimento dell'area del contenuto del bottone
	    button5.setFocusPainted(false); // Rimuovi l'effetto di focus sul bottone
	    button5.setOpaque(false); // Rendi il bottone trasparente
		button5.addActionListener(this);
		panel.add(button5);
		
		
		
	}


	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == button1) {
			if(utente.getTipo().equals("polo")) {
				this.dispose();
				GestioneTerritorioPolo gestpolo = new GestioneTerritorioPolo(utente);
			}
			else if(utente.getTipo().equals("diocesi")) {
				this.dispose();
				GestioneTerritorioDiocesi gestdiocesi = new GestioneTerritorioDiocesi(utente);
			}
			else if(utente.getTipo().equals("help")) {
				this.dispose();
				GestioneTerritorioHELP gesthelp = new GestioneTerritorioHELP(utente);
			}
		}
		
		
		if(e.getSource() == button2) {
			this.dispose();
			GestioneReport gestreport = new GestioneReport(utente);
		}
		
		
		if(e.getSource() == button3) { //Gestione Magazzino
			this.dispose();
			
			try {
            	String query = "SELECT m.id_magazzino FROM Magazzini m JOIN utenti u ON m.responsabile=u.cf WHERE u.cf=?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, utente.getCF());
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    int idMagazzino = result.getInt("m.id_magazzino");
                    
                    if(utente.getTipo().equals("diocesi") || utente.getTipo().equals("help")) {
                    	GestioneMagazzino gestmag = new GestioneMagazzino(utente,idMagazzino);
                    }
                    
                    else if(utente.getTipo().equals("polo")) {
                    	GestioneMagazzinoPolo gestmagpolo = new GestioneMagazzinoPolo(utente,idMagazzino);
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