package technical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import tool.StandardDice;
import tool.StandardWeapon;

import creature.Diceroll;
import creature.Weapon;

public class WeaponParser {
	public static Map<String,Weapon> weaponMap = new HashMap<>();
	
	public static List<Weapon> parseWeapons(File f){
		List<Weapon> weapons = new ArrayList<>();
		try {
			Scanner sc = new Scanner(f);
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				String[] content = line.split(",");
				
				String name = content[0];
				String dmgDice = content[1];
				String profBonus = content[2];
				String range = content[3];
				String[] groups = content[4].split("-");
				String properties = content[5];
				
				// parse damagedice
				String[] dmg = dmgDice.split("d");
				final int dicetype = Integer.parseInt(dmg[1]);
				final int diceamount = Integer.parseInt(dmg[0]);
				Diceroll d = new StandardDice(dicetype, diceamount);
				
				int proficiencyBonus = Integer.parseInt(profBonus);
				
				int shortRange = 1;
				int longRange = 1;
				if(!range.equals("-")){
					String[] ranges = range.split("/");
					shortRange= Integer.parseInt(ranges[0]);
					longRange = Integer.parseInt(ranges[1]);
				}
				
				String[] attributes = {""};
				if(!properties.equals("-")){
					attributes = properties.split("-");
				}
				
				Weapon w = new StandardWeapon(name, d, proficiencyBonus, shortRange, longRange, attributes, groups);
				weapons.add(w);
				weaponMap.put(w.getName(), w);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return weapons;
	}
}
