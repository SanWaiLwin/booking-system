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
@Table(name = "purchased_package")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PurchasedPackage extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4830416237362008714L;

	@Column(name = "remaining_credits")
	private int remainingCredits;

	@Column(name = "purchase_date")
	private Date purchaseDate = new Date();

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "packages_id")
	private Packages pack;

	public void decrementRemainingCredits(int credit) {
		if (remainingCredits >= credit) {
			remainingCredits -= credit;
		} else {
			throw new IllegalStateException("Insufficient credits to decrement.");
		}
	}

	public void incrementRemainingCredits(int credit) {
		remainingCredits += credit;
	}
}
