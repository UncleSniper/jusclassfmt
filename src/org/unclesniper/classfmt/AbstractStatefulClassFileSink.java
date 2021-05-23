package org.unclesniper.classfmt;

public abstract class AbstractStatefulClassFileSink {

	private interface MayEnd {

		boolean mayEnd(int memberCount, int attributeCount, long attributeLength);

	}

	private static final MayEnd COUNT_MEMBERS
			= (memberCount, attributeCount, attributeLength) -> memberCount <= 0;
	private static final MayEnd COUNT_ATTRIBUTES
			= (memberCount, attributeCount, attributeLength) -> attributeCount <= 0;
	private static final MayEnd COUNT_ATTRIBUTE_BYTES
			= (memberCount, attributeCount, attributeLength) -> attributeLength <= 0L;

	private enum State {

		BEFORE_VERSION("version()"),
		BEFORE_BEGIN_CONSTANTS("beginConstants()"),
		IN_CONSTANTS("constant*()", "endConstants()", AbstractStatefulClassFileSink.COUNT_MEMBERS),
		BEFORE_ACCESS_FLAGS("accessFlags()"),
		BEFORE_THIS_CLASS("thisClass()"),
		BEFORE_SUPER_CLASS("superClass()"),
		BEFORE_BEGIN_INTERFACES("beginInterfaces()"),
		IN_INTERFACES("superInterface()", "endInterfaces()", AbstractStatefulClassFileSink.COUNT_MEMBERS),
		BEFORE_BEGIN_FIELDS("beginFields()"),
		IN_FIELDS("beginField()", "endFields()", AbstractStatefulClassFileSink.COUNT_MEMBERS),
		BEFORE_FIELD_BEGIN_ATTRIBUTES("beginAttributes()"),
		IN_FIELD_ATTRIBUTES("beginAttribute()", "endAttributes()", AbstractStatefulClassFileSink.COUNT_ATTRIBUTES),
		IN_FIELD_ATTRIBUTE("attributeBytes()", "endAttribute()",
				AbstractStatefulClassFileSink.COUNT_ATTRIBUTE_BYTES),
		BEFORE_END_FIELD("endField()"),
		BEFORE_BEGIN_METHODS("beginMethods()"),
		IN_METHODS("beginMethod()", "endMethods()", AbstractStatefulClassFileSink.COUNT_MEMBERS),
		BEFORE_METHOD_BEGIN_ATTRIBUTES("beginAttributes()"),
		IN_METHOD_ATTRIBUTES("beginAttribute()", "endAttributes()", AbstractStatefulClassFileSink.COUNT_ATTRIBUTES),
		IN_METHOD_ATTRIBUTE("attributeBytes()", "endAttribute()",
				AbstractStatefulClassFileSink.COUNT_ATTRIBUTE_BYTES),
		BEFORE_END_METHOD("endMethod()"),
		BEFORE_BEGIN_ATTRIBUTES("beginAttributes()"),
		IN_ATTRIBUTES("beginAttribute()", "endAttributes()", AbstractStatefulClassFileSink.COUNT_ATTRIBUTES),
		IN_ATTRIBUTE("attributeBytes()", "endAttribute()", AbstractStatefulClassFileSink.COUNT_ATTRIBUTE_BYTES),
		AFTER_EVERYTHING(null);

		final String expectedCall;

		final String endingCall;

		final MayEnd mayEnd;

		State next;

		State up;

		static {
			BEFORE_FIELD_BEGIN_ATTRIBUTES.next = IN_FIELD_ATTRIBUTES;
			BEFORE_METHOD_BEGIN_ATTRIBUTES.next = IN_METHOD_ATTRIBUTES;
			BEFORE_BEGIN_ATTRIBUTES.next = IN_ATTRIBUTES;
			IN_FIELD_ATTRIBUTES.next = IN_FIELD_ATTRIBUTE;
			IN_METHOD_ATTRIBUTES.next = IN_METHOD_ATTRIBUTE;
			IN_ATTRIBUTES.next = IN_ATTRIBUTE;
			IN_FIELD_ATTRIBUTE.up = IN_FIELD_ATTRIBUTES;
			IN_METHOD_ATTRIBUTE.up = IN_METHOD_ATTRIBUTES;
			IN_ATTRIBUTE.up = IN_ATTRIBUTES;
			IN_FIELD_ATTRIBUTES.up = BEFORE_END_FIELD;
			IN_METHOD_ATTRIBUTES.up = BEFORE_END_METHOD;
			IN_ATTRIBUTES.up = AFTER_EVERYTHING;
		}

