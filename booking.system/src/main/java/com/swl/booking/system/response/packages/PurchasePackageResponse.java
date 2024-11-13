package com.swl.booking.system.response.packages;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PurchasePackageResponse implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -1972335130301368661L;

	private List<PurchasePackageData> packageList;

}
