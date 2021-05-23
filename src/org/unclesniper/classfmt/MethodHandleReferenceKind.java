package org.unclesniper.classfmt;

public enum MethodHandleReferenceKind {

	GET_FIELD("REF_getField"),
	GET_STATIC("REF_getStatic"),
	PUT_FIELD("REF_putField"),
	PUT_STATIC("REF_putStatic"),
	INVOKE_VIRTUAL("REF_invokeVirtual"),
	INVOKE_STATIC("REF_invokeStatic"),
	INVOKE_SPECIAL("REF_invokeSpecial"),
	NEW_INVOKE_SPECIAL("REF_newInvokeSpecial"),
	INVOKE_INTERFACE("REF_invokeInterface");

	private static final MethodHandleReferenceKind[] TABLE = MethodHandleReferenceKind.values();

	public final String specName;

	public final int code = ordinal() + 1;

	private MethodHandleReferenceKind(String specName) {
		this.specName = specName;
	}

	public static MethodHandleReferenceKind byCode(int code) {
		--code;
		if(code < 0 || code >= MethodHandleReferenceKind.TABLE.length)
			return null;
		return MethodHandleReferenceKind.TABLE[code];
	}

}
