package br.com.zenix.gladiator.player.gamer.interact;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import br.com.zenix.gladiator.commands.base.GladiatorListener;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class InteractListener extends GladiatorListener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerInteract(PlayerInteractEvent event) {

		if (event.getItem() == null)
			return;

		Player player = event.getPlayer();

		ItemStack item = event.getItem();
		Damageable entityDamageable = player;
		if (item.getType().equals(Material.MUSHROOM_SOUP) && event.getAction().name().contains("RIGHT")) {
			event.setCancelled(true);

			if (entityDamageable.getHealth() != entityDamageable.getMaxHealth()) {
				entityDamageable.setHealth((entityDamageable.getHealth() + 7.0 > entityDamageable.getMaxHealth())
						? entityDamageable.getMaxHealth() : (entityDamageable.getHealth() + 7.0));
				player.setItemInHand(new ItemStack(Material.BOWL));
			}
		}
	}
}
