package gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

public interface Button {
	public void click(int mousebutton);
	public Shape shape();
	public void render(Graphics g);
	public void buffer(String c);
	public void forget();
}
