package token;

import org.newdawn.slick.geom.Rectangle;

public class BasicToken {
	public final int ID;
	public int x;
	public int y;
	public int width;
	public int height;
	
	public BasicToken(int id , int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.ID = id;
	}
	
	public Rectangle rect() {
		return new Rectangle(x, y, width, height);
	}
}
