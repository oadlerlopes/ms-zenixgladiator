package br.com.zenix.gladiator.player.gamer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;

import br.com.zenix.core.spigot.commands.base.MessagesConstructor;
import br.com.zenix.core.spigot.player.account.Account;
import br.com.zenix.core.spigot.player.events.ServerTimeEvent;
import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.warps.type.WarpType;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */

public class GamerListener extends GladiatorListener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void login(PlayerLoginEvent event) {
		if (event.getResult() != org.bukkit.event.player.PlayerLoginEvent.Result.ALLOWED)
			return;

		Player player = event.getPlayer();
		Gamer gamer = new Gamer(getManager().getCoreManager().getAccountManager().getAccount(player));
		getManager().getGamerManager().addGamer(gamer);

		getManager().getGamerManager().getLogger().log(
				"The player with uuid " + player.getUniqueId() + "(" + player.getName() + ") was loaded correctly.");
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);

		Player player = event.getPlayer();
		Gamer gamer = getManager().getGamerManager().getGamer(player);
		gamer.update();

		getManager().getAdminManager().setPlayerQuit(player);
	}

	@EventHandler
	public void onServerGamer(ServerTimeEvent event) {
		for (Player players : Bukkit.getOnlinePlayers()) {
			getManager().getGamerManager().updateTab(getManager().getGamerManager().getGamer(players));
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
		playerJoinEvent.setJoinMessage(null);

		Player player = playerJoinEvent.getPlayer();
		Gamer gamer = getManager().getGamerManager().getGamer(player);

		player.teleport(new Location(Bukkit.getWorld("world"), -751, 15, -95));

		getManager().getGamerManager().removePottionEffects(player);
		getManager().getGamerManager().resetPlayer(player);

		getManager().getGamerManager().updateTab(gamer);

		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);

		player.updateInventory();
		player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 4.0F, 4.0F);

		StringBuilder stringTitle = new StringBuilder();
		stringTitle.append("§6§lGLADIATOR");

		StringBuilder stringSubTitle = new StringBuilder();
		stringSubTitle.append("§9§lBatalhe contra outros players!".toUpperCase());

		MessagesConstructor.sendTitleMessage(player, stringTitle.toString(), stringSubTitle.toString());

		player.sendMessage(" ");
		player.sendMessage("§6§lZENIX GLADIATOR");
		player.sendMessage(" ");
		player.sendMessage("§fEscolha um player e o §6§lDESAFIE§f para a §e§lBATALHA");
		player.sendMessage("§fem uma §9§lARENA GLADIATOR! §f");
		player.sendMessage(" ");
		player.sendMessage("§9§lTENHA UM BOM JOGO!");
		player.sendMessage(" ");
		
		getManager().getGamerManager().removePottionEffects(player);
		getManager().getGamerManager().resetPlayer(player);

		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		
		getManager().getWarpTeleport().teleport(player, WarpType.SPAWN);

		if (player.hasPermission("zenix.beta.2x2")) {
			Account account = getManager().getCoreManager().getAccountManager().getAccount(player);
			account.setDoubleRunning(true);
		}

		if (!getManager().getAdminManager().isAdmin(player)) {
			if (player.hasPermission("commons.cmd.moderate")) {
				player.chat("/admin");
			}
		}

		for (Player players : Bukkit.getOnlinePlayers()) {
			if (getManager().getAdminManager().isAdmin(players)) {
				if (!player.hasPermission("commons.admin")) {
					player.hidePlayer(players);
				}
			}
			if (getManager().getGladiatorManager().getFight().isFighting(players.getUniqueId())) {
				players.hidePlayer(player);
			}
			if (getManager().getGamerManager().getGamer(players).isSpectate()) {
				players.hidePlayer(player);
			}
		}

	}
}
