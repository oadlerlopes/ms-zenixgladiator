package br.com.zenix.gladiator.commands.base;

import org.bukkit.event.Listener;

import br.com.zenix.gladiator.Gladiator;
import br.com.zenix.gladiator.managers.Manager;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */
public class GladiatorListener implements Listener {

	public Manager getManager() {
		return Gladiator.getManager();
	}

}
