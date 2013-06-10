package tool;

import gui.Button;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import creature.Attack;
import creature.CreatureTemplate;
import creature.Diceroll;
import creature.Weapon;

import token.BasicToken;
import token.TokenFactory;
import token.gui.TokenHpButton;

public class Tool implements GameState {
	Random r = new Random();

	int menumod = 0;

	boolean useGrid = true;
	boolean showCreatures = false;

	int dragStartX = 0;
	int dragStartY = 0;
	int scale = 25;
	int mouseButton = 0;
	int menuWidth = 220;

	int boxWidth = 400;

	Rectangle cmBox = null;

	Input currentInput;
	BasicToken cToken;
	BasicToken mouseOver;

	List<BasicToken> tokens = new ArrayList<>();
	List<Weapon> weapons;
	List<CreatureTemplate> creatures;

	Deque<String> log = new LinkedList<String>();

	public Tool(List<Weapon> weapons, List<CreatureTemplate> creatures) {
		this.weapons = weapons;
		this.creatures = creatures;
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (cmBox != null && mouseOver != null && button == 0) {
			for (Button b : buttons) {
				if (b.shape().contains(x, y)) {
					b.click(0);
				}
			}

			Attack attack = mouseOverAttack(x, y);
			if (attack != null) {
				int[] results = rollAttack(attack);
				log.addFirst("Rolled " + results[0] + " versus "
						+ attack.getDefense() + " with " + results[1]
						+ " damage");
			}
		}
	}

	private int[] rollAttack(Attack attack) {
		int attackRoll = rollDice(d20) + attack.getAttackBonus();
		int damageRoll = rollDice(attack.getDamageRoll())
				+ attack.getDamageBonus();
		int[] results = { attackRoll, damageRoll };
		return results;
	}

	public Attack mouseOverAttack(int x, int y) {
		if (cmBox != null && mouseOver != null) {
			if (x >= mouseOver.x && x <= mouseOver.x + boxWidth
					&& y > mouseOver.y + 45 + mouseOver.height) {
				int index = (y - (mouseOver.y + 45 + mouseOver.height)) / 15;
				if (index < mouseOver.template.getAttacks().size()) {
					return mouseOver.template.getAttacks().get(index);
				}
			}
		}
		return null;
	}

	public Diceroll d20 = new Diceroll() {
		@Override
		public int getDice() {
			return 20;
		}

		@Override
		public int getAmount() {
			return 1;
		}
	};

	private int rollDice(Diceroll damageRoll) {
		int sum = 0;
		for (int i = 0; i < damageRoll.getAmount(); i++) {
			int temp = r.nextInt(damageRoll.getDice()) + 1;
			sum += temp;
			if ((temp == 1 || temp == 20) && damageRoll.getDice() == 20) {
				log.addFirst("d" + damageRoll.getDice() + " rolled " + temp);
			}
			testLog();
		}
		return sum;
	}

	private void testLog() {
		while (log.size() > 15) {
			log.removeLast();
		}
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		mx = newx;
		my = newy;

		if (cToken != null) {
			cToken.x += newx - oldx;
			cToken.y += newy - oldy;

			mouseOver = null;
			cmBox = null;
			destroyMouseBox();
		}
		// testMouseOverBox(newx, newy, false);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		boolean found = false;
		// test if we were inside button / buffer input, and we now have left it

		for (Button b : buttons) {
			if (b.shape().contains(oldx, oldy)
					&& !b.shape().contains(newx, newy)) {
				b.forget();
			}
		}

		// first, test if inside shown box
		if (cmBox != null && cmBox.contains(newx, newy)) {

		} else { // not inside the box, check for other tokens.
			testMouseOverBox(newx, newy, found);
		}
		mx = newx;
		my = newy;
	}

