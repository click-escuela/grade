package click.escuela.grade.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Errors {
	private final int status;
	private final String message;
	private List<ValidationError> validationErrors = new ArrayList<>();

	public Errors(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public void addFieldError(String field, String message) {
		ValidationError error = new ValidationError(message, field);
		validationErrors.add(error);
	}

	public List<ValidationError> getFieldErrors() {
		return validationErrors;
	}
}
