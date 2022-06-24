package br.com.zenix.gladiator.battle.gladiator.managements.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.core.spigot.bo2.BO2Constructor.FutureBlock;
import br.com.zenix.gladiator.battle.gladiator.listeners.PlayerBlockChange;
import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.warps.constructor.ItemConstructor;
import br.com.zenix.gladiator.warps.type.WarpType;

/**
 * Copyright (C) Guilherme Fane, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */

public class Arena extends GladiatorListener {

	public Arena() {
	}

	private Player gladiator;
	private Player target;
	private Location tpLocationGladiator;
	private Location tpLocationTarget;
	private BukkitRunnable witherEffect;
	private List<Block> blocksToRemove;
	private Listener listener;

	private Block mainBlock;

	private int time, timePassed;
	private boolean active = true;

	public Location getTpLocationGladiator() {
		return tpLocationGladiator;
	}

	public Location getTpLocationTarget() {
		return tpLocationTarget;
	}

	public Arena(Player gladiator, Player target, int time) {

		this.gladiator = gladiator;
		this.target = target;
		this.time = time;
		this.blocksToRemove = new ArrayList<>();

		sendGladiator();
		startCleaner();

		this.listener = new Listener() {

			@EventHandler(priority = EventPriority.LOWEST)
			public void onDeath(PlayerDeathEvent event) {
				Player p = event.getEntity();
				if (!inGladiator(p)) {
					return;
				}
				if (p == gladiator) {
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 12500));
					event.getDrops().clear();
					deleteBattle(target, gladiator);
					return;
				}
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 12500));
				event.getDrops().clear();
				deleteBattle(gladiator, target);
			}

			@EventHandler
			public void onQuit(PlayerQuitEvent event) {
				Player p = event.getPlayer();
				if (!inGladiator(p)) {
					return;
				}
				if (event.getPlayer().isDead()) {
					return;
				}
				if (p == gladiator) {
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 12500));
					deleteBattle(target, gladiator);
					return;
				}
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 12500));
				deleteBattle(gladiator, target);
			}

			@EventHandler
			public void onKick(PlayerKickEvent event) {
				Player p = event.getPlayer();
				if (!inGladiator(p)) {
					return;
				}
				if (event.getPlayer().isDead()) {
					return;
				}
				if (p == gladiator) {
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 12500));
					deleteBattle(target, gladiator);
					return;
				}
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 12500));
				deleteBattle(gladiator, target);
			}

		};
		getManager().registerListener(listener);
	}

	public void startCleaner() {
		new BukkitRunnable() {
			public void run() {

				if (timePassed == time) {
					cleanArena();
					timePassed = 0;
				}

				if (!active)
					cancel();

				timePassed++;

			}
		}.runTaskTimer(getManager().getPlugin(), 20L, 20L);
	}

	public void cleanArena() {
		for (double x = -16.0D; x <= 16.0D; x += 1.0D) {
			for (double z = -16.0D; z <= 16.0D; z += 1.0D) {
				for (double y = 0.0D; y <= 15.0D; y += 1.0D) {
					Block block = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 125 + y, mainBlock.getZ() + z).getBlock();
					if (block.getType() != Material.STAINED_GLASS && block.getType() != Material.GLASS) {
						block.setType(Material.AIR);
					}
				}
			}
		}
	}

	public boolean inGladiator(Player player) {
		return (player == this.gladiator) || (player == this.target);
	}

	public void destroy() {
		active = false;

		HandlerList.unregisterAll(this.listener);
	}

	public void sendGladiator() {
		Location loc = new Location(Bukkit.getWorld("gladiator"), getManager().getXLocation() + 150, 125.0D, getManager().getZLocation() + 150);
		getManager().setXLocation(getManager().getXLocation() + 150);
		getManager().setZLocation(getManager().getZLocation() + 150);

		Block mainBlock = loc.getBlock();
		mainBlock.getLocation().getChunk().load();

		generateArena(mainBlock);

		this.tpLocationGladiator = this.gladiator.getLocation().clone();
		this.tpLocationTarget = this.target.getLocation().clone();
		Location l1 = new Location(mainBlock.getWorld(), mainBlock.getX() + 6.5D, 128.0D, mainBlock.getZ() + 6.5D);
		l1.setYaw(135.0F);
		Location l2 = new Location(mainBlock.getWorld(), mainBlock.getX() - 5.5D, 128.0D, mainBlock.getZ() - 5.5D);
		l2.setYaw(315.0F);

		mainBlock.getLocation().getChunk().load();

		this.target.teleport(l1);
		this.gladiator.teleport(l2);

		PlayerBlockChange.playersInGladiator.add(this.gladiator.getUniqueId());
		PlayerBlockChange.playersInGladiator.add(this.target.getUniqueId());

		this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 12500));
		this.target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 12500));

		this.witherEffect = new BukkitRunnable() {
			public void run() {
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1200, 5));
				target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1200, 5));
			}
		};
		this.witherEffect.runTaskLater(getManager().getPlugin(), 6800L);

	}

	private void cleanUp(final Player player) {
		new BukkitRunnable() {
			public void run() {
				player.closeInventory();
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				player.updateInventory();

				getManager().getGamerManager().resetPlayer(player);
				getManager().getGamerManager().removePottionEffects(player);
				getManager().getWarpTeleport().teleport(player, WarpType.SPAWN);

				player.playSound(player.getLocation(), Sound.LEVEL_UP, 4.0F, 4.0F);

				new ItemConstructor(getManager(), player, WarpType.SPAWN);
				getManager().getGladiatorManager().getFight().removeFight(player.getUniqueId());
				
				getManager().getHologramManager().scoreHologram.hide(player);
				getManager().getHologramManager().scoreHologram.show(player);
				
				getManager().getHologramManager().scoreHologramDeaths.hide(player);
				getManager().getHologramManager().scoreHologramDeaths.show(player);
				
				getManager().getHologramManager().scoreHologramMKS.hide(player);
				getManager().getHologramManager().scoreHologramMKS.show(player);
			}
		}.runTaskLater(getManager().getPlugin(), 4L);
		
		new BukkitRunnable() {
			public void run() {
				getManager().getHologramManager().scoreHologram.hide(player);
				getManager().getHologramManager().scoreHologram.show(player);
				
				getManager().getHologramManager().scoreHologramDeaths.hide(player);
				getManager().getHologramManager().scoreHologramDeaths.show(player);
				
				getManager().getHologramManager().scoreHologramMKS.hide(player);
				getManager().getHologramManager().scoreHologramMKS.show(player);
			}
		}.runTaskLater(getManager().getPlugin(), 8L);
	}

	public void teleportBack(Player winner, Player loser) {
		cleanUp(winner);
		cleanUp(loser);
		PlayerBlockChange.playersInGladiator.remove(winner.getUniqueId());
		PlayerBlockChange.playersInGladiator.remove(loser.getUniqueId());

		for (Block s : getManager().getGamerManager().getGamer(winner).getBlocks()) {
			if (this.blocksToRemove.contains(s)) {
				this.blocksToRemove.remove(s);
			}
			if (blocksToRemove.contains(s)) {
				blocksToRemove.remove(s);
			}
			if (PlayerBlockChange.gladiatorBlocks.contains(s)) {
				PlayerBlockChange.gladiatorBlocks.remove(s);
			}
		}

		getManager().getGamerManager().getGamer(winner).getBlocks().clear();

		removeBlocks();
		stop();
		destroy();
	}

	public void deleteBattle(Player winner, Player loser) {
		PlayerBlockChange.playersInGladiator.remove(winner.getUniqueId());
		PlayerBlockChange.playersInGladiator.remove(loser.getUniqueId());
		removeBlocks();
		stop();
		destroy();
	}

	@SuppressWarnings("deprecation")
	public void generateArena(Block mainBlock) {
		this.mainBlock = mainBlock;

		Location l = mainBlock.getLocation();
		l.setY(125.0D);

		for (FutureBlock fb : getManager().getCoreManager().getBO2().load(l, new File(Bukkit.getWorldContainer().getAbsoluteFile() + "/plugins/Gladiator/Gladiator" + new Random().nextInt(8) + ".bo2"))) {
			fb.getLocation().getBlock().setTypeIdAndData(fb.getId(), fb.getData(), true);
			
			PlayerBlockChange.gladiatorBlocks.add(fb.getLocation().getBlock());

			this.blocksToRemove.add(fb.getLocation().getBlock());
		}

		for (double x = -12.0D; x <= 10.0D; x += 1.0D) {
			for (double z = -11.0D; z <= 11.0D; z += 1.0D) {
				for (double y = 1.0D; y <= 12.0D; y += 1.0D) {
					Location loc = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 125.0D, mainBlock.getZ() + z);
					this.blocksToRemove.add(loc.getBlock());
				}
			}
		}
	}

	public void removeBlocks() {
		deleteStructure();
	}

	public void deleteStructure() {
		this.blocksToRemove.clear();
	}

	public void stop() {
		this.witherEffect.cancel();
	}

}