	private void testMouseOverBox(int newx, int newy, boolean found) {
		if (!showCreatures || newx > menuWidth) {
			for (BasicToken t : tokens) {
				if (t.rect().contains(newx, newy)) {
					mouseOver = t;
					found = true;
					createMouseBox(t);

				}
			}
			if (!found) {
				destroyMouseBox();
				mouseOver = null;
				cmBox = null;
			}
		}
	}

	public Attack dragAttack = null;
	public int startx, starty;
	public List<BasicToken> selected = new ArrayList<BasicToken>();
	boolean pressed = false;

	@Override
	public void mousePressed(int button, int x, int y) {
		mouseButton = button;
		if (button == 1) {
			this.dragAttack = mouseOverAttack(x, y);
			dragAttackused = false;
		} else if (!showCreatures || x > menuWidth && button == 0) {
			for (BasicToken t : tokens) {
				if (selected.contains(t)) {
					pressed = false;
				} else if (t.rect().contains(x, y)) {
					cToken = t;
					dragStartX = t.x;
					dragStartY = t.y;
				}
			}
		} else if (showCreatures && x < menuWidth && button == 0) {
			int index = y / 50;
			if (index < creatures.size()) {
				cToken = TokenFactory.basicToken(x, y, scale, scale,
						creatures.get(index + menumod));
				tokens.add(cToken);
				dragStartX = x;
				dragStartY = y;
			}
		}

	}

	private int gridify(int pos) {
		return scale * Math.round((float) pos / (float) scale);
	}

	boolean dragAttackused = false;

	@Override
	public void mouseReleased(int button, int x, int y) {

		if (cmBox != null && cmBox.contains(x, y)) {

		} else if (button == 0) {
			if (cToken != null && useGrid) {
				cToken.x = gridify(cToken.x);
				cToken.y = gridify(cToken.y);
				mouseOver = cToken;
				createMouseBox(mouseOver);
			}
			cToken = null;

		}
		if (button == 1 && dragAttack != null) {
			if (!dragAttackused) {
				for (BasicToken t : tokens) {
					if (t.rect().contains(x, y)) {
						dragAttack(t);
					}
				}
			}
			dragAttack = null;
		}
	}

	private int dragAttack(BasicToken t) {
		int[] results = rollAttack(dragAttack);
		int defense = strToDefense(dragAttack.getDefense());
		if (results[0] >= t.template.getDefense(defense)) {
			t.hpmod -= results[1];
			log.addFirst("Hit with " + results[0] + " vs "
					+ dragAttack.getDefense() + " for " + results[1]
					+ " damage");
			return results[1];
		}
		log.addFirst("Missed with " + results[0] + " vs "
				+ dragAttack.getDefense());
		return 0;
	}

	public int strToDefense(String def) {
		if ("AC".equals(def)) {
			return CreatureTemplate.AC;
		}
		if ("REF".equals(def)) {
			return CreatureTemplate.REFLEX;
		}
		if ("FORT".equals(def)) {
			return CreatureTemplate.FORTITUDE;
		}
		if ("WILL".equals(def)) {
			return CreatureTemplate.WILL;
		}
		return CreatureTemplate.AC;
	}

	int mx, my;

