package technical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tool.StandardCreature;

import creature.Creature;
import creature.Weapon;

public class CreatureParser {
	public static Creature parseFile(File f){
		Creature result = null;
		try {
			Scanner sc = new Scanner(f);
			String name = sc.nextLine();
			int level = Integer.parseInt(sc.nextLine());
			int initiative = Integer.parseInt(sc.nextLine());
			int speed = Integer.parseInt(sc.nextLine());
			
			String[] abbs = sc.nextLine().split(" ");
			int[] abbInts = new int[6];
			for(int i = 0; i < 6; i++){
				abbInts[i] = Integer.parseInt(abbs[i]);
			}
			
			String[] mods = sc.nextLine().split(" ");
			int[] modsInts = new int[6];
			for(int i = 0; i < 6; i++){
				modsInts[i] = Integer.parseInt(mods[i]);
			}
			
			String[] defs = sc.nextLine().split(" ");
			int[] defsInts = new int[4];
			for(int i = 0; i < 4; i++){
				defsInts[i] = Integer.parseInt(defs[i]);
			}
			
			int hp = Integer.parseInt(sc.nextLine());
			
			List<Weapon> weapons = new ArrayList<>();
			
			while(sc.hasNextLine()){
				String wname = sc.nextLine();
				Weapon w = WeaponParser.weaponMap.get(wname);
				if(w != null){
					weapons.add(w);
				}
			}
			
			StandardCreature creature = new StandardCreature(name, level, abbInts, modsInts, defsInts, hp, weapons, initiative, speed);
			result = creature;			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
}
