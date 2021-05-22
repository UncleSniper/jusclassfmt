package org.unclesniper.classfmt;

import java.io.IOException;

public class ClassFileParser {

	private enum Expected {
		BYTE,
		SHORT,
		INT,
		LONG,
		BYTES
	}

	private enum State {

		BEFORE_MAGIC,
		BEFORE_MINOR,
		BEFORE_MAJOR,
		BEFORE_CPOOL_COUNT,
		BEFORE_CPOOL_TAG,
		BEFORE_CPOOL_CLASS_NAME,
		BEFORE_CPOOL_FIELDREF_CLASS,
		BEFORE_CPOOL_FIELDREF_NAME_AND_TYPE,
		BEFORE_CPOOL_METHODREF_CLASS,
		BEFORE_CPOOL_METHODREF_NAME_AND_TYPE,
		BEFORE_CPOOL_INTERFACE_METHODREF_CLASS,
		BEFORE_CPOOL_INTERFACE_METHODREF_NAME_AND_TYPE,
		BEFORE_CPOOL_STRING_INDEX,
		BEFORE_CPOOL_INTEGER_VALUE,
		BEFORE_CPOOL_FLOAT_VALUE,
		BEFORE_CPOOL_LONG_VALUE,
		BEFORE_CPOOL_DOUBLE_VALUE,
		BEFORE_CPOOL_NAME_AND_TYPE_NAME,
		BEFORE_CPOOL_NAME_AND_TYPE_DESCRIPTOR,
		BEFORE_CPOOL_UTF8_LENGTH,
		IN_CPOOL_UTF8_BYTES,
		BEFORE_CPOOL_METHOD_HANDLE_KIND,
		BEFORE_CPOOL_METHOD_HANDLE_INDEX,
		BEFORE_CPOOL_METHOD_TYPE_DESCRIPTOR,
		BEFORE_CPOOL_INVOKE_DYNAMIC_BOOTSTRAP_METHOD,
		BEFORE_CPOOL_INVOKE_DYNAMIC_NAME_AND_TYPE,
		BEFORE_ACCESS_FLAGS,
		BEFORE_THIS_CLASS,
		BEFORE_SUPER_CLASS,
		BEFORE_INTERFACES_COUNT,
		BEFORE_INTERFACE_INDEX,
		BEFORE_FIELDS_COUNT,
		BEFORE_FIELD_ACCESS_FLAGS,
		BEFORE_FIELD_NAME,
		BEFORE_FIELD_DESCRIPTOR,
		BEFORE_FIELD_ATTRIBUTES_COUNT,
		BEFORE_FIELD_ATTRIBUTE_NAME,
		BEFORE_FIELD_ATTRIBUTE_LENGTH,
		IN_FIELD_ATTRIBUTE_BYTES,
		BEFORE_METHODS_COUNT;

		State next;

		static {
			BEFORE_CPOOL_FIELDREF_CLASS.next = BEFORE_CPOOL_FIELDREF_NAME_AND_TYPE;
			BEFORE_CPOOL_METHODREF_CLASS.next = BEFORE_CPOOL_METHODREF_NAME_AND_TYPE;
			BEFORE_CPOOL_INTERFACE_METHODREF_CLASS.next = BEFORE_CPOOL_INTERFACE_METHODREF_NAME_AND_TYPE;
		}

	}

	private enum UTF8State {

		NONE(0, 0),
		S2B1(2, 1),
		S3B1(3, 1),
		S3B2(3, 2);

		private final int sequenceLength;

		private final int sequenceOffset;

		UTF8State next;

		static {
			S3B1.next = S3B2;
		}

		private UTF8State(int sequenceLength, int sequenceOffset) {
			this.sequenceLength = sequenceLength;
			this.sequenceOffset = sequenceOffset;
		}

	}

	private ClassFileSink sink;

	private Expected expected;

	private long expectedLength;

	private long expectedOffset;

	private int haveExpected;

	private final byte[] expectedBuffer = new byte[8];

	private boolean parserBroken;

	private State state;

	private short short0;

	private short short1;

	private int memberCount;

	private int attributeCount;

	private int int0;

	private int int1;

	private char[] charBuffer;

	private UTF8State utf8State;

	private MethodHandleReferenceKind methodHandleReferenceKind;

	public ClassFileParser(ClassFileSink sink) {
		this.sink = sink;
		reset();
	}

	public ClassFileSink getSink() {
		return sink;
	}

	public void setSink(ClassFileSink sink) {
		this.sink = sink;
	}

