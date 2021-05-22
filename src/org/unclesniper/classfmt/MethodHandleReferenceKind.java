package org.unclesniper.classfmt;

public enum MethodHandleReferenceKind {

	GET_FIELD,
	GET_STATIC,
	PUT_FIELD,
	PUT_STATIC,
	INVOKE_VIRTUAL,
	INVOKE_STATIC,
	INVOKE_SPECIAL,
	NEW_INVOKE_SPECIAL,
	INVOKE_INTERFACE;

	private static final MethodHandleReferenceKind[] TABLE = MethodHandleReferenceKind.values();

	public final int code = ordinal() + 1;

	public static MethodHandleReferenceKind byCode(int code) {
		--code;
		if(code < 0 || code >= MethodHandleReferenceKind.TABLE.length)
			return null;
		return MethodHandleReferenceKind.TABLE[code];
	}

}
