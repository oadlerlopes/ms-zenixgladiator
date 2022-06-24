package br.com.zenix.gladiator.player.admin;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.player.admin.AdminManager.AdminLevel;

public class Vanish extends GladiatorListener {

	static HashMap<UUID, AdminLevel> vanished = new HashMap<>();

	public Vanish() {
	}

	public Vanish(Manager manager) {

	}

	public void makeVanished(Player p) {
		if (p.hasPermission("commons.tag.dono")) {
			makeVanished(p, AdminLevel.DONO);
		} else if (p.hasPermission("commons.tag.admin")) {
			makeVanished(p, AdminLevel.ADMIN);
		} else if (p.hasPermission("commons.tag.gerente")) {
			makeVanished(p, AdminLevel.ADMIN);
		} else if (p.hasPermission("commons.tag.mod")) {
			makeVanished(p, AdminLevel.MODPLUS);
		} else if (p.hasPermission("commons.tag.trial")) {
			makeVanished(p, AdminLevel.MOD);
		}
	}

	public void makeVanished(Player p, AdminLevel level) {
		if (level.equals(AdminLevel.MOD)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!getManager().getGladiatorManager().getFight().isFighting(player.getUniqueId())
						|| !getManager().getGamerManager().getGamer(player).isSpectate()) {
					player.showPlayer(p);
				} else {
					player.hidePlayer(p);
				}
				if (!player.getName().equals(p.getName())) {
					if (!player.hasPermission("commons.tag.trial")) {
						player.hidePlayer(p);
					}
				}
			}
		} else if (level.equals(AdminLevel.MODPLUS)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!getManager().getGladiatorManager().getFight().isFighting(player.getUniqueId())
						|| !getManager().getGamerManager().getGamer(player).isSpectate()) {
					player.showPlayer(p);
				} else {
					player.hidePlayer(p);
				}
				if (!player.getName().equals(p.getName())) {
					if (!player.hasPermission("commons.tag.mod")) {
						player.hidePlayer(p);
					}
				}
			}
		} else if (level.equals(AdminLevel.ADMIN)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!getManager().getGladiatorManager().getFight().isFighting(player.getUniqueId())
						|| !getManager().getGamerManager().getGamer(player).isSpectate()) {
					player.showPlayer(p);
				} else {
					player.hidePlayer(p);
				}
				if (!player.getName().equals(p.getName())) {
					if (!player.hasPermission("commons.tag.admin") || !player.hasPermission("commons.tag.gerente")) {
						player.hidePlayer(p);
					}
				}
			}
		} else if (level.equals(AdminLevel.DONO)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!getManager().getGladiatorManager().getFight().isFighting(player.getUniqueId())
						|| !getManager().getGamerManager().getGamer(player).isSpectate()) {
					player.showPlayer(p);
				} else {
					player.hidePlayer(p);
				}
				if (!player.getName().equals(p.getName())) {
					if (!player.hasPermission("commons.tag.dono")) {
						player.hidePlayer(p);
					}
				}
			}
		}
		vanished.put(p.getUniqueId(), level);
	}

	public boolean isVanished(Player p) {
		return (vanished.containsKey(p.getUniqueId()))
				&& (!((AdminLevel) vanished.get(p.getUniqueId())).equals(AdminLevel.PLAYER));
	}

	public AdminLevel getPlayerLevel(Player p) {
		return (AdminLevel) vanished.get(p.getUniqueId());
	}

	public void updateVanished() {
		for (Player p : Bukkit.getOnlinePlayers()) {

			if (getManager().getGladiatorManager().getFight().isFighting(p.getUniqueId())
					|| getManager().getGamerManager().getGamer(p).isSpectate()) {
				return;
			}

			if (isVanished(p)) {
				makeVanished(p, (AdminLevel) vanished.get(p.getUniqueId()));
			} else {
				makeVisible(p);
			}
		}
	}

	public void updateVanished(Player player) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!player.getName().equals(p.getName())) {
				if (isVanished(p)) {
					if (!player.hasPermission("commons.tag.trial")) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					}
				} else if (!getManager().getGladiatorManager().getFight().isFighting(player.getUniqueId())
						|| !getManager().getGamerManager().getGamer(player).isSpectate()) {
					if (!player.canSee(p)) {
						if (!getManager().getGladiatorManager().getFight().isFighting(player.getUniqueId())
								|| !getManager().getGamerManager().getGamer(player).isSpectate()) {
							player.showPlayer(p);
						} else {
							player.hidePlayer(p);
						}
					}
				}
			}
		}
	}

	public void removeVanished(Player p) {
		vanished.remove(p.getUniqueId());
	}

	public void makeVisible(Player p) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!getManager().getGladiatorManager().getFight().isFighting(player.getUniqueId())
					|| !getManager().getGamerManager().getGamer(player).isSpectate()) {
				player.showPlayer(p);
			} else {
				player.hidePlayer(p);
			}
		}
		vanished.put(p.getUniqueId(), AdminLevel.PLAYER);
	}
}
