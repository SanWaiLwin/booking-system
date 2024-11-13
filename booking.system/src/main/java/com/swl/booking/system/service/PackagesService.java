package com.swl.booking.system.service;

import com.swl.booking.system.request.packages.PackageAvaiableRequest;
import com.swl.booking.system.request.packages.PurchasePackageRequest;
import com.swl.booking.system.response.packages.PackageAvaiableResponse;
import com.swl.booking.system.response.packages.MyPackageResponse;

public interface PackagesService {

	PackageAvaiableResponse getByFilter(PackageAvaiableRequest req);

	void purchasePackage(PurchasePackageRequest req); 

	MyPackageResponse getDataByFilter();

}
