package br.com.zenix.gladiator.warps.constructor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import br.com.zenix.gladiator.Gladiator;
import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.gamer.GamerManager;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.player.item.ItemsCache;
import br.com.zenix.gladiator.utilitaries.item.ItemBuilder;
import br.com.zenix.gladiator.warps.type.WarpType;

public class ItemConstructor {
	
	public ItemConstructor(Manager manager, Player player, WarpType warp) {
		PlayerInventory inventory = player.getInventory();
		ItemBuilder item = new ItemBuilder(Material.AIR);
		GamerManager gamerManager = Gladiator.getManager().getGamerManager();
		Gamer gamer = gamerManager.getGamer(player);

		item = ItemsCache.DEFAULT_SWORD.getItem(0).setMaterial(Material.DIAMOND_SWORD).setUnbreakable();

		item.setUnbreakable();

		player.closeInventory();
		inventory.clear();
		inventory.setArmorContents(null);
		player.setFireTicks(0);

		if (warp.equals(WarpType.SPAWN)) {
			item.setMaterial(Material.IRON_FENCE).setName("§aDesafiar para Gladiator§7 (Normal)").build(inventory, 1);
			item.setMaterial(Material.INK_SACK).setName("§aGladiator Rápido§7 (Clique)").setDurability(8).build(inventory, 3);
			item.setMaterial(Material.PISTON_BASE).setName("§aGladiator Customizado§7 (Clique)").build(inventory, 5);
			item.setMaterial(Material.CHEST).setName("§aConfigurações§7 (Clique)").build(inventory, 7);
			gamer.setPvP(false);

		}

	}

}
