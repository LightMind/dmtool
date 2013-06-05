package tool;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Live extends StateBasedGame {

	public Live() {
		super("DM-Tool");
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new Tool());
	}

}
