package br.com.zenix.gladiator.player.gamer.inventory;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import br.com.zenix.core.spigot.player.item.ItemBuilder;
import br.com.zenix.gladiator.commands.base.GladiatorListener;
import br.com.zenix.gladiator.gamer.Gamer;

public class PreferencesInventory extends GladiatorListener {

	public static final HashMap<UUID, Integer> inventoryIndex = new HashMap<>();

	public void generate(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 27, "Preferências");
		int index = inventoryIndex.containsKey(player.getUniqueId()) ? inventoryIndex.get(player.getUniqueId()) : 1;
		update(player, inventory, index);
		player.openInventory(inventory);
	}

	public void update(Player player, Inventory inventory, int index) {
		inventoryIndex.put(player.getUniqueId(), index);
		Gamer gamer = getManager().getGamerManager().getGamer(player);

		inventory.clear();

		inventory.setItem(10, new ItemBuilder(Material.BOOK).setEnchant(Enchantment.DURABILITY, 1).setName("§eScoreboard")
				.setDescription("§7Desative a sua §escoreboard§7 e tenha", "§7um ganho significativo em seus frames!", "", "§aClique para " + (gamer.isScore() ? "desativar" : "ativar") + " sua scoreboard").getStack());

		inventory.setItem(12, new ItemBuilder(Material.SLIME_BALL).setEnchant(Enchantment.DURABILITY, 1).setName("§eLimpar gladiator").setDescription("§7Limpe a arena §agladiator§7 de tempo", "§7em tempo e termine mais rapido!", "",
				"§aClique para aumentar o tempo! Atual: §7" + getManager().getUtils().toTime(getManager().getGamerManager().getGamer(player).getTimeToClear())).getStack());

		player.playSound(player.getLocation(), Sound.CLICK, 5F, 5F);
		player.updateInventory();
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getClickedInventory() != null) {
			if (event.getClickedInventory().getTitle().startsWith("Preferências")) {
				event.setCancelled(true);
				if (event.getCurrentItem() != null) {

					if (!inventoryIndex.containsKey(player.getUniqueId()))
						inventoryIndex.put(player.getUniqueId(), 1);

					Material type = event.getCurrentItem().getType();
					Gamer gamer = getManager().getGamerManager().getGamer(player);

					if (type == Material.AIR)
						return;

					if (type.equals(Material.BOOK)) {
						player.closeInventory();
						player.sendMessage("§c§lERRO §fEsta opção estará disponível em breve.");
						
						
						/***
						 * if (gamer.isScore()) {
						 * 
						 * gamer.setScore(false);
						 * 
						 * Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getManager().getPlugin(),
						 * new Runnable() { public void run() {
						 * player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); } },
						 * 20L);
						 * 
						 * player.sendMessage("§6§lSETTINGS §fA sua scoreboard foi " + (gamer.isScore()
						 * ? "desativada" : "ativada") + "!"); } else { gamer.setScore(true);
						 * 
						 * player.sendMessage("§6§lSETTINGS §fA sua scoreboard foi " + (gamer.isScore()
						 * ? "desativada" : "ativada") + "!"); }
						 */
					}

					if (type.equals(Material.SLIME_BALL)) {
						player.closeInventory();

						if (gamer.getTimeToClear() == 300) {
							gamer.setTimeToClear(240);
						} else if (gamer.getTimeToClear() == 240) {
							gamer.setTimeToClear(180);
						} else if (gamer.getTimeToClear() == 180) {
							gamer.setTimeToClear(120);
						} else if (gamer.getTimeToClear() == 120) {
							gamer.setTimeToClear(60);
						} else if (gamer.getTimeToClear() == 60) {
							gamer.setTimeToClear(300);
						}

						player.sendMessage("§6§lSETTINGS §fO tempo para clear foi alterado para §e" + getManager().getUtils().toTime(gamer.getTimeToClear()));
					}

				}
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (inventoryIndex.containsKey(event.getPlayer().getUniqueId()))
			inventoryIndex.remove(event.getPlayer().getUniqueId());
	}

	public String formatOldTime(Integer i) {
		int minutes = i.intValue() / 60;
		int seconds = i.intValue() % 60;
		String disMinu = (minutes < 10 ? "" : "") + minutes;
		String disSec = (seconds < 10 ? "0" : "") + seconds;
		String formattedTime = disMinu + ":" + disSec;
		return formattedTime;
	}

	public int[] range(int start, int stop) {
		int[] result = new int[stop - start];

		for (int i = 0; i < stop - start; i++)
			result[i] = start + i;

		return result;
	}

}
