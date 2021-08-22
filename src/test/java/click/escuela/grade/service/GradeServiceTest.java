package click.escuela.grade.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import click.escuela.grade.dto.CourseStudentsShortDTO;
import click.escuela.grade.dto.StudentShortDTO;
import click.escuela.grade.enumerator.GradeMessage;

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
	private Grade grade;
	private UUID id;
	private UUID studentId;
	private UUID courseId;
	private Integer schoolId;
	private List<CourseStudentsShortDTO> courses = new ArrayList<>();


	@Before
	public void setUp() {
		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		studentId = UUID.randomUUID();
		courseId = UUID.randomUUID();
		schoolId = 1234;
		grade = Grade.builder().id(id).name("Examen").subject("Matematica").type(GradeType.HOMEWORK).schoolId(schoolId)
				.courseId(courseId).number(10).studentId(studentId).build();
		gradeApi = GradeApi.builder().name("Examen").subject("Matematica").studentId(studentId.toString())
				.type(GradeType.HOMEWORK.toString()).courseId(courseId.toString()).schoolId(schoolId).number(10)
				.build();
		Optional<Grade> optional = Optional.of(grade);
		CourseStudentsShortDTO course = new CourseStudentsShortDTO();
		course.setCountStudent(20);
		course.setDivision("B");
		course.setId(courseId.toString());
		course.setYear(10);
		StudentShortDTO student = new StudentShortDTO();
		student.setId(studentId.toString());
		student.setName("Anotnio");
		student.setSurname("Liendro");
		List<StudentShortDTO> students = new ArrayList<>();
		students.add(student);
		course.setStudents(students);
		courses.add(course);
		
		Mockito.when(Mapper.mapperToGrade(gradeApi)).thenReturn(grade);
		Mockito.when(gradeRepository.findById(id)).thenReturn(optional);
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
		GradeApi gradeApi = GradeApi.builder().name("Parcial").subject("Literatura").type(GradeType.EXAM.toString())
				.number(5).build();
		Mockito.when(gradeRepository.save(null)).thenThrow(IllegalArgumentException.class);

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			gradeServiceImpl.create(gradeApi);
		}).withMessage(GradeMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenUpdateIsOk() {
		boolean hasError = false;
		try {
			gradeApi.setId(id.toString());
			gradeServiceImpl.update(gradeApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenUpdateIsError() {
		id = UUID.randomUUID();

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			gradeApi.setId(id.toString());
			gradeServiceImpl.update(gradeApi);
		}).withMessage(GradeMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetByIdIsOK() throws TransactionException {
		boolean hasError = false;
		try {
			gradeServiceImpl.getById(id.toString());
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetByIdIsError() throws TransactionException {
		id = UUID.randomUUID();
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			gradeServiceImpl.getById(id.toString());
		}).withMessage(GradeMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetBySchoolIsOK() throws TransactionException {
		boolean hasError = false;
		try {
			gradeServiceImpl.getBySchoolId(schoolId.toString());
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetBySchoolIsError() {
		boolean hasError = false;
		try {
			gradeServiceImpl.getBySchoolId(null);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isTrue();
	}

	@Test
	public void whenGetByIdCourseIsOK() {
		boolean hasError = false;
		try {
			gradeServiceImpl.getByCourseId(courseId.toString());
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetByIdCourseIsError() {
		boolean hasError = false;
		try {
			gradeServiceImpl.getByCourseId(null);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isTrue();
	}

	@Test
	public void whenGetByIdStudentIsOK() {
		boolean hasError = false;
		try {
			gradeServiceImpl.getByStudentId(studentId.toString());
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetByIdStudentIsError() {
		boolean hasError = false;
		try {
			gradeServiceImpl.getByStudentId(null);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isTrue();
	}
	
	@Test
	public void whenGetCourseWithGradesIsOk() {
		boolean hasError = false;
		try {
			gradeServiceImpl.getCoursesWithGrades(courses);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

}
