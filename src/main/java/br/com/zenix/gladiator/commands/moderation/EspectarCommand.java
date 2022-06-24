package br.com.zenix.gladiator.commands.moderation;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.gladiator.battle.gladiator.managements.containers.GladiatorMode;
import br.com.zenix.gladiator.commands.base.GladiatorCommand;
import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.warps.type.WarpType;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class EspectarCommand extends GladiatorCommand {

	public EspectarCommand() {
		super("espectar");
	}

	@Override
	public boolean execute(CommandSender commandSender, String label, String[] args) {
		if (!isPlayer(commandSender)) {
			return false;
		}

		if (!hasPermission(commandSender, "espectar")) {
			return false;
		}

		Player player = (Player) commandSender;

		if (args.length != 1) {
			player.sendMessage("§b§lESPECTAR §fUse: /espectar <nick>");
			return false;
		}

		Player toSpec = Bukkit.getPlayer(args[0]);
		Gamer gamer = getManager().getGamerManager().getGamer(player);
		if (toSpec == null) {
			sendOfflinePlayerMessage(commandSender, args[0]);
			return false;
		}

		if (!getManager().getAdminManager().isAdmin(player)) {
			player.sendMessage("§b§lESPECTAR §fVocê não está no ADMIN");
			return false;
		}

		GladiatorMode fight = getManager().getGladiatorManager().getFight();

		if (!fight.isFighting(toSpec.getUniqueId())) {
			player.sendMessage("§b§lESPECTAR §fO player não está em nenhuma batalha.");
			return false;
		}

		if (gamer.isSpectate()) {
			player.sendMessage("§b§lESPECTAR §fVocê já está ESPECTANDO um player!");
			return false;
		}

		gamer.setSpectate(true);
		gamer.setSpectatePlayer(toSpec);

		player.sendMessage("§b§lESPECTAR §fVocê agora está observando a luta!");

		for (Player players : Bukkit.getOnlinePlayers()) {
			player.hidePlayer(players);
			player.showPlayer(toSpec);
			player.showPlayer(fight.getBattle(toSpec.getUniqueId()).getOpponent());
			player.teleport(toSpec);
		}

		new BukkitRunnable() {

			public void run() {
				if (gamer.isSpectate() == true) {

					if (!fight.isFighting(gamer.getSpectatePlayer().getUniqueId()) || !fight.isFighting(
							fight.getBattle(gamer.getSpectatePlayer().getUniqueId()).getOpponent().getUniqueId())
							|| !gamer.getSpectatePlayer().isOnline()) {
						gamer.setSpectate(false);
						gamer.setSpectatePlayer(null);
						player.sendMessage("§b§lESPECTADORES §fOs dois acabaram a luta!");
						getManager().getConfigManager().teleportPlayer(player, WarpType.SPAWN);
						for (Player players : Bukkit.getOnlinePlayers()) {
							player.showPlayer(players);
							if (getManager().getGladiatorManager().getFight().isFighting(players.getUniqueId())) {
								players.hidePlayer(player);
							}
							if (getManager().getGamerManager().getGamer(players).isSpectate()) {
								players.hidePlayer(player);
							}
							if (getManager().getAdminManager().isAdmin(players)) {
								if (!player.hasPermission("commons.cmd.bypass")) {
									player.hidePlayer(players);
								}
							}
						}
						cancel();
					}
				}
			}
		}.runTaskTimer(getManager().getPlugin(), 0L, 20L);

		return true;
	}

}