	public void reset() {
		expectInt();
		state = State.BEFORE_MAGIC;
		parserBroken = false;
	}

	public void pushBytes(byte[] buffer, int offset, int length) throws IOException, ClassFileFormatException {
		if(parserBroken)
			throw new IllegalStateException("Class file parser is in undefined state");
		if(length < 0)
			throw new IllegalArgumentException("Length cannot be negative: " + length);
		if(length == 0)
			return;
		if(buffer == null)
			throw new IllegalArgumentException("Buffer cannot be null if length is positive");
		if(offset < 0)
			throw new IllegalArgumentException("Offset cannot be negative: " + offset);
		int end = offset + length;
		if(end >= buffer.length)
			throw new IllegalArgumentException("End of slice extends beyond end of buffer: " + end
					+ " >= " + buffer.length);
		try {
			while(offset < end) {
				switch(expected) {
					case BYTE:
						foundByte(buffer[offset]);
						++offset;
						break;
					case SHORT:
						if(haveExpected > 0) {
							expectedBuffer[1] = buffer[offset];
							foundShort(ClassFileParser.decodeShort(expectedBuffer, 0));
							++offset;
							break;
						}
						if(end - offset < 2) {
							expectedBuffer[0] = buffer[offset];
							haveExpected = 1;
							++offset;
							break;
						}
						foundShort(ClassFileParser.decodeShort(buffer, offset));
						offset += 2;
						break;
					case INT:
						if(end - offset < 4 - haveExpected) {
							for(; offset < end; ++offset)
								expectedBuffer[haveExpected++] = buffer[offset];
							break;
						}
						if(haveExpected == 0) {
							foundInt(ClassFileParser.decodeInt(buffer, offset));
							offset += 4;
							break;
						}
						for(int i = haveExpected; i < 4; ++i)
							expectedBuffer[i] = buffer[offset + (i - haveExpected)];
						foundInt(ClassFileParser.decodeInt(expectedBuffer, 0));
						offset += 4 - haveExpected;
						break;
					case LONG:
						if(end - offset < 8 - haveExpected) {
							for(; offset < end; ++offset)
								expectedBuffer[haveExpected++] = buffer[offset];
							break;
						}
						if(haveExpected == 0) {
							foundLong(ClassFileParser.decodeLong(buffer, offset));
							offset += 8;
							break;
						}
						for(int i = haveExpected; i < 8; ++i)
							expectedBuffer[i] = buffer[offset + (i - haveExpected)];
						foundLong(ClassFileParser.decodeLong(expectedBuffer, 0));
						offset += 8 - haveExpected;
						break;
					case BYTES:
						{
							long available = end - offset;
							long limit = expectedLength - expectedOffset;
							int min = (int)(limit < available ? limit : available);
							foundBytes(buffer, offset, min);
							offset += min;
							expectedOffset += min;
							if(expectedOffset == expectedLength)
								foundBytes(null, 0, 0);
						}
						break;
					default:
						throw new Doom("Unrecognized Expected: " + expected.name());
				}
			}
		}
		catch(IOException | ClassFileFormatException | RuntimeException | Error e) {
			parserBroken = true;
			throw e;
		}
	}

	private void expectByte() {
		expected = Expected.BYTE;
	}

	private void expectShort() {
		expected = Expected.SHORT;
		haveExpected = 0;
	}

	private void expectInt() {
		expected = Expected.INT;
		haveExpected = 0;
	}

	private void expectLong() {
		expected = Expected.LONG;
		haveExpected = 0;
	}

	private void expectBytes(long length) throws IOException, ClassFileFormatException {
		expectedOffset = 0L;
		if(length == 0)
			foundBytes(null, 0, 0);
		else {
			expected = Expected.BYTES;
			expectedLength = length;
		}
	}

