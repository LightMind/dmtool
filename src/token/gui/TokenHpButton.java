package token.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import token.BasicToken;

import gui.Button;

public class TokenHpButton implements Button {

	public int x, y;
	public BasicToken t;

	public TokenHpButton(int x, int y, BasicToken t) {
		this.x = x;
		this.y = y;
		this.t = t;
	}

	@Override
	public void click(int mousebutton) {
		if (mousebutton == 1)
			t.hpmod++;
		if (mousebutton == -1)
			t.hpmod--;
	}

	@Override
	public Shape shape() {
		return new Rectangle(x, y, 200, 20);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(new Color(0, 100, 0, 90));
		g.fillRect(0, 0, 200, 20);
		g.setColor(new Color(255, 255, 255));
		g.drawString(buffer, 200 - buffer.length() * 9, 0);
	}

	public String buffer = "";

	@Override
	public void buffer(String c) {
		if ((c.equals("-"))&& !buffer.isEmpty()) {
			return;
		}
		if (!c.equals("EOI")) {

			buffer += c;
		} else {
			if (buffer.length() <= 5 && !buffer.equals("-") && buffer.length() > 0)
				t.hpmod += Integer.parseInt(buffer);
			buffer = "";
		}
	}

	@Override
	public void forget() {
		buffer = "";
	}

}
