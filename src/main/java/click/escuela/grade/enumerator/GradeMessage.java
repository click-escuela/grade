package click.escuela.grade.enumerator;

public enum GradeMessage {

	CREATE_OK("CREATE_OK", "Se creó la nota correctamente"),
	CREATE_ERROR("CREATE_ERROR", "No se pudo crear la nota correctamente"),
	GET_ERROR("GET_ERROR", "La nota que busca no existe"), 
	UPDATE_OK("UPDATE_OK", "Se modificó la nota correctamente"),
	UPDATE_ERROR("UPDATE_ERROR", "No se pudo modificar la nota correctamente");

	private String code;
	private String description;

	GradeMessage(String code, String description) {
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
