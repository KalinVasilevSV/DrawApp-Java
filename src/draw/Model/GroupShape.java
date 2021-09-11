package draw.Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Vector;

public class GroupShape extends Shape{

    private Vector<draw.Model.Shape> groupShape= new Vector<draw.Model.Shape>();
    
    private static int count = 0;
    
    private Rectangle rectangle;
    
    private Color fillColor;
    private Color borderColor;
    private String name;
    private boolean isSelected = false;
    private double rotation = 0;
    private draw.Model.MyBasicStroke borderStroke= new MyBasicStroke();
    
    public GroupShape(Shape selection) {
    	super();
    	this.groupShape = new Vector<draw.Model.Shape>();
    	this.addElement(selection);
    	this.setName("GroupShape" + (++count));
    	this.setZPosition(Integer.MAX_VALUE);
    	groupShape.sort(null);
    }
    
    public GroupShape(Vector<draw.Model.Shape> selectionMulti) {
    	super();
    	initSelectionBox();
    	
    	for(int i = 0;i<selectionMulti.size();i++) {
    		selectionMulti.get(i).setGroup(this);
    		this.groupShape.add(selectionMulti.get(i));
    		}
    	this.setName("GroupShape" + (++count));
    	this.setZPosition(Integer.MAX_VALUE);
    	groupShape.sort(null);
    }

    /**
     * Проверка дали точка point принадлежи на елемента.
     * @param point - Точка.
     * @return Връща true, ако точката принадлежи на елемента и false, ако не пренадлежи.
     */
    @Override
    public boolean Contains(Point point) {
        boolean cont = false;
        for(int i = 0;i<groupShape.size();i++) {
        	cont = groupShape.get(i).Contains(point);
        	if(cont) break;
        }
        return cont;
    }

    //TODO
    //May require rework for groups containing other groups
    /**
     * Визуализира елемента.
     * @param grfx - Къде да бъде визуализиран елемента.
     */
    @Override
    public void DrawSelf(Graphics grfx) { 
    }
    
    @Override
    public void DrawSelectionBox(Graphics g) {
    	Rectangle r = getRectangle();
    	Graphics2D g2 = (Graphics2D)g;
    	AffineTransform oTransform = g2.getTransform();
    	setSelectionBox(r);
    	if(isSelected) {
    		oTransform.rotate(getRotation(), r.getCenterX(), r.getCenterY());
        	g2.setStroke(getSelectionstroke());
    		g2.draw(oTransform.createTransformedShape(getSelectionBox()));
    	}
    }
    
    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle value) {
        rectangle = value;
    }
    
    @Override
    public void initSelectionBox() {
    	int minX = Integer.MAX_VALUE;
    	int maxX = Integer.MIN_VALUE;
    	int minY = Integer.MAX_VALUE;
    	int maxY = Integer.MIN_VALUE;
    	for(int i = 0;i<groupShape.size();i++) {
    		draw.Model.Shape shape = groupShape.get(i);
    		minX = (int) Math.min(minX,shape.getLocation().getX());
    		maxX = (int) Math.max(maxX,shape.getLocation().getX()+shape.getWidth());
    		minY = (int) Math.min(minY,shape.getLocation().getY());
    		maxY = (int) Math.max(maxY,shape.getLocation().getY()+shape.getHeight());
    	}
    	this.setRectangle(new Rectangle(minX,minY,maxX-minX,maxY-minY));
    	this.setSelectionBox(rectangle);
    }
    
    @Override
    public void reinitSelectionBox() { 
    	initSelectionBox();
    }

    public int getWidth() {
        return this.getRectangle().width;
    }

    public void setWidth(int value) {
        rectangle.width = value;
    }

    public int getHeight() {
        return this.getRectangle().height;
    }

    public void setHeight(int value) {
        rectangle.height = value;
    }

    public Point getLocation() {
        return this.getRectangle().getLocation();
    }

    public void setLocation(Point value) {
    	double diffX = rectangle.getX()-value.getX();
    	double diffY = rectangle.getY()-value.getY();
        rectangle.setLocation(value);
    	for(int i = 0;i<groupShape.size();i++) {
    		Shape subShape = groupShape.get(i);
    		Point subLocation = new Point((int)(subShape.getRectangle().getX()-diffX),(int)(subShape.getRectangle().getY()-diffY));
    		subShape.setLocation(subLocation);
    	}
    }

    public java.awt.Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(java.awt.Color value) {
    	this.fillColor = value;
    	for(int i = 0;i<groupShape.size();i++) {
    		groupShape.get(i).setFillColor(value);;
    	}
    }

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		for(int i = 0;i<groupShape.size();i++) {
    		groupShape.get(i).setBorderColor(borderColor);;
    	}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		for(int i = 0;i<groupShape.size();i++) {
    		groupShape.get(i).setSelected(isSelected);;
    	}
		reinitSelectionBox();
	}

	public double getRotation() {
		return Math.toDegrees(rotation);
	}

	public void setRotation(double rotation) {
		this.rotation = Math.toRadians(rotation);
	}

	public MyBasicStroke getBorderStroke() {
		return borderStroke;
	}

	public void setBorderStroke(MyBasicStroke borderStroke) {
		this.borderStroke = borderStroke;
		for(int i = 0;i<groupShape.size();i++) {
    		groupShape.get(i).setBorderStroke(borderStroke);;
    	}
	}
	
	public void addElement(Shape element) {
		this.groupShape.add(element);
	}
	
	
	// ZOrder methods overridden since group shapes should have no Z Order
	// but should be updated to move all contained elements 
	@Override
	public int getZPosition() {
		return -1; //
	}
	
	@Override
	public void setZPosition(int zPosition) {
		super.setZPosition(Integer.MAX_VALUE);
	}
	
	public Vector<draw.Model.Shape> getCollection(){
		return groupShape;
	}
	
	public void clearContent() {
		for(int i = 0;i<groupShape.size();i++) {
    		groupShape.get(i).clearGroup();
    	}
	}
	//TODO
	// May add removeElement()
}
