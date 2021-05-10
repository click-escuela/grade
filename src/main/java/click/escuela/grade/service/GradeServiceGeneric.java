package click.escuela.grade.service;

import java.util.List;

import click.escuela.grade.exception.TransactionException;

public interface GradeServiceGeneric<T, S> {

	public void create(String id, T entity) throws TransactionException;

	public List<S> findAll();
}
