package org.unclesniper.classfmt;

import java.io.IOException;

public abstract class StatefulClassFileSink extends AbstractStatefulClassFileSink implements ClassFileSink {

	public StatefulClassFileSink() {}

	protected abstract void versionImpl(int major, int minor) throws IOException, ClassFileFormatException;

	protected abstract void beginConstantsImpl(int count) throws IOException, ClassFileFormatException;

	protected abstract void constantClassImpl(int nameIndex) throws IOException, ClassFileFormatException;

	protected abstract void constantFieldrefImpl(int classIndex, int nameAndTypeIndex)
			throws IOException, ClassFileFormatException;

	protected abstract void constantMethodrefImpl(int classIndex, int nameAndTypeIndex)
			throws IOException, ClassFileFormatException;

	protected abstract void constantInterfaceMethodrefImpl(int classIndex, int nameAndTypeIndex)
			throws IOException, ClassFileFormatException;

	protected abstract void constantStringImpl(int stringIndex) throws IOException, ClassFileFormatException;

	protected abstract void constantIntegerImpl(int value) throws IOException, ClassFileFormatException;

	protected abstract void constantFloatImpl(float value) throws IOException, ClassFileFormatException;

	protected abstract void constantLongImpl(long value) throws IOException, ClassFileFormatException;

	protected abstract void constantDoubleImpl(double value) throws IOException, ClassFileFormatException;

	protected abstract void constantNameAndTypeImpl(int nameIndex, int descriptorIndex)
			throws IOException, ClassFileFormatException;

	protected abstract void constantUTF8Impl(String value) throws IOException, ClassFileFormatException;

	protected abstract void constantMethodHandleImpl(MethodHandleReferenceKind referenceKind, int referenceIndex)
			throws IOException, ClassFileFormatException;

	protected abstract void constantMethodTypeImpl(int descriptorIndex) throws IOException, ClassFileFormatException;

	protected abstract void constantInvokeDynamicImpl(int bootstrapMethodAttrIndex, int nameAndTypeIndex)
			throws IOException, ClassFileFormatException;

	protected abstract void endConstantsImpl() throws IOException, ClassFileFormatException;

	protected abstract void accessFlagsImpl(int flags) throws IOException, ClassFileFormatException;

	protected abstract void thisClassImpl(int thisClassIndex) throws IOException, ClassFileFormatException;

	protected abstract void superClassImpl(int superClassIndex) throws IOException, ClassFileFormatException;

	protected abstract void beginInterfacesImpl(int count) throws IOException, ClassFileFormatException;

	protected abstract void superInterfaceImpl(int classInfoIndex) throws IOException, ClassFileFormatException;

	protected abstract void endInterfacesImpl() throws IOException, ClassFileFormatException;

	protected abstract void beginFieldsImpl(int count) throws IOException, ClassFileFormatException;

	protected abstract void beginFieldImpl(int accessFlags, int nameIndex, int descriptorIndex)
			throws IOException, ClassFileFormatException;

	protected abstract void beginAttributesImpl(int count) throws IOException, ClassFileFormatException;

	protected abstract void beginAttributeImpl(int nameIndex, long length)
			throws IOException, ClassFileFormatException;

	protected abstract void attributeBytesImpl(byte[] buffer, int offset, int length)
			throws IOException, ClassFileFormatException;

	protected abstract void endAttributeImpl() throws IOException, ClassFileFormatException;

	protected abstract void endAttributesImpl() throws IOException, ClassFileFormatException;

	protected abstract void endFieldImpl() throws IOException, ClassFileFormatException;

	protected abstract void endFieldsImpl() throws IOException, ClassFileFormatException;

	protected abstract void beginMethodsImpl(int count) throws IOException, ClassFileFormatException;

	protected abstract void beginMethodImpl(int accessFlags, int nameIndex, int descriptorIndex)
			throws IOException, ClassFileFormatException;

	protected abstract void endMethodImpl() throws IOException, ClassFileFormatException;

	protected abstract void endMethodsImpl() throws IOException, ClassFileFormatException;

	@Override
	public final void version(int major, int minor) throws IOException, ClassFileFormatException {
		announceVersion();
		versionImpl(major, minor);
	}

	@Override
	public final void beginConstants(int count) throws IOException, ClassFileFormatException {
		announceBeginConstants(count);
		beginConstantsImpl(count);
	}

	@Override
	public final void constantClass(int nameIndex) throws IOException, ClassFileFormatException {
		announceConstant("constantClass");
		constantClassImpl(nameIndex);
	}

	@Override
	public final void constantFieldref(int classIndex, int nameAndTypeIndex)
			throws IOException, ClassFileFormatException {
		announceConstant("constantFieldref");
		constantFieldrefImpl(classIndex, nameAndTypeIndex);
	}

	@Override
	public final void constantMethodref(int classIndex, int nameAndTypeIndex)
			throws IOException, ClassFileFormatException {
		announceConstant("constantMethodref");
		constantMethodrefImpl(classIndex, nameAndTypeIndex);
	}

	@Override
	public final void constantInterfaceMethodref(int classIndex, int nameAndTypeIndex)
			throws IOException, ClassFileFormatException {
		announceConstant("constantInterfaceMethodref");
		constantInterfaceMethodrefImpl(classIndex, nameAndTypeIndex);
	}

