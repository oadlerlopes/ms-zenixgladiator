package br.com.zenix.gladiator.battle.gladiator.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import br.com.zenix.gladiator.battle.gladiator.managements.containers.GladiatorMode;
import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.warps.type.WarpType;

public class PlayerDamageToPlayers extends GladiatorListener {

	@EventHandler
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

		if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
			return;

		GladiatorMode gladiatorMode = getManager().getGladiatorManager().getFight();

		Entity entity = event.getEntity();
		Entity damager = event.getDamager();

		Player playerEntity = (Player) entity;
		Player playerDamager = (Player) damager;

		Gamer gamerEntity = getManager().getGamerManager().getGamer(playerEntity);
		if (gamerEntity.getWarp().equals(WarpType.SPAWN)) {
			if (!gladiatorMode.isFighting(gamerEntity.getUniqueId())) {
				event.setCancelled(true);
			} else if (!gladiatorMode.getFighters().get(gamerEntity.getUniqueId()).getOpponentUUID()
					.equals(damager.getUniqueId())) {
				event.setCancelled(true);
			}
		}
		
		Gamer gamerDamager = getManager().getGamerManager().getGamer(playerDamager);
		if (gamerDamager.getWarp().equals(WarpType.SPAWN)) {
			if (!gladiatorMode.isFighting(gamerDamager.getUniqueId())) {
				event.setCancelled(true);
			} else if (!gladiatorMode.getFighters().get(gamerDamager.getUniqueId()).getOpponentUUID().equals(entity.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

}
