package creature;

public interface Attack {
	public String getName();
	public Diceroll getDamageRoll();
	public int getAttackBonus();
	public int getDamageBonus();
	public String getDefense();
}
