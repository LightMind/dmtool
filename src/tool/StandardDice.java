package tool;

import creature.Diceroll;

public class StandardDice implements Diceroll {
	
	private final int dice,amount;
	
	public StandardDice(int d, int amount){
		this.amount = amount;
		this.dice = d;
	}
	
	@Override
	public int getDice() {
		return dice;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "StandardDice [dice= " + amount + "d" + dice + "]";
	}
	
	

}
