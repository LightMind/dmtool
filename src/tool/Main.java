package tool;

import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.SlickException;

import technical.CreatureParser;
import technical.WeaponParser;

public class Main {
	public static void main(String[] args) throws SlickException{
		Game l = new Live();
		WeaponParser.parseWeapons(new File("res/weapons.txt"));
		CreatureParser.parseFile(new File("res/creatures/OrcRaider.txt"));
		
		AppGameContainer gc = new AppGameContainer(l,1000,600,false);
		
		gc.setTargetFrameRate(120);
		
		gc.start();
	}
}
