package br.com.zenix.gladiator.managers.managements;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapelessRecipe;

import br.com.zenix.core.spigot.player.item.ItemBuilder;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.managers.managements.constructor.Management;
import br.com.zenix.gladiator.warps.type.WarpType;

public class ConfigManager extends Management {

	public ConfigManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		createSoups();
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static void createSoups() {
		ItemBuilder soup = new ItemBuilder().setMaterial(Material.MUSHROOM_SOUP);
		ShapelessRecipe cocoa = new ShapelessRecipe(soup.getStack());
		ShapelessRecipe cactus = new ShapelessRecipe(soup.getStack());
		ShapelessRecipe pumpkin = new ShapelessRecipe(soup.getStack());
		ShapelessRecipe melon = new ShapelessRecipe(soup.getStack());
		ShapelessRecipe flower = new ShapelessRecipe(soup.getStack());
		ShapelessRecipe nether = new ShapelessRecipe(soup.getStack());

		cocoa.addIngredient(Material.BOWL);
		cocoa.addIngredient(Material.INK_SACK, 3);

		cactus.addIngredient(Material.BOWL);
		cactus.addIngredient(2, Material.CACTUS);

		pumpkin.addIngredient(Material.BOWL);
		pumpkin.addIngredient(2, Material.PUMPKIN_SEEDS);

		melon.addIngredient(Material.BOWL);
		melon.addIngredient(2, Material.MELON_SEEDS);

		nether.addIngredient(Material.BOWL);
		nether.addIngredient(Material.getMaterial(372));
		
		flower.addIngredient(Material.BOWL);
		flower.addIngredient(Material.RED_ROSE);
		flower.addIngredient(Material.YELLOW_FLOWER);

		Bukkit.addRecipe(cocoa);
		Bukkit.addRecipe(cactus);
		Bukkit.addRecipe(pumpkin);
		Bukkit.addRecipe(melon);
		Bukkit.addRecipe(nether);
		Bukkit.addRecipe(flower);
	}

	public String getConfig(String obj) {
		return getManager().getPlugin().getConfig().getString("Mysql." + obj);
	}

	public void registerInConfig(Player player, String config) {
		FileConfiguration file = getManager().getPlugin().getConfig();
		Location location = player.getLocation();

		file.set(config + ".world", location.getWorld().getName());
		file.set(config + ".x", location.getX());
		file.set(config + ".y", location.getY());
		file.set(config + ".z", location.getZ());
		file.set(config + ".pitch", location.getPitch());
		file.set(config + ".yaw", location.getYaw());

		getManager().getPlugin().saveConfig();
	}

	public void registerInConfig(Object where, Object toSet) {
		FileConfiguration file = getManager().getPlugin().getConfig();
		file.set(String.valueOf(where), String.valueOf(toSet));
		getManager().getPlugin().saveConfig();
	}

	public Location getLocationFromConfig(String config) {
		FileConfiguration file = getManager().getPlugin().getConfig();
		Location location = new Location(Bukkit.getWorld("world"), file.getDouble(config + ".x"),
				file.getDouble(config + ".y"), file.getDouble(config + ".z"));

		location.setPitch(file.getLong(config + ".pitch"));
		location.setYaw(file.getLong(config + ".yaw"));

		return location;
	}

	public Location getLocationFromConfig(String config, String world) {
		FileConfiguration file = getManager().getPlugin().getConfig();
		Location location = new Location(Bukkit.getWorld(world), file.getDouble(config + ".x"),
				file.getDouble(config + ".y"), file.getDouble(config + ".z"));

		location.setPitch(file.getLong(config + ".pitch"));
		location.setYaw(file.getLong(config + ".yaw"));

		return location;
	}

	public void teleportPlayer(Player player, String config) {
		FileConfiguration file = getManager().getPlugin().getConfig();

		if (!file.contains(config + ".x"))
			return;

		player.teleport(getLocationFromConfig(config, "world"));
	}

	public void teleportPlayer(Player player, WarpType warp, String config) {
		FileConfiguration file = getManager().getPlugin().getConfig();

		if (!file.contains(config + ".x"))
			return;

		player.teleport(getLocationFromConfig(config, "world"));
	}

	public void teleportPlayer(Player player, WarpType warp) {
		if (warp.equals(WarpType.SPAWN)) {
			player.teleport(new Location(Bukkit.getWorld("world"), -751, 15, -95));
		}
	}

	public int getMaxId(String config, int start) {
		FileConfiguration file = getManager().getPlugin().getConfig();
		int max = 0;

		for (int i = start; i < 100; i++)
			if (file.contains(config + i))
				max = i;

		return max;
	}

}
