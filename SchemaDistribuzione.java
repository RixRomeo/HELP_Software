
//Stiamo simulando che il sistema abbiamo creato questo schema di distribuzione!

package progetto_help;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SchemaDistribuzione extends JFrame implements ActionListener {
    private JTable table;
    private Connection conn;
    private Utente utente;
    private JButton button1;
    private String testolabel;
    private int luogo;
    private int PoloId;
    private int quantita;

    public SchemaDistribuzione(Utente utente) {
        this.utente = utente;
       
        quantita = 100;

        try {
            conn = Database.getConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la connessione al database");
        }

        String[] columnNames = {" ", " "};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rende tutte le celle non modificabili
            }
        };

        try {
            //String cfUtente = utente.getCF();
            String query = "SELECT * FROM SchemaDistribuzione WHERE destinatario = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, utente.getCF());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                ResultSetMetaData metaData = result.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = result.getString(i);

                    String[] row = {columnName, value};
                    model.addRow(row);
                }
            }

            result.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
        }

        table = new JTable(model);

        // Creazione del pannello che contiene la tabella
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        
        
        if(utente.getTipo().equals("help")) {
        	try {
        		
        		String DiocesiRandomQuery = "SELECT m.id_magazzino, m.responsabile FROM Magazzini m JOIN utenti u ON m.responsabile=u.cf where u.tipo='diocesi' ORDER BY RAND() LIMIT 1";
         	    PreparedStatement randomDioceseStatement = conn.prepareStatement(DiocesiRandomQuery);
         	    ResultSet randomDioceseResult = randomDioceseStatement.executeQuery();
         	    if (randomDioceseResult.next()) {
         	        int randomDioceseId = randomDioceseResult.getInt("m.id_magazzino");
         	     
         	        testolabel = "inviare " + (quantita-40) + " panini alla diocesi: "+randomDioceseId;
         	        
         	    }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
            }
        }
        
        if(utente.getTipo().equals("diocesi")) {
        	try {
        		
        		String PrendiDiocesiQuery = "SELECT id_diocesi FROM Diocesi WHERE cf_gestore=?";
        	    PreparedStatement PrendiDiocesistatement = conn.prepareStatement(PrendiDiocesiQuery);
        	    PrendiDiocesistatement.setString(1,utente.getCF());
        	    ResultSet PrendiDiocesiResult = PrendiDiocesistatement.executeQuery();
        	    
        	    while(PrendiDiocesiResult.next()) {
        	    	luogo=PrendiDiocesiResult.getInt("id_diocesi");
        	    }
        		
        		
        		
        		String RandomQuery = "SELECT m.id_magazzino, m.responsabile FROM Magazzini m JOIN utenti u ON m.responsabile=u.cf WHERE u.tipo='polo' AND u.cf=(SELECT cf_gestore FROM poli WHERE diocesi = ?) ORDER BY RAND() LIMIT 1";
        	    PreparedStatement randomPoloStatement = conn.prepareStatement(RandomQuery);
        	    randomPoloStatement.setInt(1, luogo);

        	    ResultSet randomPoloResult = randomPoloStatement.executeQuery();
        	    if (randomPoloResult.next()) {
        	        int randomPoloId = randomPoloResult.getInt("m.id_magazzino");
         	    
        	        quantita = quantita-60;
        	        testolabel = "inviare " + (quantita) + " panini al polo: "+randomPoloId;
         	        
         	    }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
            }
        }
        
        
        if(utente.getTipo().equals("polo")) {
        	try {
        		
        		String PrelevaPoloquery = "SELECT id_polo FROM poli WHERE cf_gestore = ?";
        		PreparedStatement PrelevaPoloStatement = conn.prepareStatement(PrelevaPoloquery);
        		PrelevaPoloStatement.setString(1, utente.getCF());
        		ResultSet PrelevaPoloResult = PrelevaPoloStatement.executeQuery();
        		
        		if(PrelevaPoloResult.next()) {
        			PoloId = PrelevaPoloResult.getInt("id_polo");
        		}
        		
        		
        		
        		String FamigliaRandomQuery = "SELECT id_famiglia FROM Famiglie WHERE polo = ? ORDER BY RAND() LIMIT 1";
        		PreparedStatement randomFamigliaStatement = conn.prepareStatement(FamigliaRandomQuery);
        		randomFamigliaStatement.setInt(1,PoloId);
         	    ResultSet randomDioceseResult = randomFamigliaStatement.executeQuery();
         	    if (randomDioceseResult.next()) {
         	        int famigliaid = randomDioceseResult.getInt("id_famiglia");
         	        
         	       quantita = quantita - 90; 
         	       testolabel = "dare " + (quantita) + " panini alla famiglia: "+famigliaid;
         	        
         	    }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query");
            }
        }
        
        
     // Creazione del JLabel con il testo desiderato
        JLabel label = new JLabel(testolabel);

        // Creazione del pannello per il JLabel
        JPanel labelPanel = new JPanel();
        labelPanel.add(label);

        // Creazione dei pulsanti
        button1 = new JButton("Indietro");

        // Creazione di un pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(button1);

        button1.addActionListener(this);

        // Creazione di un pannello principale per contenere la tabella e il JLabel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(labelPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Creazione della finestra principale
        this.setTitle("Schema Distribuzione");
        this.setSize(400, 350);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            this.dispose();
        }
    }
}
