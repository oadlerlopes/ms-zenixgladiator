package br.com.zenix.gladiator.battle;

import org.bukkit.entity.Player;

import br.com.zenix.gladiator.battle.gladiator.managements.constructor.BattleConstructor;
import br.com.zenix.gladiator.battle.gladiator.managements.containers.GladiatorMode;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.managers.managements.constructor.Management;

public class BattleManager extends Management {

	private final GladiatorMode gladiatorMode = new GladiatorMode();

	public BattleManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		return true;
	}

	public GladiatorMode getFight() {
		return gladiatorMode;
	}

	public String getFighter(Player player) {
		if (!getFight().isFighting(player.getUniqueId())) {
			return "Ninguém";
		}

		GladiatorMode sujectFight = getFight();
		BattleConstructor sujectBattle = sujectFight.getBattle(player.getUniqueId());
		String toTranslate = sujectBattle.getOpponent().getName();

		String finalResult = "NOT-FOUND";
		if (finalResult.equals("NOT-FOUND")) {
			if (toTranslate.length() == 16) {
				String shorts = toTranslate.substring(0, toTranslate.length() - 2);
				finalResult = shorts;
			} else if (toTranslate.length() == 15) {
				String shorts = toTranslate.substring(0, toTranslate.length() - 1);
				finalResult = shorts;
			} else {
				finalResult = toTranslate;
			}
		}

		return finalResult;
	}

}
