package draw.GUI;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ZOrderDialog extends JDialog{
	
	private boolean isOKed = false;
	
	JLabel zLabel = new JLabel("Z Position: ");
	JTextField zText = new JTextField();
	
	JButton zOK = new JButton("OK");
	JButton zCancel = new JButton("Cancel");
	
	int zPosition = -1; // may have to correct value
	
	public ZOrderDialog() {
		ZOrderDialog self = this;
		this.setLayout(new FlowLayout());
		this.setSize(new Dimension(200,200));
		this.setResizable(false);
		this.setTitle("Z Order dialog");
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        
        zText.setColumns(8);
        zOK.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent e) {
        		while(true) {
        			try {
        				setZPosition(Integer.parseInt(zText.getText()));
        				zText.setText(null);
        				self.setOKed(true);
        				self.setVisible(false);
        				break;
        			}catch(Exception ex) {
        				JOptionPane.showMessageDialog(self, "Must enter an integer number!");
        				self.setOKed(false);
        				zText.setText(null);
        				return;
        			}
        		}
        	}
        });
        zCancel.addMouseListener(new java.awt.event.MouseAdapter() {
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
        
        this.add(zLabel);
        this.add(zText);
        this.add(zOK);
        this.add(zCancel);
	}

	public int getZPosition() {
		return zPosition;
	}

	public void setZPosition(int zPosition) {
		this.zPosition = zPosition;
	}

	public boolean isOKed() {
		return isOKed;
	}

	public void setOKed(boolean isOKed) {
		this.isOKed = isOKed;
	}
	
}
