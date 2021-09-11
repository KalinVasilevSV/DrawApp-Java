/**
 * DialogProcessor.java
 */

package draw.Processors;

import java.awt.*;
import java.util.Vector;

import draw.Model.GroupShape;

/**
 * Класът, който ще бъде използван при управляване на диалога.
 */
public class DialogProcessor extends DisplayProcessor {
    /**
     * Избран елемент.
     */
//    private draw.Model.Shape selection;
    private Vector<draw.Model.Shape> selection = new Vector<draw.Model.Shape>();
//    private draw.Model.Shape topSelection;
    
    // Не е необходим в реализацията на Java
    ///**
    // * Дали в момента диалога е в състояние на "влачене" на избрания елемент.
    // */
    //private boolean isDragging;
    
    /**
     * Последна позиция на мишката при "влачене".
     * Използва се за определяне на вектора на транслация.
     */
    private Point lastLocation;

    private boolean includeGroups = false; // similar switch can be used for singular and multiple selection logic

    public DialogProcessor() {
    }

    /**
     * Добавя примитив - правоъгълник на произволно място върху клиентската област.
     */
    public void AddRandomRectangle() {
        int x = 100 + (int)Math.round(Math.random()*900);
        int y = 100 + (int)Math.round(Math.random()*500);
        draw.Model.RectangleShape rect = new draw.Model.RectangleShape(new Rectangle(x,y,100,200));
        rect.setFillColor(java.awt.Color.WHITE);
        rect.setBorderColor(java.awt.Color.BLACK);
        addElement(rect);
    }
    
    public void AddRandomEllipse() {
        int x = 100 + (int)Math.round(Math.random()*900);
        int y = 100 + (int)Math.round(Math.random()*500);
        draw.Model.EllipseShape ellipse = new draw.Model.EllipseShape(new Rectangle(x,y,100,200));
        ellipse.setFillColor(java.awt.Color.GRAY);
        ellipse.setBorderColor(java.awt.Color.BLACK);
        addElement(ellipse);
    }
    
    public void AddRandomLine() {
        int x = 100 + (int)Math.round(Math.random()*900);
        int y = 100 + (int)Math.round(Math.random()*500);
        int v = 100 + (int)Math.round(Math.random()*60);
        int w = 100 + (int)Math.round(Math.random()*70);
        draw.Model.LineShape line = new draw.Model.LineShape(new Rectangle(x,y,v,w));
        line.setFillColor(java.awt.Color.BLACK);
        line.setBorderColor(java.awt.Color.BLACK);
        addElement(line);
    }
    
    public void AddRandomTriangle() {
        int x = 100 + (int)Math.round(Math.random()*900);
        int y = 100 + (int)Math.round(Math.random()*500);
        draw.Model.TriangleShape tri = new draw.Model.TriangleShape(new Rectangle(x,y,100,200));
        tri.setFillColor(java.awt.Color.WHITE);
        tri.setBorderColor(java.awt.Color.BLACK);
        addElement(tri);
    }
    
    public void AddRoundShapeExam() {
        int x = 100 + (int)Math.round(Math.random()*900);
        int y = 100 + (int)Math.round(Math.random()*500);
        draw.Model.RoundShapeExam round = new draw.Model.RoundShapeExam(new Rectangle(x,y,100,200));
        round.setFillColor(java.awt.Color.WHITE);
        round.setBorderColor(java.awt.Color.BLACK);
        addElement(round);
    }

    /**
     * Проверява дали дадена точка е в елемента.
     * Обхожда в ред обратен на визуализацията с цел намиране на
     * "най-горния" елемент т.е. този който виждаме под мишката.
     * @param point - Указана точка.
     * @return Елемента на изображението, на който принадлежи дадената точка.
     */
    public Vector<draw.Model.Shape> ContainsPoint(Point point) {
        for (int i = shapeList.size() - 1; i >= 0; i--) {
        	if(!isIncludeGroups() && shapeList.get(i).getClass()==GroupShape.class)
        		continue;
            if (shapeList.get(i).Contains(point)) {
            	if(selection.contains(shapeList.get(i)))
            		return selection;
                if(selection.size()>0) {        	
                	for(int j = 0;j<selection.size();j++)
                		selection.get(j).setSelected(false); 
                	selection.removeAllElements();
                	}
                selection.add(shapeList.get(i));
                selection.get(0).setSelected(true);
                return selection;
            }
        }
        if(selection.size()>0) {
        	for(int j = 0;j<selection.size();j++)
        		selection.get(j).setSelected(false); 
        	}
    	selection.removeAllElements();
        return selection;
    }

