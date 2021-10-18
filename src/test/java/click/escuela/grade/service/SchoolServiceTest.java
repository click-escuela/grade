package click.escuela.grade.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.grade.enumerator.SchoolMessage;
import click.escuela.grade.exception.SchoolException;
import click.escuela.grade.model.School;
import click.escuela.grade.repository.SchoolRepository;
import click.escuela.grade.service.impl.SchoolServiceImpl;


@RunWith(PowerMockRunner.class)
public class SchoolServiceTest {

	@Mock
	private SchoolRepository schoolRepository;
	
	private SchoolServiceImpl schoolServiceImpl = new SchoolServiceImpl();
	private School school;
	private UUID id;

	@Before
	public void setUp() {

		id = UUID.randomUUID();
		school = School.builder().id(id).name("Colegio Nacional").cellPhone("47589869")
				.email("colegionacional@edu.gob.com").adress("Entre Rios 1418")
				.build();
		
		Optional<School> optional = Optional.of(school);

		Mockito.when(schoolRepository.findById(id)).thenReturn(optional);

		ReflectionTestUtils.setField(schoolServiceImpl, "schoolRepository", schoolRepository);
	}

	@Test
	public void whenGetByIdIsOK() throws SchoolException {
		schoolServiceImpl.getById(id.toString());
		verify(schoolRepository).findById(id);
	}

	@Test
	public void whenGetByIdIsError() {
		id = UUID.randomUUID();
		assertThatExceptionOfType(SchoolException.class).isThrownBy(() -> {
			schoolServiceImpl.getById(id.toString());
		}).withMessage(SchoolMessage.GET_ERROR.getDescription());
	}
	
}