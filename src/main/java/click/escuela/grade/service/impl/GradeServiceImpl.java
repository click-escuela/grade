package click.escuela.grade.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.grade.api.GradeApi;
import click.escuela.grade.dto.GradeDTO;
import click.escuela.grade.enumerator.GradeEnum;
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
	public void create(String studentId, GradeApi gradeApi) throws TransactionException {
		try {
			Grade grade = Mapper.mapperToGrade(gradeApi);
			grade.setStudentId(UUID.fromString(studentId));
			gradeRepository.save(grade);
		} catch (Exception e) {
			throw new TransactionException(GradeEnum.CREATE_ERROR.getCode(), GradeEnum.CREATE_ERROR.getDescription());
		}
	}

	@Override
	public List<GradeDTO> findAll() {
		List<Grade> listGrades = gradeRepository.findAll();
		return Mapper.mapperToGradesDTO(listGrades);
	}

}