    //Contains method for multiple selection
    public Vector<draw.Model.Shape> ContainsPointMulti(Point point) {
        for (int i = shapeList.size() - 1; i >= 0; i--) {
        	if(!isIncludeGroups() && shapeList.get(i).getClass()==GroupShape.class)
        		continue;
            if (shapeList.get(i).Contains(point)) {
            	if(selection.contains(shapeList.get(i)))
            		return selection;
                shapeList.get(i).setSelected(true);
                selection.add(shapeList.get(i));
                return selection;
            }
        }
        if(selection.size()>0) {
        	for(int i = 0;i<selection.size();i++)
        		selection.get(i).setSelected(false);
        	selection.removeAllElements();
        	}
        return selection;
    }
    /**
     * Транслация на избраният елемент на вектор определен от <paramref name="p>p< paramref>
     * @param p - Вектор на транслация.
     */
    public void TranslateTo(Point p) {
        if ( !selection.isEmpty()) {
        	for(int i = 0; i<selection.size();i++)
        		selection.get(i).setLocation(new Point(selection.get(i).getLocation().x + p.x - lastLocation.x, selection.get(i).getLocation().y + p.y - lastLocation.y));
            lastLocation = p;
        }
    }

    public void addElement(draw.Model.Shape s) {
    	//TODO
    	if(s.getClass()==GroupShape.class) 
    		shapeList.add(s);
    	else
    		shapeList.insertElementAt(s, 0); 
    	updateZOrder();
    }
    
    public void removeElement(draw.Model.Shape s) {
    	if(s.getClass()==GroupShape.class) 
    		removeAllElements(((GroupShape)s).getCollection());
    	shapeList.remove(s);
    	updateZOrder();
    }
    
    public void removeAllElements(java.util.Collection<draw.Model.Shape> c) {
    	shapeList.removeAll(c);
    	updateZOrder();
    }
    
    public void addGroup(GroupShape g) {
    	shapeList.add(g);
    }
    
    public void removeGroup(GroupShape g) {
    	shapeList.remove(g);
    }
    
//    public draw.Model.Shape getSelection() {
//    	if(selection.isEmpty()) return null;
//        return selection.get(0);
//    }
//
//    public void setSelection(draw.Model.Shape value) {
//    	if(value==null) return;
//    	selection.clear();
//        selection.add(value);
//    }
    
    public boolean isIncludeGroups() {
		return includeGroups;
	}

	public void setIncludeGroups(boolean includeGroups) {
		this.includeGroups = includeGroups;
	}

	public Vector<draw.Model.Shape> getSelection() {
        return selection;
    }

    public void setSelection(Vector<draw.Model.Shape> value) {
        selection = value;
    }
    
    //public boolean getIsDragging() {
    //    return isDragging;
    //}

    //public void setIsDragging(boolean value) {
    //    isDragging = value;
    //}

    public Point getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Point value) {
        lastLocation = value;
    }
    

	public void clearSelection() {
    	for(int i = 0;i<selection.size();i++)
    		selection.get(i).setSelected(false);
    	selection.removeAllElements();
    }
    
    public Vector<String> getSelectionNames(){
    	Vector<String> selectionNames = new Vector<String>();
    	for(int i = 0;i<selection.size();i++)
    		selectionNames.add(selection.get(i).getName());
    	return selectionNames;
    }
    
    //TODO
    //Update to cascade zOrder changes for groups
    // Currently tries to getZPosition() for groups which returns -1, an invalid index
    public void setZOrder(draw.Model.Shape shape, int zPosition) {
    	if(shape.getClass()==GroupShape.class) {
    		shapeList.remove(shape);
    		shapeList.add(shape);
    		Vector<draw.Model.Shape> gElements = ((GroupShape)shape).getCollection();
    		for(int i=0;i<gElements.size();i++)
    			this.setZOrder(gElements.get(i), zPosition+i);
        	updateZOrder();
        	return;
    	}
    		
    	int oZPosition = shapeList.indexOf(shape);
    	
    	if(zPosition<oZPosition) {
    		shapeList.remove(shape);
    		shapeList.insertElementAt(shape, zPosition);
    	}
    	else if(zPosition > oZPosition) {
    		shapeList.insertElementAt(shape, zPosition+1);
    		shapeList.remove(oZPosition);
    	}
    	updateZOrder();
    }
    
    public void updateZOrder() {
    	for(int i = 0;i<shapeList.size();i++) {
    		shapeList.get(i).setZPosition(i);
    	}
    }
}
