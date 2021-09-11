/**
 * Shape.java
 */

package draw.Model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.Vector;

import draw.GUI.DrawApp;

/**
 *   Базовия клас на примитивите, който съдържа общите характеристики на примитивите.
 */
public abstract class Shape implements Serializable, Comparable<draw.Model.Shape>{
    /**
     *   Обхващащ правоъгълник на елемента.
     */
    private Rectangle rectangle;
	
    java.awt.Shape drawShape = new Path2D.Double();
	
    private draw.Model.MyBasicStroke borderStroke= new MyBasicStroke();
    
    draw.Model.GroupShape group = null;
    
    private java.awt.Shape selectionBox;
    private final static BasicStroke selectionStroke = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, new float[] {10.0f}, 0.0f);
    
    /**
     *   Цвят на елемента.
     */
    private String name;
    
    public static Color selectionColor = java.awt.Color.RED;
    private Color fillColor;
    private Color borderColor;

    AffineTransform transform = new AffineTransform();
    private double rotation = 0.0;

    private boolean isSelected = false;
    
    private int zPosition;
    
    public Shape() {
    }

    public Shape(Rectangle rect) {
        rectangle = rect;
        setSelectionBox(rect);
    }

    public Shape(draw.Model.Shape shape) {
        this.setHeight(shape.getHeight());
        this.setLocation(shape.getLocation());
        this.rectangle = shape.rectangle;
        this.setWidth(shape.getWidth());
        this.setFillColor(shape.getFillColor());
        this.setBorderColor(shape.getBorderColor());
    }

    /**
     * Проверка дали точка point принадлежи на елемента.
     * @param point - Точка.
     * @return Връща true, ако точката принадлежи на елемента и false, ако не пренадлежи.
     */
    public boolean Contains(Point point) {
        return getRectangle().contains(point);
    }

    /**
     * Визуализира елемента.
     * @param grfx - Къде да бъде визуализиран елемента.
     */
    public void DrawSelf(Graphics grfx) {
        //  shape.Rectangle.Inflate(shape.BorderWidth, shape.BorderWidth)
    }
    
    public void DrawSelectionBox(Graphics g) {
    }
    
    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle value) {
        rectangle = value;
    }

    public java.awt.Shape getSelectionBox() {
		return selectionBox;
	}

    /**
     * Sets the selection box for this shape to a slightly expanded rectangle than the input.
     * @param rect - the input rectangle
     */
	public void setSelectionBox(Rectangle rect) {
		this.selectionBox = new Rectangle((int)rect.getX()-3,(int)rect.getY()-3,(int)rect.getWidth()+6,(int)rect.getHeight()+6);
	}
	
    public void initSelectionBox() {
    }
    
	public void reinitSelectionBox() {
		setSelectionBox(getRectangle());
	}

	public static BasicStroke getSelectionstroke() {
		return selectionStroke;
	}

	public java.awt.Shape getDrawShape() {
		return drawShape;
	}

	public void setDrawShape(java.awt.Shape drawShape) {
		this.drawShape = drawShape;
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
        rectangle.setLocation(value);
    }

    public java.awt.Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(java.awt.Color value) {
        fillColor = value;
    }

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
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
	}

	public int getZPosition() {
		return zPosition;
	}

	public void setZPosition(int zPosition) {
		this.zPosition = zPosition;
	}
	
	@Override
	public int compareTo(draw.Model.Shape s) {
		int thisZ = this.getZPosition();
		int sZ = s.getZPosition();
		if(thisZ>sZ)
			return 1;
		else if(thisZ<sZ)
			return -1;
		return 0;
	}

    /**
     * Custom method.
     * Returns shape's rotation in degrees
     * @return Returns shape's rotation in degrees
     */
	public double getRotation() {
		return Math.toDegrees(rotation);
	}

	public void setRotation(double rotation) {
		this.rotation = Math.toRadians(rotation);
		transform.setToIdentity();
		Rectangle r = getRectangle();
		transform.rotate(rotation, r.getCenterX(), r.getCenterY());
	}
	
	public draw.Model.GroupShape getGroup() {
		return group;
	}

	public void setGroup(draw.Model.GroupShape group) {
		this.group = group;
	}
	
	public void clearGroup() {
		this.group = null;
	}
	
	public AffineTransform getTransform() {
		return transform;
	}

	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}
	
	public void clearTransform(){
		transform.setToIdentity();
	}
	
	public void appendTransform(AffineTransform transform) {
		this.transform.preConcatenate(transform);
	}

	public MyBasicStroke getBorderStroke() {
		return borderStroke;
	}

	public void setBorderStroke(MyBasicStroke borderStroke) {
		this.borderStroke = borderStroke;
	}
}
