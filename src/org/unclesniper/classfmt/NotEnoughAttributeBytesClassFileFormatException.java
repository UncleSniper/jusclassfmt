package org.unclesniper.classfmt;

public class NotEnoughAttributeBytesClassFileFormatException extends ClassFileFormatException {

	public NotEnoughAttributeBytesClassFileFormatException() {
		super("Received fewer bytes in calls to attributeBytes() than announced in call to beginAttribute()");
	}

}
