package com.swl.booking.system.response.packages;

import java.io.Serializable;

import com.swl.booking.system.entity.Packages;
import lombok.Data;

@Data
public class PackageAvaiableData implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -8689012640367426712L;

	private Long id;

	private String name;

	private int credits;

	private double price; 

	public PackageAvaiableData(Packages data) {
		if (data != null) {
			this.id = data.getId();
			this.name = data.getName() != null ? data.getName() : "Unknown";
			this.credits = data.getCredits();
			this.price = data.getPrice(); 
		}
	}
}
