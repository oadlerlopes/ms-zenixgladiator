package br.com.zenix.hungergames;

import java.io.File;
import java.net.URL;
import java.util.UUID;

/**
 * Copyright (C) Guilherme Fane, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */

public class TestFile {

	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());

	}

	public static final class FileNew {

		public FileNew(String name) {
			try {
				URL url = getClass().getResource(name);
				System.out.println("URL name: " + url);
				System.out.println("Path URL: " + url.getPath());
				System.out.println("File URL: " + url.getFile());

				File file = new File(url.getFile());
				System.out.println("Checking the file: " + file.getName() + "/" + file.getAbsolutePath());
				System.out.println("Exists: " + file.exists());
				System.out.println("Path: " + file.getPath());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
