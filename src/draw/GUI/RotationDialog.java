package draw.GUI;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class RotationDialog extends JDialog{
	
	private boolean isOKed = false;
	
    private JTextField rotationText = new JTextField();
	private double rotationDeg = 0;
	
    private JLabel rotationLabel = new JLabel("Rotation(deg): ");
    private JButton rotationOK = new JButton("OK");
    private JButton rotationCancel = new JButton("Cancel");
    
	public RotationDialog() {
		RotationDialog self = this;
		this.setLayout(new FlowLayout());
        this.setSize(new Dimension(200,100));
        this.setResizable(false);
        this.setTitle("Rotation dialog");
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        

        rotationText.setColumns(8);
        rotationOK.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent evt) {	
        		while(true) {
        			try {
        				setRotationDeg(Integer.parseInt(rotationText.getText()));
        				rotationText.setText(null);
        				self.setOKed(true);
        				self.setVisible(false);
        				break;
        			}catch(Exception e) {
        				JOptionPane.showMessageDialog(self, "Must enter an integer!");
        				self.setOKed(false);
        				rotationText.setText(null);
        				return;
        			}
        		}
        	}
        });
        rotationCancel.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent evt) {
        		self.setOKed(false);
        		self.setVisible(false);
        	}
        });
        
        this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				self.setOKed(false);
				self.setVisible(false);
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        this.add(rotationLabel);
        this.add(rotationText);
        this.add(rotationOK);
        this.add(rotationCancel);
	}
	
    public JTextField getRotationText() {
		return rotationText;
	}

	public void setRotationText(JTextField rotationText) {
		this.rotationText = rotationText;
	}

	public double getRotationDeg() {
		return rotationDeg;
	}

	public void setRotationDeg(double rotationDeg) {
		this.rotationDeg = rotationDeg;
	}

	public boolean isOKed() {
		return isOKed;
	}

	public void setOKed(boolean isOKed) {
		this.isOKed = isOKed;
	}
}
