package click.escuela.grade.service;

import click.escuela.grade.exception.TransactionException;

public interface GradeGeneric<T, S> {
	
	public void create(T entity) throws TransactionException;

	public S getById(String id) throws TransactionException;
}
