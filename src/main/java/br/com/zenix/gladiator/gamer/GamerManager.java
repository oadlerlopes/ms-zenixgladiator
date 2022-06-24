package br.com.zenix.gladiator.gamer;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.spigotmc.ProtocolInjector;

import br.com.zenix.core.spigot.player.account.Account;
import br.com.zenix.gladiator.Gladiator;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.managers.managements.constructor.Management;
import br.com.zenix.gladiator.player.item.ItemsCache;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.PlayerConnection;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class GamerManager extends Management {

	private HashMap<UUID, Gamer> gamers = new HashMap<UUID, Gamer>();

	public GamerManager(Manager manager) {
		super(manager, "Gamer");
	}

	public boolean initialize() {
		return true;
	}

	public Gamer addGamer(Gamer gamer) {
		gamers.put(gamer.getUniqueId(), gamer);
		return gamer;
	}

	public void removeGamer(UUID uniqueId) {
		gamers.remove(uniqueId);
	}

	public Gamer getGamer(UUID uniqueId) {
		return gamers.get(uniqueId);
	}

	public Gamer getGamer(Player player) {
		return gamers.get(player.getUniqueId());
	}

	public HashMap<UUID, Gamer> getGamers() {
		return gamers;
	}

	public void updateTab(Gamer gamer) {
		StringBuilder headerBuilder = new StringBuilder();
		headerBuilder.append(" \n ");
		headerBuilder.append("§6§lZENIX");
		headerBuilder.append(" \n ");

		StringBuilder footerBuilder = new StringBuilder();
		footerBuilder.append(" \n ");
		footerBuilder.append("§bNick: §f" + gamer.getAccount().getPlayer().getName() + " §1- §bLiga: §f"
				+ gamer.getAccount().getLeague().getName().toUpperCase() + " §1- §bXP: §f"
				+ gamer.getAccount().getXp());
		footerBuilder.append(" \n ");
		footerBuilder.append("§bMais informações em §fwww.zenix.cc");
		footerBuilder.append(" \n ");

		updateTab(gamer.getAccount().getPlayer(), headerBuilder.toString(), footerBuilder.toString());
	}
	
	public void giveDamage(LivingEntity reciveDamage, Player giveDamage, double damage, boolean bool) {
		if (reciveDamage == null || reciveDamage.isDead() || giveDamage == null || giveDamage.isDead())
			return;

		reciveDamage.setNoDamageTicks(0);

		if (bool) {
			if (reciveDamage.getHealth() < damage) {
				reciveDamage.setHealth(1.0D);
				giveDamage.setMetadata("custom",
						new FixedMetadataValue(Gladiator.getPlugin(Gladiator.class), null));
				reciveDamage.damage(6.0D, giveDamage);
			} else {
				reciveDamage.damage(damage);
			}
		} else {
			giveDamage.setMetadata("custom", new FixedMetadataValue(Gladiator.getPlugin(Gladiator.class), null));
			reciveDamage.damage(damage, giveDamage);
		}
	}

	public void removePottionEffects(Player player) {
		for (PotionEffect potionEffect : player.getActivePotionEffects())
			player.removePotionEffect(potionEffect.getType());
	}

	public void resetPlayer(Player player) {
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false);
		player.setHealth(20.0D);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setExp(0.0f);

		((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);
	}

	public void forceUpdate(Account account) {
		account.update();
	}

	public void fillInventory(Inventory inventory, ItemsCache item, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			if (inventory.getItem(i) == null)
				item.build(inventory);
		}
	}

	public void fillInventory(Player player, ItemsCache item, int quantity) {
		fillInventory(player.getInventory(), item, quantity);
	}

	public void fillInventory(Gamer gamer, ItemsCache item, int quantity) {
		fillInventory(gamer.getAccount().getPlayer().getInventory(), item, quantity);
	}

	public void updateTab(Player player, String up, String down) {
		if (((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion() >= 46) {
			PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
			connection.sendPacket(new ProtocolInjector.PacketTabHeader(ChatSerializer.a("{'text': '" + up + "'}"),
					ChatSerializer.a("{'text': '" + down + "'}")));
		}
	}

}
