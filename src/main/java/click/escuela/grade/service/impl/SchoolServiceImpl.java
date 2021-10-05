package click.escuela.grade.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.grade.enumerator.SchoolMessage;
import click.escuela.grade.exception.SchoolException;
import click.escuela.grade.model.School;
import click.escuela.grade.repository.SchoolRepository;


@Service
public class SchoolServiceImpl {

	@Autowired
	private SchoolRepository schoolRepository;

	private Optional<School> findById(String id) throws SchoolException {
		return Optional.of(schoolRepository.findById(UUID.fromString(id)))
				.orElseThrow(() -> new SchoolException(SchoolMessage.GET_ERROR));
	}

	public School getById(String id) throws SchoolException {
		Optional<School> schoolOptional = findById(id);
		if (schoolOptional.isPresent()) {
			return schoolOptional.get();
		} else {
			throw new SchoolException(SchoolMessage.GET_ERROR);
		}
	}
	
}