	private void foundByte(byte b) throws IOException, ClassFileFormatException {
		switch(state) {
			case BEFORE_CPOOL_TAG:
				{
					int code = b & 0xFF;
					ConstantTag tag = ConstantTag.byCode(code);
					if(tag == null)
						throw new InvalidConstantPoolTagClassFileFormatException(code);
					switch(tag) {
						case CLASS:
							state = State.BEFORE_CPOOL_CLASS_NAME;
							expectShort();
							break;
						case FIELDREF:
							state = State.BEFORE_CPOOL_FIELDREF_CLASS;
							expectShort();
							break;
						case METHODREF:
							state = State.BEFORE_CPOOL_METHODREF_CLASS;
							expectShort();
							break;
						case INTERFACE_METHODREF:
							state = State.BEFORE_CPOOL_INTERFACE_METHODREF_CLASS;
							expectShort();
							break;
						case STRING:
							state = State.BEFORE_CPOOL_STRING_INDEX;
							expectShort();
							break;
						case INTEGER:
							state = State.BEFORE_CPOOL_INTEGER_VALUE;
							expectInt();
							break;
						case FLOAT:
							state = State.BEFORE_CPOOL_FLOAT_VALUE;
							expectInt();
							break;
						case LONG:
							state = State.BEFORE_CPOOL_LONG_VALUE;
							expectLong();
							break;
						case DOUBLE:
							state = State.BEFORE_CPOOL_DOUBLE_VALUE;
							expectLong();
							break;
						case NAME_AND_TYPE:
							state = State.BEFORE_CPOOL_NAME_AND_TYPE_NAME;
							expectShort();
							break;
						case UTF8:
							state = State.BEFORE_CPOOL_UTF8_LENGTH;
							expectShort();
							break;
						case METHOD_HANDLE:
							state = State.BEFORE_CPOOL_METHOD_HANDLE_KIND;
							expectByte();
							break;
						case METHOD_TYPE:
							state = State.BEFORE_CPOOL_METHOD_TYPE_DESCRIPTOR;
							expectShort();
							break;
						case INVOKE_DYNAMIC:
							state = State.BEFORE_CPOOL_INVOKE_DYNAMIC_BOOTSTRAP_METHOD;
							expectShort();
							break;
						default:
							throw new Doom("Unrecognized ConstantTag: " + tag.name());
					}
				}
				break;
			case BEFORE_CPOOL_METHOD_HANDLE_KIND:
				{
					int code = b & 0xFF;
					methodHandleReferenceKind = MethodHandleReferenceKind.byCode(code);
					if(methodHandleReferenceKind == null)
						throw new InvalidMethodHandleReferenceKindClassFileFormatException(code);
					state = State.BEFORE_CPOOL_METHOD_HANDLE_INDEX;
					expectShort();
				}
				break;
			default:
				throw new Doom("Received byte in state " + state.name());
		}
	}

