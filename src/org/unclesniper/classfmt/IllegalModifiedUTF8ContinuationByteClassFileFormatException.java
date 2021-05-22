package org.unclesniper.classfmt;

public class IllegalModifiedUTF8ContinuationByteClassFileFormatException
		extends InvalidModifiedUTF8ClassFileFormatException {

	private final int sequenceOffset;

	private final int sequenceLength;

	private final int illegalContinuation;

	public IllegalModifiedUTF8ContinuationByteClassFileFormatException(int invalidOffset, int illegalContinuation,
			int sequenceOffset, int sequenceLength) {
		super("Illegal continuation byte "
				+ IllegalModifiedUTF8InitiatorByteClassFileFormatException.toHex(illegalContinuation)
				+ " in modified UTF-8 string after byte " + sequenceOffset + " of a " + sequenceLength
				+ " byte sequence at offset " + invalidOffset, invalidOffset);
		this.sequenceOffset = sequenceOffset;
		this.sequenceLength = sequenceLength;
		this.illegalContinuation = illegalContinuation;
	}

	public int getSequenceOffset() {
		return sequenceOffset;
	}

	public int getSequenceLength() {
		return sequenceLength;
	}

	public int getIllegalContinuation() {
		return illegalContinuation;
	}

}
