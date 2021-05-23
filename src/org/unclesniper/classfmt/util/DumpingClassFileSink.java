package org.unclesniper.classfmt.util;

import java.io.Writer;
import java.io.IOException;
import org.unclesniper.classfmt.ClassFileConstants;
import org.unclesniper.classfmt.StatefulClassFileSink;
import org.unclesniper.classfmt.ClassFileFormatException;
import org.unclesniper.classfmt.MethodHandleReferenceKind;

public class DumpingClassFileSink extends StatefulClassFileSink {

	private static final char[] EOL_CHARS = System.getProperty("line.separator").toCharArray();

	private static final char[] INDENT_CHARS = "    ".toCharArray();

	private static final char[] ZEROES = "0000000".toCharArray();

	private Writer writer;

	private int poolIndex;

	private boolean inMember;

	public DumpingClassFileSink(Writer writer) {
		this.writer = writer;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	private void println() throws IOException {
		writer.write(DumpingClassFileSink.EOL_CHARS);
	}

	private void indent(int level) throws IOException {
		for(; level > 0; --level)
			writer.write(DumpingClassFileSink.INDENT_CHARS);
	}

	private void print(String s) throws IOException {
		writer.write(s);
	}

	private void println(String s) throws IOException {
		writer.write(s);
		writer.write(DumpingClassFileSink.EOL_CHARS);
	}

	private void print(int value) throws IOException {
		writer.write(String.valueOf(value));
	}

	private void print(char c) throws IOException {
		writer.write(c);
	}

	private void println(char c) throws IOException {
		writer.write(c);
		println();
	}

	private void println(int value) throws IOException {
		writer.write(String.valueOf(value));
		println();
	}

	private void print(long value) throws IOException {
		writer.write(String.valueOf(value));
	}

	private void println(long value) throws IOException {
		writer.write(String.valueOf(value));
		println();
	}

	@Override
	protected void versionImpl(int major, int minor) throws IOException {
		print("Version: ");
		print(major);
		print('.');
		println(minor);
		writer.flush();
	}

	@Override
	protected void beginConstantsImpl(int count) throws IOException {
		print("Constant pool (size = ");
		print(count);
		println("):");
		writer.flush();
	}

	private void prepareConstant() throws IOException {
		indent(1);
		print('#');
		print(++poolIndex);
		print(": ");
	}

	@Override
	protected void constantClassImpl(int nameIndex) throws IOException {
		prepareConstant();
		print("CONSTANT_Class_info: name_index = ");
		println(nameIndex);
		writer.flush();
	}

	@Override
	protected void constantFieldrefImpl(int classIndex, int nameAndTypeIndex) throws IOException {
		prepareConstant();
		print("CONSTANT_Fieldref_info: class_index = ");
		print(classIndex);
		print(", name_and_type_index = ");
		println(nameAndTypeIndex);
		writer.flush();
	}

	@Override
	protected void constantMethodrefImpl(int classIndex, int nameAndTypeIndex) throws IOException {
		prepareConstant();
		print("CONSTANT_Methodref_info: class_index = ");
		print(classIndex);
		print(", name_and_type_index = ");
		println(nameAndTypeIndex);
		writer.flush();
	}

	@Override
	protected void constantInterfaceMethodrefImpl(int classIndex, int nameAndTypeIndex) throws IOException {
		prepareConstant();
		print("CONSTANT_InterfaceMethodref_info: class_index = ");
		print(classIndex);
		print(", name_and_type_index = ");
		println(nameAndTypeIndex);
		writer.flush();
	}

	@Override
	protected void constantStringImpl(int stringIndex) throws IOException {
		prepareConstant();
		print("CONSTANT_String_info: string_index = ");
		println(stringIndex);
		writer.flush();
	}

	@Override
	protected void constantIntegerImpl(int value) throws IOException {
		prepareConstant();
		print("CONSTANT_Integer_info: bytes = ");
		println(value);
		writer.flush();
	}

	@Override
	protected void constantFloatImpl(float value) throws IOException {
		prepareConstant();
		print("CONSTANT_Float_info: bytes = 0x");
		print(Long.toHexString(0x100000000L | (long)Float.floatToIntBits(value)).substring(1).toUpperCase());
		print(" (");
		print(String.valueOf(value));
		println(')');
		writer.flush();
	}

	@Override
	protected void constantLongImpl(long value) throws IOException {
		prepareConstant();
		++poolIndex;
		print("CONSTANT_Long_info: bytes = ");
		println(value);
		writer.flush();
	}

	@Override
	protected void constantDoubleImpl(double value) throws IOException {
		prepareConstant();
		++poolIndex;
		print("CONSTANT_Double_info: bytes = 0x");
		String hex = Long.toHexString(Double.doubleToLongBits(value)).toUpperCase();
		int length = hex.length();
		if(length < 8)
			writer.write(DumpingClassFileSink.ZEROES, 0, 8 - length);
		print(hex);
		print(" (");
		print(String.valueOf(value));
		println(')');
		writer.flush();
	}

	@Override
	protected void constantNameAndTypeImpl(int nameIndex, int descriptorIndex) throws IOException {
		prepareConstant();
		print("CONSTANT_NameAndType_info: name_index = ");
		print(nameIndex);
		print(", descriptor_index = ");
		println(descriptorIndex);
		writer.flush();
	}

	@Override
	protected void constantUTF8Impl(String value) throws IOException {
		prepareConstant();
		print("CONSTANT_Utf8_info: value = '");
		int length = value.length();
		for(int i = 0; i < length; ++i) {
			char c = value.charAt(i);
			switch(c) {
				case '\b':
					print("\\b");
					break;
				case '\f':
					print("\\f");
					break;
				case '\n':
					print("\\n");
					break;
				case '\r':
					print("\\r");
					break;
				case '\t':
					print("\\t");
					break;
				default:
					if(c >= ' ' && c <= '~')
						print(c);
					else {
						int code = (int)c;
						print("\\u");
						print(Integer.toHexString(code | 0x10000).substring(1).toUpperCase());
					}
					break;
			}
		}
		println('\'');
		writer.flush();
	}

	@Override
	protected void constantMethodHandleImpl(MethodHandleReferenceKind referenceKind, int referenceIndex)
			throws IOException {
		prepareConstant();
		print("CONSTANT_MethodHandle_info: reference_kind = ");
		print(referenceKind.specName);
		print(", reference_index = ");
		println(referenceIndex);
		writer.flush();
	}

	@Override
	protected void constantMethodTypeImpl(int descriptorIndex) throws IOException {
		prepareConstant();
		print("CONSTANT_MethodType_info: descriptor_index = ");
		println(descriptorIndex);
		writer.flush();
	}

	@Override
	protected void constantInvokeDynamicImpl(int bootstrapMethodAttrIndex, int nameAndTypeIndex)
			throws IOException {
		prepareConstant();
		print("CONSTANT_InvokeDynamic_info: bootstrap_method_attr_index = ");
		print(bootstrapMethodAttrIndex);
		print(", name_and_type_index = ");
		println(nameAndTypeIndex);
		writer.flush();
	}

	@Override
	protected void endConstantsImpl() throws IOException {}

	@Override
	protected void accessFlagsImpl(int flags) throws IOException {
		print("Access flags: 0x");
		print(Integer.toHexString(flags).toUpperCase());
		print(" (");
		print(ClassFileConstants.formatClassAccessFlags(flags));
		println(')');
		writer.flush();
	}

	@Override
	protected void thisClassImpl(int thisClassIndex) throws IOException {
		print("this_class = ");
		println(thisClassIndex);
		writer.flush();
	}

	@Override
	protected void superClassImpl(int superClassIndex) throws IOException {
		print("super_class = ");
		println(superClassIndex);
		writer.flush();
	}

	@Override
	protected void beginInterfacesImpl(int count) throws IOException {
		print("Interfaces (count = ");
		print(count);
		println("):");
		writer.flush();
	}

	@Override
	protected void superInterfaceImpl(int classInfoIndex) throws IOException {
		indent(1);
		print('#');
		println(classInfoIndex);
		writer.flush();
	}

	@Override
	protected void endInterfacesImpl() throws IOException {}

	@Override
	protected void beginFieldsImpl(int count) throws IOException {
		print("Fields (count = ");
		print(count);
		println("):");
		writer.flush();
	}

	@Override
	protected void beginFieldImpl(int accessFlags, int nameIndex, int descriptorIndex) throws IOException {
		indent(1);
		print("access_flags = 0x");
		print(Integer.toHexString(accessFlags).toUpperCase());
		print(" (");
		print(ClassFileConstants.formatFieldAccessFlags(accessFlags));
		print("), name_index = ");
		print(nameIndex);
		print(", descriptor_index = ");
		print(descriptorIndex);
		inMember = true;
		writer.flush();
	}

	@Override
	protected void beginAttributesImpl(int count) throws IOException {
		if(inMember) {
			print(", attributes_count = ");
			println(count);
		}
		else {
			print("Attributes (count = ");
			print(count);
			println("):");
		}
		writer.flush();
	}

	@Override
	protected void beginAttributeImpl(int nameIndex, long length) throws IOException {
		indent(inMember ? 2 : 1);
		print("Attribute: attribute_name_index = ");
		print(nameIndex);
		print(", attribute_length = ");
		println(length);
		writer.flush();
	}

	@Override
	protected void attributeBytesImpl(byte[] buffer, int offset, int length) throws IOException {}

	@Override
	protected void endAttributeImpl() throws IOException {}

	@Override
	protected void endAttributesImpl() throws IOException {}

	@Override
	protected void endFieldImpl() throws IOException {}

	@Override
	protected void endFieldsImpl() throws IOException {
		inMember = false;
	}

	@Override
	protected void beginMethodsImpl(int count) throws IOException {
		print("Methods (count = ");
		print(count);
		println("):");
		writer.flush();
	}

	@Override
	protected void beginMethodImpl(int accessFlags, int nameIndex, int descriptorIndex) throws IOException {
		indent(1);
		print("access_flags = 0x");
		print(Integer.toHexString(accessFlags).toUpperCase());
		print(" (");
		print(ClassFileConstants.formatMethodAccessFlags(accessFlags));
		print("), name_index = ");
		print(nameIndex);
		print(", descriptor_index = ");
		print(descriptorIndex);
		inMember = true;
		writer.flush();
	}

	@Override
	protected void endMethodImpl() throws IOException {
		inMember = false;
	}

	@Override
	protected void endMethodsImpl() throws IOException {}

}
