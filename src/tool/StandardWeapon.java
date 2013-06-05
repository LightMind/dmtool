package tool;

import java.util.ArrayList;
import java.util.Collections;

import creature.Attack;
import creature.Diceroll;
import creature.Weapon;

public class StandardWeapon implements Weapon{

	public final String name;
	public final Diceroll diceroll;
	public final ArrayList<String> properties;
	public final ArrayList<String> groups;
	public final int longRange;
	public final int shortRange;
	public final int bonus;
	
	public StandardWeapon(String name, Diceroll diceroll, int bonus, int shortRange, int longRange, String[] properties, String[] groups){
		this.name = name;
		this.diceroll = diceroll;
		this.bonus = bonus;
		this.shortRange = shortRange;
		this.longRange = longRange;
		this.groups = new ArrayList<>();
		Collections.addAll(this.groups, groups);
		this.properties = new ArrayList<>();
		Collections.addAll(this.properties, properties);
	}
	
	@Override
	public Attack getAttack() {
		return new Attack() {
			@Override
			public Diceroll getDamageRoll() {
				return diceroll;
			}
			
			@Override
			public int getDamageBonus() {
				return 0;
			}
			
			@Override
			public int getAttackBonus() {
				return bonus;
			}
		};
	}

	@Override
	public boolean depleting() {
		return properties.contains("LightThrown") || properties.contains("HeavyThrown");
	}
	
	public boolean ranged(){
		return groups.contains("Bow") || groups.contains("Sling") || groups.contains("Crossbow");
	}

	@Override
	public String toString() {
		return "StandardWeapon [name=" + name + ", diceroll=" + diceroll
				+ ", properties=" + properties + ", groups=" + groups
				+ ", longRange=" + longRange + ", shortRange=" + shortRange
				+ ", bonus=" + bonus + "]";
	}

	@Override
	public int getShortRange() {
		return shortRange;
	}

	@Override
	public int getLongRange() {
		return longRange;
	}

	@Override
	public String getName() {
		return name;
	}
	
	
}
