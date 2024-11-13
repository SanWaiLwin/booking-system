package com.swl.booking.system.entity; 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "country")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Country extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = -3098711280205890132L;

	@Column(name = "name")
	private String name;
}
