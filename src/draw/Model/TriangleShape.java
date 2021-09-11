package draw.Model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class TriangleShape extends draw.Model.Shape{
	
	private static int count =0;
	Polygon triangle = new Polygon();
	
	public TriangleShape(Rectangle rect) {
		super(rect);
		this.setTriangle(rect);
		this.setName("TriangleShape"+(++count));
	}
	
	public Polygon getTriangle() {
		return triangle;
	}
	
	public void setTriangle(Rectangle rect) {
		triangle = new Polygon(new int[] {(int) rect.getMinX(),(int) rect.getMaxX(),rect.x,}, new int[] {(int) rect.getMaxY(),(int) rect.getMinY(),rect.y}, 3); // will REDO for randomization
	}
	
	public void setTriangle(draw.Model.TriangleShape triangleShape) {
		this.setTriangle(triangleShape.getRectangle());
	}
	
	@Override
	public boolean Contains(Point point) {
		return triangle.contains(point);
	}
	
	@Override
	public void DrawSelf(Graphics grfx) {
		super.DrawSelf(grfx);
		Graphics2D g2 = (Graphics2D)grfx;
		this.setTriangle(getRectangle());
        g2.setStroke(getBorderStroke());
		g2.setColor(getFillColor());
		g2.fill(triangle);
		g2.setColor(isSelected() ? draw.Model.Shape.selectionColor : getBorderColor());
		g2.draw(triangle);
	}
}
