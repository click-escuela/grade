package click.escuela.grade.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CourseDTO {

	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "year")
	private Integer year;
	
	@JsonProperty(value = "division")
	private String division;
	
	@JsonProperty(value = "countStudent")
	private Integer countStudent;
	
	@JsonProperty(value = "grades")
	private List<GradeDTO> grades;

}
