package click.escuela.grade.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
	
}
