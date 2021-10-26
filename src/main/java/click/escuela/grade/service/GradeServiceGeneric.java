package click.escuela.grade.service;

import java.util.List;

import click.escuela.grade.exception.TransactionException;

public interface GradeServiceGeneric<T, S> {
	
	public void update(String schoolId, T entity) throws TransactionException;

	public List<S> findAll();
}
