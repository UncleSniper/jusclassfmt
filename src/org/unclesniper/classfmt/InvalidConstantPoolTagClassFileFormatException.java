package org.unclesniper.classfmt;

public class InvalidConstantPoolTagClassFileFormatException extends ClassFileFormatException {

	private final int invalidCode;

	public InvalidConstantPoolTagClassFileFormatException(int invalidCode) {
		super("Invalid constant pool tag: " + invalidCode);
		this.invalidCode = invalidCode;
	}

	public int getInvalidCode() {
		return invalidCode;
	}

}
