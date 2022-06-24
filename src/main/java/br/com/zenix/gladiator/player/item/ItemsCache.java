package br.com.zenix.gladiator.player.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.utilitaries.item.ItemBuilder;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */
public enum ItemsCache {

	SOUP(new ItemBuilder[] { new ItemBuilder(Material.MUSHROOM_SOUP).setName("§b§lSopa").setBreakable() }, null),
	RECRAFT(new ItemBuilder[] { new ItemBuilder(Material.RED_MUSHROOM).setAmount(64).setBreakable(), new ItemBuilder(Material.BROWN_MUSHROOM).setAmount(64).setBreakable(),new ItemBuilder(Material.BOWL).setAmount(64).setBreakable() }, new Integer[] { 13, 14, 15 }),
	RECRAFT2(new ItemBuilder[] { new ItemBuilder(Material.BLAZE_POWDER).setAmount(64).setBreakable(), new ItemBuilder(Material.SPIDER_EYE).setAmount(64).setBreakable() }, new Integer[] { 13,14 }),
	DEFAULT_SWORD(new ItemBuilder[] { new ItemBuilder(Material.DIAMOND_SWORD).setName("§b§lDiamond Sword").setUnbreakable() }, null),
	COMPASS(new ItemBuilder[] { new ItemBuilder(Material.COMPASS).setName("§b§lBússola") }, new Integer[] { 8 });

	private ItemBuilder[] items;
	private Integer[] slots;

	ItemsCache(ItemBuilder[] items, Integer[] slots) {
		this.items = items;
		this.slots = slots;
	}

	public ItemBuilder[] getItems() {
		return items;
	}

	public ItemBuilder getItem(int id) {
		return id <= items.length - 1 ? items[id] : items[0];
	}

	public Integer[] getSlots() {
		return slots;
	}

	public void build(Inventory inventory) {
		for (int i = 0; i < items.length; i++) {
			if (slots == null)
				getItem(i).build(inventory);
			else
				getItem(i).build(inventory, slots[i]);
		}
	}

	public void build(Player player) {
		build(player.getInventory());
	}

	public void build(Gamer gamer) {
		build(gamer.getAccount().getPlayer().getInventory());
	}

}
