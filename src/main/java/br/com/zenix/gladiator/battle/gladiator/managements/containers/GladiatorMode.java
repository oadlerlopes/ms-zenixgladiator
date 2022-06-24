package br.com.zenix.gladiator.battle.gladiator.managements.containers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.zenix.gladiator.battle.gladiator.managements.RunnableToInvitation;
import br.com.zenix.gladiator.battle.gladiator.managements.constructor.BattleConstructor;
import br.com.zenix.gladiator.battle.gladiator.managements.custom.Custom;
import br.com.zenix.gladiator.battle.gladiator.modules.BattleModules;

public class GladiatorMode {

	public static final HashMap<UUID, BattleConstructor> fights = new HashMap<>();
	private static final HashMap<UUID, RunnableToInvitation> runnableToInvitations = new HashMap<>();
	private static final HashMap<UUID, Custom> customizations = new HashMap<>();

	public HashMap<UUID, BattleConstructor> getFighters() {
		return fights;
	}

	public BattleConstructor getBattle(UUID uniqueId) {
		return fights.get(uniqueId);
	}

	public HashMap<UUID, RunnableToInvitation> getInvitations() {
		return runnableToInvitations;
	}

	public RunnableToInvitation getInvitation(UUID uniqueId) {
		return runnableToInvitations.get(uniqueId);
	}

	public HashMap<UUID, Custom> getCustomizations() {
		return customizations;
	}

	public Custom getCustom(Player player) {
		return customizations.get(player.getUniqueId());
	}

	public void addCustomization(Player player) {
		customizations.put(player.getUniqueId(), new Custom());
	}

	public BattleConstructor addFight(Player player, Player player2, BattleModules battleModules, int soups) {
		fights.put(player.getUniqueId(), new BattleConstructor(battleModules, soups, player2, getCustom(player)));
		fights.put(player2.getUniqueId(), new BattleConstructor(battleModules, soups, player, getCustom(player2)));

		return fights.get(player.getUniqueId());
	}

	public void removeFight(UUID uuid) {
		fights.remove(uuid);
	}

	public void removeCustomization(Player player) {
		customizations.remove(player.getUniqueId());
	}

	public void addInvitation(Player player, Player player2, BattleModules battleModules) {
		runnableToInvitations.put(player.getUniqueId(), new RunnableToInvitation(player2, battleModules));
	}

	public void removeInvitation(Player player) {
		runnableToInvitations.remove(player.getUniqueId());
	}

	public boolean isFighting(UUID uuid) {
		return (fights.containsKey(uuid) || fights.keySet().contains(uuid)) ? true : false;
	}

	public boolean invited(UUID uuid) {
		return runnableToInvitations.keySet().contains(uuid);
	}

	public boolean customized(Player player) {
		return customizations.containsKey(player.getUniqueId());
	}

	public boolean recivedInvitation(UUID uuid1, UUID uuid2) {
		return runnableToInvitations.get(uuid2) != null
				&& runnableToInvitations.get(uuid2).gePlayerTo().equals(Bukkit.getPlayer(uuid1));
	}

}