	private void foundShort(short s) throws IOException, ClassFileFormatException {
		switch(state) {
			case BEFORE_MINOR:
				short0 = s;
				state = State.BEFORE_MAJOR;
				expectShort();
				break;
			case BEFORE_MAJOR:
				sink.version(s & 0xFFFF, short0 & 0xFFFF);
				state = State.BEFORE_CPOOL_COUNT;
				expectShort();
				break;
			case BEFORE_CPOOL_COUNT:
				memberCount = s & 0xFFFF;
				if(memberCount == 0)
					throw new ZeroSizeConstantPoolClassFileFormatException();
				sink.beginConstants(memberCount - 1);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_CLASS_NAME:
				sink.constantClass(s & 0xFFFF);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_FIELDREF_CLASS:
			case BEFORE_CPOOL_METHODREF_CLASS:
			case BEFORE_CPOOL_INTERFACE_METHODREF_CLASS:
				short0 = s;
				state = state.next;
				expectShort();
				break;
			case BEFORE_CPOOL_FIELDREF_NAME_AND_TYPE:
				sink.constantFieldref(short0 & 0xFFFF, s & 0xFFFF);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_METHODREF_NAME_AND_TYPE:
				sink.constantMethodref(short0 & 0xFFFF, s & 0xFFFF);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_INTERFACE_METHODREF_NAME_AND_TYPE:
				sink.constantInterfaceMethodref(short0 & 0xFFFF, s & 0xFFFF);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_STRING_INDEX:
				sink.constantString(s & 0xFFFF);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_NAME_AND_TYPE_NAME:
				short0 = s;
				state = State.BEFORE_CPOOL_NAME_AND_TYPE_DESCRIPTOR;
				expectShort();
				break;
			case BEFORE_CPOOL_NAME_AND_TYPE_DESCRIPTOR:
				sink.constantNameAndType(short0 & 0xFFFF, s & 0xFFFF);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_UTF8_LENGTH:
				{
					int length = s & 0xFFFF;
					if(length > 0) {
						int currentLength = charBuffer == null ? 0 : charBuffer.length;
						if(currentLength < length)
							charBuffer = new char[length];
					}
					int0 = 0;
					state = State.IN_CPOOL_UTF8_BYTES;
					utf8State = UTF8State.NONE;
					expectBytes(length);
				}
				break;
			case BEFORE_CPOOL_METHOD_HANDLE_INDEX:
				sink.constantMethodHandle(methodHandleReferenceKind, s & 0xFFFF);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_METHOD_TYPE_DESCRIPTOR:
				sink.constantMethodType(s & 0xFFFF);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_INVOKE_DYNAMIC_BOOTSTRAP_METHOD:
				short0 = s;
				state = State.BEFORE_CPOOL_INVOKE_DYNAMIC_NAME_AND_TYPE;
				expectShort();
				break;
			case BEFORE_CPOOL_INVOKE_DYNAMIC_NAME_AND_TYPE:
				sink.constantInvokeDynamic(short0 & 0xFFFF, s & 0xFFFF);
				nextPoolConstant();
				break;
			case BEFORE_ACCESS_FLAGS:
				sink.accessFlags(s & 0xFFFF);
				state = State.BEFORE_THIS_CLASS;
				expectShort();
				break;
			case BEFORE_THIS_CLASS:
				sink.thisClass(s & 0xFFFF);
				state = State.BEFORE_SUPER_CLASS;
				expectShort();
				break;
			case BEFORE_SUPER_CLASS:
				sink.superClass(s & 0xFFFF);
				state = State.BEFORE_INTERFACES_COUNT;
				expectShort();
				break;
			case BEFORE_INTERFACES_COUNT:
				memberCount = s & 0xFFFF;
				sink.beginInterfaces(memberCount);
				nextSuperInterface();
				break;
			case BEFORE_INTERFACE_INDEX:
				if(s == (short)0)
					throw new ZeroSuperInterfaceIndexClassFileFormatException();
				sink.superInterface(s & 0xFFFF);
				--memberCount;
				nextSuperInterface();
				break;
			case BEFORE_FIELDS_COUNT:
				memberCount = s & 0xFFFF;
				sink.beginFields(memberCount);
				nextField();
				break;
			case BEFORE_FIELD_ACCESS_FLAGS:
				short0 = s;
				state = State.BEFORE_FIELD_NAME;
				expectShort();
				break;
			case BEFORE_FIELD_NAME:
				short1 = s;
				state = State.BEFORE_FIELD_DESCRIPTOR;
				expectShort();
				break;
			case BEFORE_FIELD_DESCRIPTOR:
				sink.beginField(short0 & 0xFFFF, short1 & 0xFFFF, s & 0xFFFF);
				state = State.BEFORE_FIELD_ATTRIBUTES_COUNT;
				expectShort();
				break;
			case BEFORE_FIELD_ATTRIBUTES_COUNT:
				attributeCount = s & 0xFFFF;
				sink.beginAttributes(attributeCount);
				nextFieldAttribute();
				break;
			case BEFORE_FIELD_ATTRIBUTE_NAME:
				short0 = s;
				state = State.BEFORE_FIELD_ATTRIBUTE_LENGTH;
				expectInt();
				break;
			case BEFORE_METHODS_COUNT:
				//TODO
			default:
				throw new Doom("Received short in state " + state.name());
		}
	}

	private void foundInt(int i) throws IOException, ClassFileFormatException {
		switch(state) {
			case BEFORE_MAGIC:
				if(i != 0xCAFEBABE)
					throw new BadMagicClassFileFormatException(i);
				state = State.BEFORE_MINOR;
				expectShort();
				break;
			case BEFORE_CPOOL_INTEGER_VALUE:
				sink.constantInteger(i);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_FLOAT_VALUE:
				sink.constantFloat(Float.intBitsToFloat(i));
				nextPoolConstant();
				break;
			case BEFORE_FIELD_ATTRIBUTE_LENGTH:
				{
					long length = (long)i & 0xFFFFFFFFL;
					sink.beginAttribute(short0 & 0xFFFF, length);
					state = State.IN_FIELD_ATTRIBUTE_BYTES;
					expectBytes(length);
				}
				break;
			default:
				throw new Doom("Received int in state " + state.name());
		}
	}

	private void foundLong(long l) throws IOException, ClassFileFormatException {
		switch(state) {
			case BEFORE_CPOOL_LONG_VALUE:
				sink.constantLong(l);
				nextPoolConstant();
				break;
			case BEFORE_CPOOL_DOUBLE_VALUE:
				sink.constantDouble(Double.longBitsToDouble(l));
				nextPoolConstant();
				break;
			default:
				throw new Doom("Received long in state " + state.name());
		}
	}

	private void foundBytes(byte[] buffer, int offset, int length) throws IOException, ClassFileFormatException {
		switch(state) {
			case IN_CPOOL_UTF8_BYTES:
				foundMUTF8Bytes(buffer, offset, length);
				break;
			case IN_FIELD_ATTRIBUTE_BYTES:
				if(length == 0) {
					sink.endAttribute();
					nextFieldAttribute();
				}
				else
					sink.attributeBytes(buffer, offset, length);
				break;
			default:
				throw new Doom("Received byte[] in state " + state.name());
		}
	}

