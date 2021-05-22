package org.unclesniper.classfmt;

public class PrematureEndOfModifiedUTF8ClassFileFormatException
		extends InvalidModifiedUTF8ClassFileFormatException {

	private final int sequenceOffset;

	private final int sequenceLength;

	public PrematureEndOfModifiedUTF8ClassFileFormatException(int invalidOffset,
			int sequenceOffset, int sequenceLength) {
		super("Modified UTF-8 string ends after byte " + sequenceOffset + " of a " + sequenceLength
				+ " byte sequence at offset " + invalidOffset, invalidOffset);
		this.sequenceOffset = sequenceOffset;
		this.sequenceLength = sequenceLength;
	}

	public int getSequenceOffset() {
		return sequenceOffset;
	}

	public int getSequenceLength() {
		return sequenceLength;
	}

}
