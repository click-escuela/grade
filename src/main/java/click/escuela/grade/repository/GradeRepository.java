package click.escuela.grade.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.grade.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, UUID> {

}
