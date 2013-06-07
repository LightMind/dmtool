package tool;

import java.util.ArrayList;
import java.util.List;

import creature.CreatureTemplate;
import creature.Weapon;

public class StandardCreature implements CreatureTemplate {

	private String name;
	private int[] ability;
	private int[] abmod;
	private int[] def;
	private int level;
	private int maxHP;
	private int initiativeBonus;
	private ArrayList<Weapon> weapons;
	private int speed;
	
		
	public StandardCreature(String name, int level, int[] ab, int[] mod, int[] def, int hp, List<Weapon> wpns, int initiative, int spd){
		this.name = name;
		this.level = level;
		this.ability = ab;
		this.abmod = mod;
		this.def = def;
		this.maxHP = hp;
		this.initiativeBonus = initiative;
		this.weapons = new ArrayList<>(wpns);
		this.speed = spd;
	}
	
	@Override
	public int getAbility(int ability) {
		return this.ability[ability-1];
	}

	@Override
	public int getModifier(int ability) {
		return this.abmod[ability-1];
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getMaxHP() {
		return maxHP;
	}

	@Override
	public int getInititative() {
		return initiativeBonus;
	}

	@Override
	public List<Weapon> getWeapons() {
		return weapons;
	}

	@Override
	public int getDefense(int defense) {
		return def[defense-20];
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	@Override
	public String getName() {
		return name;
	}

}
