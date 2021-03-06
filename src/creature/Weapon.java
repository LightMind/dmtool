package creature;

public interface Weapon {
	public String getName();
	public Attack getAttack();
	/**
	 * depleting weapons will be removed from the list after being used for ranged attacks.
	 * @return
	 */
	public boolean depleting();
	public boolean ranged();
	
	public int getShortRange();
	public int getLongRange();
}
