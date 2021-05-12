package click.escuela.grade.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import click.escuela.grade.enumerator.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "grade")
@Entity
@Builder
public class Grade {
	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "course_id", nullable = false)
	private UUID courseId;
	
	@Column(name = "subject", nullable = false)
	private String subject;
	
	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private TypeEnum type;
	
	@Column(name = "number", nullable = false)
	private Integer number;

	@Column(name = "student_id", nullable = false)
	private UUID studentId;
}
