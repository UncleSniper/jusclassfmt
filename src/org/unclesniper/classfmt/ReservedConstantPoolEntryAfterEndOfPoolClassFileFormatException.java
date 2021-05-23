package org.unclesniper.classfmt;

public class ReservedConstantPoolEntryAfterEndOfPoolClassFileFormatException extends ClassFileFormatException {

	public ReservedConstantPoolEntryAfterEndOfPoolClassFileFormatException() {
		super("Reserved constant pool entry after end of pool");
	}

}
