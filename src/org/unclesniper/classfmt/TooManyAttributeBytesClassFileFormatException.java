package org.unclesniper.classfmt;

public class TooManyAttributeBytesClassFileFormatException extends ClassFileFormatException {

	public TooManyAttributeBytesClassFileFormatException() {
		super("Received more bytes in calls to attributeBytes() than announced in call to beginAttribute()");
	}

}
