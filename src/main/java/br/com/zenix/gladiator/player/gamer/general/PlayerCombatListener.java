package br.com.zenix.gladiator.player.gamer.general;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.gamer.Gamer;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class PlayerCombatListener extends GladiatorListener {

	@EventHandler
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Gamer dataEntity = getManager().getGamerManager().getGamer(event.getEntity().getUniqueId());
			Gamer dataDamager = getManager().getGamerManager().getGamer(event.getDamager().getUniqueId());

			if (!dataEntity.hasPvP() || !dataDamager.hasPvP()) {
				event.setCancelled(true);
				return;
			}

			if (!dataEntity.inCombat()) {
				dataEntity.setCombat(10);
			}

			if (!dataDamager.inCombat())
				dataDamager.setCombat(10);

		} else if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

			if (!gamer.hasPvP()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

			if (!gamer.hasPvP()) {
				event.setCancelled(true);
			}
		}
	}
}
