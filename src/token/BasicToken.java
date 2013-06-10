package token;

import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

import creature.CreatureTemplate;

public class BasicToken {
	public final int ID;
	public int x;
	public int y;
	public int width;
	public int height;

	public int hpmod = 0;
	public int[] abbilitymods = { 0, 0, 0, 0, 0, 0 };

	public int initiative = 0;

	public CreatureTemplate template;

	public BasicToken(int id, int x, int y, int width, int height,
			CreatureTemplate t) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		template = t;
		this.ID = id;

		Random r = new Random();
		initiative = r.nextInt(20) + 1 + t.getInititative();
	}

	public float healthPercent() {
		return ((float) template.getMaxHP() + (float) hpmod)
				/ (float) template.getMaxHP();
	}

	public Rectangle rect() {
		return new Rectangle(x, y, width, height);
	}

}
