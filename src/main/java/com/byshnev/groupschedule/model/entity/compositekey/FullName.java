package com.byshnev.groupschedule.model.entity.compositekey;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FullName implements Serializable {
	private String name;
	private String surname;
	private String patronymic;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FullName fullName = (FullName) o;
		return name.equals(fullName.name) && surname.equals(fullName.surname) && patronymic.equals(fullName.patronymic);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, surname, patronymic);
	}
}
