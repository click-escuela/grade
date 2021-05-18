package click.escuela.grade.enumerator;

public enum GradeEnum {

	CREATE_OK("CREATE_OK", "Se creó la nota correctamente"),
	CREATE_ERROR("CREATE_ERROR", "No se pudo crear la nota correctamente"),
	GET_ERROR("GET_ERROR", "La nota que se busca no existe");

	private String code;
	private String description;

	GradeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
