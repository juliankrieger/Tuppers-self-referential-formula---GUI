package TSRF;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.time.Year;

import javax.swing.*;

import org.w3c.dom.css.Rect;

import TSRF.Rectangle;


//TODO
/*
* - finish drawGridv2:
*   - I want to color little squares for the draw area instead of lines, which is needed because we need to test if we are in a
*     "square" to test if we can  color in the tile in the grid. Thus, an array with all the different tiles is needed
*
* - In the MouseEvent function for clicking will be a test if we are in an uncolored tile, and if so, color it on clicking.
*   Because the MouseClick event returns a position with e.getPos or something like that, but coloring a rectangle at that
*   Position starts at Pos.X and Pos.Y and colors everything bottom right from that, the aforementioned check is needed.
*
* - If we are in a tile, color a FIXED position, the UpperLeftCorner of the tile which is still white and not on the border
*
* - Because we should have a tile array after, check that if its color or not:
*   - if it IS colored: Add a 1 to the Num array
*   - if it IS NOT colored: Add a 0 to the Num array
* - After everything is finished : Calculate Tuppers formula and return the calculated k in the JFRAME
* - Make the return Field copy-able.
*/
public class Drawer extends JPanel implements MouseListener{

	public static void main(String[] args){
		//TODO
		new Drawer();
	}
	
	static final int height = 500; //currently static height of image
	static final int width = 1500; //currently static width of image
	static final Point window = new Point(width, height);
	static final Rectangle window_frame = new Rectangle(new Point(0, 0), window);
	
	private int  tupper_scale = 10;
	private int tupper_height = 17 * tupper_scale; //static height needed for Tuppers formula
	private int tupper_width = 106 * tupper_scale; //static width needed for Tuppers formula
	private Point tupper = new Point(tupper_width, tupper_height);
	private Rectangle tupper_frame = new Rectangle(new Point(0, 0), tupper);

	
	private int columns = tupper.x / tupper_scale; //colums for the grid
	private int rows = tupper.y / tupper_scale; //rows for the grid
	
	private Point dimension = new Point(((int)(tupper.x / columns)), ((int)(tupper.y / rows))); //Dimension for the drawGridv2 method
	private Rectangle tile = new Rectangle(new Point(0, 0), new Point(10, 10)); //the grey square
	private Point offset = (new Point((tile.LowerRightCorner.x - tile.UpperLeftCorner.x), (tile.LowerRightCorner.x - tile.UpperLeftCorner.x))); 
	private Point gridStartPoint = new Point(20, 20);
	private Rectangle[] tileArray = new Rectangle[17 * 106];
	
	BufferedImage image; //Buffered Image
	
	
	Drawer(){
		image = new BufferedImage(window.x, window.y, BufferedImage.TYPE_INT_ARGB_PRE); //Init buffered Image
		setPreferredSize(new Dimension(window.x, window.y)); //Set BI Size
		JFrame frame = new JFrame("Tuppers formula"); //Start JFrame
		this.addMouseListener(this); //Add the mouse listener to the component
		frame.add(this); //add this component to the frame
		frame.pack(); //pack the frame
		frame.setVisible(true); //set it to visible
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exit on Close 
		
		
		initializeGrid(gridStartPoint);
	}
	
	public void initializeGrid(Point startPoint){
		Graphics graphics = image.getGraphics();
		
		moveGrid(startPoint);
		drawGrid(graphics, Color.LIGHT_GRAY, tile);
		
		graphics.setColor(Color.BLACK);	
		graphics.drawRect(tupper_frame.UpperLeftCorner.x, tupper_frame.UpperLeftCorner.y,
				tupper_frame.LowerRightCorner.x - gridStartPoint.x, tupper_frame.LowerRightCorner.y - gridStartPoint.y); //Black border
		
		
		graphics.dispose();
		repaint();
	}
	
	
	
	public void drawGrid(Graphics graphics, Color color, Rectangle tile){ //the plan is to color different rectangles,
		graphics.setColor(color);                                                        //because that's needed for later
		
		for(int k = 0; k < rows; k++){
			for(int i = 0; i < columns; i++){ // columns
					Point updatedULC = new Point (tile.UpperLeftCorner.x + offset.x * i, tile.UpperLeftCorner.y + offset.y *k);
					
                	if(k != rows - 2 && i != columns - 2 && k != rows - 1 && i != columns - 1){ //dirty workaround
						graphics.drawRect(updatedULC.x, updatedULC.y, 
								tile.LowerRightCorner.x, tile.LowerRightCorner.y);
                	}
                	
                	tileArray[i + ((tupper_width / tupper_scale) * k)] = 
                			new Rectangle(new Point(updatedULC.x - 1, updatedULC.y - 1), 
                					new Point(tile.LowerRightCorner.x + (offset.x * i) - 1,
                							tile.LowerRightCorner.y + (offset.y * k) - 1));
			}
		}
	}
	
	public Point movePoint(Point base, Point vector){
		base.x += vector.x;
		base.y += vector.y;
		
		return base;
	}
	
	public void moveGrid(Point p){
		this.tile.UpperLeftCorner = movePoint(this.tile.UpperLeftCorner, p);
		this.tile.LowerRightCorner = movePoint(this.tile.LowerRightCorner, p);
		
		this.tupper_frame.UpperLeftCorner = movePoint(this.tupper_frame.UpperLeftCorner, p);
		this.tupper_frame.LowerRightCorner = movePoint(this.tupper_frame.LowerRightCorner, p);
	}
	
	public Rectangle acceptableTile(Point mousePos){ //fuck runtime
		
		Rectangle colorField = null;
		boolean bool = false;
		for(int i = 0; i < tileArray.length; i++){
			bool = bool || ((mousePos.x >= tileArray[i].UpperLeftCorner.x) 
					&& (mousePos.x <= tileArray[i].LowerRightCorner.x)
					&& (mousePos.y >= tileArray[i].UpperLeftCorner.y)
					&& (mousePos.y <= tileArray[i].LowerRightCorner.y));
			
			if(bool){
				colorField = new Rectangle(tileArray[i].UpperLeftCorner, tileArray[i].LowerRightCorner);
				break;
			}
		}
		return colorField;
	}
	
	@Override
	public void mouseClicked(MouseEvent e){ //Do something if the mouse was clicked
		Graphics graphics = image.getGraphics();
		
		graphics.setColor(Color.blue);
		Point currentMousePos = e.getPoint();
		Point pixel = new Point(9, 9);
		
		Point blackBorderLR = new Point(tupper_frame.LowerRightCorner.x + gridStartPoint.x, 
				tupper_frame.LowerRightCorner.y + gridStartPoint.y);
		
		System.out.println("Current Mouse Position: " + currentMousePos.x + " " + currentMousePos.y);
		System.out.println("Current Black Border LRC Point :" + blackBorderLR);
		
		
		Rectangle colorField = acceptableTile(currentMousePos);
		if(colorField != null 
				&& (currentMousePos.x <= (blackBorderLR.x))
				&& (currentMousePos.y <= blackBorderLR.x)){

			graphics.fillRect(colorField.UpperLeftCorner.x + 2, colorField.UpperLeftCorner.y + 2, pixel.x, pixel.y);
			graphics.dispose();
			repaint();
		} else if(colorField == null){
			System.out.println("colorField is null");
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) { //Init if the mouse enters the field
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void paintComponent(Graphics graphics){
		graphics.drawImage(image, 0, 0, null);
	}
}
