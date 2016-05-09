package util;

import java.awt.Color;

public enum Rarity {
	COMMON, UNCOMMON, RARE, MYTHIC_RARE, SPECIAL, BASIC_LAND;
	
	public static Rarity stringToRarity(String s){
		switch(s.toLowerCase()){
		case "common": return COMMON;
		case "uncommon": return UNCOMMON;
		case "rare": return RARE;
		case "mythic rare": return MYTHIC_RARE;
		case "special": return SPECIAL;
		case "basic land": return BASIC_LAND;
		default: System.out.println(s); assert false; return null;
		}
	}
	
	private static final Color GOLD = new Color(255,215,0);
	private static final Color SILVER = new Color(255,215,0);
	
	public static Color stringToColor(Rarity r) {
		switch(r){
		case COMMON: case BASIC_LAND: return Color.BLACK;
		case UNCOMMON: return SILVER;
		case RARE: case SPECIAL: return GOLD;
		case MYTHIC_RARE: return Color.ORANGE;
		default: return Color.BLACK;
		}
	}
}
