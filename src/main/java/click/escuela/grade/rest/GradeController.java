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
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.grade.api.GradeApi;
import click.escuela.grade.dto.CourseDTO;
import click.escuela.grade.dto.CourseStudentsShortDTO;
import click.escuela.grade.dto.GradeDTO;
import click.escuela.grade.dto.StudentShortDTO;
import click.escuela.grade.enumerator.GradeMessage;
import click.escuela.grade.exception.TransactionException;
import click.escuela.grade.service.impl.GradeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/grade")
public class GradeController {

	@Autowired
	private GradeServiceImpl gradeService;

	// Metodo de prueba
	@Operation(summary = "Get all the grades", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeDTO.class))) })
	@GetMapping(value = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<GradeDTO>> getGrades() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(gradeService.findAll());
	}	
	@Operation(summary = "Get grade by Id", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeDTO.class))) })
	@GetMapping(value = "{gradeId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<GradeDTO> getById(
			@Parameter(name = "Grade id", required = true) @PathVariable("gradeId") String gradeId) throws TransactionException{
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(gradeService.getById(gradeId));
	}

	@Operation(summary = "Get grade by studentId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeDTO.class))) })
	@GetMapping(value = "student/{studentId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<GradeDTO>> getByStudent(
			@Parameter(name = "Student id", required = true) @PathVariable("studentId") String studentId){
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(gradeService.getByStudentId(studentId));
	}
	
	@Operation(summary = "Get grades by schoolId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeDTO.class))) })
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<GradeDTO>> getBySchool(
			@Parameter(name = "School id", required = true) @PathVariable("schoolId") String schoolId) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(gradeService.getBySchoolId(schoolId));
	}

	@Operation(summary = "Get grades by courseId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GradeDTO.class))) })
	@GetMapping(value = "course/{courseId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<GradeDTO>> getByCourse(
			@Parameter(name = "Course id", required = true) @PathVariable("courseId") String courseId){
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(gradeService.getByCourseId(courseId));
	}
	
	@Operation(summary = "Create grade", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<GradeMessage> create(@RequestBody @Validated GradeApi gradeApi) throws TransactionException {
		gradeService.create(gradeApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(GradeMessage.CREATE_OK);
	}
	
	@Operation(summary = "Update grade", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<GradeMessage> update(@RequestBody @Validated GradeApi gradeApi) throws TransactionException {
		gradeService.update(gradeApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(GradeMessage.UPDATE_OK);
	}
	
	@Operation(summary = "Get courses with grades", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseDTO.class))) })
	@PutMapping(value = "courses", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<CourseStudentsShortDTO>> getCoursesWithGrades(
			@RequestBody @Validated List<CourseStudentsShortDTO> courses){
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(gradeService.getCoursesWithGrades(courses));
	}
	
	@Operation(summary = "Get students with grades", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentShortDTO.class))) })
	@PutMapping(value = "students", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<StudentShortDTO>> getStudentsWithGrades(
			@RequestBody @Validated List<StudentShortDTO> students){
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(gradeService.getStudentsWithGrades(students));
	}
	
}
