/*
 * DrawView.java
 */

package draw.GUI;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.ResourceMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import draw.Model.GroupShape;
import draw.Processors.DialogProcessor;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

/**
 * The application's main frame.
 * See {@linktourl https://www.profperoni.info/appframework/org/jdesktop/application/FrameView.html }
 */
public class DrawView extends FrameView {
    
    /**
     * Агрегирания диалогов процесор. Улеснява манипулацията на модела.
     */
    private DialogProcessor dialogProcessor;
    
    /**
     * Достъп до доалоговия процесор.
     * @return Инстанцията на диалоговия процесор
     */
    public DialogProcessor getDialogProcessor() {
        return dialogProcessor;
    }
 
    public DrawView(SingleFrameApplication app) {
        super(app); // Calls constructor of Application class
       
        initComponents(); // initializes companents using the values in DrawView.properties file

        // Създава се инстанция от класа на диалоговия процесор.
        dialogProcessor = new DialogProcessor();
        
        // Създаваме поле за рисуване и го правим главен компонент в изгледа.
        DrawViewPortPaint drawViewPortPaint = new draw.GUI.DrawViewPortPaint(this);
//        drawViewPortPaint.setBackground(java.awt.Color.WHITE);
        setComponent(drawViewPortPaint);
        // Прихващане на събитията на мишката.
        drawViewPortPaint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            	if(GroupSelectButton.isSelected())
            		dialogProcessor.setIncludeGroups(true);
            	else
            		dialogProcessor.setIncludeGroups(false);
            	
            	if (DragToggleButton.isSelected()) {
            		dialogProcessor.setSelection(dialogProcessor.ContainsPoint(evt.getPoint()));
            		Vector<draw.Model.Shape> selection = dialogProcessor.getSelection();
            		if (!selection.isEmpty()) {
            			statusMessageLabel.setText("Последно действие: Селекция на примитив; " + "Name: "+ selection.get(0).getName()+ "; Type: "+ selection.get(0).getClass()+"; zPosition: " + selection.get(0).getZPosition());
            			//dialogProcessor.setIsDragging(true);
            			dialogProcessor.setLastLocation(evt.getPoint());
            			dialogProcessor.repaint();
            		}
            	}else if(MultiSelectButton.isSelected()) {
            		dialogProcessor.setSelection(dialogProcessor.ContainsPointMulti(evt.getPoint()));
            		Vector<draw.Model.Shape> selectionMulti = dialogProcessor.getSelection();
            		if (!selectionMulti.isEmpty()) {
            			statusMessageLabel.setText("Последно действие: Селекция на множество примитиви; " + "Names of selected elements: "+ dialogProcessor.getSelectionNames());
            			//dialogProcessor.setIsDragging(true);
            			dialogProcessor.setLastLocation(evt.getPoint());
            			dialogProcessor.repaint();
            		}
            	}
            	
            	dialogProcessor.repaint(); //may optimize repaint be specifying a rect to repaint!!!

            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                //dialogProcessor.setIsDragging(false);
            }
        });
        drawViewPortPaint.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                //if (dialogProcessor.getIsDragging()) {
                    if (dialogProcessor.getSelection() != null) statusMessageLabel.setText("Последно действие: Влачене");
                    dialogProcessor.TranslateTo(evt.getPoint());
                    dialogProcessor.repaint();
                //}
            }
        });
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
        
        // Икона на главния прозорец
        ImageIcon icon = resourceMap.getImageIcon("DrawIcon");
        getFrame().setIconImage(icon.getImage()); 

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        
        // Listeners monitoring the state of Single and Multi selection buttons
        DragToggleButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JToggleButton source = (JToggleButton)e.getSource();
        		if(source.isSelected())
        			MultiSelectButton.setSelected(false);
        	}
        });
        
        MultiSelectButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JToggleButton source = (JToggleButton)e.getSource();
        		if(source.isSelected())
        			DragToggleButton.setSelected(false);
        	}
        });
    }

    /**
     * Показва диалогова кутия с информация за програмата.
     */
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = DrawApp.getApplication().getMainFrame();
            aboutBox = new DrawAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        DrawApp.getApplication().show(aboutBox);
    }

    /**
     * Бутон, който поставя на произволно място правоъгълник със зададените размери.
     * Променя се лентата със състоянието и се инвалидира контрола, в който визуализираме.
     */
    //TODO
    // AddRandom methods will have to be replaced with parametrized ones
    @Action
    public void drawRectangle() {
        dialogProcessor.AddRandomRectangle();
        statusMessageLabel.setText("Последно действие: Рисуване на правоъгълник");
        dialogProcessor.repaint();
    }

    @Action
    public void drawEllipse() {
    	dialogProcessor.AddRandomEllipse();
    	statusMessageLabel.setText("Последно действие: Рисуване на елипса");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void drawLine() {
    	dialogProcessor.AddRandomLine(); 
    	statusMessageLabel.setText("Последно действие: Рисуване на линия");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void drawTriangle() {
    	dialogProcessor.AddRandomTriangle();
    	statusMessageLabel.setText("Последно действие: Рисуване на триъгълник");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void drawRoundShapeExam() {
    	dialogProcessor.AddRoundShapeExam();
    	statusMessageLabel.setText("Последно действие: Рисуване на RoundShapeExam");
    	dialogProcessor.repaint();
    }
    
    @Action
    public void changeColor() {
    	Vector<draw.Model.Shape> selection = dialogProcessor.getSelection();
    	if(selection.isEmpty()) {
    		JOptionPane.showMessageDialog(this.getComponent(), "Must select an element first to change its color.");
    		return;
    	}
    	colorDialog = new draw.GUI.FillColorDialog();
    	colorDialog.setVisible(true);
    	if(colorDialog.isOKed()) {
    		for(int i=0;i<selection.size();i++) {
    		selection.get(i).setFillColor(colorDialog.getSelectedFillColor());
    		}
    		statusMessageLabel.setText("Последно действие: Смяна цвят на " + dialogProcessor.getSelectionNames());
    	}
		
    	dialogProcessor.clearSelection();
		dialogProcessor.repaint();
    }
    
    @Action
    public void changeBorder() {
    	Vector<draw.Model.Shape>selection = dialogProcessor.getSelection();
    	if(selection == null) {
    		JOptionPane.showMessageDialog(this.getComponent(), "Must select an element first to change its border color.");
    		return;
    	}
    
    	borderDialog = new draw.GUI.BorderDialog();
    	borderDialog.setVisible(true);
    	if(borderDialog.isOKed()) {
    		for(int i=0;i<selection.size();i++){
    		selection.get(i).setBorderColor(borderDialog.getBorderColor());
    		selection.get(i).setBorderStroke(borderDialog.getBorderStroke());
    		}
    		statusMessageLabel.setText("Последно действие: Смяна на граница на " + dialogProcessor.getSelectionNames());
    	}
		dialogProcessor.clearSelection();
		dialogProcessor.repaint();
    }
    
    @Action
    public void renameElement() {
    	Vector<draw.Model.Shape> selection = dialogProcessor.getSelection();
    	
    	if(selection.isEmpty()) {
    		JOptionPane.showMessageDialog(this.getComponent(), "Must select an element first to change its name.");
    		return;
    	}else if(selection.size()>1) {
    		JOptionPane.showMessageDialog(this.getComponent(), "Must select only one element to change its name.");
    		return;
    	}
    	draw.Model.Shape selectedElement = selection.get(0);
    	
    	String oldName = selectedElement.getName();
    	String newName = JOptionPane.showInputDialog(this.getComponent(),"Rename Element. Current name: "+selectedElement.getName(),oldName);
    	if(newName == null)
    		return;
    	else
    		selectedElement.setName(newName);
    }
    
    @Action
    public void resizeElement() {
    	//TODO

    }
    
    @Action
    public void rotateElement() {
    	Vector<draw.Model.Shape> selection =dialogProcessor.getSelection();
    	rotationDialog = new draw.GUI.RotationDialog();
        rotationDialog.setVisible(true);
        if(rotationDialog.isOKed()) {
    		for(int i=0;i<selection.size();i++){
    		selection.get(i).setRotation(rotationDialog.getRotationDeg());
    		}
            statusMessageLabel.setText("Последно действие: Ротация на " + dialogProcessor.getSelectionNames());
        }
        
        dialogProcessor.clearSelection();
        dialogProcessor.repaint();

        
    }
    
    //File menu methods
    //Have to do something about the cancel button
    @Action
    public void saveFile() {
    	Vector<draw.Model.Shape> modelObject = dialogProcessor.getShapeList();
    	
    	Path filePath = null;
    	
    	JFileChooser fc = new JFileChooser();
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Draw files", "draw");
    	fc.setFileFilter(filter);
    	int returnVal = fc.showSaveDialog(this.getComponent());
    	if(returnVal == JFileChooser.APPROVE_OPTION)
    		filePath =Paths.get(fc.getSelectedFile().getAbsolutePath());
    	
        try {
            FileOutputStream fileOut =
            new FileOutputStream(filePath.toString());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(modelObject);
            out.close();
            fileOut.close();
            statusMessageLabel.setText("Model data is saved in "+filePath);
         } catch (IOException ex) {
        	 statusMessageLabel.setText(ex.getMessage());
         }
    }
    
    @SuppressWarnings("unchecked")
	@Action
    public void openFile() {
    	Vector<draw.Model.Shape> modelObject = null;
    	Path filePath = null;
    	
    	JFileChooser fc = new JFileChooser();
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Draw files", "draw");
    	fc.setFileFilter(filter);
    	int returnVal = fc.showOpenDialog(this.getComponent());
    	
    	if(returnVal == JFileChooser.APPROVE_OPTION)
    		filePath = Paths.get(fc.getSelectedFile().getAbsolutePath());
    	
        try {
            FileInputStream fileIn = new FileInputStream(filePath.toString());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            modelObject = (Vector<draw.Model.Shape>)in.readObject();
            in.close();
            fileIn.close();
         } catch (IOException ex) {
        	 statusMessageLabel.setText(ex.getMessage());
            return;
         } catch (ClassNotFoundException c) {
        	 statusMessageLabel.setText(c.getMessage());
            return;
         }
        dialogProcessor.setShapeList(modelObject);
        dialogProcessor.repaint();
        
        for(int i = 0;i<modelObject.size();i++) {
        	draw.Model.Shape shape = modelObject.get(i);
        	draw.Model.MyBasicStroke stroke = shape.getBorderStroke();
        	System.out.println(stroke.getLineWidth()+" "+stroke.getEndCap()+" "+stroke.getLineJoin());
        }
    }
    
    @Action
    public void newFile() {
    	dialogProcessor.getShapeList().clear();
    	dialogProcessor.repaint();
    }
    
    /**
     * Creates a group of elements
     */
    @Action
    public void createGroup() {
    	Vector<draw.Model.Shape> selectionMulti = dialogProcessor.getSelection();
    	draw.Model.GroupShape newGroup = new draw.Model.GroupShape(selectionMulti);
    	
    	//dialogProcessor.getShapeList().removeAll(newGroup.getCollection());

    	dialogProcessor.clearSelection();
    	dialogProcessor.addGroup(newGroup);
    	
        statusMessageLabel.setText("Created "+newGroup.getName());
        dialogProcessor.repaint();
    }
    
    //TODO
    //Update below methods to call proper methods from dialogProcessor
    
    /**
     * Deletes all currently selected groups.
     */
    @Action
    public void disbandGroup() {
    	Vector<draw.Model.Shape> selection = dialogProcessor.getSelection();
    	
    	for(int i = 0; i<selection.size();i++) {
    		if(selection.get(i).getClass()==GroupShape.class) {
    			GroupShape g = (GroupShape)selection.get(i);
    			g.clearContent();
    			dialogProcessor.removeGroup(g);
    		}
    	}
    	dialogProcessor.clearSelection();
    	dialogProcessor.repaint();
    }
    
    //Must update below method with correct calls to remove methods
    /**
     * Deletes all selected elements and groups.
     */
    @Action
    public void deleteElement() {
    	Vector<draw.Model.Shape> selection = dialogProcessor.getSelection();

    	for(int i = 0; i<selection.size();i++) 
    		dialogProcessor.removeElement(selection.get(i));
    	
    	dialogProcessor.clearSelection();
    	dialogProcessor.repaint();
    }
    
    @Action
    public void changeZOrder() {
    	Vector<draw.Model.Shape> selection = dialogProcessor.getSelection();
    	if(selection.isEmpty()) {
    		JOptionPane.showMessageDialog(this.getComponent(), "Must select an element first to change its Z Order.");
    		return;
    	}
    	else if(selection.size()>1)
    	{
    		JOptionPane.showMessageDialog(this.getComponent(), "Must select ONLY one element to change its Z Order.");
    		return;
    	}
    	
    	draw.Model.Shape selectedElement = selection.firstElement();
    	
    	zOrderDialog = new ZOrderDialog();
    	zOrderDialog.setVisible(true);
    	if(zOrderDialog.isOKed()) {
    		dialogProcessor.setZOrder(selectedElement, zOrderDialog.getZPosition());
    		statusMessageLabel.setText("Последно действие: Смяна Z позиция на " + dialogProcessor.getSelectionNames());
    	}
    	dialogProcessor.clearSelection();
    	dialogProcessor.repaint();
    }
    
    /** 
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem saveMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem openMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem newMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        imageMenu = new javax.swing.JMenu();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        toolBar = new javax.swing.JToolBar();
        //Buttons
        DrawRoundShapeExamButton = new javax.swing.JButton();
        DrawRectangleButton = new javax.swing.JButton();
        DrawEllipseButton = new javax.swing.JButton();
        DrawLineButton = new javax.swing.JButton();
        DrawTriangleButton = new javax.swing.JButton();
        ColorButton = new javax.swing.JButton();
        BorderButton = new javax.swing.JButton();
        RenameButton = new javax.swing.JButton();
        ResizeButton = new javax.swing.JButton();
        RotateButton = new javax.swing.JButton();
        DeleteButton =  new javax.swing.JButton();
        GroupCreateButton = new javax.swing.JButton();
        GroupDisbandButton = new javax.swing.JButton();
        ZOrderButton = new javax.swing.JButton();
        GroupSelectButton = new javax.swing.JToggleButton();
        MultiSelectButton = new javax.swing.JToggleButton();
        DragToggleButton = new javax.swing.JToggleButton();
        viewPort = new javax.swing.JPanel();
        		
        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(draw.GUI.DrawApp.class).getContext().getResourceMap(DrawView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(draw.GUI.DrawApp.class).getContext().getActionMap(DrawView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        saveMenuItem.setAction(actionMap.get("saveFile"));
        saveMenuItem.setName("saveMenuItem");
        openMenuItem.setAction(actionMap.get("openFile"));
        openMenuItem.setName("openMenuItem");
        newMenuItem.setAction(actionMap.get("newFile"));
        newMenuItem.setName("newMenuItem");
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);


        menuBar.add(fileMenu);

        editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N
        menuBar.add(editMenu);

        imageMenu.setText(resourceMap.getString("imageMenu.text")); // NOI18N
        imageMenu.setName("imageMenu"); // NOI18N
        menuBar.add(imageMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 421, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        DragToggleButton.setIcon(resourceMap.getIcon("DragToggleButton.icon")); // NOI18N
        DragToggleButton.setText(resourceMap.getString("DragToggleButton.text")); // NOI18N
        DragToggleButton.setFocusable(false);
        DragToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DragToggleButton.setName("DragToggleButton"); // NOI18N
        DragToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DragToggleButton.setToolTipText("Select and drag an element");
        toolBar.add(DragToggleButton);
        
        MultiSelectButton.setIcon(resourceMap.getIcon("MultiSelectButton.icon"));
        MultiSelectButton.setText(resourceMap.getString("MultiSelectButton.text"));
        MultiSelectButton.setFocusable(false);
        MultiSelectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        MultiSelectButton.setName("MultiSelectButton");
        MultiSelectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        MultiSelectButton.setToolTipText("Select multiple elements");
        toolBar.add(MultiSelectButton);
        
        GroupSelectButton.setAction(actionMap.get("selectGroup"));
        GroupSelectButton.setIcon(resourceMap.getIcon("GroupSelectButton.icon"));
        GroupSelectButton.setText(resourceMap.getString("GroupSelectButton.text"));
        GroupSelectButton.setFocusable(false);
        GroupSelectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GroupSelectButton.setName("GroupSelectButton");
        GroupSelectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        GroupSelectButton.setToolTipText("Include groups when selecting elements");
        GroupSelectButton.setSelected(true);
        toolBar.add(GroupSelectButton);
        
        DeleteButton.setAction(actionMap.get("deleteElement"));
        DeleteButton.setIcon(resourceMap.getIcon("DeleteButton.icon"));
        DeleteButton.setText(resourceMap.getString("DeleteButton.text"));
        DeleteButton.setFocusable(false);
        DeleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DeleteButton.setName("DeleteButton");
        DeleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DeleteButton.setToolTipText("Delete selected elements");
        toolBar.add(DeleteButton);
        
        DrawRectangleButton.setAction(actionMap.get("drawRectangle")); // NOI18N
        DrawRectangleButton.setIcon(resourceMap.getIcon("DrawRectangleButton.icon")); // NOI18N
        DrawRectangleButton.setText(resourceMap.getString("DrawRectangleButton.text")); // NOI18N
        DrawRectangleButton.setFocusable(false);
        DrawRectangleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DrawRectangleButton.setName("DrawRectangleButton"); // NOI18N
        DrawRectangleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DrawRectangleButton.setToolTipText("Create a rectangle");
        toolBar.add(DrawRectangleButton);
        
        DrawEllipseButton.setAction(actionMap.get("drawEllipse")); // NOI18N
        DrawEllipseButton.setIcon(resourceMap.getIcon("DrawEllipseButton.icon")); // NOI18N
        DrawEllipseButton.setText(resourceMap.getString("DrawEllipseButton.text")); // NOI18N
        DrawEllipseButton.setFocusable(false);
        DrawEllipseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DrawEllipseButton.setName("DrawEllipseButton"); // NOI18N
        DrawEllipseButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DrawEllipseButton.setToolTipText("Create an ellipse");
        toolBar.add(DrawEllipseButton);
        
        DrawLineButton.setAction(actionMap.get("drawLine"));
        DrawLineButton.setIcon(resourceMap.getIcon("DrawLineButton.icon"));
        DrawLineButton.setText(resourceMap.getString("DrawLineButton.text"));
        DrawLineButton.setFocusable(false);
        DrawLineButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DrawLineButton.setName("DrawLineButton");
        DrawLineButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DrawLineButton.setToolTipText("Create a line");
        toolBar.add(DrawLineButton);
        
        DrawTriangleButton.setAction(actionMap.get("drawTriangle"));
        DrawTriangleButton.setIcon(resourceMap.getIcon("DrawTriangleButton.icon"));
        DrawTriangleButton.setText(resourceMap.getString("DrawTriangleButton.text"));
        DrawTriangleButton.setFocusable(false);
        DrawTriangleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DrawTriangleButton.setName("DrawTriangleButton");
        DrawTriangleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DrawTriangleButton.setToolTipText("Create a triangle");
        toolBar.add(DrawTriangleButton);
        
        ColorButton.setAction(actionMap.get("changeColor"));
        ColorButton.setIcon(resourceMap.getIcon("ColorButton.icon"));
        ColorButton.setText(resourceMap.getString("ColorButton.text"));
        ColorButton.setFocusable(false);
        ColorButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ColorButton.setName("ColorButton");
        ColorButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ColorButton.setToolTipText("Change the fill color of an element");
        toolBar.add(ColorButton);
        
        BorderButton.setAction(actionMap.get("changeBorder"));
        BorderButton.setIcon(resourceMap.getIcon("BorderButton.icon"));
        BorderButton.setText(resourceMap.getString("BorderButton.text"));
        BorderButton.setFocusable(false);
        BorderButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BorderButton.setName("BorderButton");
        BorderButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BorderButton.setToolTipText("Change the border color of an element");
        toolBar.add(BorderButton);
        
        RenameButton.setAction(actionMap.get("renameElement"));
        RenameButton.setIcon(resourceMap.getIcon("RenameButton.icon"));
        RenameButton.setText(resourceMap.getString("RenameButton.text"));
        RenameButton.setFocusable(false);
        RenameButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RenameButton.setName("RenameButton");
        RenameButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        RenameButton.setToolTipText("Rename an element");
        toolBar.add(RenameButton);
        
        ResizeButton.setAction(actionMap.get("resizeElement"));
        ResizeButton.setIcon(resourceMap.getIcon("ResizeButton.icon"));
        ResizeButton.setText(resourceMap.getString("ResizeButton.text"));
        ResizeButton.setFocusable(false);
        ResizeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ResizeButton.setName("ResizeButton");
        ResizeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ResizeButton.setToolTipText("Resize an element");
        toolBar.add(ResizeButton);
        
        RotateButton.setAction(actionMap.get("rotateElement"));
        RotateButton.setIcon(resourceMap.getIcon("RotateButton.icon"));
        RotateButton.setText(resourceMap.getString("RotateButton.text"));
        RotateButton.setFocusable(false);
        RotateButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RotateButton.setName("RotateButton");
        RotateButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        RotateButton.setToolTipText("Rotate an element");
        toolBar.add(RotateButton);
        
        GroupCreateButton.setAction(actionMap.get("createGroup"));
        GroupCreateButton.setIcon(resourceMap.getIcon("GroupCreateButton.icon"));
        GroupCreateButton.setText(resourceMap.getString("GroupCreateButton.text"));
        GroupCreateButton.setFocusable(false);
        GroupCreateButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GroupCreateButton.setName("GroupCreateButton");
        GroupCreateButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        GroupCreateButton.setToolTipText("Create a group of elements");
        toolBar.add(GroupCreateButton);

        
        GroupDisbandButton.setAction(actionMap.get("disbandGroup"));
        GroupDisbandButton.setIcon(resourceMap.getIcon("GroupDisbandButton.icon"));
        GroupDisbandButton.setText(resourceMap.getString("GroupDisbandButton.text"));
        GroupDisbandButton.setFocusable(false);
        GroupDisbandButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GroupDisbandButton.setName("GroupDisbandButton");
        GroupDisbandButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        GroupDisbandButton.setToolTipText("Disband a group of elements");
        toolBar.add(GroupDisbandButton);
        
        DrawRoundShapeExamButton.setAction(actionMap.get("drawRoundShapeExam")); // NOI18N
        DrawRoundShapeExamButton.setIcon(resourceMap.getIcon("DrawRoundShapeExamButton.icon")); // NOI18N
        DrawRoundShapeExamButton.setText(resourceMap.getString("DrawRoundShapeExamButton.text")); // NOI18N
        DrawRoundShapeExamButton.setFocusable(false);
        DrawRoundShapeExamButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DrawRoundShapeExamButton.setName("DrawRoundShapeExamButton"); // NOI18N
        DrawRoundShapeExamButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DrawRoundShapeExamButton.setToolTipText("Create a RoundShapeExam");
        toolBar.add(DrawRoundShapeExamButton);
        
        ZOrderButton.setAction(actionMap.get("changeZOrder"));
        ZOrderButton.setIcon(resourceMap.getIcon("ZOrderButton.icon"));
        ZOrderButton.setText(resourceMap.getString("ZOrderButton.text"));
        ZOrderButton.setFocusable(false);
        ZOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ZOrderButton.setName("ZOrderButton");
        ZOrderButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ZOrderButton.setToolTipText("Change the Z order of an element");
        toolBar.add(ZOrderButton);
        
        viewPort.setName("viewPort"); // NOI18N

        javax.swing.GroupLayout viewPortLayout = new javax.swing.GroupLayout(viewPort);
        viewPort.setLayout(viewPortLayout);
        viewPortLayout.setHorizontalGroup(
            viewPortLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 591, Short.MAX_VALUE)
        );
        viewPortLayout.setVerticalGroup(
            viewPortLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );

        setComponent(viewPort);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolBar);
        
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton DragToggleButton;
    private javax.swing.JButton DrawRectangleButton;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu imageMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JPanel viewPort;
    
    // added buttons and tools
    private javax.swing.JButton DrawRoundShapeExamButton;
    private javax.swing.JButton DrawEllipseButton;
    private javax.swing.JButton DrawLineButton;
    private javax.swing.JButton DrawTriangleButton;
    private javax.swing.JButton ColorButton;
    private javax.swing.JButton BorderButton;
    private javax.swing.JButton RenameButton;
    private javax.swing.JButton ResizeButton;
    private javax.swing.JButton RotateButton;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JButton GroupCreateButton;
    private javax.swing.JButton GroupDisbandButton;
    private javax.swing.JButton ZOrderButton;
    private javax.swing.JToggleButton GroupSelectButton;
    private javax.swing.JToggleButton MultiSelectButton;
    
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private draw.GUI.RotationDialog rotationDialog;
    private draw.GUI.FillColorDialog colorDialog;
    private draw.GUI.BorderDialog borderDialog;
    private draw.GUI.ZOrderDialog zOrderDialog;
}
