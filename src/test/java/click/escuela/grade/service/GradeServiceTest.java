package click.escuela.grade.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.grade.api.GradeApi;
import click.escuela.grade.mapper.Mapper;
import click.escuela.grade.model.Grade;
import click.escuela.grade.repository.GradeRepository;
import click.escuela.grade.service.impl.GradeServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class GradeServiceTest {

	@Mock
	private GradeRepository gradeRepository;

	private GradeServiceImpl gradeServiceImpl = new GradeServiceImpl();
	private GradeApi gradeApi;
	private UUID id;
	private UUID studentId;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		studentId = UUID.randomUUID();

		Grade grade = Grade.builder().id(id).name("Examen").subject("Matematica").type("Domiciliario")
				.course("Segundo año").studentId(studentId).build();

		gradeApi = GradeApi.builder().name("Examen").subject("Matematica").type("Domiciliario").course("Segundo año")
				.build();

		Mockito.when(Mapper.mapperToGrade(gradeApi)).thenReturn(grade);
		Mockito.when(gradeRepository.save(grade)).thenReturn(grade);

		ReflectionTestUtils.setField(gradeServiceImpl, "gradeRepository", gradeRepository);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError=false;
		try {
			gradeServiceImpl.create(studentId.toString(), gradeApi);
		}catch(Exception e) {
			hasError=true;
		}
		assertThat(hasError).isFalse();
	}

}
