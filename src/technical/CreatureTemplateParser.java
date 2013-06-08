package technical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tool.StandardCreature;
import tool.StandardDice;

import creature.Attack;
import creature.CreatureTemplate;
import creature.Diceroll;
import creature.Weapon;

public class CreatureTemplateParser {
	public static CreatureTemplate parseFile(File f) {
		CreatureTemplate result = null;
		try {
			Scanner sc = new Scanner(f);
			String name = sc.nextLine();
			int level = Integer.parseInt(sc.nextLine());
			int initiative = Integer.parseInt(sc.nextLine());
			int speed = Integer.parseInt(sc.nextLine());

			String[] abbs = sc.nextLine().split(" ");
			int[] abbInts = new int[6];
			for (int i = 0; i < 6; i++) {
				abbInts[i] = Integer.parseInt(abbs[i]);
			}

			String[] mods = sc.nextLine().split(" ");
			int[] modsInts = new int[6];
			for (int i = 0; i < 6; i++) {
				modsInts[i] = Integer.parseInt(mods[i]);
			}

			String[] defs = sc.nextLine().split(" ");
			int[] defsInts = new int[4];
			for (int i = 0; i < 4; i++) {
				defsInts[i] = Integer.parseInt(defs[i]);
			}

			int hp = Integer.parseInt(sc.nextLine());

			/*
			 * List<Weapon> weapons = new ArrayList<>();
			 *
			 * while (sc.hasNextLine()) { String wname = sc.nextLine(); Weapon w
			 * = WeaponParser.weaponMap.get(wname); if (w != null) {
			 * weapons.add(w); } }
			 */

			List<Attack> attacks = new ArrayList<Attack>();
			while (sc.hasNextLine()) {
				String w = sc.nextLine();
				if (w.matches("\\w+\\s\\d+\\s((AC)|(REF)|(WILL)|(FORT))\\s\\d+")) {
					parseMeleeAttackTwo(attacks, w);
				}
				if (w.matches("\\w+\\s\\d+\\s((AC)|(REF)|(WILL)|(FORT))\\s\\d+d\\d+\\+?\\d*")) {
					parseMeleeAttackOne(attacks, w);
				}
				if (w.matches("\\w+\\s\\d+/\\d+\\s\\d+\\s((AC)|(REF)|(WILL)|(FORT))\\s\\d+d\\d+\\+?\\d*")) {
					parseRangedAttackOne(attacks, w);
				}
			}

			StandardCreature creature = new StandardCreature(name, level,
					abbInts, modsInts, defsInts, hp, attacks, initiative, speed);
			result = creature;
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private static void parseRangedAttackOne(List<Attack> attacks, String w) {
		String[] tt = w.split(" ");
		final String attackName = tt[0];

		final int shortRange = Integer.parseInt(tt[1].substring(0,
				tt[1].indexOf('/')));
		final int longRange = Integer.parseInt(tt[1].substring(1 + tt[1]
				.indexOf('/')));
		final int attackModifier = Integer.parseInt(tt[2]);
		final String versus = tt[3];

		int diceAmount = Integer
				.parseInt(tt[4].substring(0, tt[4].indexOf('d')));
		int dice1 = 0;
		int bonus1 = 0;
		if (tt[4].contains("+")) {
			dice1 = Integer.parseInt(tt[4].substring(tt[4].indexOf('d') + 1,
					tt[4].indexOf('+')));
			bonus1 = Integer.parseInt(tt[4].substring(tt[4].indexOf('+')));
		} else {
			dice1 = Integer.parseInt(tt[4].substring(tt[4].indexOf('d') + 1));
		}
		final Diceroll diceroll = new StandardDice(diceAmount, dice1);

		final int bonus = bonus1;

		attacks.add(new Attack() {

			@Override
			public String getName() {
				return attackName;
			}

			@Override
			public Diceroll getDamageRoll() {
				return diceroll;
			}

			@Override
			public int getDamageBonus() {
				// TODO Auto-generated method stub
				return bonus;
			}

			@Override
			public int getAttackBonus() {
				return attackModifier;
			}

			public String toString() {
				return attackName + " " + shortRange + "/" + longRange + " +"
						+ attackModifier + " vs " + versus + "; " + diceroll
						+ " +" + bonus + " dmg";
			}

			@Override
			public String getDefense() {
				return versus;
			}
		});
	}

	private static void parseMeleeAttackOne(List<Attack> attacks, String w) {
		String[] tt = w.split(" ");
		final String attackName = tt[0];
		final int attackModifier = Integer.parseInt(tt[1]);
		final String versus = tt[2];

		int diceAmount = Integer
				.parseInt(tt[3].substring(0, tt[3].indexOf('d')));
		int dice1 = 0;
		int bonus1 = 0;
		if (tt[3].contains("+")) {
			dice1 = Integer.parseInt(tt[3].substring(tt[3].indexOf('d') + 1,
					tt[3].indexOf('+')));
			bonus1 = Integer.parseInt(tt[3].substring(tt[3].indexOf('+')));
		} else {
			dice1 = Integer.parseInt(tt[3].substring(tt[3].indexOf('d') + 1));
		}
		final Diceroll diceroll = new StandardDice(diceAmount, dice1);

		final int bonus = bonus1;

		attacks.add(new Attack() {

			@Override
			public String getName() {
				return attackName;
			}

			@Override
			public Diceroll getDamageRoll() {
				return diceroll;
			}

			@Override
			public int getDamageBonus() {
				// TODO Auto-generated method stub
				return bonus;
			}

			@Override
			public int getAttackBonus() {
				return attackModifier;
			}

			public String toString() {
				return attackName + " +" + attackModifier + " vs " + versus
						+ "; " + diceroll + " +" + bonus + " dmg";
			}

			@Override
			public String getDefense() {
				return versus;
			}
		});
	}

	private static void parseMeleeAttackTwo(List<Attack> attacks, String w) {
		String[] tt = w.split(" ");
		final String attackName = tt[0];
		final int attackModifier = Integer.parseInt(tt[1]);
		final String versus = tt[2];

		final int bonus = Integer.parseInt(tt[3]);
		final Diceroll diceroll = new StandardDice(0, 0);

		attacks.add(new Attack() {

			@Override
			public String getName() {
				return attackName;
			}

			@Override
			public Diceroll getDamageRoll() {
				return diceroll;
			}

			@Override
			public int getDamageBonus() {
				// TODO Auto-generated method stub
				return bonus;
			}

			@Override
			public int getAttackBonus() {
				return attackModifier;
			}

			public String toString() {
				return attackName + " +" + attackModifier + " vs " + versus
						+ "; " + bonus + " dmg";
			}

			@Override
			public String getDefense() {
				return versus;
			}
		});
	}
}