	@Override
	public void mouseWheelMoved(int change) {
		if (cmBox != null && mouseOver != null) {
			for (Button b : buttons) {
				if (b.shape().contains(mx, my)) {
					b.click(change > 0 ? 1 : -1);
				}
			}
		}
		if (showCreatures && mx < menuWidth) {
			menumod += change > 0 ? -1 : 1;
			if (menumod < 0) {
				menumod = 0;
			}
		}
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public void inputStarted() {

	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput(Input input) {
		currentInput = input;
	}

	@Override
	public void keyPressed(int key, char c) {
		showCreatures = key == Keyboard.KEY_Q;

		if (cmBox != null && mouseOver != null) {
			for (Button b : buttons) {
				if (b.shape().contains(mx, my)) {
					if (key == Keyboard.KEY_SUBTRACT) {
						b.buffer("-");
					} else if (c >= '0' && c <= '9'
							|| key == Keyboard.KEY_MINUS) {
						b.buffer("" + c);
					} else if (key == Keyboard.KEY_BACK) {
						b.forget();
					} else {
						b.buffer("EOI");
					}
				}
			}
			Attack a = mouseOverAttack(mx, my);

			if (a != null && c >= '1' && c <= '9') {
				System.out.println("mouseover attakcs");
				int times = Integer.parseInt("" + c);
				multipleAttacks(a, times);
			} else if (dragAttack != null && c >= '1' && c <= '9') {
				System.out.println("Test multiple drag attacks");
				dragAttackused = true;
				BasicToken target = null;
				for (BasicToken t : tokens) {
					if (t.rect().contains(mx, my)) {
						target = t;
					}
				}
				if (target != null) {
					multipleAttacks(dragAttack, Integer.parseInt("" + c),
							target);
				}
			}
		}
	}

	private void multipleAttacks(Attack a, int times, BasicToken target) {
		int totalDamage = 0;
		log.addFirst("------ " + times + " Attacks -------");
		for (int i = 0; i < times; i++) {
			totalDamage += dragAttack(target);
		}
		log.addFirst("# Damage = " + totalDamage + " #");
	}

	private void multipleAttacks(Attack a, int times) {
		int totalDamage = 0;
		log.addFirst("------ " + times + " Attacks -------");
		for (int i = 0; i < times; i++) {
			int[] results = rollAttack(a);
			totalDamage += results[1];
			log.addFirst(results[0] + " vs " + a.getDefense() + " : "
					+ results[1] + "dmg");
		}
		log.addFirst("# Damage = " + totalDamage + " #");
	}

	@Override
	public void keyReleased(int key, char c) {
		showCreatures = showCreatures && !(key == Keyboard.KEY_Q);
	}

	@Override
	public void controllerButtonPressed(int controller, int button) {

	}

	@Override
	public void controllerButtonReleased(int controller, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerDownPressed(int controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerDownReleased(int controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerLeftPressed(int controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerLeftReleased(int controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerRightPressed(int controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerRightReleased(int controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerUpPressed(int controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerUpReleased(int controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		currentInput = container.getInput();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		g.setColor(new Color(22, 40, 22));
		for (int i = 0; i < container.getWidth(); i += scale) {
			g.drawLine(i, 0, i, container.getHeight());
		}

		for (int h = 0; h < container.getHeight(); h += scale) {
			g.drawLine(0, h, container.getWidth(), h);
		}

		for (BasicToken t : tokens) {
			g.setColor(Color.white);
			g.fillRect(t.x, t.y, t.width, t.height);
			g.setColor(Color.lightGray);
			g.drawRect(t.x, t.y, t.width, t.height);
			g.setColor(new Color(120, 20, 20, 128));
			g.fillRect(t.x, t.y, t.width, t.height * t.healthPercent());
			g.setColor(Color.blue);
			g.drawString("" + t.initiative, t.x + 5, t.y + 5);
		}

		if (cToken != null) {
			if (useGrid) {
				g.setColor(new Color(100, 160, 100, 127));
				Rectangle rect = cToken.rect();
				rect.setX(gridify((int) rect.getX()));
				rect.setY(gridify((int) rect.getY()));
				g.fill(rect);
			}

			g.setColor(Color.green);
			int xdiff = Math.abs(gridify(dragStartX) - gridify(cToken.x));
			int ydiff = Math.abs(gridify(dragStartY) - gridify(cToken.y));
			g.drawString("dist:" + Math.max(xdiff / scale, ydiff / scale),
					cToken.x - 30, cToken.y - 30);
		}

		int str = 0;

		for (String s : log) {
			g.setColor(new Color(60, 200, 60, (10 - str) * 10 + 150));
			int x = container.getWidth() - s.length() * 9 - 10;
			int y = container.getHeight() - 15 - 17 * str;
			str++;
			g.drawString(s, x, y);
		}

		if (mouseOver != null && dragAttack == null) {
			g.setColor(Color.lightGray);
			g.fill(cmBox);
			g.setColor(Color.black);
			g.drawString("" + mouseOver.template.getName(), mouseOver.x + 5,
					mouseOver.y + mouseOver.height);
			g.drawString(
					"Hp: " + (mouseOver.hpmod + mouseOver.template.getMaxHP())
							+ " / " + mouseOver.template.getMaxHP(),
					cmBox.getX() + 10, cmBox.getY() + 30);

			int index = 0;
			for (Attack at : mouseOver.template.getAttacks()) {
				g.drawLine(mouseOver.x + 10, mouseOver.y + 45
						+ mouseOver.height + 15 * index, mouseOver.x + 100,
						mouseOver.y + 45 + mouseOver.height + 15 * index);
				g.drawString(at.toString(), mouseOver.x + 10, mouseOver.y + 45
						+ mouseOver.height + 15 * index);
				index++;
			}

			int defbase = mouseOver.x + boxWidth - 180;
			int ybase = mouseOver.y + mouseOver.height;
			CreatureTemplate template = mouseOver.template;
			g.drawString("AC  : " + template.getDefense(CreatureTemplate.AC),
					defbase, ybase);
			g.drawString(
					"REF : " + template.getDefense(CreatureTemplate.REFLEX),
					defbase + 100, ybase);
			g.drawString(
					"FORT: " + template.getDefense(CreatureTemplate.FORTITUDE),
					defbase, ybase + 18);
			g.drawString("WILL: " + template.getDefense(CreatureTemplate.WILL),
					defbase + 100, ybase + 18);

		}

		if (mouseOver != null && dragAttack == null)
			for (Button b : buttons) {
				g.translate(b.shape().getMinX(), b.shape().getMinY());
				g.pushTransform();
				b.render(g);
				g.popTransform();
				g.resetTransform();
			}

		if (showCreatures) {
			g.setColor(new Color(30, 30, 100, 230));
			g.fillRect(0, 0, menuWidth, container.getHeight());
			g.setColor(Color.white);
			for (int i = menumod; i < creatures.size(); i++) {
				int baseline = 50 * i - menumod * 50;
				g.setColor(Color.white);
				g.fillRect(0, baseline, menuWidth, 50);
				g.setColor(Color.black);
				g.drawRect(0, baseline, menuWidth, 50);

				CreatureTemplate c = creatures.get(i);
				g.drawString(c.getName() + " lvl " + c.getLevel(), 5,
						baseline + 5);
			}
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

	}

	public Rectangle getMouseOverBox(BasicToken mouseOver) {
		if (mouseOver == null) {
			return null;
		}
		int height = mouseOver.template.getAttacks().size() * 15 + 65;

		return new Rectangle(mouseOver.x, mouseOver.y + mouseOver.height - 5,
				boxWidth, height);
	}

	public List<Button> buttons = new ArrayList<Button>();

	public void createMouseBox(BasicToken token) {
		buttons.clear();
		cmBox = getMouseOverBox(token);
		Button bHealthUp = new TokenHpButton((int) cmBox.getX(),
				(int) cmBox.getY() + 30, token);
		buttons.add(bHealthUp);
	}

	public void destroyMouseBox() {
		buttons.clear();
		cmBox = null;
	}

	public int getAbilityDamageBonus(BasicToken t, Weapon w) {
		List<String> properties = w.getProperties();
		if (w.ranged() && !properties.contains("HeavyThrown")) {
			return (t.template.getAbility(CreatureTemplate.DEXTERITY) - 10) / 2;
		}
		return (t.template.getAbility(CreatureTemplate.STRENGTH) - 10) / 2;
	}
}
