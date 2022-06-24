package br.com.zenix.gladiator.gamer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import br.com.zenix.core.spigot.player.account.Account;
import br.com.zenix.gladiator.Gladiator;
import br.com.zenix.gladiator.managers.Manager;
import br.com.zenix.gladiator.warps.type.WarpType;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class Gamer {

	private UUID uniqueId;

	private final Account account;

	private WarpType warp;
	private List<Block> blocks = new ArrayList<Block>();

	private boolean pvp = false;
	private boolean score = true;
	private boolean spectate = false;
	private Player spectatePlayer;

	private int timeToClear;

	private long waitTime = 0, inCombat = 0;

	private boolean scoreboard;

	public Gamer(Account account) {
		this.account = account;
		this.uniqueId = account.getUniqueId();

		scoreboard = false;
		this.spectate = false;

		warp = WarpType.NONE;
		timeToClear = 300;
	}

	public void addBlocks(Block block) {
		blocks.add(block);
	}

	public void removeBlocks(Block block) {
		blocks.remove(block);
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public boolean isScore() {
		return score;
	}

	public void setTimeToClear(int timeToClear) {
		this.timeToClear = timeToClear;
	}

	public int getTimeToClear() {
		return timeToClear;
	}

	public void setScore(boolean score) {
		this.score = score;
	}

	public Player getSpectatePlayer() {
		return spectatePlayer;
	}

	public void setSpectatePlayer(Player spectatePlayer) {
		this.spectatePlayer = spectatePlayer;
	}

	private Manager getManager() {
		return Gladiator.getManager();
	}

	public void update() {
		getManager().getGamerManager().forceUpdate(getAccount());
	}

	public boolean isSpectate() {
		return spectate;
	}

	public void setSpectate(boolean spectate) {
		this.spectate = spectate;
	}

	public WarpType getWarp() {
		return warp;
	}

	public UUID getUniqueId() {
		return uniqueId;
	}

	public boolean isScoreboard() {
		return scoreboard;
	}

	public long getWaitTime() {
		long time = waitTime - System.currentTimeMillis();
		return time > 0 ? time : 0;
	}

	public boolean inWaitTime() {
		return waitTime > System.currentTimeMillis();
	}

	public void addWait(int seconds) {
		waitTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
	}

	public void removeWaitTime() {
		waitTime = 0;
	}

	public boolean hasPvP() {
		return pvp;
	}

	public void setPvP(boolean pvp) {
		setPvPByMessage(pvp, false);
	}

	public void setWarp(WarpType warp) {
		this.warp = warp;
	}

	public void setPvPByMessage(boolean pvp, boolean message) {
		if (this.pvp == pvp)
			return;

		this.pvp = pvp;

		if (!message || account.getPlayer() == null)
			return;
	}

	public boolean inCombat() {
		return TimeUnit.MILLISECONDS.toSeconds(inCombat - System.currentTimeMillis()) > 0;
	}

	public void setCombat(int seconds) {
		inCombat = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
	}

	public void setScoreboard(boolean scoreboard) {
		this.scoreboard = scoreboard;
	}

	public int getTime() {
		return (int) TimeUnit.MILLISECONDS.toSeconds(getWaitTime());
	}

	public Account getAccount() {
		return account;
	}

}
