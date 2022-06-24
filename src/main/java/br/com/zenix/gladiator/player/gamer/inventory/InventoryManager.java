package br.com.zenix.gladiator.player.gamer.inventory;

import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.managers.managements.constructor.Management;

public class InventoryManager extends Management {

	private PreferencesInventory preferencesInventory;

	public InventoryManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		this.preferencesInventory = new PreferencesInventory();

		return preferencesInventory != null;
	}
	
	public PreferencesInventory getPreferencesInventory() {
		return preferencesInventory;
	}

}
