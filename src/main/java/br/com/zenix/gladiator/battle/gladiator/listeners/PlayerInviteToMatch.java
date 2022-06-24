package br.com.zenix.gladiator.battle.gladiator.listeners;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.gladiator.battle.gladiator.managements.RunnableStartingMatch;
import br.com.zenix.gladiator.battle.gladiator.managements.containers.GladiatorMode;
import br.com.zenix.gladiator.battle.gladiator.modules.BattleModules;
import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.utilitaries.item.ItemBuilder;
import br.com.zenix.gladiator.warps.type.WarpType;

public class PlayerInviteToMatch extends GladiatorListener {

	private static ArrayList<UUID> fast = new ArrayList<>();

	public static ArrayList<UUID> getFastPlayers() {
		return fast;
	}

	@EventHandler
	private void onInteract(PlayerInteractEntityEvent event) {
		if (!(event.getRightClicked() instanceof Player))
			return;

		Player player = event.getPlayer();
		Player player2 = (Player) event.getRightClicked();

		Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

		if (gamer.getWarp().equals(WarpType.SPAWN)) {

			ItemStack item = event.getPlayer().getItemInHand();

			if (item.getType().equals(Material.IRON_FENCE) || item.getType().equals(Material.PAPER) || item.getType().equals(Material.BONE)) {

				GladiatorMode gladiatorMode = getManager().getGladiatorManager().getFight();

				if (gladiatorMode.isFighting(player.getUniqueId()) || !getManager().getGamerManager().getGamer(player2).getWarp().equals(WarpType.SPAWN))
					return;

				if (gladiatorMode.isFighting(player2.getUniqueId())) {
					player.sendMessage("§c§lERRO §fO player §n" + player2.getName() + "§f já está batalhando!");
					return;
				}

				if (gladiatorMode.recivedInvitation(player.getUniqueId(), player2.getUniqueId())) {
					BattleModules battleModules = gladiatorMode.getInvitation(player2.getUniqueId()).getBattleType();
					if ((battleModules.equals(BattleModules.NORMAL) && item.getType().equals(Material.IRON_FENCE))) {
						fast.remove(player.getUniqueId());
						new ItemBuilder(Material.INK_SACK).setName("§aDesafiar para Gladiator §7(Clique)").setDurability(8).build(player.getInventory(), getManager().getUtils().findItem(player.getInventory(), Material.INK_SACK));
						fast.remove(player2.getUniqueId());
						new ItemBuilder(Material.INK_SACK).setName("§aDesafiar para Gladiator §7(Clique)").setDurability(8).build(player2.getInventory(), getManager().getUtils().findItem(player2.getInventory(), Material.INK_SACK));
						new RunnableStartingMatch(player, player2, gladiatorMode.getInvitations().get(player2.getUniqueId()).getBattleType());
						return;
					}
				}

				/* Check if the player don't have wait time */

				if (gamer.inWaitTime()) {
					player.sendMessage("§c§lCOOLDOWN §fAguarde para poder desafiar novamente!");
					return;
				}

				/* Challenging for a battle */

				BattleModules battleModules = item.getType().equals(Material.IRON_FENCE) ? BattleModules.NORMAL : BattleModules.NORMAL;

				gladiatorMode.addInvitation(player, player2, battleModules);
				player.sendMessage("§3§lDESAFIO §fVocê §3§lENVIOU§f um desafio para O player §b" + player2.getName() + "§f!");
				player2.sendMessage("§3§lDESAFIO §fO player §b" + player.getName() + "§f te §b§lDESAFIOU§f!");

				gamer.addWait(6);
			} else if (item.getType().equals(Material.PISTON_BASE)) {

				player.sendMessage("§c§lERRO §fEste modo de gladiator estará disponível em breve.");
				/***
				
				if (gamer.getTimeToClear() == 0) {
					player.sendMessage("§c§lERRO §fVocê ainda não customizou nada do seu match!");
					return;
				}

				GladiatorMode gladiatorMode = getManager().getGladiatorManager().getFight();

				if (gladiatorMode.isFighting(player.getUniqueId()) || !getManager().getGamerManager().getGamer(player2).getWarp().equals(WarpType.SPAWN))
					return;

				if (gladiatorMode.isFighting(player2.getUniqueId())) {
					player.sendMessage("§c§lERRO §fO player §n" + player2.getName() + "§f já está batalhando!");
					return;
				}

				if (gladiatorMode.recivedInvitation(player.getUniqueId(), player2.getUniqueId())) {
					BattleModules battleModules = gladiatorMode.getInvitation(player2.getUniqueId()).getBattleType();
					if ((battleModules.equals(BattleModules.CUSTOM) && item.getType().equals(Material.PISTON_BASE))) {
						fast.remove(player.getUniqueId());
						new ItemBuilder(Material.INK_SACK).setName("§aGladiator customizado §7(Clique)").setDurability(8).build(player.getInventory(), getManager().getUtils().findItem(player.getInventory(), Material.INK_SACK));
						fast.remove(player2.getUniqueId());
						new ItemBuilder(Material.INK_SACK).setName("§aGladiator customizado §7(Clique)").setDurability(8).build(player2.getInventory(), getManager().getUtils().findItem(player2.getInventory(), Material.INK_SACK));
						new RunnableStartingMatch(player, player2, gladiatorMode.getInvitations().get(player2.getUniqueId()).getBattleType());
						return;
					}
				}

				if (gamer.inWaitTime()) {
					player.sendMessage("§c§lCOOLDOWN §fAguarde para poder desafiar novamente!");
					return;
				}

				gladiatorMode.addInvitation(player, player2, BattleModules.CUSTOM);

				player.sendMessage("§3§lDESAFIO §fVocê §3§lENVIOU§f um desafio para O player §b" + player2.getName() + "§f!");
				player2.sendMessage("§3§lDESAFIO §fO player §b" + player.getName() + "§f te §b§lDESAFIOU§f para uma batalha!");
				player2.sendMessage("§3§lDESAFIO §fA arena será limpa a cada " + getManager().getUtils().toTime(gamer.getTimeToClear()));

				gamer.addWait(6);*/
			}

		}
	}

