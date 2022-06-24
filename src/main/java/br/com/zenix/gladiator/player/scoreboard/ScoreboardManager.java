package br.com.zenix.gladiator.player.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.core.plugin.data.handler.type.DataType;
import br.com.zenix.core.spigot.player.account.Account;
import br.com.zenix.core.spigot.player.scoreboard.ScoreboardScroller;
import br.com.zenix.core.spigot.player.scoreboard.ScoreboardConstructor;
import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.managers.managements.constructor.Management;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class ScoreboardManager extends Management {

	private String title = "§6§lZENIX";
	private ScoreboardScroller scoreboardScroller;

	public ScoreboardManager(Manager manager) {
		super(manager, "Scoreboard");
	}

	public boolean initialize() {

		scoreboardScroller = new ScoreboardScroller(" GLADIATOR ", "§f§l", "§6§l", "§e§l", 3);

		return startScores();
	}

	public boolean startScores() {

		new BukkitRunnable() {
			public void run() {
				if (Bukkit.getOnlinePlayers().size() == 0)
					return;

				title = "§f§l" + scoreboardScroller.next();

				for (Player player : Bukkit.getOnlinePlayers()) {
					updateScoreboard(player);

				}

			}
		}.runTaskTimer(getManager().getPlugin(), 2, 2);
		return true;
	}

	@SuppressWarnings("unused")
	public void createScoreboard(Player player) {
		ScoreboardConstructor scoreboardHandler = new ScoreboardConstructor(player);

		Gamer gamer = getManager().getGamerManager().getGamer(player);
		Account account = gamer.getAccount();

		if (gamer == null) {
			player.kickPlayer("§4§lERRO §fA sua conta está com problemas!");
		}

		if (account.getRank().getName() == null || account.getLeague().getName() == null || gamer == null) {
			player.kickPlayer("§4§lERRO §fA sua conta está com problemas!");
		}

		scoreboardHandler.initialize(" §6§l" + getManager().getCoreManager().getServerName().toUpperCase() + " ");

		scoreboardHandler.setScore("§b§c", "§2§l", "§7");
		scoreboardHandler.setScore("§fWins: ", "", "0");
		scoreboardHandler.setScore("§fLosses: ", "", "0");
		scoreboardHandler.setScore("§fKillstreak: ", "", "0");
		scoreboardHandler.setScore("§fXP: ", "", "§b0");
		scoreboardHandler.setScore("§fLiga: ", "", "§f- UNRANKED");
		scoreboardHandler.setScore("§d§c", "§2§l", "§7");
		scoreboardHandler.setScore("§fBatalhando ", "", "§fcontra:");
		scoreboardHandler.setScore("§a", "", "§3Ninguém");

		scoreboardHandler.setScore("§a§2", "§3§c", "§8");
		scoreboardHandler.setScore("www.zenix", "§6", "§6.cc");

		account.setScoreboardHandler(scoreboardHandler);
	}

	public void updateScoreboard(Player player) {

		Gamer gamer = getManager().getGamerManager().getGamer(player);
		Account account = gamer.getAccount();

		if (account.getRank().getName() == null || account.getLeague().getName() == null || gamer == null) {
			player.kickPlayer("§4§lERRO §fA sua conta está com problemas!");
		}

		if (account.getScoreboardHandler() == null) {
			createScoreboard(player);
		}

		ScoreboardConstructor scoreboardHandler = account.getScoreboardHandler();

		scoreboardHandler.setDisplayName(title);

		if (account.getRank().getName() == null || account.getLeague().getName() == null || gamer == null) {
			player.kickPlayer("§4§lERRO §fA sua conta está com problemas!");
			return;
		}

		scoreboardHandler.updateScore("§fWins: ", "§f",
				"§e" + account.getDataHandler().getValue(DataType.GLADIATOR_WIN).getValue());
		scoreboardHandler.updateScore("§fLosses: ", "§f",
				"§e" + account.getDataHandler().getValue(DataType.GLADIATOR_LOSE).getValue());
		scoreboardHandler.updateScore("§fKillstreak: ", "§f",
				"§a" + account.getDataHandler().getValue(DataType.GLADIATOR_KILLSTREAK).getValue());
		scoreboardHandler.updateScore("§fXP: ", "",
				"§a" + account.getDataHandler().getValue(DataType.GLOBAL_XP).getValue());
		scoreboardHandler.updateScore("§fLiga: ", "", "" + account.getLeague().getColor()
				+ account.getLeague().getSymbol() + " " + account.getLeague().getName().toUpperCase());

		scoreboardHandler.updateScore("§a", "", "§a" + getManager().getGladiatorManager().getFighter(player));

	}
}
