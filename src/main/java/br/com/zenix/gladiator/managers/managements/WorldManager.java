package br.com.zenix.gladiator.managers.managements;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.managers.managements.constructor.Management;

public class WorldManager extends Management {

	public WorldManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		return loadWorlds();
	}

	public boolean loadWorlds() {
		getManager().getPlugin().getServer().createWorld(new WorldCreator("gladiator"));
		getLogger().log("The world 'Gladiator' has loaded correcly.");
		

		for (World world : Bukkit.getWorlds()) {
			world.setAutoSave(false);
			world.setThundering(false);
			if (world.hasStorm()) {
				world.setStorm(false);
			}
			world.setWeatherDuration(1000);
			world.setTime(6000L);
			world.setDifficulty(Difficulty.PEACEFUL);
			world.setWeatherDuration(999999999);
			world.setGameRuleValue("doDaylightCycle", "false");
		}
		
		return true;
	}

}
