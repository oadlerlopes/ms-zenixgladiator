package br.com.zenix.gladiator.battle.gladiator.managements;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.gladiator.Gladiator;
import br.com.zenix.gladiator.battle.gladiator.listeners.PlayerInviteToMatch;
import br.com.zenix.gladiator.battle.gladiator.managements.arena.Arena;
import br.com.zenix.gladiator.battle.gladiator.modules.BattleModules;
import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.player.item.ItemsCache;
import br.com.zenix.gladiator.utilitaries.armor.ArmorUtilitaries;
import br.com.zenix.gladiator.utilitaries.item.ItemBuilder;

public class RunnableStartingMatch {

	private Manager manager;

	private Player player1, player2;

	private BattleModules battleModules;

	public RunnableStartingMatch() {
		this.manager = Gladiator.getManager();
	}

	public RunnableStartingMatch(Player player1, Player player2, BattleModules battleModules) {
		this.manager = Gladiator.getManager();
		this.player1 = player1;
		this.player2 = player2;
		this.battleModules = battleModules;
		start();

		for (Player player : Bukkit.getOnlinePlayers()) {
			player1.hidePlayer(player);
			player2.hidePlayer(player);
			if (manager.getGladiatorManager().getFight().isFighting(player.getUniqueId())) {
				player.hidePlayer(player1);
				player.hidePlayer(player2);
			}
			if (manager.getGamerManager().getGamer(player).isSpectate()) {
				player1.hidePlayer(player);
				player2.hidePlayer(player);
				player.hidePlayer(player1);
				player.hidePlayer(player2);
			}
		}

		player2.showPlayer(player1);
		player1.showPlayer(player2);

		player2.showPlayer(player1);
		player1.showPlayer(player2);
	}

	private void prepare(Player player) {
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);

		manager.getGladiatorManager().getFight().removeInvitation(player);

		Gladiator.getManager().getGamerManager().resetPlayer(player);
	}

	private int getSoups() {
		return battleModules.getSoups();
	}

	@SuppressWarnings("deprecation")
	private void giveItems(Player player) {
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setExp(0.0f);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.closeInventory();

		ItemsCache.DEFAULT_SWORD.getItem(0).setMaterial(Material.DIAMOND_SWORD).setUnbreakable().setEnchant(Enchantment.DAMAGE_ALL, 1).setName("§aDiamond Sword").build(player);

		new ItemBuilder(Material.LAVA_BUCKET).setAmount(1).build(player);
		new ItemBuilder(Material.WATER_BUCKET).setAmount(1).build(player);

		for (int i = 1; i < 5; i++) {
			new ItemBuilder(Material.MUSHROOM_SOUP).setAmount(1).build(player);
		}

		new ItemBuilder(Material.getMaterial(17)).setAmount(32).build(player);
		new ItemBuilder(Material.getMaterial(139)).setAmount(64).build(player);

		new ItemBuilder(Material.IRON_HELMET).build(player);
		new ItemBuilder(Material.IRON_CHESTPLATE).build(player);
		new ItemBuilder(Material.IRON_LEGGINGS).build(player);
		new ItemBuilder(Material.IRON_BOOTS).build(player);
		new ItemBuilder(Material.BOWL).setAmount(64).build(player);
		new ItemBuilder(Material.getMaterial(351)).setDurability(3).setAmount(64).build(player);
		new ItemBuilder(Material.getMaterial(351)).setDurability(3).setAmount(64).build(player);
		new ItemBuilder(Material.getMaterial(351)).setDurability(3).setAmount(64).build(player);
		new ItemBuilder(Material.STONE_PICKAXE).build(player);
		new ItemBuilder(Material.IRON_HELMET).build(player);
		new ItemBuilder(Material.IRON_CHESTPLATE).build(player);
		new ItemBuilder(Material.IRON_LEGGINGS).build(player);
		new ItemBuilder(Material.IRON_BOOTS).build(player);
		new ItemBuilder(Material.BOWL).setAmount(64).build(player);
		new ItemBuilder(Material.getMaterial(351)).setDurability(3).setAmount(64).build(player);
		new ItemBuilder(Material.getMaterial(351)).setDurability(3).setAmount(64).build(player);
		new ItemBuilder(Material.getMaterial(351)).setDurability(3).setAmount(64).build(player);
		new ItemBuilder(Material.STONE_AXE).build(player);
		new ItemBuilder(Material.LAVA_BUCKET).setAmount(1).build(player);
		new ItemBuilder(Material.LAVA_BUCKET).setAmount(1).build(player);
		new ItemBuilder(Material.WATER_BUCKET).setAmount(1).build(player);

		for (int i = 1; i < 36; i++) {
			new ItemBuilder(Material.MUSHROOM_SOUP).setAmount(1).build(player);
		}

		ArmorUtilitaries.updateArmor(player, "IRON", true);

		manager.getGamerManager().getGamer(player).setPvP(true);

		Gamer gamer = manager.getGamerManager().getGamer(player);
		gamer.setPvP(true);
	}

	private void sendMessage(Player player, Player p2) {
		player.sendMessage("§b§l1V1 §fVocê irá lutar com o player §b" + p2.getName() + "§7!");
	}

	private void start() {
		manager.getGladiatorManager().getFight().addFight(player1, player2, battleModules, getSoups());

		new Arena(player1, player2, manager.getGamerManager().getGamer(player1).getTimeToClear());

		prepare(player1);
		prepare(player2);
		giveItems(player1);
		giveItems(player2);
		sendMessage(player1, player2);
		sendMessage(player2, player1);

		PlayerInviteToMatch.getFastPlayers().remove(player1.getUniqueId());
		PlayerInviteToMatch.getFastPlayers().remove(player2.getUniqueId());

		manager.getGamerManager().getGamer(player1).getAccount().setScoreboardHandler(null);
		manager.getGamerManager().getGamer(player2).getAccount().setScoreboardHandler(null);
		manager.getVanish().updateVanished();

		for (Player players : Bukkit.getOnlinePlayers()) {

			player1.getLocation().getChunk().getWorld().refreshChunk(player1.getLocation().getBlockX(), player1.getLocation().getBlockZ());
			player2.getLocation().getChunk().getWorld().refreshChunk(player2.getLocation().getBlockX(), player2.getLocation().getBlockZ());

			players.showPlayer(player1);
			players.showPlayer(player2);
			if (!manager.getAdminManager().isAdmin(players)) {
				player1.showPlayer(players);
				player2.showPlayer(players);
			} else {
				player1.hidePlayer(players);
				player2.hidePlayer(players);
			}
		}

		if (battleModules.equals(BattleModules.CUSTOM)) {
			new BukkitRunnable() {

				public void run() {
					for (Block s : Gladiator.getManager().getGamerManager().getGamer(player1).getBlocks()) {
						s.setType(Material.AIR);
					}
					for (Block s : Gladiator.getManager().getGamerManager().getGamer(player2).getBlocks()) {
						s.setType(Material.AIR);
					}
				}
			}.runTaskTimer(Gladiator.getManager().getPlugin(), 0L, 0L);
		}

	}

}
