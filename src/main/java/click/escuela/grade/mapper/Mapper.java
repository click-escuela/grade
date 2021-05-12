package click.escuela.grade.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import click.escuela.grade.api.GradeApi;
import click.escuela.grade.dto.GradeDTO;
import click.escuela.grade.model.Grade;

@Component
public class Mapper {
	
	private Mapper() {
	    throw new IllegalStateException("");
	}
	
	private static ModelMapper modelMapper = new ModelMapper();

	public static Grade mapperToGrade(GradeApi gradeApi) {
		Grade grade=modelMapper.map(gradeApi, Grade.class);
		grade.setCourseId(UUID.fromString(gradeApi.getCourseId()));
		grade.setStudentId(UUID.fromString(gradeApi.getStudentId()));
		
		return grade;
	}

	public static GradeDTO mapperToGradeDTO(Grade grade) {
		return modelMapper.map(grade, GradeDTO.class);
	}

	public static List<GradeDTO> mapperToGradesDTO(List<Grade> grades) {
		List<GradeDTO> gradeDTOlist = new ArrayList<>();
		grades.stream().forEach(p -> gradeDTOlist.add(mapperToGradeDTO(p)));
		return gradeDTOlist;
	}

}
