package br.com.zenix.gladiator.player.gamer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.gamer.Gamer;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class GamerSpawnEvents extends GladiatorListener {

	@EventHandler
	private void onPlayerDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();

		Gamer gamer = getManager().getGamerManager().getGamer(player);

		if (!gamer.hasPvP()) {
			event.setCancelled(true);
			cancelPlayerDrop(player);
		}
	}

	public void cancelPlayerDrop(Player player) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getManager().getPlugin(), new Runnable() {
			public void run() {
				player.updateInventory();
				player.updateInventory();
				player.updateInventory();
				player.updateInventory();
				player.updateInventory();
			}
		}, 1L);
	}

	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

		if (getManager().getAdminManager().isAdmin(player)) {
			event.setCancelled(true);
			return;
		}
		if (!gamer.hasPvP()) {
			event.setCancelled(true);
			cancelPlayerDrop(event.getPlayer());
		}
	}

	@EventHandler
	private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String message = event.getMessage();

		if (message.equals("/kill")) {
			event.setCancelled(true);
			return;
		}
	}
}
