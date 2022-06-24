package br.com.zenix.gladiator.battle.gladiator.managements.constructor;

import java.util.UUID;

import org.bukkit.entity.Player;

import br.com.zenix.gladiator.battle.gladiator.managements.custom.Custom;
import br.com.zenix.gladiator.battle.gladiator.modules.BattleModules;

public class BattleConstructor {

	private BattleModules battleModules;

	private int soups;

	private Player opponent;

	private Custom custom;

	public BattleConstructor(BattleModules battleModules, int soups, Player opponent, Custom custom) {
		this.battleModules = battleModules;
		this.soups = soups;
		this.opponent = opponent;
		this.custom = custom;
	}

	public BattleModules getType() {
		return battleModules;
	}

	public int getSoups() {
		return soups;
	}

	public Player getOpponent() {
		return opponent;
	}

	public UUID getOpponentUUID() {
		return opponent.getUniqueId();
	}

	public Custom getCustom() {
		return custom;
	}

}
