package org.unclesniper.classfmt;

public class BadMagicClassFileFormatException extends ClassFileFormatException {

	private final int badMagic;

	public BadMagicClassFileFormatException(int badMagic) {
		super("Expected class file magic 0xCAFEBABE, but got " + BadMagicClassFileFormatException.toHex(badMagic));
		this.badMagic = badMagic;
	}

	public int getBadMagic() {
		return badMagic;
	}

	public static String toHex(int magic) {
		return "0x" + Long.toHexString(((long)magic & 0xFFFFFFFFL) | 0x100000000L).substring(1).toUpperCase();
	}

}
