package TSRF;

import java.awt.*;

public class Rectangle {
	public Point UpperLeftCorner = new Point(0, 0);
	public Point LowerRightCorner = new Point(0, 0);
	
	
	
	public Rectangle(Point UpperLeftCorner, Point LowerRightCorner){
		this.UpperLeftCorner.x = UpperLeftCorner.x;
		this.UpperLeftCorner.y = UpperLeftCorner.y;
		
		this.LowerRightCorner.x = LowerRightCorner.x;
		this.LowerRightCorner.y = LowerRightCorner.y;
	}
	
	
	
/*	public Point getUpperLeftCorner(){ //deprecated
		return this.UpperLeftCorner;
	}
	
	public Point getLowerRightCorner(){
		return this.LowerRightCorner;
	}
	*/
}
