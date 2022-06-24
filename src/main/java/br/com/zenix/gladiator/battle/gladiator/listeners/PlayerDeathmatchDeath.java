package br.com.zenix.gladiator.battle.gladiator.listeners;

import java.text.DecimalFormat;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.core.plugin.data.handler.DataHandler;
import br.com.zenix.core.plugin.data.handler.type.DataType;
import br.com.zenix.core.spigot.player.account.Account;
import br.com.zenix.core.spigot.player.league.player.PlayerLeague;
import br.com.zenix.gladiator.battle.gladiator.managements.constructor.BattleConstructor;
import br.com.zenix.gladiator.battle.gladiator.managements.containers.GladiatorMode;
import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.gamer.Gamer;
import br.com.zenix.gladiator.warps.constructor.ItemConstructor;
import br.com.zenix.gladiator.warps.type.WarpType;

public class PlayerDeathmatchDeath extends GladiatorListener {

	private void cleanUp(final Player player) {
		new BukkitRunnable() {
			public void run() {
				player.closeInventory();
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);

				getManager().getGamerManager().resetPlayer(player);
				getManager().getGamerManager().removePottionEffects(player);
				getManager().getConfigManager().teleportPlayer(player, WarpType.SPAWN);

				player.playSound(player.getLocation(), Sound.LEVEL_UP, 4.0F, 4.0F);

				new ItemConstructor(getManager(), player, WarpType.SPAWN);
				getManager().getGladiatorManager().getFight().removeFight(player.getUniqueId());
				
				getManager().getHologramManager().scoreHologram.hide(player);
				getManager().getHologramManager().scoreHologram.show(player);
				
				getManager().getHologramManager().scoreHologramDeaths.hide(player);
				getManager().getHologramManager().scoreHologramDeaths.show(player);
				
				getManager().getHologramManager().scoreHologramMKS.hide(player);
				getManager().getHologramManager().scoreHologramMKS.show(player);
				
			}
		}.runTaskLater(getManager().getPlugin(), 1L);
		
