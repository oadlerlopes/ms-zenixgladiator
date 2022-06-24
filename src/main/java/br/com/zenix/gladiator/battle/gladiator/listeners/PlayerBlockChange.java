package br.com.zenix.gladiator.battle.gladiator.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import br.com.zenix.gladiator.battle.gladiator.managements.containers.GladiatorMode;
import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.gamer.Gamer;

public class PlayerBlockChange extends GladiatorListener {

	public static List<UUID> playersInGladiator = new ArrayList<UUID>();
	public static List<Block> gladiatorBlocks = new ArrayList<Block>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlock(BlockDamageEvent event) {
		GladiatorMode gladiatorMode = getManager().getGladiatorManager().getFight();
		if (gladiatorMode.isFighting(event.getPlayer().getUniqueId())) {
			Block b = event.getBlock();
			Player p = event.getPlayer();
			if (b.getType() == Material.GLASS) {
				p.sendBlockChange(b.getLocation(), Material.BEDROCK, (byte) 0);
			}
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		Iterator<Block> blockIt = event.blockList().iterator();
		while (blockIt.hasNext()) {
			Block b = (Block) blockIt.next();
			if (gladiatorBlocks.contains(b)) {
				blockIt.remove();
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (gladiatorBlocks.contains(event.getBlock())) {
			event.setCancelled(true);
		}
		GladiatorMode fight = getManager().getGladiatorManager().getFight();
		if (!fight.isFighting(event.getPlayer().getUniqueId())) {
			if (event.getPlayer().hasPermission("gladiator.*") && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}
		} else {
			Gamer gamer = getManager().getGamerManager().getGamer(event.getPlayer());
			gamer.removeBlocks(event.getBlock());
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		GladiatorMode fight = getManager().getGladiatorManager().getFight();
		if (!fight.isFighting(event.getPlayer().getUniqueId())) {
			if (event.getPlayer().hasPermission("gladiator.*") && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}
		} else {
			Gamer gamer = getManager().getGamerManager().getGamer(event.getPlayer());
			gamer.addBlocks(event.getBlock());
		}
	}

	public static boolean inGladiator(Player p) {
		return playersInGladiator.contains(p.getUniqueId());
	}

}
