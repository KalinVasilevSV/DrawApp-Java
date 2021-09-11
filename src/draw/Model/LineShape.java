package draw.Model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

public class LineShape extends draw.Model.Shape{
	
	private static int count = 0;
	
	Line2D.Double baseLine = new Line2D.Double();
	java.awt.Shape selectionShape = new Polygon();
	
	public LineShape(Rectangle rect) {
		super(rect);
		Point A = getLocation();
		Point B = new Point((int)rect.getMaxX(),(int)rect.getMaxY());
		baseLine.setLine(A,B);
		setSelectionBox(rect);
		setDrawShape(new Path2D.Double(baseLine));
		this.initSelectionShape(rect);
		this.setName("LineShape"+(++count));
	}
	
	public LineShape(draw.Model.LineShape line) {
		super(line.getRectangle());
	}
	
	public void setSelectionBox(java.awt.Shape selectionShape) {
		this.selectionShape = selectionShape;
	}
	
	//TODO
	//Improve margins on the selection shape
	public void initSelectionShape(Rectangle rect) {
		int x1 = rect.x-30;
		int x2 = rect.x+30;
		int x3 = (int) (rect.getMaxX()+30);
		int x4 = (int) (rect.getMaxX()-30);
		int y1 = rect.y;
		int y2 = y1;
		int y3 = (int) rect.getMaxY();
		int y4 = y3;
		selectionShape = new Polygon(new int[] {x1,x2,x3,x4},new int[] {y1,y2,y3,y4}, 4);
	}
	
	public void reinitSelectionShape(Rectangle rect) {
		initSelectionShape(rect);
	}
	
	@Override
	public boolean Contains(Point point) {
		return selectionShape.contains(point);
	}
	
	public void DrawSelf(Graphics grfx) {
		super.DrawSelf(grfx);
		Graphics2D g2 = (Graphics2D)grfx;
        Rectangle r = getRectangle();
		baseLine.setLine(new LineShape(r).baseLine);
		setSelectionBox(r);
        reinitSelectionShape(r);
        AffineTransform oTransform = g2.getTransform();
        if(group != null) {
        	oTransform.rotate(group.getRotation(),group.getRectangle().getCenterX(),group.getRectangle().getCenterY());}
        oTransform.rotate(getRotation(),r.getCenterX(),r.getCenterY());
        setDrawShape(oTransform.createTransformedShape(baseLine));
		this.setSelectionBox(oTransform.createTransformedShape(selectionShape));
        g2.setStroke(getBorderStroke());
		g2.setColor(getBorderColor()); //unlike other classes LineShape uses borderColor
		g2.draw(drawShape); // maybe introduce randomization of these, line requires serious rework
        if(isSelected()) {
        	g2.setStroke(getSelectionstroke());
        	g2.draw(selectionShape);
        	};
	}
}
