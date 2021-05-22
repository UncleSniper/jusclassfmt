package org.unclesniper.classfmt;

public class IllegalModifiedUTF8InitiatorByteClassFileFormatException
		extends InvalidModifiedUTF8ClassFileFormatException {

	private final int illegalInitiator;

	public IllegalModifiedUTF8InitiatorByteClassFileFormatException(int invalidOffset, int illegalInitiator) {
		super("Illegal initiator byte "
				+ IllegalModifiedUTF8InitiatorByteClassFileFormatException.toHex(illegalInitiator)
				+ " in modified UTF-8 string at offset " + invalidOffset, invalidOffset);
		this.illegalInitiator = illegalInitiator;
	}

	public int getIllegalInitiator() {
		return illegalInitiator;
	}

	public static String toHex(int initiator) {
		return "0x" + Integer.toHexString((initiator & 0xFF) | 0x100).substring(1).toUpperCase();
	}

}
