package creature;

import java.util.List;

public interface CreatureTemplate {
	public static int STRENGTH = 1;
	public static int CONSTITUTION = 2;
	public static int INTELLIGENCE = 4;
	public static int DEXTERITY = 3;
	public static int WISDOM = 5;
	public static int CHARISMA = 6;
	
	public static int AC = 20;
	public static int FORTITUDE = 21;
	public static int REFLEX = 22;
	public static int WILL = 23;

	public int getAbility(int ability);
	public int getModifier(int ability);
	public int getDefense(int defense);
	public int getLevel();
	public int getMaxHP();
	public int getInititative();
	public int getSpeed();
	public String getName();
	List<Attack> getAttacks();
}
