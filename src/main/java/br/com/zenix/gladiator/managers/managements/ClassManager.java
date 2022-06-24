package br.com.zenix.gladiator.managers.managements;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.event.Listener;

import br.com.zenix.core.plugin.utilitaries.loader.Getter;
import br.com.zenix.gladiator.commands.base.GladiatorCommand;
import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.managers.managements.constructor.Management;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class ClassManager extends Management {

	public ClassManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		return load();
	}

	public boolean load() {
		getLogger().log("Starting trying to load all the classes of commands and listeners of the plugin.");

		for (Class<?> classes : Getter.getClassesForPackage(getManager().getPlugin(), "br.com.zenix.gladiator")) {
			try {
				if (GladiatorCommand.class.isAssignableFrom(classes) && !classes.getSimpleName().equals("GladiatorCommand")) {
					GladiatorCommand command = (GladiatorCommand) classes.newInstance();
					((CraftServer) Bukkit.getServer()).getCommandMap().register(command.getName(), command);
					getManager().getLogger().debug("The commmand " + classes.getSimpleName() + " has loaded.");
				}
			} catch (Exception exception) {
				getManager().getLogger().error("Error in load command - " + classes.getSimpleName() + "!",
						exception);
				return false;
			}

			try {
				Listener listener = null;

				if (classes.getSimpleName().equals("")) {
					continue;
				} else if (Listener.class.isAssignableFrom(classes)
						&& !GladiatorListener.class.isAssignableFrom(classes)) {
					listener = (Listener) classes.getConstructor(Manager.class).newInstance(getManager());
				}
				if (Listener.class.isAssignableFrom(classes) && GladiatorListener.class.isAssignableFrom(classes)) {
					listener = (Listener) classes.getConstructor().newInstance();
				}
				if (listener == null)
					continue;
				Bukkit.getPluginManager().registerEvents(listener, getManager().getPlugin());
				getManager().getLogger().debug("The listener " + listener.getClass().getSimpleName() + " has loaded!");
			} catch (Exception exception) {
				getLogger().error("Error to load the listener " + classes.getSimpleName() + ", stopping the process!",
						exception);
				return false;
			}
		}
		return true;
	}

}