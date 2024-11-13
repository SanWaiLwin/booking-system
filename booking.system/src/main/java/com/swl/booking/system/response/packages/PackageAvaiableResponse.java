package com.swl.booking.system.response.packages;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PackageAvaiableResponse implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -7781423537166722059L;

	private List<PackagesData> packageList;
}
