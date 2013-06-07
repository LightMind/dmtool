package tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.SlickException;

import creature.CreatureTemplate;
import creature.Weapon;

import technical.CreatureTemplateParser;
import technical.WeaponParser;

public class Main {
	public static void main(String[] args) throws SlickException {

		List<Weapon> w1 = WeaponParser
				.parseWeapons(new File("res/weapons.txt"));
		List<CreatureTemplate> w2 = new ArrayList<CreatureTemplate>();
		File creatures = new File("res/creatures/");
		if (creatures.isDirectory()) {
			for (File f : creatures.listFiles()) {
				w2.add(CreatureTemplateParser.parseFile(f));
			}
		}

		Game l = new Live(w1, w2);

		AppGameContainer gc = new AppGameContainer(l, 1000, 600, false);

		gc.setTargetFrameRate(120);

		gc.start();
	}
}