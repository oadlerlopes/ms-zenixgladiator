package br.com.zenix.gladiator.player.gamer.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import br.com.zenix.gladiator.commands.base.GladiatorListener;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class PlayerServerEvent extends GladiatorListener {

	@EventHandler
	public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
		if (event.getRegainReason() == RegainReason.SATIATED || event.getRegainReason() == RegainReason.REGEN)
			event.setCancelled(true);
	}

	@EventHandler
	private void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
	}

	@EventHandler
	private void onLeavesDecay(LeavesDecayEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	private void onWeatherChange(WeatherChangeEvent event) {
		event.getWorld().setTime(0);
		event.getWorld().setWeatherDuration(Integer.MAX_VALUE);
		event.setCancelled(true);
	}

	@EventHandler
	private void onPlayerBedEnter(PlayerBedEnterEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	private void onServerListPing(ServerListPingEvent event) {
		event.setMotd(
				"          §8§m--§b§m]--§6§l Zenix§f | §dPvP Server§b §m--[§8§m--§f \n       §f§lADQUIRA §e§lBETA§f§l EM §3§lLOJA.ZENIX.CC");
	}

	@EventHandler
	private void onSignChange(SignChangeEvent event) {
		if (event.getLine(0).equals("potions")) {
			event.setLine(0, "");
			event.setLine(1, "§6Zenix");
			event.setLine(2, "§7» §8Potions");
			event.setLine(3, "");
		} else if (event.getLine(0).equals("soups")) {
			event.setLine(0, "");
			event.setLine(1, "§6Zenix");
			event.setLine(2, "§7» §8Sopas");
			event.setLine(3, "");
		} else if (event.getLine(0).equals("recraft")) {
			event.setLine(0, "");
			event.setLine(1, "§6Zenix");
			event.setLine(2, "§7» §8Recraft");
		}
	}

}
