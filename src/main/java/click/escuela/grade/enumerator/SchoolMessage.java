package click.escuela.grade.enumerator;

public enum SchoolMessage {

	GET_ERROR("GET_ERROR","No se pudo encontrar la escuela");

	private String code;
	private String description;
	
	SchoolMessage(String code, String description) {
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
