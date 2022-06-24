package br.com.zenix.gladiator.commands.base;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zenix.gladiator.Gladiator;
import br.com.zenix.gladiator.managers.Manager;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public abstract class GladiatorCommand extends Command {

	public boolean enabled = true;
	public String command = "§e§lCOMANDO §f";
	public static final String ERROR = "§c§lERROR §f";
	public static final String NO_PERMISSION = "§c§lPERMISSAO §f";
	public static final String OFFLINE = "§c§lOFFLINE §f";

	public GladiatorCommand(String name) {
		super(name);
	}

	public GladiatorCommand(String name, String description) {
		super(name, description, "", new ArrayList<String>());
	}

	public GladiatorCommand(String name, String description, List<String> aliases) {
		super(name, description, "", aliases);
	}

	public abstract boolean execute(CommandSender commandSender, String label, String[] args);

	public Manager getManager() {
		return Gladiator.getManager();
	}

	public Integer getInteger(String string) {
		return Integer.valueOf(string);
	}

	public boolean isPlayer(CommandSender sender) {
		return sender instanceof Player;
	}

	public boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public boolean hasPermission(CommandSender commandSender, String perm) {
		boolean hasPermission = commandSender.hasPermission("pvp.cmd." + perm);
		if (!hasPermission)
			sendPermissionMessage(commandSender);
		return hasPermission;
	}

	public boolean isUUID(String string) {
		try {
			UUID.fromString(string);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean isNick(String string) {
		return Bukkit.getOfflinePlayer(string) != null;
	}

	@SuppressWarnings("deprecation")
	public UUID getUUID(String args) {
		return isNick(args) ? Bukkit.getOfflinePlayer(args).getUniqueId() : Bukkit.getPlayer(args).getUniqueId();
	}

	public static String getError() {
		return ERROR;
	}

	public static String getOffline() {
		return OFFLINE;
	}

	public static String getNoPermission() {
		return NO_PERMISSION;
	}

	public String getArgs(String[] args, int starting) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = starting; i < args.length; i++) {
			stringBuilder.append(args[i] + " ");
		}
		return stringBuilder.toString();
	}

	public void sendNumericMessage(CommandSender commandSender) {
		commandSender.sendMessage("§cVocê informou um caractere com um número. Números não são permitidos.");
	}

	public void sendPermissionMessage(CommandSender commandSender) {
		commandSender.sendMessage("§cVocê não tem permissão.");
	}

	public void sendExecutorMessage(CommandSender commandSender) {
		commandSender.sendMessage("ERRO: Somente players podem usar esse comando.");
	}

	public void sendArgumentMessage(CommandSender commandSender, String command, String args) {
		commandSender.sendMessage("§aUse: §f" + args);
	}

	public void sendOfflinePlayerMessage(CommandSender commandSender, String player) {
		commandSender.sendMessage(getOffline() + "O player " + player + " está offline.");
	}

	public void sendMessage(CommandSender commandSender, String args) {
		commandSender.sendMessage("" + args);
	}

	public void sendWarning(String warning) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("zenix.admin")) {
				player.sendMessage("§7(!) " + warning + "");
			}
		}
	}
}
