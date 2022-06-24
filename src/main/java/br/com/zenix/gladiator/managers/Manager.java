package br.com.zenix.gladiator.managers;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.core.plugin.logger.Logger;
import br.com.zenix.core.spigot.Core;
import br.com.zenix.core.spigot.manager.CoreManager;
import br.com.zenix.core.spigot.server.type.ServerType;
import br.com.zenix.gladiator.Gladiator;
import br.com.zenix.gladiator.battle.BattleManager;
import br.com.zenix.gladiator.gamer.GamerManager;
import br.com.zenix.gladiator.managers.managements.ClassManager;
import br.com.zenix.gladiator.managers.managements.ConfigManager;
import br.com.zenix.gladiator.managers.managements.HologramManager;
import br.com.zenix.gladiator.managers.managements.WorldManager;
import br.com.zenix.gladiator.player.admin.AdminManager;
import br.com.zenix.gladiator.player.admin.Vanish;
import br.com.zenix.gladiator.player.gamer.inventory.InventoryManager;
import br.com.zenix.gladiator.player.scoreboard.ScoreboardManager;
import br.com.zenix.gladiator.utilitaries.Utilitaries;
import br.com.zenix.gladiator.warps.WarpManager;

public class Manager {

	private final Gladiator plugin;
	private final CoreManager coreManager;

	public Utilitaries utils = new Utilitaries();

	private ClassManager classManager;
	private ScoreboardManager scoreboardManager;
	private BattleManager battleManager;
	private GamerManager gamerManager;
	private AdminManager adminManager;
	private WarpManager warpManager;
	private ConfigManager configManager;
	private WorldManager worldManager;

	private InventoryManager inventoryManager;
	private HologramManager hologramManager;

	private int xLocation = 0;
	private int zLocation = -100000;

	private int time;
	private Vanish vanish;

	public Manager(Core core) {
		this.plugin = Gladiator.getPlugin(Gladiator.class);
		getPlugin().saveDefaultConfig();

		getLogger().log(
				"Starting the plugin " + plugin.getName() + " version " + plugin.getDescription().getVersion() + "...");

		coreManager = Core.getCoreManager();

		getLogger().log("Making connection with plugin " + coreManager.getPlugin().getName() + " version "
				+ coreManager.getPlugin().getDescription().getVersion() + ".");

		getLogger().log("The plugin " + plugin.getName() + " version " + plugin.getDescription().getVersion()
				+ " was started correcly.");

		Core.getCoreManager().setServerType(ServerType.GLADIATOR);

		gamerManager = new GamerManager(this);
		if (!gamerManager.correctlyStart()) {
			return;
		}

		scoreboardManager = new ScoreboardManager(this);
		if (!scoreboardManager.correctlyStart()) {
			return;
		}

		inventoryManager = new InventoryManager(this);
		if (!inventoryManager.correctlyStart()) {
			return;
		}

		hologramManager = new HologramManager(this);
		if (!hologramManager.correctlyStart()) {
			return;
		}

		classManager = new ClassManager(this);
		if (!classManager.correctlyStart()) {
			return;
		}

		adminManager = new AdminManager(this);
		if (!adminManager.correctlyStart()) {
			return;
		}

		worldManager = new WorldManager(this);
		if (!worldManager.correctlyStart()) {
			return;
		}

		configManager = new ConfigManager(this);
		if (!configManager.correctlyStart()) {
			return;
		}

		battleManager = new BattleManager(this);
		if (!battleManager.correctlyStart()) {
			return;
		}

		warpManager = new WarpManager(this);
		if (!warpManager.correctlyStart()) {
			return;
		}

		vanish = new Vanish(this);

		time = 7200;

		new BukkitRunnable() {
			public void run() {
				if (time <= 0) {
					Bukkit.shutdown();
				}

				time--;
			}
		}.runTaskTimer(getPlugin(), 0L, 20L);
	}

	public HologramManager getHologramManager() {
		return hologramManager;
	}

	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	public int getXLocation() {
		return xLocation;
	}

	public void setXLocation(int xLocation) {
		this.xLocation = xLocation;
	}

	public int getZLocation() {
		return zLocation;
	}

	public void setZLocation(int zLocation) {
		this.zLocation = zLocation;
	}

	public void registerListener(Listener listener) {
		PluginManager manager = Bukkit.getPluginManager();
		manager.registerEvents(listener, getPlugin());
	}

	public Vanish getVanish() {
		return vanish;
	}

	public Gladiator getPlugin() {
		return plugin;
	}

	public CoreManager getCoreManager() {
		return coreManager;
	}

	public ScoreboardManager getPlayerScoreboard() {
		return scoreboardManager;
	}

	public ClassManager getClassManager() {
		return classManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public BattleManager getBattleManager() {
		return battleManager;
	}

	public WorldManager getWorldManager() {
		return worldManager;
	}

	public BattleManager getGladiatorManager() {
		return battleManager;
	}

	public WarpManager getWarpTeleport() {
		return warpManager;
	}

	public AdminManager getAdminManager() {
		return adminManager;
	}

	public int getMaxPlayers() {
		return Bukkit.getMaxPlayers();
	}

	public Logger getLogger() {
		return getPlugin().getLoggerSecondary();
	}

	public GamerManager getGamerManager() {
		return gamerManager;
	}

	public Utilitaries getUtils() {
		return utils;
	}

}
