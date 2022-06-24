package br.com.zenix.gladiator.player.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import br.com.zenix.gladiator.commands.base.GladiatorListener;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class AdminListener extends GladiatorListener {

	@EventHandler
	private void onCancelBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (getManager().getAdminManager().isAdmin(player) && !player.hasPermission("pvp.cmd.admin"))
			event.setCancelled(true);
	}

	@EventHandler
	private void onInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Player) {
			Player player = event.getPlayer();

			if (getManager().getAdminManager().isAdmin(player)) {
				Player clicked = (Player) event.getRightClicked();
				ItemStack item = player.getInventory().getItemInHand();

				if (item.getType().equals(Material.AIR)) {
					player.performCommand("invsee " + clicked.getName());
				}
			}
		}
	}

	@EventHandler
	private void onInteract(PlayerInteractEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInHand();
		if (item == null)
			return;
		if (item.getType().equals(Material.MAGMA_CREAM)) {
			final Player player = event.getPlayer();
			if (getManager().getAdminManager().isAdmin(player)) {
				player.chat("/admin");
			}
		}
	}
}
