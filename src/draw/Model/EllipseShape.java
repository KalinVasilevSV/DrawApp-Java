package draw.Model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class EllipseShape extends draw.Model.Shape{
	
	private static int count = 0;
	
	Ellipse2D.Double baseEllipse= new Ellipse2D.Double();
	
	public EllipseShape (Rectangle rect){
		super(rect);
		baseEllipse.setFrame(rect);
		setDrawShape(new Path2D.Double(baseEllipse));
		this.setName("EllipseShape"+(++count));
	}
	
	public Ellipse2D.Double getShape() {
		return baseEllipse;
	}
	
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
        baseEllipse.setFrame(r);
        setSelectionBox(r);
        AffineTransform oTransform = g2.getTransform();
        if(group != null)
        	oTransform.rotate(group.getRotation(),group.getRectangle().getCenterX(),group.getRectangle().getCenterY());
        oTransform.rotate(getRotation(), r.getCenterX(), r.getCenterY());
        setDrawShape(oTransform.createTransformedShape(baseEllipse));

        g2.setStroke(getBorderStroke());
        g2.setColor(getFillColor());
        g2.fill(drawShape);
        g2.setColor(getBorderColor());
        g2.setColor(getBorderColor());
        g2.draw(drawShape);
    }
    
    @Override
    public void DrawSelectionBox(Graphics g) {
        if(isSelected()) {
        Graphics2D g2 = (Graphics2D)g;
        Rectangle r = getRectangle();
        AffineTransform oTransform = g2.getTransform();
      if(group != null)
      	oTransform.rotate(group.getRotation(),group.getRectangle().getCenterX(),group.getRectangle().getCenterY());
      oTransform.rotate(getRotation(), r.getCenterX(), r.getCenterY());
      	g2.setStroke(getSelectionstroke());
      	g2.draw(oTransform.createTransformedShape(getSelectionBox()));
      	}
    }
}
