package click.escuela.grade.service;

import java.util.List;

import click.escuela.grade.exception.TransactionException;

public interface GradeServiceGeneric<T, S> {

	public void create(T entity) throws TransactionException;
	
	public void update(T entity) throws TransactionException;

	public List<S> findAll();
}
