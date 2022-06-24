package br.com.zenix.gladiator.battle.gladiator.modules;

public enum BattleModules {

	NORMAL("Normal", 8),
	FAST("Fast", 8),
	CUSTOM("Customizado", 8);

	private int soups;
	private String name;

	BattleModules(String name, int soups) {
		this.soups = soups;
		this.name = name;
	}

	public int getSoups() {
		return soups;
	}

	public String getName() {
		return name;
	}

}
