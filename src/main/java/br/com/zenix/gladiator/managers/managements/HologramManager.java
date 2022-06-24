package br.com.zenix.gladiator.managers.managements;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import br.com.zenix.core.plugin.data.management.utilitaries.Callback;
import br.com.zenix.core.spigot.player.account.Account;
import br.com.zenix.core.spigot.player.hologram.Hologram;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.managers.managements.constructor.Management;
import br.com.zenix.gladiator.managers.managements.hologram.PlayerTop;
import br.com.zenix.gladiator.managers.managements.hologram.PlayerTop.PlayerTopLoses;
import br.com.zenix.gladiator.managers.managements.hologram.PlayerTop.PlayerTopMKS;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class HologramManager extends Management {

	private static final List<PlayerTop> topScore = new ArrayList<>();
	private static final List<PlayerTopLoses> topScoreLoses = new ArrayList<>();
	private static final List<PlayerTopMKS> topScoreMKS = new ArrayList<>();
	public Hologram scoreHologram, scoreHologramDeaths, scoreHologramMKS;

	public HologramManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		return updateHolograms();
	}

	public boolean updateHolograms() {
		topScore.clear();
		loadList(topScore);
		topScoreLoses.clear();
		loadListLoses(topScoreLoses);
		topScoreMKS.clear();
		loadListMKS(topScoreMKS);
		return setHolograms();
	}

	public boolean loadList(List<PlayerTop> list) {
		ResultSet set = getManager().getCoreManager().getDataManager().getMySQL()
				.executeQuery("SELECT * FROM `global_data` WHERE `type`=23 ORDER BY `value` DESC LIMIT 15");
		try {
			int id = 0;
			while (set.next()) {
				id++;
				list.add(new PlayerTop(id, getManager().getCoreManager().getNameFetcher().getName(set.getInt("player")),
						set.getInt("value")));
			}
			set.close();
			return true;
		} catch (Exception exeption) {
			exeption.printStackTrace();
			return false;
		}

	}

	public boolean loadListLoses(List<PlayerTopLoses> list) {
		ResultSet set = getManager().getCoreManager().getDataManager().getMySQL()
				.executeQuery("SELECT * FROM `global_data` WHERE `type`=24 ORDER BY `value` DESC LIMIT 15");
		try {
			int id = 0;
			while (set.next()) {
				id++;
				list.add(new PlayerTopLoses(id,
						getManager().getCoreManager().getNameFetcher().getName(set.getInt("player")),
						set.getInt("value")));
			}
			set.close();
			return true;
		} catch (Exception exeption) {
			exeption.printStackTrace();
			return false;
		}

	}

	public boolean loadListMKS(List<PlayerTopMKS> list) {
		ResultSet set = getManager().getCoreManager().getDataManager().getMySQL()
				.executeQuery("SELECT * FROM `global_data` WHERE `type`=27 ORDER BY `value` DESC LIMIT 15");
		try {
			int id = 0;
			while (set.next()) {
				id++;
				list.add(new PlayerTopMKS(id,
						getManager().getCoreManager().getNameFetcher().getName(set.getInt("player")),
						set.getInt("value")));
			}
			set.close();
			return true;
		} catch (Exception exeption) {
			exeption.printStackTrace();
			return false;
		}

	}

	public boolean setHolograms() {
		topScore.clear();
		loadList(topScore);

		if (scoreHologram != null) {
			scoreHologram.remove();
			scoreHologram = null;
		}

		Location scoreLocation = new Location(Bukkit.getServer().getWorld("world"), -751.8, 18.7, -82.1);
		scoreHologram = new Hologram("§E§lTOP 10 §f§lWINS", scoreLocation, true);
		scoreHologram.addLine("   ");

		for (PlayerTop scoreTop : topScore) {
			if (scoreTop != null) {
				String player = scoreTop.getName(); 
				UUID uuid = getManager().getCoreManager().getNameFetcher().getUUID(player);

				Account account = new Account(uuid);

				if (!account.isLoaded()) {
					account.load(new Callback<Boolean>() {
						public void finish(Boolean bool) {
							if (bool)
								getManager().getCoreManager().getAccountManager().getAccounts().put(uuid, account);
							else {
								try {
									Thread.sleep(500L);
								} catch (InterruptedException exception) {
									exception.printStackTrace();
								}
							}
						}
					});
					account.updatePlayer(player);
				}

				String line = "";
				if (account.isLoaded() && account != null) {
					String group = getManager().getCoreManager().getPermissionManager()
							.getRank(account.getRank().getId()).getTag().getColor();
					String name = group + scoreTop.getName();

					line = "§a" + scoreTop.getId() + ". §6" + name.replace("§l", "") + " §7- §e" + scoreTop.getTop();

				} else {

					String name = scoreTop.getName();

					line = "§a" + scoreTop.getId() + ". §7" + name + " §7- §e" + scoreTop.getTop();
				}
				scoreHologram.addLine(line);

			}
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			scoreHologram.hide(player);
			scoreHologram.show(player);

		}

		////////////

		topScoreLoses.clear();
		loadListLoses(topScoreLoses);

		if (scoreHologramDeaths != null) {
			scoreHologramDeaths.remove();
			scoreHologramDeaths = null;
		}

		Location scoreLocationDeaths = new Location(Bukkit.getServer().getWorld("world"), -746.4, 18.7, -83.5);
		scoreHologramDeaths = new Hologram("§E§lTOP 10 §f§lLOSSES", scoreLocationDeaths, true);
		scoreHologramDeaths.addLine("   ");

		for (PlayerTopLoses scoreTop : topScoreLoses) {
			if (scoreTop != null) {
				String player = scoreTop.getName();
				UUID uuid = getManager().getCoreManager().getNameFetcher().getUUID(player);

				Account account = new Account(uuid);

				if (!account.isLoaded()) {
					account.load(new Callback<Boolean>() {
						public void finish(Boolean bool) {
							if (bool)
								getManager().getCoreManager().getAccountManager().getAccounts().put(uuid, account);
							else {
								try {
									Thread.sleep(500L);
								} catch (InterruptedException exception) {
									exception.printStackTrace();
								}
							}
						}
					});
					account.updatePlayer(player);
				}

				String line = "";
				if (account.isLoaded() && account != null) {
					String group = getManager().getCoreManager().getPermissionManager()
							.getRank(account.getRank().getId()).getTag().getColor();
					String name = group + scoreTop.getName();

					line = "§a" + scoreTop.getId() + ". §6" + name.replace("§l", "") + " §7- §e" + scoreTop.getTop();

				} else {

					String name = scoreTop.getName();

					line = "§a" + scoreTop.getId() + ". §7" + name + " §7- §e" + scoreTop.getTop();
				}
				scoreHologramDeaths.addLine(line);
			}
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			scoreHologramDeaths.hide(player);
			scoreHologramDeaths.show(player);
		}

		////////////

		topScoreMKS.clear();
		loadListMKS(topScoreMKS);

		if (scoreHologramMKS != null) {
			scoreHologramMKS.remove();
			scoreHologramMKS = null;
		}

		Location scoreLocationMKS = new Location(Bukkit.getServer().getWorld("world"), -756.5, 18.7, -83.3);
		scoreHologramMKS = new Hologram("§E§lTOP 10 §f§lMOST KILLSTREAK", scoreLocationMKS, true);
		scoreHologramMKS.addLine("   ");

		for (PlayerTopMKS scoreTop : topScoreMKS) {
			if (scoreTop != null) {
				String player = scoreTop.getName();
				UUID uuid = getManager().getCoreManager().getNameFetcher().getUUID(player);

				Account account = new Account(uuid);

				if (!account.isLoaded()) {
					account.load(new Callback<Boolean>() {
						public void finish(Boolean bool) {
							if (bool)
								getManager().getCoreManager().getAccountManager().getAccounts().put(uuid, account);
							else {
								try {
									Thread.sleep(500L);
								} catch (InterruptedException exception) {
									exception.printStackTrace();
								}
							}
						}
					});
					account.updatePlayer(player);
				}

				String line = "";
				if (account.isLoaded() && account != null) {
					String group = getManager().getCoreManager().getPermissionManager()
							.getRank(account.getRank().getId()).getTag().getColor();
					String name = group + scoreTop.getName();

					line = "§a" + scoreTop.getId() + ". §6" + name.replace("§l", "") + " §7- §e" + scoreTop.getTop();

				} else {

					String name = scoreTop.getName();

					line = "§a" + scoreTop.getId() + ". §7" + name + " §7- §e" + scoreTop.getTop();
				}
				scoreHologramMKS.addLine(line);
			}
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			scoreHologramMKS.hide(player);
			scoreHologramMKS.show(player);
		}

		return true;

	}

}
