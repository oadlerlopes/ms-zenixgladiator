package br.com.zenix.gladiator.battle.gladiator.managements.custom;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Custom {
	
	private Material armor, sword;
	private boolean recraft, sharp;

	public Custom() {
		armor = Material.IRON_CHESTPLATE;
		sword = Material.DIAMOND_SWORD;
		recraft = true;
		sharp = false;
	}


	public Material getArmor() {
		return armor;
	}

	public Material getSword() {
		return sword;
	}

	public boolean recraft() {
		return recraft;
	}

	public boolean sharp() {
		return sharp;
	}

	public void setArmor(Material material) {
		armor = material;
	}

	public void setSword(Material material) {
		this.sword = material;
	}

	public void setRecraft(boolean recraft) {
		this.recraft = recraft;
	}

	public void setSharp(boolean sharp) {
		this.sharp = sharp;
	}

	public void build(Player player) {
		String material = armor.name().replace("_CHESTPLATE", "");
		ItemStack[] armor = { new ItemStack(Material.getMaterial(material + "_HELMET")),
				new ItemStack(Material.getMaterial(material + "CHESTPLATE")),
				new ItemStack(Material.getMaterial(material + "_LEGGINGS")),
				new ItemStack(Material.getMaterial(material + "_BOOTS")) };
		player.getInventory().setArmorContents(armor);
	}

}