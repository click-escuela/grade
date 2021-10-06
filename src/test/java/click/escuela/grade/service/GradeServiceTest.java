package click.escuela.grade.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

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
import click.escuela.grade.api.GradeCreateApi;
import click.escuela.grade.dto.CourseStudentsShortDTO;
import click.escuela.grade.dto.GradeDTO;
import click.escuela.grade.dto.StudentShortDTO;
import click.escuela.grade.enumerator.GradeMessage;

import click.escuela.grade.enumerator.GradeType;
import click.escuela.grade.exception.SchoolException;
import click.escuela.grade.exception.TransactionException;
import click.escuela.grade.mapper.Mapper;
import click.escuela.grade.model.Grade;
import click.escuela.grade.model.School;
import click.escuela.grade.repository.GradeRepository;
import click.escuela.grade.service.impl.GradeServiceImpl;
import click.escuela.grade.service.impl.SchoolServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class GradeServiceTest {

	@Mock
	private GradeRepository gradeRepository;
	
	@Mock
	private SchoolServiceImpl schoolService;

	private GradeServiceImpl gradeServiceImpl = new GradeServiceImpl();
	private GradeApi gradeApi;
	private GradeCreateApi gradeCreateApi;
	private Grade grade;
	private UUID id;
	private UUID studentId;
	private UUID courseId;
	private UUID schoolId;
	private List<CourseStudentsShortDTO> courses = new ArrayList<>();
	private List<UUID> listUUID = new ArrayList<>();
	private List<StudentShortDTO> students = new ArrayList<>();
	private List<UUID> listUUIDStudents = new ArrayList<>();

	@Before
	public void setUp() throws SchoolException{
		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		studentId = UUID.randomUUID();
		courseId = UUID.randomUUID();
		schoolId = UUID.randomUUID();
		School school = new School();
		school.setId(schoolId);
		grade = Grade.builder().id(id).name("Examen").subject("Matematica").type(GradeType.HOMEWORK).school(school)
				.courseId(courseId).number(10).studentId(studentId).build();
		gradeApi = GradeApi.builder().name("Examen").subject("Matematica").studentId(studentId.toString())
				.type(GradeType.HOMEWORK.toString()).courseId(courseId.toString()).number(10)
				.build();
		List<String> studentsIds = new ArrayList<>();
		studentsIds.add(studentId.toString());
		List<Integer> notes = new ArrayList<>();
		notes.add(10);
		gradeCreateApi = GradeCreateApi.builder().name("Examen").subject("Matematica").studentIds(studentsIds)
				.type(GradeType.HOMEWORK.toString()).courseId(courseId.toString()).numbers(notes)
				.build();
		Optional<Grade> optional = Optional.of(grade);
		CourseStudentsShortDTO course = new CourseStudentsShortDTO();
		course.setCountStudent(20);
		course.setDivision("B");
		course.setId(courseId.toString());
		course.setYear(10);
		List<Grade> grades = new ArrayList<>();
		grades.add(grade);
		StudentShortDTO student = new StudentShortDTO();
		student.setId(studentId.toString());
		student.setName("Anotnio");
		student.setSurname("Liendro");
		student.setGrades(Mapper.mapperToGradesDTO(grades));

		students.add(student);
		course.setStudents(students);
		courses.add(course);
		listUUID.add(courseId);
		listUUIDStudents.add(studentId);
		Mockito.when(Mapper.mapperToGrade(gradeApi)).thenReturn(grade);
		Mockito.when(Mapper.mapperToGrade(gradeCreateApi)).thenReturn(grade);
		Mockito.when(gradeRepository.findById(id)).thenReturn(optional);
		Mockito.when(gradeRepository.findByIdAndSchoolId(id, schoolId)).thenReturn(optional);
		Mockito.when(gradeRepository.save(grade)).thenReturn(grade);
		Mockito.when(gradeRepository.findAll()).thenReturn(grades);
		Mockito.when(gradeRepository.findByCourseIdIn(listUUID)).thenReturn(grades);
		Mockito.when(gradeRepository.findByStudentIdIn(listUUIDStudents)).thenReturn(grades);
		Mockito.when(schoolService.getById(schoolId.toString())).thenReturn(school);

		ReflectionTestUtils.setField(gradeServiceImpl, "gradeRepository", gradeRepository);
		ReflectionTestUtils.setField(gradeServiceImpl, "schoolService", schoolService);

	}

	@Test
	public void whenCreateIsOk() {
		boolean hasError = false;
		try {
			gradeServiceImpl.create(schoolId.toString(), gradeCreateApi);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenCreateIsError() {
		Mockito.when(gradeRepository.save(null)).thenThrow(IllegalArgumentException.class);
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			gradeServiceImpl.create(schoolId.toString(), new GradeCreateApi());
		}).withMessage(GradeMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenUpdateIsOk() {
		boolean hasError = false;
		try {
			gradeApi.setId(id.toString());
			gradeServiceImpl.update(schoolId.toString(), gradeApi);
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
			gradeServiceImpl.update(schoolId.toString(), gradeApi);
		}).withMessage(GradeMessage.GET_ERROR.getDescription());
	}

	@Test
	public void whenGetByIdIsOK() throws TransactionException {
		boolean hasError = false;
		try {
			gradeServiceImpl.getById(schoolId.toString(), id.toString());
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}

	@Test
	public void whenGetByIdIsError(){
		id = UUID.randomUUID();
		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			gradeServiceImpl.getById(schoolId.toString(), id.toString());
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
	
	@Test
	public void whenGetCourseWithGradesIsEmpty() {
		courses.get(0).setId(UUID.randomUUID().toString());
		List<GradeDTO> grades = gradeServiceImpl.getCoursesWithGrades(courses).get(0).getStudents().get(0).getGrades();
		assertThat(grades).isEmpty();
	}
	
	@Test
	public void whenGetCourseWithGradesIsEmptyTwo() {
		courses.get(0).getStudents().get(0).setId(UUID.randomUUID().toString());
		List<GradeDTO> grades = gradeServiceImpl.getCoursesWithGrades(courses).get(0).getStudents().get(0).getGrades();
		assertThat(grades).isEmpty();
	}

	@Test
	public void whenGetAllIsOK() {
		gradeServiceImpl.findAll();
		verify(gradeRepository).findAll();
	}
	
	@Test
	public void whenGetStudentsWithGradesIsOk() {
		boolean hasError = false;
		try {
			gradeServiceImpl.getStudentsWithGrades(students);
		} catch (Exception e) {
			hasError = true;
		}
		assertThat(hasError).isFalse();
	}
	
	@Test
	public void whenGetStudentsWithGradesIsEmpty() {
		students.get(0).setId(UUID.randomUUID().toString());
		List<GradeDTO> grades = gradeServiceImpl.getStudentsWithGrades(students).get(0).getGrades();
		assertThat(grades).isEmpty();
	}
	
}
