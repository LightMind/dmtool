package tool;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import creature.Creature;
import creature.Weapon;

import token.BasicToken;
import token.TokenFactory;

public class Tool implements GameState {
	boolean useGrid = true;
	boolean showCreatures = false;

	int dragStartX = 0;
	int dragStartY = 0;
	int scale = 25;
	int mouseButton = 0;
	int menuWidth = 200;

	Input currentInput;
	BasicToken cToken;
	BasicToken mouseOver;

	List<BasicToken> tokens = new ArrayList<>();
	List<Weapon> weapons;
	List<Creature> creatures;

	public Tool(List<Weapon> weapons, List<Creature> creatures) {
		this.weapons = weapons;
		this.creatures = creatures;
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {

	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (mouseButton == 0) {
			if (cToken == null)
				return;
			cToken.x += newx - oldx;
			cToken.y += newy - oldy;

			mouseOver = null;
		}
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		boolean found = false;
		if (!showCreatures || newx > menuWidth) {
			for (BasicToken t : tokens) {
				if (t.rect().contains(newx, newy)) {
					mouseOver = t;
					found = true;
				}
			}
			if (!found) {
				mouseOver = null;
			}
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		mouseButton = button;
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

	private int gridify(int pos) {
		return scale * Math.round((float) pos / (float) scale);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (button == 0) {
			if (cToken != null && useGrid) {
				cToken.x = gridify(cToken.x);
				cToken.y = gridify(cToken.y);

			}
			mouseOver = cToken;
			cToken = null;
		}
	}

	@Override
	public void mouseWheelMoved(int change) {

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
		if (key == Keyboard.KEY_W) {
			BasicToken bt = TokenFactory.basicToken(
					useGrid ? gridify(currentInput.getMouseX()) : currentInput
							.getMouseX(),
					useGrid ? gridify(currentInput.getMouseY()) : currentInput
							.getMouseY(), scale, scale);
			tokens.add(bt);
		}
		if (key == Keyboard.KEY_E) {
			BasicToken bt = TokenFactory.basicToken(
					useGrid ? gridify(currentInput.getMouseX()) : currentInput
							.getMouseX(),
					useGrid ? gridify(currentInput.getMouseY()) : currentInput
							.getMouseY(), scale * 2, scale * 2);
			tokens.add(bt);
		}
		showCreatures = key == Keyboard.KEY_Q;
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
			g.fillRect(mouseOver.x + 5, mouseOver.y + 15, 150, 100);
			g.setColor(Color.black);
			g.drawString(" " + mouseOver.x, mouseOver.x + 10, mouseOver.y + 20);
			g.drawString(" " + mouseOver.ID, mouseOver.x + 10, mouseOver.y + 35);
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

				Creature c = creatures.get(i);
				g.drawString(c.getName() + " lvl " + c.getLevel(), 5,
						baseline + 5);
			}
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

	}

}
