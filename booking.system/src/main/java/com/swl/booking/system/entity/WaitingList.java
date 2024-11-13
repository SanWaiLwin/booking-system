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
@Table(name = "waiting_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WaitingList extends BaseEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = 8828148906360012970L;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "booking_class_id", nullable = false)
	private BookingClass waitingListClass;

	@Column(name = "waiting_list_date")
	private Date waitingListDate = new Date();

}
