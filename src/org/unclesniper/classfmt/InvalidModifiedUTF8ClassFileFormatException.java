package org.unclesniper.classfmt;

public class InvalidModifiedUTF8ClassFileFormatException extends ClassFileFormatException {

	private final int invalidOffset;

	public InvalidModifiedUTF8ClassFileFormatException(String message, int invalidOffset) {
		super(message);
		this.invalidOffset = invalidOffset;
	}

	public int getInvalidOffset() {
		return invalidOffset;
	}

}
