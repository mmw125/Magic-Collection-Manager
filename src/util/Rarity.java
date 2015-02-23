package util;

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
}
