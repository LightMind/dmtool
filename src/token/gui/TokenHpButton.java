package token.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import token.BasicToken;

import gui.Button;

public class TokenHpButton implements Button{

	public int x,y;
	public BasicToken t;

	public TokenHpButton(int x, int y, BasicToken t){
		this.x = x;
		this.y = y;
		this.t = t;
	}

	@Override
	public void click() {
		t.hpmod++;
		System.out.println("click");
	}

	@Override
	public Shape shape() {
		return new Rectangle(x,y,20,20);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(0,0,20,20);
	}

}
