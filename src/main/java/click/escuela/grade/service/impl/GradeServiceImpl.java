package click.escuela.grade.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.grade.api.GradeApi;
import click.escuela.grade.dto.GradeDTO;
import click.escuela.grade.enumerator.GradeMessage;

import click.escuela.grade.exception.TransactionException;
import click.escuela.grade.mapper.Mapper;
import click.escuela.grade.model.Grade;
import click.escuela.grade.repository.GradeRepository;
import click.escuela.grade.service.GradeServiceGeneric;

@Service
public class GradeServiceImpl implements GradeServiceGeneric<GradeApi, GradeDTO> {

	@Autowired
	private GradeRepository gradeRepository;

	@Override
	public void create(GradeApi gradeApi) throws TransactionException {
		try {
			Grade grade = Mapper.mapperToGrade(gradeApi);
			gradeRepository.save(grade);
		} catch (Exception e) {
			throw new TransactionException(GradeMessage.CREATE_ERROR.getCode(),
					GradeMessage.CREATE_ERROR.getDescription());

		}
	}

	@Override
	public List<GradeDTO> findAll() {
		List<Grade> listGrades = gradeRepository.findAll();
		return Mapper.mapperToGradesDTO(listGrades);
	}

	@Override
	public void update(GradeApi gradeApi) throws TransactionException {

		findById(gradeApi.getId()).ifPresent(grade -> {

			grade.setName(gradeApi.getName());
			grade.setNumber(gradeApi.getNumber());
			grade.setSubject(gradeApi.getSubject());
			grade.setSchoolId(gradeApi.getSchoolId());
			grade.setType(Mapper.mapperToEnum(gradeApi.getType()));
			grade.setCourseId(UUID.fromString(gradeApi.getCourseId()));
			grade.setStudentId(UUID.fromString(gradeApi.getStudentId()));

			gradeRepository.save(grade);
		});

	}

	public Optional<Grade> findById(String id) throws TransactionException {
		return Optional.of(gradeRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new TransactionException(GradeMessage.GET_ERROR.getCode(),
						GradeMessage.GET_ERROR.getDescription())));
	}

	public GradeDTO getById(String id) throws TransactionException {
		Grade grade = findById(id).orElseThrow(() -> new TransactionException(GradeMessage.GET_ERROR.getCode(),
				GradeMessage.GET_ERROR.getDescription()));
		return Mapper.mapperToGradeDTO(grade);
	}

	public List<GradeDTO> getBySchoolId(String schoolId) {
		return Mapper.mapperToGradesDTO(gradeRepository.findBySchoolId(Integer.valueOf(schoolId)));
	}

	public List<GradeDTO> getByStudentId(String studentId) {
		return Mapper.mapperToGradesDTO(gradeRepository.findByStudentId(UUID.fromString(studentId)));
	}

	public List<GradeDTO> getByCourseId(String gradeId) {
		return Mapper.mapperToGradesDTO(gradeRepository.findByCourseId(UUID.fromString(gradeId)));
	}

}
