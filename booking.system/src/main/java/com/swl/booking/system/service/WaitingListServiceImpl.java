package com.swl.booking.system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.swl.booking.system.entity.BookingClass;
import com.swl.booking.system.entity.User;
import com.swl.booking.system.entity.WaitingList;
import com.swl.booking.system.repository.WaitingListRepository;
import com.swl.booking.system.response.booking.WaitingListData;
import com.swl.booking.system.response.booking.WaitingListResponse;
import com.swl.booking.system.security.UserPrincipal;
import com.swl.booking.system.util.CommonConstant;
import com.swl.booking.system.util.CommonUtil;

@Service
public class WaitingListServiceImpl implements WaitingListService {

	private final WaitingListRepository waitingListRepository;

	public WaitingListServiceImpl(WaitingListRepository waitingListRepository) {
		this.waitingListRepository = waitingListRepository;
	}

	@Override
	public WaitingListResponse getWaitingList() {
		UserPrincipal userData = CommonUtil.getUserPrincipalFromAuthentication();
		List<WaitingList> dataList = waitingListRepository.findByUserId(userData.getId());

		WaitingListResponse resp = new WaitingListResponse();
		resp.setWaitingList(prepareWaitingListDataList(dataList));
		return resp;
	}

	private List<WaitingListData> prepareWaitingListDataList(List<WaitingList> dataList) {
		List<WaitingListData> resp = dataList.stream().map(data -> prepareWaitingListData(data))
				.collect(Collectors.toList());
		return resp;
	}

	private WaitingListData prepareWaitingListData(WaitingList entity) {
		WaitingListData data = new WaitingListData();
		data.setId(entity.getId());
		BookingClass waitClass = entity.getWaitingListClass();
		data.setClassName(waitClass.getName());
		data.setCountryName(waitClass.getCountry().getName());
		data.setExpiryDate(CommonUtil.dateToString(CommonConstant.STD_DATE_FORMAT, waitClass.getExpiryDate()));
		data.setWaitingStartDate(CommonUtil.dateToString(CommonConstant.STD_DATE_FORMAT, entity.getWaitingListDate()));
		return data;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void addToWaitingListInNewTransaction(User user, BookingClass bookingClass) {
		WaitingList waitingList = new WaitingList();
		waitingList.setUser(user);
		waitingList.setWaitingListClass(bookingClass);
		waitingListRepository.save(waitingList); 
	}
}