	/* Fast Battle */

	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final Gamer gamer = getManager().getGamerManager().getGamer(player);
		final ItemStack item = player.getItemInHand();

		if (gamer.getWarp().equals(WarpType.SPAWN) && !getManager().getGladiatorManager().getFight().isFighting(player.getUniqueId())) {

			if (item.getType().equals(Material.CHEST)) {
				getManager().getInventoryManager().getPreferencesInventory().generate(player);
			}

			if (!fast.contains(player.getUniqueId()) && item.getType().equals(Material.INK_SACK) && item.getDurability() == 8) {
				item.setDurability((short) 10);
				fast.add(player.getUniqueId());

				player.sendMessage("§a§lFAST MATCH §fVocê §a§lENTROU §fna fila do Gladiator rápido!");

				new BukkitRunnable() {
					int time;

					public void run() {
 
						if (getManager().getGladiatorManager().getFight().isFighting(player.getUniqueId()) || !gamer.getWarp().equals(WarpType.SPAWN) || !containsItem(player.getInventory(), Material.INK_SACK, 10)
								|| !fast.contains(player.getUniqueId())) {
							fast.remove(player.getUniqueId());
							cancel();
							return;
						}

						if (time == 10 && fast.contains(player.getUniqueId()) && !getManager().getGladiatorManager().getFight().isFighting(player.getUniqueId()) && gamer.getWarp().equals(WarpType.SPAWN)) {
							fast.remove(player.getUniqueId());

							new ItemBuilder(Material.INK_SACK).setName("§aDesafiar para Gladiator §7(Clique)").setDurability(8).build(player.getInventory(), getManager().getUtils().findItem(player.getInventory(), Material.INK_SACK));

							player.sendMessage("§c§lINDISPONIBILIDADE §fNão há §c§lNINGUEM§f disponível no momento. Tente mais tarde!");
							cancel();
							return;
						}

						time++;

						new ItemBuilder(Material.AIR).chanceItemStack(item).setName("§aDesafiar para Gladiator §7(Clique)").build(player.getInventory(), getManager().getUtils().findItem(player.getInventory(), item.getType()));
					}
				}.runTaskTimerAsynchronously(getManager().getPlugin(), 0L, 20L);

				if (fast.size() == 2) {
					new RunnableStartingMatch(Bukkit.getPlayer(fast.get(0)), Bukkit.getPlayer(fast.get(1)), BattleModules.FAST);
					fast.clear();
				}

			} else if (fast.contains(player.getUniqueId()) && item.getType().equals(Material.INK_SACK) && item.getDurability() == 10) {
				fast.remove(player.getUniqueId());
				new ItemBuilder(Material.INK_SACK).setName("§aDesafiar para Gladiator §7(Clique)").setDurability(8).build(player.getInventory(), getManager().getUtils().findItem(player.getInventory(), Material.INK_SACK));
				player.sendMessage("§a§lFAST MATCH §fVocê §c§lSAIU§f da fila do Gladiator rápido!");
			}

			return;
		}
	}

	private boolean containsItem(Inventory inventory, Material material, int durability) {
		boolean contains = false;
		for (ItemStack itemStack : inventory.getContents()) {
			if (itemStack != null && itemStack.getType().equals(material) && itemStack.getDurability() == durability) {
				contains = true;
			}
		}
		return contains;
	}

}
