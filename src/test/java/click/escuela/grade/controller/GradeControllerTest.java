package click.escuela.grade.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.grade.api.GradeApi;
import click.escuela.grade.enumerator.GradeEnum;
import click.escuela.grade.enumerator.GradeType;
import click.escuela.grade.exception.TransactionException;
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
	private static String EMPTY = "";
	private String schoolId;
	private String studentId;
	private String courseId;

	@Before
	public void setup() throws TransactionException {
		mockMvc = MockMvcBuilders.standaloneSetup(gradeController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(gradeController, "gradeService", gradeService);

		schoolId = "1234";
		studentId = UUID.randomUUID().toString();
		courseId = UUID.randomUUID().toString();
		gradeApi = GradeApi.builder().name("Examen").subject("Matematica").studentId(studentId).type(GradeType.HOMEWORK.toString()).courseId(courseId)
				.number(10).build();

		doNothing().when(gradeService).create(Mockito.any());
	}

	@Test
	public void whenCreateIsOk() throws JsonProcessingException, Exception {

		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/grade", schoolId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeApi)))
				.andExpect(status().is2xxSuccessful()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains(GradeEnum.CREATE_OK.name());

	}

	@Test
	public void whenCreateButNameEmpty() throws JsonProcessingException, Exception {

		gradeApi.setName(EMPTY);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/grade", schoolId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Name cannot be empty");

	}

	@Test
	public void whenCreateButSubjectEmpty() throws JsonProcessingException, Exception {

		gradeApi.setSubject(EMPTY);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/grade", schoolId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Subject cannot be empty");

	}

	@Test
	public void whenCreateButCourseEmpty() throws JsonProcessingException, Exception {

		gradeApi.setCourseId(EMPTY);
		;
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/grade", schoolId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Course cannot be empty");

	}
	
	@Test
	public void whenCreateButStudentEmpty() throws JsonProcessingException, Exception {

		gradeApi.setStudentId(EMPTY);
		;
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/grade", schoolId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Student cannot be empty");

	}

	@Test
	public void whenCreateButTypeEmpty() throws JsonProcessingException, Exception {

		gradeApi.setType(EMPTY);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/grade", schoolId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Type cannot be empty");

	}

	@Test
	public void whenCreateButNumberNull() throws JsonProcessingException, Exception {

		gradeApi.setNumber(null);
		MvcResult result = mockMvc
				.perform(post("/school/{schoolId}/grade", schoolId)
						.contentType(MediaType.APPLICATION_JSON).content(toJson(gradeApi)))
				.andExpect(status().isBadRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		assertThat(response).contains("Number cannot be null");

	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}
}
