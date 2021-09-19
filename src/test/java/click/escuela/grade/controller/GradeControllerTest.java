package click.escuela.grade.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.grade.api.GradeApi;
import click.escuela.grade.api.GradeCreateApi;
import click.escuela.grade.dto.CourseStudentsShortDTO;
import click.escuela.grade.dto.GradeDTO;
import click.escuela.grade.dto.StudentShortDTO;
import click.escuela.grade.enumerator.GradeMessage;
import click.escuela.grade.enumerator.GradeType;
import click.escuela.grade.exception.TransactionException;
import click.escuela.grade.mapper.Mapper;
import click.escuela.grade.model.Grade;

import click.escuela.grade.rest.GradeController;
import click.escuela.grade.rest.handler.Handler;
import click.escuela.grade.service.impl.GradeServiceImpl;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class GradeControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private GradeController gradeController;

	@Mock
	private GradeServiceImpl gradeService;

	private ObjectMapper mapper;
	private GradeApi gradeApi;
	private GradeCreateApi gradeCreateApi;
	private static String EMPTY = "";
	private String id;

	private String schoolId;
	private String studentId;
	private String courseId;
	private List<CourseStudentsShortDTO> courses = new ArrayList<>();

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(gradeController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(gradeController, "gradeService", gradeService);

		id = UUID.randomUUID().toString();
		schoolId = "1234";
		studentId = UUID.randomUUID().toString();
		courseId = UUID.randomUUID().toString();
		gradeApi = GradeApi.builder().name("Examen").subject("Matematica").studentId(studentId)
				.type(GradeType.HOMEWORK.toString()).courseId(courseId).schoolId(Integer.valueOf(schoolId)).number(10)
				.build();
		List<String> studentsIds = new ArrayList<>();
		studentsIds.add(studentId.toString());
		List<Integer> notes = new ArrayList<>();
		notes.add(10);
		gradeCreateApi = GradeCreateApi.builder().name("Examen").subject("Matematica").studentIds(studentsIds)
				.type(GradeType.HOMEWORK.toString()).courseId(courseId.toString()).schoolId(1234).numbers(notes)
				.build();
		Grade grade = Grade.builder().id(UUID.fromString(id)).name("Examen").subject("Matematica")
				.studentId(UUID.fromString(studentId)).type(GradeType.HOMEWORK).courseId(UUID.fromString(courseId))
				.schoolId(Integer.valueOf(schoolId)).number(10).build();
		List<Grade> grades = new ArrayList<>();
		grades.add(grade);
		
		CourseStudentsShortDTO course = new CourseStudentsShortDTO();
		course.setCountStudent(20);
		course.setDivision("B");
		course.setId(courseId);
		course.setYear(10);
		StudentShortDTO student = new StudentShortDTO();
		student.setId(studentId);
		student.setName("Anotnio");
		student.setSurname("Liendro");
		student.setGrades(Mapper.mapperToGradesDTO(grades));
		List<StudentShortDTO> students = new ArrayList<>();
		students.add(student);
		course.setStudents(students);
		courses.add(course);

		doNothing().when(gradeService).create(Mockito.any());
		Mockito.when(gradeService.getById(id)).thenReturn(Mapper.mapperToGradeDTO(grade));
		Mockito.when(gradeService.getBySchoolId(schoolId)).thenReturn(Mapper.mapperToGradesDTO(grades));
		Mockito.when(gradeService.getByCourseId(courseId)).thenReturn(Mapper.mapperToGradesDTO(grades));
		Mockito.when(gradeService.getByStudentId(studentId)).thenReturn(Mapper.mapperToGradesDTO(grades));
		Mockito.when(gradeService.getCoursesWithGrades(Mockito.any())).thenReturn(courses);
		Mockito.when(gradeService.getStudentsWithGrades(Mockito.any())).thenReturn(students);

	}

	@Test
	public void whenCreateIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/grade", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeCreateApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(GradeMessage.CREATE_OK.name());

	}

	@Test
	public void whenCreateButNameEmpty() throws JsonProcessingException, Exception {
		gradeCreateApi.setName(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/grade", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeCreateApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Name cannot be empty");

	}

	@Test
	public void whenCreateButSubjectEmpty() throws JsonProcessingException, Exception {
		gradeCreateApi.setSubject(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/grade", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeCreateApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Subject cannot be empty");

	}

	@Test
	public void whenCreateButCourseEmpty() throws JsonProcessingException, Exception {
		gradeCreateApi.setCourseId(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/grade", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeCreateApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Course cannot be empty");
	}

	

	@Test
	public void whenCreateButTypeEmpty() throws JsonProcessingException, Exception {
		gradeCreateApi.setType(EMPTY);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/grade", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeCreateApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Type cannot be empty");
	}


	@Test
	public void whenCreateButSchoolNull() throws JsonProcessingException, Exception {
		gradeCreateApi.setSchoolId(null);
		MvcResult result = mockMvc.perform(post("/school/{schoolId}/grade", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeCreateApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("School cannot be null");
	}

	@Test
	public void whenUpdateOk() throws JsonProcessingException, Exception {

		gradeApi.setId(id);
		MvcResult result = mockMvc.perform(put("/school/{schoolId}/grade", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(GradeMessage.UPDATE_OK.name());
	}

	@Test
	public void whenUpdateError() throws JsonProcessingException, Exception {
		doThrow(new TransactionException(GradeMessage.UPDATE_ERROR.getCode(),
				GradeMessage.UPDATE_ERROR.getDescription())).when(gradeService).update(Mockito.any());

		gradeApi.setId(id);
		MvcResult result = mockMvc.perform(put("/school/{schoolId}/grade", schoolId)
				.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeApi))).andExpect(status().isBadRequest())
				.andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(GradeMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void getByIdIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/grade/{gradeId}", schoolId, id.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();
		GradeDTO response = mapper.readValue(result.getResponse().getContentAsString(), GradeDTO.class);
		assertThat(response).hasFieldOrPropertyWithValue("id", id.toString());
	}

	@Test
	public void getByIdIsError() throws JsonProcessingException, Exception {
		id = UUID.randomUUID().toString();
		doThrow(new TransactionException(GradeMessage.GET_ERROR.getCode(), GradeMessage.GET_ERROR.getDescription()))
				.when(gradeService).getById(id);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.get("/school/{schoolId}/grade/{gradeId}", schoolId, id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(GradeMessage.GET_ERROR.getDescription());
	}

	@Test
	public void getByIdSchoolIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/grade", schoolId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<GradeDTO>> typeReference = new TypeReference<List<GradeDTO>>() {
		};
		List<GradeDTO> results = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(results.get(0).getId()).contains(id.toString());
	}

	@Test
	public void getByIdSchoolIsEmpty() throws JsonProcessingException, Exception {
		schoolId = "6666";
		Mockito.when(gradeService.getBySchoolId(schoolId)).thenReturn(new ArrayList<>());

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/grade", schoolId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

	@Test
	public void getByIdStudentIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/grade/student/{studentId}", schoolId, studentId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<GradeDTO>> typeReference = new TypeReference<List<GradeDTO>>() {
		};
		List<GradeDTO> results = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(results.get(0).getId()).contains(id.toString());
	}

	@Test
	public void getByIdStudentIsEmpty() throws JsonProcessingException, Exception {
		studentId = UUID.randomUUID().toString();
		Mockito.when(gradeService.getByStudentId(studentId)).thenReturn(new ArrayList<>());

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/grade/student/{studentId}", schoolId, studentId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

	@Test
	public void getByIdCourseIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/grade/course/{courseId}", schoolId, courseId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();

		TypeReference<List<GradeDTO>> typeReference = new TypeReference<List<GradeDTO>>() {
		};
		List<GradeDTO> results = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(results.get(0).getId()).contains(id.toString());
	}

	@Test
	public void getByIdCourseIsEmpty() throws JsonProcessingException, Exception {
		courseId = UUID.randomUUID().toString();
		Mockito.when(gradeService.getByCourseId(courseId)).thenReturn(new ArrayList<>());

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/school/{schoolId}/grade/course/{courseId}", schoolId, courseId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("");
	}

	@Test
	public void getCoursesWithGradesIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.put("/school/{schoolId}/grade/courses", schoolId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(courses)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		TypeReference<List<CourseStudentsShortDTO>> typeReference = new TypeReference<List<CourseStudentsShortDTO>>() {};
		List<CourseStudentsShortDTO> results = mapper.readValue(result.getResponse().getContentAsString(),
				typeReference);
		assertThat(results.get(0).getId()).isEqualTo(courseId);
	}
	
	@Test
	public void getStudentsWithGradesIsOk() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.put("/school/{schoolId}/grade/students", schoolId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(courses)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		TypeReference<List<StudentShortDTO>> typeReference = new TypeReference<List<StudentShortDTO>>() {};
		List<StudentShortDTO> results = mapper.readValue(result.getResponse().getContentAsString(),
				typeReference);
		assertThat(results.get(0).getId()).isEqualTo(studentId);
	}

	@Test
	public void getGrades() throws JsonProcessingException, Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/school/{schoolId}/grade", schoolId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.ACCEPTED.value())).andReturn();
		TypeReference<List<GradeDTO>> typeReference = new TypeReference<List<GradeDTO>>() {};
		List<GradeDTO> results = mapper.readValue(result.getResponse().getContentAsString(), typeReference);
		assertThat(results.get(0).getId()).isEqualTo(id);
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}
}
