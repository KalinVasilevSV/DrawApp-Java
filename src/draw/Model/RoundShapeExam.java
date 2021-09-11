package draw.Model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

public class RoundShapeExam extends Shape{

	private static int count = 0;
	
	Ellipse2D.Double baseEllipse= new Ellipse2D.Double();
	
	public RoundShapeExam (Rectangle rect){
		super(new Rectangle(rect.x,rect.y,rect.height,rect.height));
		baseEllipse.setFrame(rect);
		setDrawShape(new Path2D.Double(baseEllipse));
		this.setName("RoundShapeExam"+(++count));
	}
	
	public Ellipse2D.Double getShape() {
		return baseEllipse;
	}
	
	@Override
	public boolean Contains(Point point) {
		return baseEllipse.contains(point);
	}
	
    /**
     * Частта, визуализираща конкретния примитив.
     */
    @Override
    public void DrawSelf(Graphics grfx) {
        super.DrawSelf(grfx);
        Graphics2D g2 = (Graphics2D)grfx;
        Rectangle r = getRectangle();
        baseEllipse.setFrame(r);
        setSelectionBox(r);
        AffineTransform oTransform = g2.getTransform();
        if(group != null)
        	oTransform.rotate(group.getRotation(),group.getRectangle().getCenterX(),group.getRectangle().getCenterY());
        oTransform.rotate(getRotation(),r.getCenterX(),r.getCenterY());
        setDrawShape(oTransform.createTransformedShape(baseEllipse));
        g2.setStroke(getBorderStroke());
        g2.setColor(getFillColor());
        g2.fill(drawShape);
        g2.setColor(getBorderColor());
        g2.draw(drawShape);
        if(isSelected()) {
        	g2.setStroke(getSelectionstroke());
        	g2.draw(oTransform.createTransformedShape(getSelectionBox()));
        	}
        
        g2.setClip(drawShape);
        java.awt.Shape vert = oTransform.createTransformedShape(new Line2D.Double(r.getCenterX(), r.getMinY(), r.getCenterX(), r.getMaxY()));
        g2.draw(vert);
        java.awt.Shape hor = oTransform.createTransformedShape(new Line2D.Double(r.getMinX(), r.getCenterY(), r.getMaxX(), r.getCenterY()));
        g2.draw(hor);
        java.awt.Shape hor2 = oTransform.createTransformedShape(new Line2D.Double(r.getMinX(), r.getCenterY()-r.height/4, r.getMaxX(), r.getCenterY()-r.height/4));
        g2.draw(hor2);
        java.awt.Shape hor3 = oTransform.createTransformedShape(new Line2D.Double(r.getMinX(), r.getCenterY()+r.height/4, r.getMaxX(), r.getCenterY()+r.height/4));
        g2.draw(hor3);

    }
}
