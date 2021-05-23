package org.unclesniper.classfmt;

public class PrematureEndOfStreamClassFileFormatException extends ClassFileFormatException {

	public PrematureEndOfStreamClassFileFormatException() {
		super("Unexpected end of class file");
	}

}
