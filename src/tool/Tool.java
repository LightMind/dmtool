package tool;

import gui.Button;

import java.util.ArrayList;
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

	boolean useGrid = true;
	boolean showCreatures = false;

	int dragStartX = 0;
	int dragStartY = 0;
	int scale = 25;
	int mouseButton = 0;
	int menuWidth = 200;

	int boxWidth = 400;
	int boxHeight = 200;

	Rectangle cmBox = null;

	Input currentInput;
	BasicToken cToken;
	BasicToken mouseOver;

	List<BasicToken> tokens = new ArrayList<>();
	List<Weapon> weapons;
	List<CreatureTemplate> creatures;

	public Tool(List<Weapon> weapons, List<CreatureTemplate> creatures) {
		this.weapons = weapons;
		this.creatures = creatures;
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (cmBox != null && mouseOver != null) {
			for (Button b : buttons) {
				if (b.shape().contains(x, y)) {
					b.click(0);
				}
			}
			if (x >= mouseOver.x && x <= mouseOver.x + boxWidth
					&& y > mouseOver.y + 45 + mouseOver.height) {
				int index = (y - (mouseOver.y + 45 + mouseOver.height)) / 15;
				if (index < mouseOver.template.getAttacks().size()) {
					Attack attack = mouseOver.template.getAttacks().get(index);
					int attackRoll = r.nextInt(20) + 1
							+ attack.getAttackBonus();
					int damageRoll = rollDice(attack.getDamageRoll())
							+ attack.getDamageBonus();
					System.out.println("Rolled " + attackRoll + " versus "
							+ attack.getDefense() + " with " + damageRoll
							+ " damage");
				}
			}
		}
	}

	private int rollDice(Diceroll damageRoll) {
		int sum = 0;
		for (int i = 0; i < damageRoll.getDice(); i++) {
			int temp = r.nextInt(damageRoll.getAmount()) + 1;
			sum += temp;
			System.out
					.println("d" + damageRoll.getAmount() + " rolled " + temp);
		}
		return sum;
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (mouseButton == 0) {
			if (cToken == null)
				return;
			cToken.x += newx - oldx;
			cToken.y += newy - oldy;

			mouseOver = null;
			cmBox = null;
			destroyMouseBox();
		}
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
		mx = newx;
		my = newy;
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		mouseButton = button;
		if (!showCreatures || x > menuWidth) {
			if (button == 0) {
				for (BasicToken t : tokens) {
					if (t.rect().contains(x, y)) {
						cToken = t;
						dragStartX = t.x;
						dragStartY = t.y;
					}
				}
			}
		}
		if (showCreatures && x < menuWidth) {
			int index = y / 50;
			if (index < creatures.size()) {
				cToken = TokenFactory.basicToken(x, y, scale, scale,
						creatures.get(index));
				tokens.add(cToken);
				dragStartX = x;
				dragStartY = y;
			}
		}
	}

	private int gridify(int pos) {
		return scale * Math.round((float) pos / (float) scale);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (cmBox != null && cmBox.contains(x, y)) {

		} else if (button == 0) {
			if (cToken != null && useGrid) {
				cToken.x = gridify(cToken.x);
				cToken.y = gridify(cToken.y);

			}
			mouseOver = cToken;
			if (cToken != null)
				createMouseBox(mouseOver);
			cToken = null;
		}
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
		}
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

		if (mouseOver != null) {
			g.setColor(Color.lightGray);
			g.fill(cmBox);
			g.setColor(Color.black);
			g.drawString(" " + mouseOver.template.getName(), mouseOver.x + 5,
					mouseOver.y + 5 + mouseOver.height);
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

		}

		if (mouseOver != null)
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
			for (int i = 0; i < creatures.size(); i++) {
				int baseline = 50 * i;
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
		return new Rectangle(mouseOver.x, mouseOver.y + mouseOver.height - 5,
				boxWidth, boxHeight);
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
