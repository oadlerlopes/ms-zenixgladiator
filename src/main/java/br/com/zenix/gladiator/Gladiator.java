package br.com.zenix.gladiator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

import br.com.zenix.core.spigot.Core;
import br.com.zenix.gladiator.managers.Manager;

public class Gladiator extends Core {

	private static Manager manager;

	public void onLoad() {
		super.onLoad();
		
		String worldName = "gladiator";
		File file = new File(worldName);
		deleteDir(file);

		String worldName2 = "backup";
		File file2 = new File(worldName2);

		copyFolder(file2, file);

		for (World world : Bukkit.getWorlds()) {
			world.setThundering(false);
			world.setStorm(false);
			world.setAutoSave(false);
			world.setWeatherDuration(1000);
			world.setTime(6000L);
		}
	}

	public boolean copyFolder(File src, File dest) {
		try {
			if (src.isDirectory()) {
				if (!dest.exists())
					dest.mkdir();

				String files[] = src.list();

				for (String file : files) {
					File srcFile = new File(src, file);
					File destFile = new File(dest, file);
					copyFolder(srcFile, destFile);
				}
			} else {
				InputStream in = new FileInputStream(src);
				OutputStream out = new FileOutputStream(dest);

				byte[] buffer = new byte[1024];

				int length;
				while ((length = in.read(buffer)) > 0)
					out.write(buffer, 0, length);

				in.close();
				out.close();
			}
			return true;
		} catch (Exception e) {
			getManager().getLogger().error("Error when the plugin is trying to copy the path " + src.getAbsolutePath()
					+ " to " + dest.getAbsolutePath() + ".", e);
		}
		return false;
	}

	void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				if (!Files.isSymbolicLink(f.toPath())) {
					deleteDir(f);
				}
			}
		}
		file.delete();
	}

	public boolean deleteFile(Path path) {
		Preconditions.checkNotNull(path);
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				public FileVisitResult visitFileFailed(Path file, IOException e) {
					return handleException(e);
				}

				private FileVisitResult handleException(IOException e) {
					e.printStackTrace();
					return FileVisitResult.TERMINATE;
				}

				public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
					if (e != null)
						return handleException(e);
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
			return true;
		} catch (IOException e) {
			getManager().getLogger().error("Error when the plugin is trying to delete the path " + path, e);
			return false;
		}
	}

	public void onEnable() {

		super.onEnable();
		manager = new Manager(this);

		if (!isCorrectlyStarted())
			return;
	}

	public void onDisable() {
		super.onDisable();

		for (Player player : Bukkit.getOnlinePlayers()) {
			getManager().getGamerManager().getGamer(player).update();
		}
	}

	public static Manager getManager() {
		return manager;
	}
}
