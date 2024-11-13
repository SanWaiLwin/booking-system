package com.swl.booking.system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.swl.booking.system.entity.BookingClass;
import com.swl.booking.system.repository.BookingClassRepository; 
import com.swl.booking.system.request.booking.ViewAvaiableClassRequest;
import com.swl.booking.system.response.booking.BookingClassData;
import com.swl.booking.system.response.booking.ViewAvaiableClassResponse;

@Service
public class BookingClassServiceImpl implements BookingClassService {

	private final BookingClassRepository bookingClassRepository;

	public BookingClassServiceImpl(BookingClassRepository bookingClassRepository) {
		this.bookingClassRepository = bookingClassRepository;
	}

	@Override
	public ViewAvaiableClassResponse getByFilter(ViewAvaiableClassRequest req) {
		List<BookingClass> datas = bookingClassRepository.findByCountryId(req.getCountryId());
		return prepareDataForViewAvaiableClassResponse(datas);
	}

	private ViewAvaiableClassResponse prepareDataForViewAvaiableClassResponse(List<BookingClass> datas) {
		ViewAvaiableClassResponse resp = new ViewAvaiableClassResponse();

		List<BookingClassData> dataList = datas.stream().filter(data -> data.getAvailableSlots() > 0)
				.map(BookingClassData::new).collect(Collectors.toList());

		resp.setClassList(dataList);
		return resp;
	} 

}
