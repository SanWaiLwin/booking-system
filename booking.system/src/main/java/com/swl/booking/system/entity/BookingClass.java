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
@Table(name = "booking_class")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookingClass extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = -1857973279490297480L;

	@Column(name = "name")
	private String name;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private Country country;

	@Column(name = "required_credits")
	private int requiredCredits;

	@Column(name = "available_slots")
	private int availableSlots;

	public void decrementAvailableSlots() {
		if (availableSlots > 0) {
			availableSlots--;
		} else {
			throw new IllegalStateException("No available slots to decrement.");
		}
	}

}
