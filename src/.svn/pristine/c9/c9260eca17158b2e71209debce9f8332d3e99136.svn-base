package draw.Model;

import java.io.Serializable;
import java.awt.BasicStroke;

public class MyBasicStroke extends BasicStroke implements Serializable{
	
	private float borderWidth = 1.0f;
	private int borderCap = 2;
	private int borderJoin = 0;
	
	public MyBasicStroke() {
		super();
	}
	
	public MyBasicStroke(float width, int cap, int join) {
		super(width, cap, join);
		this.borderWidth=width;
		this.borderCap = cap;
		this.borderJoin = join;
	}
	
	@Override
	public float getLineWidth() {
		return borderWidth;
	}
	
	@Override
	public int getEndCap() {
		return borderCap;
	}
	
	@Override
	public int getLineJoin() {
		return borderJoin;
	}
}
