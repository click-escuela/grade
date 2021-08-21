package click.escuela.grade.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseStudentsShortDTO extends CourseDTO {

	@JsonProperty(value = "students")
	private List<StudentShortDTO> students;

}
