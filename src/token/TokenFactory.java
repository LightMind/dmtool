package token;

import creature.CreatureTemplate;

public class TokenFactory {
	private static int id = 100;
	
	public static BasicToken basicToken(int x, int y, int width, int height, CreatureTemplate t){
		id++;
		return new BasicToken(id, x, y, width, height,t);
	}
}
