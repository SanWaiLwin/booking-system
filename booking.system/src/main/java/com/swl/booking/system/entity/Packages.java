package com.swl.booking.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Packages extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = 8298471739906730516L;

	@Column(name = "name")
	private String name;

	@Column(name = "credits")
	private int credits;

	@Column(name = "price")
	private double price;

	@OneToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "country_id")
	private Country country; 
}
