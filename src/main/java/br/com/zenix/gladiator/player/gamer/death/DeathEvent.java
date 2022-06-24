package br.com.zenix.gladiator.player.gamer.death;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.warps.type.WarpType;

public class DeathEvent extends GladiatorListener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage("");

		Player dead = event.getEntity();
		Gamer deadGamer = getManager().getGamerManager().getGamer(dead.getUniqueId());
		dead.setHealth(20.0D);
		event.getDrops().clear();

		deadGamer.setCombat(0);

		getManager().getConfigManager().teleportPlayer(dead, WarpType.SPAWN);
		return;

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();

		Gamer gamer = getManager().getGamerManager().getGamer(event.getPlayer());

		if (gamer.getWarp().equals(WarpType.SPAWN)) {
			getManager().getConfigManager().teleportPlayer(player, WarpType.SPAWN);
			return;
		}
	}

}
