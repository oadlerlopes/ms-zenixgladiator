package br.com.zenix.gladiator.warps.type;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */
public enum WarpType {

	NONE("Spawn", false),
	SPAWN("Spawn", false);

	private String name;
	private boolean status;

	private WarpType(String name, boolean status) {
		this.name = name;
		this.status = status;
	}

	public String getName() {
		return name;
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public void setStatus(boolean status){
		this.status = status;
	}

	public static boolean contains(String warp) {
		for (WarpType warpType : WarpType.values()) {
			if (warp.equalsIgnoreCase(warpType.toString()))
				return true;
		}
		if (warp.contains("simulator"))
			return true;

		return false;
	}

	public static WarpType getFromString(String warpName) {
		for (WarpType warpType : WarpType.values()) {
			if (warpType.getName().equalsIgnoreCase(warpName))
				return warpType;
		}
		
		warpName = warpName.toLowerCase();
		
		if (warpName.contains("arena"))
			return WarpType.SPAWN;
		
		return null;
	}

}

