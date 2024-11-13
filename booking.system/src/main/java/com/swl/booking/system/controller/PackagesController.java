package com.swl.booking.system.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swl.booking.system.request.ApiRequest;
import com.swl.booking.system.request.packages.PackageAvaiableRequest;
import com.swl.booking.system.request.packages.PurchasePackageRequest;
import com.swl.booking.system.response.ApiResponse;
import com.swl.booking.system.response.packages.PackageAvaiableResponse;
import com.swl.booking.system.response.packages.PurchasePackageResponse;
import com.swl.booking.system.service.PackagesService;
import com.swl.booking.system.util.CommonConstant;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/auth/package")
public class PackagesController {

	private final PackagesService packagesService;

	public PackagesController(PackagesService packagesService) {
		this.packagesService = packagesService;
	}

	@PostMapping("/available")
	public ApiResponse<PackageAvaiableResponse> getAvailablePackages(
			@Valid @RequestBody ApiRequest<PackageAvaiableRequest> apiRequest) {
		PackageAvaiableRequest req = apiRequest.getData();
		PackageAvaiableResponse resp = packagesService.getByFilter(req);
		return new ApiResponse<>(CommonConstant.MSG_PREFIX_SUCCESS, "Avaiable packages ", resp);
	}

	@PostMapping("/purchase")
	public ApiResponse<PurchasePackageResponse> purchasePackage(
			@Valid @RequestBody ApiRequest<PurchasePackageRequest> apiRequest) {
		PurchasePackageRequest req = apiRequest.getData();
		PurchasePackageResponse resp = packagesService.getPurchasePackage(req);
		return new ApiResponse<>(CommonConstant.MSG_PREFIX_SUCCESS, "Purchased packages ", resp);
	}
}