		new BukkitRunnable() {
			public void run() {
				getManager().getHologramManager().scoreHologram.hide(player);
				getManager().getHologramManager().scoreHologram.show(player);
				
				getManager().getHologramManager().scoreHologramDeaths.hide(player);
				getManager().getHologramManager().scoreHologramDeaths.show(player);
				
				getManager().getHologramManager().scoreHologramMKS.hide(player);
				getManager().getHologramManager().scoreHologramMKS.show(player);
			}
		}.runTaskLater(getManager().getPlugin(), 4L);
	}

	private void lose(GladiatorMode gladiatorMode, Player loser, Player winner) {
		cleanUp(loser);

		Gamer gamer = getManager().getGamerManager().getGamer(loser);
		Account account = gamer.getAccount();

		DataHandler data = account.getDataHandler();
		data.getValue(DataType.GLADIATOR_LOSE).setValue(data.getValue(DataType.GLADIATOR_LOSE).getValue() + 1);
		data.getValue(DataType.GLADIATOR_KILLSTREAK).setValue(0);
		data.update(DataType.GLADIATOR_LOSE);
		data.update(DataType.GLADIATOR_KILLSTREAK);
	}

	private void win(GladiatorMode gladiatorMode, Player winner, Player loser) {
		cleanUp(winner);

		Gamer gamer = getManager().getGamerManager().getGamer(winner);
		Account account = gamer.getAccount();

		DataHandler data = account.getDataHandler();
		data.getValue(DataType.GLADIATOR_WIN).setValue(data.getValue(DataType.GLADIATOR_WIN).getValue() + 1);
		data.getValue(DataType.GLADIATOR_KILLSTREAK)
				.setValue(data.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() + 1);
		data.update(DataType.GLADIATOR_WIN);
		data.update(DataType.GLADIATOR_KILLSTREAK);

		getManager().getLogger()
				.log("O player " + winner.getName() + " ganhou o gladiator de " + loser.getName() + "!");
	}

	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {

			if (getManager().getGamerManager().getGamer(event.getEntity().getUniqueId()).getWarp().equals(WarpType.SPAWN)
					&& getManager().getGamerManager().getGamer(event.getEntity().getKiller().getUniqueId()).getWarp()
							.equals(WarpType.SPAWN)) {

				DecimalFormat dm = new DecimalFormat("##.#");
				final Player loser = event.getEntity();
				final Player winner = loser.getKiller();

				int i = 0;
				for (ItemStack soup : winner.getInventory().getContents()) {
					if ((soup != null) && (soup.getType() != Material.AIR)
							&& (soup.getType() == Material.MUSHROOM_SOUP)) {
						i += soup.getAmount();
					}
				}

				loser.sendMessage("§c§lDERROTA §fVocê §c§lPERDEU§f a batalha contra §c" + winner.getName() + "§f com "
						+ dm.format(winner.getHealth() / 2.0D) + " coracoes e " + i + " sopas restantes.");
				winner.sendMessage("§a§lVITORIA §fVocê §a§lVENCEU§f a batalha contra §a" + loser.getName() + "§f com "
						+ dm.format(winner.getHealth() / 2.0D) + " coracoes e " + i + " sopas restantes.");

				for (ItemStack itemStack : event.getDrops()) {
					itemStack.setType(Material.AIR);
				}

				GladiatorMode gladiatorMode = getManager().getGladiatorManager().getFight();

				Account accountWinner = getManager().getCoreManager().getAccountManager().getAccount(winner);
				Account accountLoser = getManager().getCoreManager().getAccountManager().getAccount(loser);

				new PlayerLeague(winner, loser).prizeLeague();

				win(gladiatorMode, winner, loser);

				DataHandler dataHandlerFirst = accountLoser.getDataHandler();

				if (accountLoser != null) {
					if (dataHandlerFirst.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() >= 10) {
						Bukkit.broadcastMessage("§4§lKILLSTREAK §1§l" + accountLoser.getPlayer().getName()
								+ "§f perdeu seu §6§lKILLSTREAK DE "
								+ dataHandlerFirst.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() + "§f para §c§l"
								+ accountWinner.getPlayer().getName() + "§f");
					}
				}

				lose(gladiatorMode, loser, winner);

				controlPlayer(winner, loser);

				DataHandler dataHandler = accountWinner.getDataHandler();

				if (dataHandler.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() % 10 == 0) {
					if (dataHandler.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() >= 10) {
						Bukkit.broadcastMessage("§4§lKILLSTREAK §1§l" + accountWinner.getPlayer().getName()
								+ "§f conseguiu um §6§lKILLSTREAK DE "
								+ dataHandler.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() + "§f");
					}
				}

				if (dataHandler.getValue(DataType.GLADIATOR_MOST_KILLSTREAK).getValue() < dataHandler
						.getValue(DataType.GLADIATOR_KILLSTREAK).getValue()) {
					dataHandler.getValue(DataType.GLADIATOR_MOST_KILLSTREAK)
							.setValue(dataHandler.getValue(DataType.GLADIATOR_KILLSTREAK).getValue());

					dataHandler.update(DataType.GLADIATOR_MOST_KILLSTREAK);
				}

				new PlayerLeague(winner).checkRank(accountWinner);
				new PlayerLeague(loser).checkRank(accountLoser);

				getManager().getHologramManager().scoreHologram.hide(winner);
				getManager().getHologramManager().scoreHologram.show(winner);
				
				getManager().getHologramManager().scoreHologram.hide(loser);
				getManager().getHologramManager().scoreHologram.show(loser);
				
				getManager().getHologramManager().scoreHologramDeaths.hide(winner);
				getManager().getHologramManager().scoreHologramDeaths.show(winner);
				
				getManager().getHologramManager().scoreHologramDeaths.hide(loser);
				getManager().getHologramManager().scoreHologramDeaths.show(loser);
				
				getManager().getHologramManager().scoreHologramMKS.hide(winner);
				getManager().getHologramManager().scoreHologramMKS.show(winner);
				
				getManager().getHologramManager().scoreHologramMKS.hide(loser);
				getManager().getHologramManager().scoreHologramMKS.show(loser);
			}

		} else {
			GladiatorMode gladiatorMode = getManager().getGladiatorManager().getFight();

			if (!getManager().getGamerManager().getGamer(event.getEntity()).getWarp().equals(WarpType.SPAWN)
					|| !gladiatorMode.isFighting(event.getEntity().getUniqueId())) {
				return;
			}

			BattleConstructor battleConstructor = gladiatorMode.getBattle(event.getEntity().getUniqueId());
			Player winner = battleConstructor.getOpponent();

			cleanUp(event.getEntity());
			cleanUp(winner);

			controlPlayer(winner, event.getEntity());

			new PlayerLeague(winner, event.getEntity()).prizeLeague();

			winner.sendMessage("§2§lVITÓRIA §fSeu oponente §2§lSE MATOU§f, portanto você §a§lVENCEU§f a batalha.");

			for (Player players : Bukkit.getOnlinePlayers()) {
				if (getManager().getGladiatorManager().getFight().isFighting(players.getUniqueId())) {
					players.hidePlayer(winner);
				}
				if (getManager().getGamerManager().getGamer(players).isSpectate()) {
					players.hidePlayer(winner);
				}
			}
			
			getManager().getHologramManager().scoreHologram.hide(winner);
			getManager().getHologramManager().scoreHologram.show(winner);
			
			getManager().getHologramManager().scoreHologramDeaths.hide(winner);
			getManager().getHologramManager().scoreHologramDeaths.show(winner);
			
			getManager().getHologramManager().scoreHologramMKS.hide(winner);
			getManager().getHologramManager().scoreHologramMKS.show(winner);
		}
	}

	private void controlPlayer(final Player winner, final Player loser) {
		new BukkitRunnable() {
			public void run() {
				GladiatorMode gladiatorMode = getManager().getGladiatorManager().getFight();

				for (Player online : Bukkit.getOnlinePlayers()) {
					if (!gladiatorMode.isFighting(online.getUniqueId())
							&& !getManager().getGamerManager().getGamer(online).isSpectate()) {
						online.showPlayer(winner);
						online.showPlayer(loser);
					}
					if (!getManager().getAdminManager().isAdmin(online)) {
						winner.showPlayer(online);
						loser.showPlayer(online);
					}

					if (getManager().getGamerManager().getGamer(online).isSpectate()) {
						winner.hidePlayer(online);
						loser.hidePlayer(online);
					}

				}
			}
		}.runTaskLater(getManager().getPlugin(), 4L);

	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerQuit(PlayerQuitEvent event) {
		Player loser = event.getPlayer();
		GladiatorMode gladiatorMode = getManager().getGladiatorManager().getFight();

		if (!gladiatorMode.isFighting(loser.getUniqueId())) {
			return;
		}

		BattleConstructor battleConstructor = gladiatorMode.getBattle(loser.getUniqueId());
		Player winner = battleConstructor.getOpponent();

		cleanUp(loser);
		cleanUp(winner);

		Account account = getManager().getGamerManager().getGamer(winner).getAccount();

		DataHandler data = account.getDataHandler();
		data.getValue(DataType.GLADIATOR_WIN).setValue(data.getValue(DataType.GLADIATOR_WIN).getValue() + 1);
		data.getValue(DataType.GLADIATOR_KILLSTREAK)
				.setValue(data.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() + 1);
		data.update(DataType.GLADIATOR_WIN);
		data.update(DataType.GLADIATOR_KILLSTREAK);

		getManager().getGamerManager().getGamer(winner).update();
		getManager().getGamerManager().getGamer(loser).update();

		Random random = new Random();
		int r = random.nextInt(6);
		int debit = r;

		if (account.isDoubleRunning()) {
			if (data.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() >= 70) {
				debit = (((data.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() * 2) / 3) + r);
			} else {
				debit = (((data.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() * 2) / 2) + r);
			}
			data.getValue(DataType.GLOBAL_XP).setValue(data.getValue(DataType.GLOBAL_XP).getValue() + debit);
			data.getValue(DataType.GLOBAL_XP);

			account.getPlayer().sendMessage("§9§lXP §fVocê ganhou §9§l" + debit + "XPs §7(DoubleXP)");
		} else {
			if (data.getValue(DataType.GLADIATOR_KILLSTREAK).getValue() == 0) {
				debit = (((4 * 2) / 2) + r);
			}
			data.getValue(DataType.GLOBAL_XP).setValue(data.getValue(DataType.GLOBAL_XP).getValue() + debit);
			data.getValue(DataType.GLOBAL_XP);

			account.getPlayer().sendMessage("§9§lXP §fVocê ganhou §9§l" + debit + "XPs §7(DoubleXP)");
		}

		controlPlayer(winner, loser);
		
		getManager().getHologramManager().scoreHologram.hide(winner);
		getManager().getHologramManager().scoreHologram.show(winner);

		getManager().getHologramManager().scoreHologramDeaths.hide(winner);
		getManager().getHologramManager().scoreHologramDeaths.show(winner);
		
		getManager().getHologramManager().scoreHologramMKS.hide(winner);
		getManager().getHologramManager().scoreHologramMKS.show(winner);
		
		winner.sendMessage("§2§lVITÓRIA §fSeu oponente §2§lDESISTIU§f de lutar, portanto você §a§lVENCEU§f a batalha.");

		for (Player all : Bukkit.getOnlinePlayers()) {
			if (!gladiatorMode.isFighting(all.getUniqueId()))
				all.showPlayer(winner);
		}

	}

}
