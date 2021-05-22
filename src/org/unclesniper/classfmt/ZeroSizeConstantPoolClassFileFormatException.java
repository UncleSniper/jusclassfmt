package org.unclesniper.classfmt;

public class ZeroSizeConstantPoolClassFileFormatException extends ClassFileFormatException {

	public ZeroSizeConstantPoolClassFileFormatException() {
		super("Constant pool has size zero");
	}

}
