package progetto_help;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfermaDialog extends JDialog implements ActionListener {
    private boolean confermato;
    
    private String testo;

    public ConfermaDialog(Frame owner, String title, String testo) {
        super(owner, title, true);
        this.testo=testo;
        setTitle("Conferma");

        confermato = false;

        JLabel lblVuoiConfermareLoperazione = new JLabel(testo);
        JButton confermaButton = new JButton("Conferma");
        JButton annullaButton = new JButton("Annulla");

        confermaButton.addActionListener(this);
        annullaButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confermaButton);
        buttonPanel.add(annullaButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(lblVuoiConfermareLoperazione, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public boolean isConfermato() {
        return confermato;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Conferma")) {
            confermato = true;
        }

        this.dispose(); // Chiude il dialogo
    }
}
