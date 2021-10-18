package click.escuela.grade.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.grade.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, UUID> {

	public List<Grade> findBySchoolId(UUID schoolId);

	public List<Grade> findByCourseId(UUID id);

	public List<Grade> findByStudentId(UUID id);

	public List<Grade> findByCourseIdIn(List<UUID> listUUID);

	public List<Grade> findByStudentIdIn(List<UUID> listUUID);

	public Optional<Grade> findByIdAndSchoolId(UUID id, UUID schoolId);
}