		private State(String expectedCall) {
			this(expectedCall, null, null);
		}

		private State(String expectedCall, String endingCall, MayEnd mayEnd) {
			this.expectedCall = expectedCall;
			this.endingCall = endingCall;
			this.mayEnd = mayEnd;
		}

	}

	private State state;

	private int memberCount;

	private int attributeCount;

	private long attributeLength;

	public AbstractStatefulClassFileSink() {
		resetState();
	}

	protected final void resetState() {
		state = State.BEFORE_VERSION;
	}

	private void die(String called) throws OutOfSequenceSinkCallClassFileFormatException {
		throw new OutOfSequenceSinkCallClassFileFormatException(called, state.endingCall == null
				? state.expectedCall : state.expectedCall + " or " + state.endingCall);
	}

	private void tooMany(String called) throws OutOfSequenceSinkCallClassFileFormatException {
		throw new OutOfSequenceSinkCallClassFileFormatException(called, state.endingCall);
	}

	private void notEnough(String called) throws OutOfSequenceSinkCallClassFileFormatException {
		throw new OutOfSequenceSinkCallClassFileFormatException(called, state.expectedCall);
	}

	protected final void announceVersion() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.BEFORE_VERSION)
			die("version()");
		state = State.BEFORE_BEGIN_CONSTANTS;
	}

	protected final void announceBeginConstants(int count) throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.BEFORE_BEGIN_CONSTANTS)
			die("beginConstants()");
		memberCount = count;
		state = State.IN_CONSTANTS;
	}

	protected final void announceConstant(String methodName) throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.IN_CONSTANTS)
			die(methodName + "()");
		if(memberCount == 0)
			tooMany(methodName + "()");
		if(memberCount > 0)
			--memberCount;
	}

	protected final void announceEndConstants() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.IN_CONSTANTS)
			die("endConstants()");
		state = State.BEFORE_ACCESS_FLAGS;
	}

	protected final void announceAccessFlags() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.BEFORE_ACCESS_FLAGS)
			die("accessFlags()");
		state = State.BEFORE_THIS_CLASS;
	}

	protected final void announceThisClass() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.BEFORE_THIS_CLASS)
			die("thisClass()");
		state = State.BEFORE_SUPER_CLASS;
	}

	protected final void announceSuperClass() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.BEFORE_SUPER_CLASS)
			die("superClass()");
		state = State.BEFORE_BEGIN_INTERFACES;
	}

	protected final void announceBeginInterfaces(int count) throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.BEFORE_BEGIN_INTERFACES)
			die("beginInterfaces()");
		memberCount = count;
		state = State.IN_INTERFACES;
	}

	protected final void announceSuperInterface() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.IN_INTERFACES)
			die("superInterface()");
		if(memberCount == 0)
			tooMany("superInterface()");
		if(memberCount > 0)
			--memberCount;
	}

	protected final void announceEndInterfaces() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.IN_INTERFACES)
			die("endInterfaces()");
		if(memberCount > 0)
			notEnough("endInterfaces()");
		state = State.BEFORE_BEGIN_FIELDS;
	}

	protected final void announceBeginFields(int count) throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.BEFORE_BEGIN_FIELDS)
			die("beginFields()");
		memberCount = count;
		state = State.IN_FIELDS;
	}

	protected final void announceBeginField() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.IN_FIELDS)
			die("beginField()");
		if(memberCount == 0)
			tooMany("beginField()");
		if(memberCount > 0)
			--memberCount;
		state = State.BEFORE_FIELD_BEGIN_ATTRIBUTES;
	}

	protected final void announceBeginAttributes(int count) throws OutOfSequenceSinkCallClassFileFormatException {
		switch(state) {
			case BEFORE_FIELD_BEGIN_ATTRIBUTES:
			case BEFORE_METHOD_BEGIN_ATTRIBUTES:
			case BEFORE_BEGIN_ATTRIBUTES:
				attributeCount = count;
				state = state.next;
				break;
			default:
				die("beginAttributes()");
				break;
		}
	}

	protected final void announceBeginAttribute(long length) throws OutOfSequenceSinkCallClassFileFormatException {
		switch(state) {
			case IN_FIELD_ATTRIBUTES:
			case IN_METHOD_ATTRIBUTES:
			case IN_ATTRIBUTES:
				if(attributeCount == 0)
					tooMany("beginAttribute()");
				if(attributeCount > 0)
					--attributeCount;
				attributeLength = length;
				state = state.next;
				break;
			default:
				die("beginAttribute()");
				break;
		}
	}

	protected final void announceAttributeBytes(int count)
			throws OutOfSequenceSinkCallClassFileFormatException, TooManyAttributeBytesClassFileFormatException {
		if(count < 0)
			throw new IllegalArgumentException("Attribute byte count cannot be negative: " + count);
		switch(state) {
			case IN_FIELD_ATTRIBUTE:
			case IN_METHOD_ATTRIBUTE:
			case IN_ATTRIBUTE:
				if(attributeLength >= 0L) {
					if((long)count > attributeLength)
						throw new TooManyAttributeBytesClassFileFormatException();
					attributeLength -= (long)count;
				}
				break;
			default:
				die("attributeBytes()");
				break;
		}
	}

	protected final void announceEndAttribute()
			throws OutOfSequenceSinkCallClassFileFormatException, NotEnoughAttributeBytesClassFileFormatException {
		switch(state) {
			case IN_FIELD_ATTRIBUTE:
			case IN_METHOD_ATTRIBUTE:
			case IN_ATTRIBUTE:
				if(attributeLength > 0L)
					throw new NotEnoughAttributeBytesClassFileFormatException();
				state = state.up;
				break;
			default:
				die("endAttribute()");
				break;
		}
	}

	protected final void announceEndAttributes() throws OutOfSequenceSinkCallClassFileFormatException {
		switch(state) {
			case IN_FIELD_ATTRIBUTES:
			case IN_METHOD_ATTRIBUTES:
			case IN_ATTRIBUTES:
				if(attributeCount > 0)
					notEnough("endAttributes()");
				state = state.up;
				break;
			default:
				die("endAttributes()");
				break;
		}
	}

	protected final void announceEndField() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.BEFORE_END_FIELD)
			die("endField()");
		state = State.IN_FIELDS;
	}

	protected final void announceEndFields() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.IN_FIELDS)
			die("endFields()");
		if(memberCount > 0)
			notEnough("endFields()");
		state = State.BEFORE_BEGIN_METHODS;
	}

	protected final void announceBeginMethods(int count) throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.BEFORE_BEGIN_METHODS)
			die("beginMethods()");
		memberCount = count;
		state = State.IN_METHODS;
	}

	protected final void announceBeginMethod() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.IN_METHODS)
			die("beginMethod()");
		if(memberCount == 0)
			tooMany("beginMethod()");
		if(memberCount > 0)
			--memberCount;
		state = State.BEFORE_METHOD_BEGIN_ATTRIBUTES;
	}

	protected final void announceEndMethod() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.BEFORE_END_METHOD)
			die("endMethod()");
		state = State.IN_METHODS;
	}

	protected final void announceEndMethods() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.IN_METHODS)
			die("endMethods()");
		if(memberCount > 0)
			notEnough("endMethods()");
		state = State.BEFORE_BEGIN_ATTRIBUTES;
	}

	protected final void announceEnd() throws OutOfSequenceSinkCallClassFileFormatException {
		if(state != State.AFTER_EVERYTHING)
			die(null);
	}

}
