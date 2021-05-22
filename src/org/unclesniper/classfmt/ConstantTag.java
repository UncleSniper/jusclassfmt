package org.unclesniper.classfmt;

public enum ConstantTag {

	CLASS(7),
	FIELDREF(9),
	METHODREF(10),
	INTERFACE_METHODREF(11),
	STRING(8),
	INTEGER(3),
	FLOAT(4),
	LONG(5),
	DOUBLE(6),
	NAME_AND_TYPE(12),
	UTF8(1),
	METHOD_HANDLE(15),
	METHOD_TYPE(16),
	INVOKE_DYNAMIC(18);

	private static final ConstantTag[] TABLE;

	static {
		ConstantTag[] values = ConstantTag.values();
		int max = -1;
		for(ConstantTag tag : values) {
			if(tag.code > max)
				max = tag.code;
		}
		TABLE = new ConstantTag[max + 1];
		for(ConstantTag tag : values)
			TABLE[tag.code] = tag;
	}

	public final int code;

	private ConstantTag(int code) {
		this.code = code;
	}

	public static ConstantTag byCode(int code) {
		if(code < 0 || code >= ConstantTag.TABLE.length)
			return null;
		return ConstantTag.TABLE[code];
	}

}
