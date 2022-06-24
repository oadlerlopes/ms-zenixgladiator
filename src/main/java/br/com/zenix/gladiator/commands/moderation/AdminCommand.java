package br.com.zenix.gladiator.commands.moderation;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zenix.core.spigot.player.events.PlayerAdminEvent;
import br.com.zenix.gladiator.commands.base.GladiatorCommand;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */
public class AdminCommand extends GladiatorCommand {

	public AdminCommand() {
		super("admin", "Enter or leave in administration mode.");
	}

	@Override
	public boolean execute(CommandSender commandSender, String label, String[] args) {
		if (!isPlayer(commandSender)) {
			return false;
		}

		if (!hasPermission(commandSender, "admin")) {
			return false;
		}

		Player player = (Player) commandSender;

		boolean enter = !getManager().getAdminManager().isAdmin(player);
		PlayerAdminEvent event = new PlayerAdminEvent(player, !getManager().getAdminManager().isAdmin(player));

		Bukkit.getPluginManager().callEvent(event);

		getManager().getAdminManager().setAdmin(player, enter);
		sendWarning("O staffer " + player.getName() + " " + (enter ? "entrou no" : "saiu do") + " modo ADMIN");
		return true;
	}

}

