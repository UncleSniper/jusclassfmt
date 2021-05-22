package org.unclesniper.classfmt;

public class InvalidMethodHandleReferenceKindClassFileFormatException extends ClassFileFormatException {

	private final int invalidCode;

	public InvalidMethodHandleReferenceKindClassFileFormatException(int invalidCode) {
		super("Invalid method handle reference kind: " + invalidCode);
		this.invalidCode = invalidCode;
	}

	public int getInvalidCode() {
		return invalidCode;
	}

}
