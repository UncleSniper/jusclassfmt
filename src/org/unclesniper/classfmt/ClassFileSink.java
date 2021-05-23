package org.unclesniper.classfmt;

import java.io.IOException;

public interface ClassFileSink {

	void version(int major, int minor) throws IOException, ClassFileFormatException;

	void beginConstants(int count) throws IOException, ClassFileFormatException;

	void constantClass(int nameIndex) throws IOException, ClassFileFormatException;

	void constantFieldref(int classIndex, int nameAndTypeIndex) throws IOException, ClassFileFormatException;

	void constantMethodref(int classIndex, int nameAndTypeIndex) throws IOException, ClassFileFormatException;

	void constantInterfaceMethodref(int classIndex, int nameAndTypeIndex)
			throws IOException, ClassFileFormatException;

	void constantString(int stringIndex) throws IOException, ClassFileFormatException;

	void constantInteger(int value) throws IOException, ClassFileFormatException;

	void constantFloat(float value) throws IOException, ClassFileFormatException;

	void constantLong(long value) throws IOException, ClassFileFormatException;

	void constantDouble(double value) throws IOException, ClassFileFormatException;

	void constantNameAndType(int nameIndex, int descriptorIndex) throws IOException, ClassFileFormatException;

	void constantUTF8(String value) throws IOException, ClassFileFormatException;

	void constantMethodHandle(MethodHandleReferenceKind referenceKind, int referenceIndex)
			throws IOException, ClassFileFormatException;

	void constantMethodType(int descriptorIndex) throws IOException, ClassFileFormatException;

	void constantInvokeDynamic(int bootstrapMethodAttrIndex, int nameAndTypeIndex)
			throws IOException, ClassFileFormatException;

	void endConstants() throws IOException, ClassFileFormatException;

	void accessFlags(int flags) throws IOException, ClassFileFormatException;

	void thisClass(int thisClassIndex) throws IOException, ClassFileFormatException;

	void superClass(int superClassIndex) throws IOException, ClassFileFormatException;

	void beginInterfaces(int count) throws IOException, ClassFileFormatException;

	void superInterface(int classInfoIndex) throws IOException, ClassFileFormatException;

	void endInterfaces() throws IOException, ClassFileFormatException;

	void beginFields(int count) throws IOException, ClassFileFormatException;

	void beginField(int accessFlags, int nameIndex, int descriptorIndex)
			throws IOException, ClassFileFormatException;

	void beginAttributes(int count) throws IOException, ClassFileFormatException;

	void beginAttribute(int nameIndex, long length) throws IOException, ClassFileFormatException;

	void attributeBytes(byte[] buffer, int offset, int length) throws IOException, ClassFileFormatException;

	void endAttribute() throws IOException, ClassFileFormatException;

	void endAttributes() throws IOException, ClassFileFormatException;

	void endField() throws IOException, ClassFileFormatException;

	void endFields() throws IOException, ClassFileFormatException;

	void beginMethods(int count) throws IOException, ClassFileFormatException;

	void beginMethod(int accessFlags, int nameIndex, int descriptorIndex)
			throws IOException, ClassFileFormatException;

	void endMethod() throws IOException, ClassFileFormatException;

	void endMethods() throws IOException, ClassFileFormatException;

}
