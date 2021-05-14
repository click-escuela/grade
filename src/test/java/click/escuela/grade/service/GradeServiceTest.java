package click.escuela.grade.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
import click.escuela.grade.enumerator.GradeType;
import click.escuela.grade.exception.TransactionException;
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
	private UUID courseId;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		studentId = UUID.randomUUID();
		courseId = UUID.randomUUID();

		Grade grade = Grade.builder().id(id).name("Examen").subject("Matematica").type(GradeType.HOMEWORK)
				.courseId(courseId).number(10).studentId(studentId).build();

		gradeApi = GradeApi.builder().name("Examen").subject("Matematica").type(GradeType.HOMEWORK.toString()).number(10).build();

		Mockito.when(Mapper.mapperToGrade(gradeApi)).thenReturn(grade);
		Mockito.when(gradeRepository.save(grade)).thenReturn(grade);

		ReflectionTestUtils.setField(gradeServiceImpl, "gradeRepository", gradeRepository);
	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			gradeServiceImpl.create(gradeApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() {

		GradeApi gradeApi = GradeApi.builder().name("Parcial").subject("Literatura").type(GradeType.EXAM.toString()).number(5)
				.build();

		Mockito.when(gradeRepository.save(null)).thenThrow(IllegalArgumentException.class);

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {

			gradeServiceImpl.create(gradeApi);
		}).withMessage("No se pudo crear la nota correctamente");
	}

}
