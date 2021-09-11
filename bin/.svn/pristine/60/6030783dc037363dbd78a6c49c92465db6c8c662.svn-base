package draw.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import draw.Model.MyBasicStroke;

public class BorderDialog extends JDialog{
	
	private boolean isOKed = false;
	
	private Color borderColor = null;
	private draw.Model.MyBasicStroke borderStroke = null;

	JColorChooser cc = new JColorChooser();
	JLabel borderLabel = new JLabel("Border width(px): ");
	JTextField borderText = new JTextField();
	JButton borderOK = new JButton("OK");
	JButton borderCancel = new JButton("Cancel");
	
	JRadioButton capButt = new JRadioButton("Cap Butt");
	JRadioButton capRound = new JRadioButton("Cap Round");
	JRadioButton capSquare = new JRadioButton("Cap Square");
	JRadioButton joinBevel = new JRadioButton("Join Bevel");
	JRadioButton joinMiter = new JRadioButton("Join Miter");
	JRadioButton joinRound = new JRadioButton("Join Round");
	
	ButtonGroup capGroup = new ButtonGroup();
	ButtonGroup joinGroup = new ButtonGroup();
	JPanel buttonGroups = new JPanel();
	JPanel okCancelButtons = new JPanel();
	
	public BorderDialog() {
		BorderDialog self = this;
		this.setLayout(new FlowLayout());
        this.setSize(700, 500);
        this.setResizable(false);
        this.setTitle("Border dialog");
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

    	cc.setPreviewPanel(new JPanel());
    	capButt.setSelected(true);
    	joinBevel.setSelected(true);

    	capButt.setName("CAP_BUTT");
    	capRound.setName("CAP_ROUND");
    	capSquare.setName("CAP_SQUARE");
    	joinBevel.setName("JOIN_BEVEL");
    	joinMiter.setName("JOIN_MITER");
    	joinRound.setName("JOIN_ROUND");
    	capGroup.add(capButt);
    	capGroup.add(capRound);
    	capGroup.add(capSquare);
    	joinGroup.add(joinBevel);
    	joinGroup.add(joinMiter);
    	joinGroup.add(joinRound);
    	
    	buttonGroups.setLayout(new GridLayout(3,2));
    	buttonGroups.add(capButt);
    	buttonGroups.add(joinBevel);
    	buttonGroups.add(capRound);
    	buttonGroups.add(joinMiter);
    	buttonGroups.add(capSquare);
    	buttonGroups.add(joinRound);
    	
    	okCancelButtons.add(borderOK);
    	okCancelButtons.add(borderCancel);
    	
    	borderText.setColumns(8);
    	
    	borderOK.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mouseClicked(java.awt.event.MouseEvent evt) {
    			Color colorSelection = cc.getSelectionModel().getSelectedColor();

    			int capStyle = -1;
    			int joinStyle = -1;

    			if(capButt.isSelected())
    				capStyle=java.awt.BasicStroke.CAP_BUTT;
    			else if(capRound.isSelected())
    				capStyle=java.awt.BasicStroke.CAP_ROUND;
    			else
    				capStyle=java.awt.BasicStroke.CAP_SQUARE;
    			
    			if(joinBevel.isSelected())
    				joinStyle=java.awt.BasicStroke.JOIN_BEVEL;
    			else if(joinMiter.isSelected())
    				joinStyle=java.awt.BasicStroke.JOIN_MITER;
    			else
    				joinStyle=java.awt.BasicStroke.JOIN_ROUND;
    			
    			MyBasicStroke strokeSelection = new MyBasicStroke(Float.parseFloat(borderText.getText()),capStyle,joinStyle);
    			
    			self.setBorderColor(colorSelection);
    			self.setBorderStroke(strokeSelection);
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
    	this.add(borderLabel);
    	this.add(borderText);
    	this.add(buttonGroups);
    	this.add(okCancelButtons);
	}

	public MyBasicStroke getBorderStroke() {
		return borderStroke;
	}

	public void setBorderStroke(MyBasicStroke borderStroke) {
		this.borderStroke = borderStroke;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	public boolean isOKed() {
		return isOKed;
	}

	public void setOKed(boolean isOKed) {
		this.isOKed = isOKed;
	}
}
