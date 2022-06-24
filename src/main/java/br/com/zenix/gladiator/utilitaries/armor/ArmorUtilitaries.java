package br.com.zenix.gladiator.utilitaries.armor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.zenix.gladiator.utilitaries.item.ItemBuilder;


/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */
public class ArmorUtilitaries {

	public static String getNextArmorType(String name, boolean none) {
		String next = new String();

		name = name.toLowerCase();

		if (name.contains("leather")) {
			next = "gold";
		} else if (name.contains("gold")) {
			next = "chainmail";
		} else if (name.contains("chainmail")) {
			next = "iron";
		} else if (name.contains("iron")) {
			next = "diamond";
		} else if (name.contains("diamond")) {
			next = (none ? "glass" : "leather");
		} else if (name.contains("glass")) {
			next = "leather";
		}

		return next;
	}

	public static ItemStack[] getArmorByName(String name, boolean breakable) {

		ItemBuilder[] leather = { new ItemBuilder(Material.LEATHER_BOOTS), new ItemBuilder(Material.LEATHER_LEGGINGS), new ItemBuilder(Material.LEATHER_CHESTPLATE),
				new ItemBuilder(Material.LEATHER_HELMET) };
		ItemBuilder[] gold = { new ItemBuilder(Material.GOLD_BOOTS), new ItemBuilder(Material.GOLD_LEGGINGS), new ItemBuilder(Material.GOLD_CHESTPLATE),
				new ItemBuilder(Material.GOLD_HELMET) };
		ItemBuilder[] chainmail = { new ItemBuilder(Material.CHAINMAIL_BOOTS), new ItemBuilder(Material.CHAINMAIL_LEGGINGS), new ItemBuilder(Material.CHAINMAIL_CHESTPLATE),
				new ItemBuilder(Material.CHAINMAIL_HELMET) };
		ItemBuilder[] iron = { new ItemBuilder(Material.IRON_BOOTS), new ItemBuilder(Material.IRON_LEGGINGS), new ItemBuilder(Material.IRON_CHESTPLATE),
				new ItemBuilder(Material.IRON_HELMET) };
		ItemBuilder[] diamond = { new ItemBuilder(Material.DIAMOND_BOOTS), new ItemBuilder(Material.DIAMOND_LEGGINGS), new ItemBuilder(Material.DIAMOND_CHESTPLATE),
				new ItemBuilder(Material.DIAMOND_HELMET) };
		ItemBuilder[] none = { new ItemBuilder(Material.GLASS), new ItemBuilder(Material.GLASS), new ItemBuilder(Material.GLASS), new ItemBuilder(Material.GLASS) };
		ItemBuilder[] current = null;

		name = name.toLowerCase();

		if (name.contains("leather")) {
			current = leather;
		} else if (name.contains("gold")) {
			current = gold;
		} else if (name.contains("chainmail")) {
			current = chainmail;
		} else if (name.contains("iron")) {
			current = iron;
		} else if (name.contains("diamond")) {
			current = diamond;
		} else if (name.contains("glass")) {
			current = none;
		}

		List<ItemStack> items = new ArrayList<>();

		for (ItemBuilder item : current) {
			item.setBreakable(breakable);
			items.add(item.getStack());
		}

		return items.toArray(new ItemStack[] {});

	}

	public static void updateArmor(Player player, String typeName, boolean breakable) {
		player.getInventory().setArmorContents(getArmorByName(typeName, breakable));
	}

	public static void updateArmor(Player player, ItemBuilder helmet, ItemBuilder chestplate, ItemBuilder leggings, ItemBuilder boots, boolean breakable) {
		player.getInventory().setHelmet(!helmet.getStack().getType().equals(Material.GLASS) ? helmet.setBreakable(breakable).getStack() : null);
		player.getInventory().setChestplate(!chestplate.getStack().getType().equals(Material.GLASS) ? chestplate.setBreakable(breakable).getStack() : null);
		player.getInventory().setLeggings(!leggings.getStack().getType().equals(Material.GLASS) ? leggings.setBreakable(breakable).getStack() : null);
		player.getInventory().setBoots(!boots.getStack().getType().equals(Material.GLASS) ? boots.setBreakable(breakable).getStack() : null);
	}

	public static void updateArmor(Player player, Material helmet, Material chestplate, Material leggings, Material boots, boolean breakable) {
		updateArmor(player, new ItemBuilder(helmet).setBreakable(breakable), new ItemBuilder(chestplate).setBreakable(breakable), new ItemBuilder(leggings).setBreakable(breakable),
				new ItemBuilder(boots).setBreakable(breakable), breakable);
	}

	public static String getNextSwordType(String name) {
		String next = new String();

		name = name.toLowerCase();

		if (name.contains("wood")) {
			next = "gold";
		} else if (name.contains("gold")) {
			next = "stone";
		} else if (name.contains("stone")) {
			next = "iron";
		} else if (name.contains("iron")) {
			next = "diamond";
		} else if (name.contains("diamond")) {
			next = "wood";
		}

		return next;
	}

	public static Material getSwordByName(String name) {
		Material current = null;

		name = name.toLowerCase();

		if (name.contains("wood")) {
			current = Material.WOOD_SWORD;
		} else if (name.contains("gold")) {
			current = Material.GOLD_SWORD;
		} else if (name.contains("stone")) {
			current = Material.STONE_SWORD;
		} else if (name.contains("iron")) {
			current = Material.IRON_SWORD;
		} else if (name.contains("diamond")) {
			current = Material.DIAMOND_SWORD;
		}

		return current;
	}

	public static String getName(Material material) {

		String name = material.name().toLowerCase();

		if (name.contains("wood")) {
			return "madeira";
		} else if (name.contains("gold")) {
			return "ouro";
		} else if (name.contains("stone")) {
			return "pedra";
		} else if (name.contains("iron")) {
			return "ferro";
		} else if (name.contains("diamond")) {
			return "diamante";
		} else if (name.contains("diamond")) {
			return "diamante";
		} else if (name.contains("leather")) {
			return "couro";
		} else if (name.contains("chainmail")) {
			return "chain";
		} else if (name.contains("glass")) {
			return "nenhum";
		}

		return null;
	}

	public static double getDamageBySword(Material material) {
		if (material.equals(Material.WOOD_SWORD)) {
			return 2D;
		} else if (material.equals(Material.GOLD_SWORD)) {
			return 2D;
		} else if (material.equals(Material.STONE_SWORD)) {
			return 2D;
		} else if (material.equals(Material.IRON_SWORD)) {
			return 4D;
		} else if (material.equals(Material.DIAMOND_SWORD)) {
			return 5D;
		}
		return 0;
	}

}