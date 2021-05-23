package org.unclesniper.classfmt;

public class ClassFileConstants {

	public static final int ACC_PUBLIC       = 0x0001;
	public static final int ACC_PRIVATE      = 0x0002;
	public static final int ACC_PROTECTED    = 0x0004;
	public static final int ACC_STATIC       = 0x0008;
	public static final int ACC_FINAL        = 0x0010;
	public static final int ACC_SUPER        = 0x0020;
	public static final int ACC_SYNCHRONIZED = 0x0020;
	public static final int ACC_VOLATILE     = 0x0040;
	public static final int ACC_BRIDGE       = 0x0040;
	public static final int ACC_TRANSIENT    = 0x0080;
	public static final int ACC_VARARGS      = 0x0080;
	public static final int ACC_NATIVE       = 0x0100;
	public static final int ACC_INTERFACE    = 0x0200;
	public static final int ACC_ABSTRACT     = 0x0400;
	public static final int ACC_STRICT       = 0x0800;
	public static final int ACC_SYNTHETIC    = 0x1000;
	public static final int ACC_ANNOTATION   = 0x2000;
	public static final int ACC_ENUM         = 0x4000;
	public static final int ACC_MANDATED     = 0x8000;

	private ClassFileConstants() {}

	private static void addFlag(int allFlags, int thisFlag, String flagName, StringBuilder sink) {
		if((allFlags & thisFlag) == 0)
			return;
		if(sink.length() > 0)
			sink.append(" | ");
		sink.append(flagName);
	}

	public static String formatClassAccessFlags(int allFlags) {
		StringBuilder builder = new StringBuilder();
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_PUBLIC, "ACC_PUBLIC", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_FINAL, "ACC_FINAL", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_SUPER, "ACC_SUPER", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_INTERFACE, "ACC_INTERFACE", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_ABSTRACT, "ACC_ABSTRACT", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_SYNTHETIC, "ACC_SYNTHETIC", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_ANNOTATION, "ACC_ANNOTATION", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_ENUM, "ACC_ENUM", builder);
		return builder.length() == 0 ? "<none>" : builder.toString();
	}

	public static String formatFieldAccessFlags(int allFlags) {
		StringBuilder builder = new StringBuilder();
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_PUBLIC, "ACC_PUBLIC", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_PRIVATE, "ACC_PRIVATE", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_PROTECTED, "ACC_PROTECTED", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_STATIC, "ACC_STATIC", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_FINAL, "ACC_FINAL", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_VOLATILE, "ACC_VOLATILE", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_TRANSIENT, "ACC_TRANSIENT", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_SYNTHETIC, "ACC_SYNTHETIC", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_ENUM, "ACC_ENUM", builder);
		return builder.length() == 0 ? "<none>" : builder.toString();
	}

	public static String formatMethodAccessFlags(int allFlags) {
		StringBuilder builder = new StringBuilder();
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_PUBLIC, "ACC_PUBLIC", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_PRIVATE, "ACC_PRIVATE", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_PROTECTED, "ACC_PROTECTED", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_STATIC, "ACC_STATIC", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_FINAL, "ACC_FINAL", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_SYNCHRONIZED, "ACC_SYNCHRONIZED", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_BRIDGE, "ACC_BRIDGE", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_VARARGS, "ACC_VARARGS", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_NATIVE, "ACC_NATIVE", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_ABSTRACT, "ACC_ABSTRACT", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_STRICT, "ACC_STRICT", builder);
		ClassFileConstants.addFlag(allFlags, ClassFileConstants.ACC_SYNTHETIC, "ACC_SYNTHETIC", builder);
		return builder.length() == 0 ? "<none>" : builder.toString();
	}

}