	@Override
	public final void constantString(int stringIndex) throws IOException, ClassFileFormatException {
		announceConstant("constantString");
		constantStringImpl(stringIndex);
	}

	@Override
	public final void constantInteger(int value) throws IOException, ClassFileFormatException {
		announceConstant("constantInteger");
		constantIntegerImpl(value);
	}

	@Override
	public final void constantFloat(float value) throws IOException, ClassFileFormatException {
		announceConstant("constantFloat");
		constantFloatImpl(value);
	}

	@Override
	public final void constantLong(long value) throws IOException, ClassFileFormatException {
		announceConstant("constantLong");
		constantLongImpl(value);
	}

	@Override
	public final void constantDouble(double value) throws IOException, ClassFileFormatException {
		announceConstant("constantDouble");
		constantDoubleImpl(value);
	}

	@Override
	public final void constantNameAndType(int nameIndex, int descriptorIndex)
			throws IOException, ClassFileFormatException {
		announceConstant("constantNameAndType");
		constantNameAndTypeImpl(nameIndex, descriptorIndex);
	}

	@Override
	public final void constantUTF8(String value) throws IOException, ClassFileFormatException {
		announceConstant("constantUTF8");
		constantUTF8Impl(value);
	}

	@Override
	public final void constantMethodHandle(MethodHandleReferenceKind referenceKind, int referenceIndex)
			throws IOException, ClassFileFormatException {
		announceConstant("constantMethodHandle");
		constantMethodHandleImpl(referenceKind, referenceIndex);
	}

	@Override
	public final void constantMethodType(int descriptorIndex) throws IOException, ClassFileFormatException {
		announceConstant("constantMethodType");
		constantMethodTypeImpl(descriptorIndex);
	}

	@Override
	public final void constantInvokeDynamic(int bootstrapMethodAttrIndex, int nameAndTypeIndex)
			throws IOException, ClassFileFormatException {
		announceConstant("constantInvokeDynamic");
		constantInvokeDynamicImpl(bootstrapMethodAttrIndex, nameAndTypeIndex);
	}

	@Override
	public final void endConstants() throws IOException, ClassFileFormatException {
		announceEndConstants();
		endConstantsImpl();
	}

	@Override
	public final void accessFlags(int flags) throws IOException, ClassFileFormatException {
		announceAccessFlags();
		accessFlagsImpl(flags);
	}

	@Override
	public final void thisClass(int thisClassIndex) throws IOException, ClassFileFormatException {
		announceThisClass();
		thisClassImpl(thisClassIndex);
	}

	@Override
	public final void superClass(int superClassIndex) throws IOException, ClassFileFormatException {
		announceSuperClass();
		superClassImpl(superClassIndex);
	}

	@Override
	public final void beginInterfaces(int count) throws IOException, ClassFileFormatException {
		announceBeginInterfaces(count);
		beginInterfacesImpl(count);
	}

	@Override
	public final void superInterface(int classInfoIndex) throws IOException, ClassFileFormatException {
		announceSuperInterface();
		superInterfaceImpl(classInfoIndex);
	}

	@Override
	public final void endInterfaces() throws IOException, ClassFileFormatException {
		announceEndInterfaces();
		endInterfacesImpl();
	}

	@Override
	public final void beginFields(int count) throws IOException, ClassFileFormatException {
		announceBeginFields(count);
		beginFieldsImpl(count);
	}

	@Override
	public final void beginField(int accessFlags, int nameIndex, int descriptorIndex)
			throws IOException, ClassFileFormatException {
		announceBeginField();
		beginFieldImpl(accessFlags, nameIndex, descriptorIndex);
	}

	@Override
	public final void beginAttributes(int count) throws IOException, ClassFileFormatException {
		announceBeginAttributes(count);
		beginAttributesImpl(count);
	}

	@Override
	public final void beginAttribute(int nameIndex, long length) throws IOException, ClassFileFormatException {
		announceBeginAttribute(length);
		beginAttributeImpl(nameIndex, length);
	}

	@Override
	public final void attributeBytes(byte[] buffer, int offset, int length)
			throws IOException, ClassFileFormatException {
		announceAttributeBytes(length);
		attributeBytesImpl(buffer, offset, length);
	}

	@Override
	public final void endAttribute() throws IOException, ClassFileFormatException {
		announceEndAttribute();
		endAttributeImpl();
	}

	@Override
	public final void endAttributes() throws IOException, ClassFileFormatException {
		announceEndAttributes();
		endAttributesImpl();
	}

	@Override
	public final void endField() throws IOException, ClassFileFormatException {
		announceEndField();
		endFieldImpl();
	}

	@Override
	public final void endFields() throws IOException, ClassFileFormatException {
		announceEndFields();
		endFieldsImpl();
	}

	@Override
	public final void beginMethods(int count) throws IOException, ClassFileFormatException {
		announceBeginMethods(count);
		beginMethodsImpl(count);
	}

	@Override
	public final void beginMethod(int accessFlags, int nameIndex, int descriptorIndex)
			throws IOException, ClassFileFormatException {
		announceBeginMethod();
		beginMethodImpl(accessFlags, nameIndex, descriptorIndex);
	}

	@Override
	public final void endMethod() throws IOException, ClassFileFormatException {
		announceEndMethod();
		endMethodImpl();
	}

	@Override
	public final void endMethods() throws IOException, ClassFileFormatException {
		announceEndMethods();
		endMethodsImpl();
	}

}
