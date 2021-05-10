package click.escuela.grade.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.grade.api.GradeApi;
import click.escuela.grade.dto.GradeDTO;
import click.escuela.grade.enumerator.GradeEnum;
import click.escuela.grade.exception.TransactionException;
import click.escuela.grade.service.impl.GradeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "")
public class GradeController {

	@Autowired
	private GradeServiceImpl gradeService;

	// Metodo de prueba
	@Operation(summary = "Get all the grades", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeDTO.class))) })
	@GetMapping(value = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<GradeDTO>> getStudents() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(gradeService.findAll());
	}

	@Operation(summary = "Create grade", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(value = "/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<GradeEnum> create(
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId,
			@RequestBody @Validated GradeApi gradeApi) throws TransactionException {
		gradeService.create(studentId, gradeApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(GradeEnum.CREATE_OK);
	}

}
