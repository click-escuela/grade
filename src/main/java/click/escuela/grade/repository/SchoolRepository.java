package click.escuela.grade.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.grade.model.School;


public interface SchoolRepository extends JpaRepository<School, UUID> {

}
