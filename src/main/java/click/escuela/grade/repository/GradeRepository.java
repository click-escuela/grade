package click.escuela.grade.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.grade.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, UUID> {

  public List<Grade> findBySchoolId(Integer schoolId);

	public List<Grade> findByCourseId(UUID id);

	public List<Grade> findByStudentId(UUID id);

	public List<Grade> findByCourseIdIn(List<UUID> listUUID);
}