	private void foundMUTF8Bytes(byte[] buffer, int offset, int length)
			throws IOException, ClassFileFormatException {
		if(length == 0) {
			if(utf8State.sequenceLength > 0)
				throw new PrematureEndOfModifiedUTF8ClassFileFormatException((int)expectedOffset,
						utf8State.sequenceOffset, utf8State.sequenceLength);
			sink.constantUTF8(int0 == 0 ? "" : String.valueOf(charBuffer, 0, int0));
			nextPoolConstant();
			return;
		}
		int end = offset + length;
		for(int i = offset; i < length; ++i) {
			int b = buffer[i] & 0xFF;
			switch(utf8State) {
				case NONE:
					if((b & 0x80) == 0)
						charBuffer[int0++] = (char)b;
					else if((b & 0xE0) == 0xC0) {
						int1 = b & 0x1F;
						utf8State = UTF8State.S2B1;
					}
					else if((b & 0xF0) == 0xE0) {
						int1 = b & 0x0F;
						utf8State = UTF8State.S3B1;
					}
					else
						throw new IllegalModifiedUTF8InitiatorByteClassFileFormatException((int)expectedOffset
								+ (i - offset), b);
					break;
				case S2B1:
				case S3B1:
				case S3B2:
					if((b & 0xC0) != 0x80)
						throw new IllegalModifiedUTF8ContinuationByteClassFileFormatException((int)expectedOffset
								+ (i - offset), b, utf8State.sequenceOffset, utf8State.sequenceLength);
					int1 = (int1 << 6) | (b & 0x3F);
					utf8State = utf8State.next;
					if(utf8State == null) {
						charBuffer[int0++] = (char)int1;
						utf8State = UTF8State.NONE;
					}
					break;
				default:
					throw new Doom("Unrecognized UTF8State: " + utf8State.name());
			}
		}
	}

	private void nextPoolConstant() throws IOException, ClassFileFormatException {
		--memberCount;
		if(memberCount == 0) {
			sink.endConstants();
			state = State.BEFORE_ACCESS_FLAGS;
			expectShort();
		}
		else {
			state = State.BEFORE_CPOOL_TAG;
			expectByte();
		}
	}

	private void nextSuperInterface() throws IOException, ClassFileFormatException {
		if(memberCount == 0) {
			sink.endInterfaces();
			state = State.BEFORE_FIELDS_COUNT;
			expectShort();
		}
		else {
			state = State.BEFORE_INTERFACE_INDEX;
			expectShort();
		}
	}

	private void nextField() throws IOException, ClassFileFormatException {
		if(memberCount == 0) {
			sink.endFields();
			state = State.BEFORE_METHODS_COUNT;
			expectShort();
		}
		else {
			state = State.BEFORE_FIELD_ACCESS_FLAGS;
			expectShort();
		}
	}

	private void nextFieldAttribute() throws IOException, ClassFileFormatException {
		if(memberCount == 0) {
			sink.endAttributes();
			nextField();
		}
		else {
			state = State.BEFORE_FIELD_ATTRIBUTE_NAME;
			expectShort();
		}
	}

	private static short decodeShort(byte[] buffer, int offset) {
		int b1 = buffer[offset] & 0xFF;
		int b0 = buffer[offset + 1] & 0xFF;
		return (short)((b1 << 8) | b0);
	}

	private static int decodeInt(byte[] buffer, int offset) {
		int b3 = buffer[offset] & 0xFF;
		int b2 = buffer[offset + 1] & 0xFF;
		int b1 = buffer[offset + 2] & 0xFF;
		int b0 = buffer[offset + 3] & 0xFF;
		return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
	}

	private static long decodeLong(byte[] buffer, int offset) {
		long b7 = buffer[offset] & 0xFF;
		long b6 = buffer[offset + 1] & 0xFF;
		long b5 = buffer[offset + 2] & 0xFF;
		long b4 = buffer[offset + 3] & 0xFF;
		long b3 = buffer[offset + 4] & 0xFF;
		long b2 = buffer[offset + 5] & 0xFF;
		long b1 = buffer[offset + 6] & 0xFF;
		long b0 = buffer[offset + 7] & 0xFF;
		return (b7 << 56) | (b6 << 48) | (b5 << 40) | (b4 << 32) | (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
	}

}
