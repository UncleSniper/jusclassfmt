package org.unclesniper.classfmt;

public class OutOfSequenceSinkCallClassFileFormatException extends ClassFileFormatException {

	private final String foundCall;

	private final String expectedCall;

	public OutOfSequenceSinkCallClassFileFormatException(String foundCall, String expectedCall) {
		super(OutOfSequenceSinkCallClassFileFormatException.makeMessage(foundCall, expectedCall));
		this.foundCall = foundCall;
		this.expectedCall = expectedCall;
	}

	public String getFoundCall() {
		return foundCall;
	}

	public String getExpectedCall() {
		return expectedCall;
	}

	private static String makeMessage(String foundCall, String expectedCall) {
		StringBuilder builder = new StringBuilder();
		if(foundCall == null)
			builder.append("Unexpected end of calls");
		else {
			builder.append("Out-of-sequence call to ");
			builder.append(foundCall);
		}
		builder.append(", expected ");
		if(expectedCall == null)
			builder.append("no further calls");
		else {
			builder.append("call to ");
			builder.append(expectedCall);
		}
		return builder.toString();
	}

}
