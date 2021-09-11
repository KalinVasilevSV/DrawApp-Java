package draw.GUI;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class FillColorDialog extends JDialog{
	
	private boolean isOKed = false;
	
	private Color selectedFillColor = null;

	JColorChooser cc = new JColorChooser();
	JButton borderOK = new JButton("OK");
	JButton borderCancel = new JButton("Cancel");
	JPanel okCancelButtons = new JPanel();
	
	public FillColorDialog() {
		FillColorDialog self = this;
        this.setLayout(new FlowLayout());
        this.setSize(700, 500);
        this.setResizable(false);
        this.setTitle("Color dialog");
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        
    	cc.setPreviewPanel(new JPanel());
    	okCancelButtons.add(borderOK);
    	okCancelButtons.add(borderCancel);
    	
    	borderOK.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mouseClicked(java.awt.event.MouseEvent evt) {
    			self.setSelectedFillColor(cc.getSelectionModel().getSelectedColor());
    			self.setOKed(true);
    			self.setVisible(false);
    		}
    	});
    	borderCancel.addMouseListener(new java.awt.event.MouseAdapter() {
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
    	this.add(cc);
    	this.add(okCancelButtons);
	}

	public Color getSelectedFillColor() {
		return cc.getSelectionModel().getSelectedColor();
	}

	public void setSelectedFillColor(Color selectedFillColor) {
		this.selectedFillColor = selectedFillColor;
	}

	public boolean isOKed() {
		return isOKed;
	}

	public void setOKed(boolean isOKed) {
		this.isOKed = isOKed;
	}
}
