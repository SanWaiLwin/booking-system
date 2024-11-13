package com.swl.booking.system.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Booking extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = 7667117462132210699L;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "booking_class_id", nullable = false)
	private BookingClass bookingClass;
	
	@Column(name = "perchased_package_id")
	private Long perchasedPackageId;

	@Column(name = "booking_date")
	private Date bookingDate = new Date();
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@Column(name = "status")
	private int status;
	
}
