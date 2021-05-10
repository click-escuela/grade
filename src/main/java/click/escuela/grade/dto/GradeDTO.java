package click.escuela.grade.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeDTO {

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "course")
	private String course;

	@JsonProperty(value = "subject")
	private String subject;

	@JsonProperty(value = "type")
	private String type;

	@JsonProperty(value = "number")
	private Integer number;
}
