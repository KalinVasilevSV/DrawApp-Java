/**
 * RectangleShape.java
 */

package draw.Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * Класът правоъгълник е основен примитив, който е наследник на базовия Shape.
 */
public class RectangleShape extends draw.Model.Shape {
    
	private static int count = 0;
	
	Rectangle baseRectangle = new Rectangle();
	
    public RectangleShape(Rectangle rect) {
        super(rect);
        baseRectangle.setFrame(rect);
        setSelectionBox(rect);
        setDrawShape(new Path2D.Double(baseRectangle));
        this.setName("RectangleShape"+(++count));
    }

    public RectangleShape(draw.Model.RectangleShape rectangle) {
    }

    /**
     * Проверка за принадлежност на точка point към правоъгълника.
     * В случая на правоъгълник този метод може да не бъде пренаписван, защото
     * Реализацията съвпада с тази на абстрактния клас Shape, който проверява
     * дали точката е в обхващащия правоъгълник на елемента (а той съвпада с
     * елемента в този случай).
     */
    @Override
    public boolean Contains(Point point) {
        return drawShape.contains(point);
    }

    /**
     * Частта, визуализираща конкретния примитив.
     */
    @Override
    public void DrawSelf(Graphics grfx) {
        super.DrawSelf(grfx);
        Graphics2D g2 = (Graphics2D)grfx;
        Rectangle r = getRectangle();
        baseRectangle.setFrame(r);
        setSelectionBox(r);
        AffineTransform oTransform = g2.getTransform();
        if(group != null)
        	oTransform.rotate(group.getRotation(),group.getRectangle().getCenterX(),group.getRectangle().getCenterY());
        oTransform.rotate(getRotation(),r.getCenterX(),r.getCenterY());
        setDrawShape(oTransform.createTransformedShape(baseRectangle));
        g2.setStroke(getBorderStroke());
        g2.setColor(getFillColor());
        g2.fill(drawShape);
        g2.setColor(getBorderColor());
        g2.draw(drawShape);
        if(isSelected()) {
        	g2.setStroke(getSelectionstroke());
        	g2.draw(oTransform.createTransformedShape(getSelectionBox()));
        	}
    }
}
