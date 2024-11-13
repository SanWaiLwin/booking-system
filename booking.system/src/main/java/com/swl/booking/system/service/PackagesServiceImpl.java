package com.swl.booking.system.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.swl.booking.system.entity.Packages;
import com.swl.booking.system.entity.PurchasedPackage;
import com.swl.booking.system.entity.User;
import com.swl.booking.system.exception.AlreadyExitException;
import com.swl.booking.system.repository.PackagesRepository;
import com.swl.booking.system.repository.PurchasedPackageRepository;
import com.swl.booking.system.repository.UserRepository;
import com.swl.booking.system.request.packages.PackageAvaiableRequest;
import com.swl.booking.system.request.packages.PaymentCardRequest;
import com.swl.booking.system.request.packages.PaymentChargeRequest;
import com.swl.booking.system.request.packages.PurchasePackageRequest;
import com.swl.booking.system.response.packages.PackageAvaiableResponse;
import com.swl.booking.system.response.packages.PackageAvaiableData;
import com.swl.booking.system.response.packages.PurchasePackageData;
import com.swl.booking.system.response.packages.MyPackageResponse;
import com.swl.booking.system.security.UserPrincipal;
import com.swl.booking.system.util.CommonConstant;
import com.swl.booking.system.util.CommonUtil;

@Service
public class PackagesServiceImpl implements PackagesService {

	private final PackagesRepository packagesRepository;

	private final PurchasedPackageRepository purchasedPackageRepository;

	private final UserRepository userRepository;

	public PackagesServiceImpl(PackagesRepository packagesRepository,
			PurchasedPackageRepository purchasedPackageRepository, UserRepository userRepository) {
		this.packagesRepository = packagesRepository;
		this.purchasedPackageRepository = purchasedPackageRepository;
		this.userRepository = userRepository;
	}

	@Override
	public PackageAvaiableResponse getByFilter(PackageAvaiableRequest req) {
		List<Packages> datas = packagesRepository.findByCountryId(req.getCountryId());
		return prepareDataForResponse(datas);
	}

	private PackageAvaiableResponse prepareDataForResponse(List<Packages> datas) {
		PackageAvaiableResponse resp = new PackageAvaiableResponse();

		List<PackageAvaiableData> packagesDataList = datas.stream().map(data -> new PackageAvaiableData(data))
				.collect(Collectors.toList());

		resp.setPackageList(packagesDataList);
		return resp;
	}

	@Override
	public void purchasePackage(PurchasePackageRequest req) {

		User user = getUser();
		Packages pack = getPackages(req.getPackageId());
		PaymentChargeRequest paymentChargeRequest = new PaymentChargeRequest(pack.getPrice(),
				CommonConstant.PAYMENT_METHOD);

		purchasePackage(user, pack, paymentChargeRequest);
	}

	private List<PurchasePackageData> preparePurchasePackageDataList(List<PurchasedPackage> dataList) {
		List<PurchasePackageData> list = dataList.stream().map(data -> preparePurchasePackageData(data))
				.collect(Collectors.toList());
		return list;
	}

	private PurchasePackageData preparePurchasePackageData(PurchasedPackage data) {
		PurchasePackageData resp = new PurchasePackageData();
		resp.setId(data.getId());
		resp.setPackageName(data.getPack().getName());
		resp.setCountry(data.getPack().getCountry().getName());
		resp.setRemainingCredits(data.getRemainingCredits());
		resp.setPurchaseDate(CommonUtil.dateToString(CommonConstant.STD_DATE_FORMAT, data.getPurchaseDate()));
		return resp;
	}

	private void purchasePackage(User user, Packages pack, PaymentChargeRequest paymentChargeRequest) {
		if (AddPaymentCard(new PaymentCardRequest(CommonConstant.PAYMENT_METHOD))
				&& PaymentCharge(paymentChargeRequest)) {

			PurchasedPackage purchasedPackage = new PurchasedPackage();
			purchasedPackage.setUser(user);
			purchasedPackage.setPack(pack);
			purchasedPackage.setRemainingCredits(pack.getCredits());
			purchasedPackageRepository.save(purchasedPackage);
		}
	}

	private Packages getPackages(Long packageId) {
		Optional<Packages> packOpt = packagesRepository.findById(packageId);
		if (packOpt.isEmpty()) {
			throw new AlreadyExitException("Package is not exist.");
		}
		return packOpt.get();
	}

	private User getUser() {
		UserPrincipal userData = CommonUtil.getUserPrincipalFromAuthentication();
		Optional<User> userOpt = userRepository.findById(userData.getId());
		if (userOpt.isEmpty()) {
			throw new AlreadyExitException("User is not exist.");
		}
		return userOpt.get();
	}

	@Override
	public MyPackageResponse getDataByFilter() {
		UserPrincipal userData = CommonUtil.getUserPrincipalFromAuthentication();
		List<PurchasedPackage> dataList = purchasedPackageRepository.findByUserId(userData.getId());

		MyPackageResponse resp = new MyPackageResponse();
		resp.setMyPackageList(preparePurchasePackageDataList(dataList));
		return resp;
	}

	public boolean AddPaymentCard(PaymentCardRequest paymentCardRequest) {
		return true;
	}

	public boolean PaymentCharge(PaymentChargeRequest paymentChargeRequest) {
		return true;
	}

}
