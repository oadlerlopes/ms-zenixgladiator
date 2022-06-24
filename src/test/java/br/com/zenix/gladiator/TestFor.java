package br.com.zenix.hungergames;

/**
 * Copyright (C) Guilherme Fane, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */

public class TestFor {

	public static void main(String[] args) {

		int index = 2;

		for (int range : range(index * 7 - 7, (index + 3) * 7 - 7)) {
			System.out.println(range);
		}
	}

	public static int[] range(int start, int stop) {
		int[] result = new int[stop - start];

		for (int i = 0; i < stop - start; i++)
			result[i] = start + i;

		return result;
	}
}
