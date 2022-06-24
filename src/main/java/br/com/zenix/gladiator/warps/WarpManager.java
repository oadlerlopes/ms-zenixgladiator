package br.com.zenix.gladiator.warps;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import br.com.zenix.gladiator.battle.gladiator.managements.containers.GladiatorMode;
import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.managers.managements.constructor.Management;
import br.com.zenix.gladiator.warps.constructor.ItemConstructor;
import br.com.zenix.gladiator.warps.type.WarpType;

public class WarpManager extends Management {

	public WarpManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		return true;
	}

	public void teleport(Player player, WarpType warp) {
		Gamer gamer = getManager().getGamerManager().getGamer(player);

		if (gamer.inCombat())
			return;

		gamer.setWarp(warp);

		getManager().getGamerManager().resetPlayer(player);
		getManager().getGamerManager().removePottionEffects(player);
		getManager().getConfigManager().teleportPlayer(player, WarpType.SPAWN);

		new ItemConstructor(getManager(), player, warp);

		player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 4.0F, 4.0F);
		player.setLevel(-5000);

		for (Player players : Bukkit.getOnlinePlayers()) {
			if (getManager().getGladiatorManager().getFight().isFighting(players.getUniqueId())) {
				players.hidePlayer(player);
			}
			if (getManager().getGamerManager().getGamer(players).isSpectate()) {
				players.hidePlayer(player);
			}
		}

	}

	public void teleportPlayer(final Player player, final WarpType warp) {
		Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

		if (gamer.getWarp().equals(WarpType.SPAWN) && new GladiatorMode().isFighting(player.getUniqueId()))
			return;

		if (gamer.inCombat())
			return;

		if (gamer.getWarp().equals(warp)) {
			teleport(player, WarpType.SPAWN);
			return;
		}

		teleport(player, warp);

		getManager().getCoreManager().getAccountManager().getAccount(player).setScoreboardHandler(null);
		getManager().getCoreManager().getAccountManager().getAccount(player).setScoreboardHandler(null);
	}

}
