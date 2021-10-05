package click.escuela.grade.exception;

import click.escuela.grade.enumerator.SchoolMessage;

public class SchoolException extends TransactionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SchoolException(SchoolMessage schoolMessage) {
		super(schoolMessage.getCode() ,schoolMessage.getDescription());
	}

}
