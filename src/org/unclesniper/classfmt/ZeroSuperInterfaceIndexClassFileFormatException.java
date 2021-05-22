package org.unclesniper.classfmt;

public class ZeroSuperInterfaceIndexClassFileFormatException extends ClassFileFormatException {

	public ZeroSuperInterfaceIndexClassFileFormatException() {
		super("Super interface index is zero");
	}

}
