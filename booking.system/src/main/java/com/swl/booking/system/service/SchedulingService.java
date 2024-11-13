package com.swl.booking.system.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swl.booking.system.entity.WaitingList;
import com.swl.booking.system.repository.WaitingListRepository;

@Service
public class SchedulingService {

	@Value("${app.schedule.fixedRate}")
	private long fixedRate;

	private final WaitingListRepository waitingListRepository;

	public SchedulingService(WaitingListRepository waitingListRepository) {
		this.waitingListRepository = waitingListRepository;
	}

	@Scheduled(fixedRateString = "${app.schedule.fixedRate}")
	@Transactional
	public void processExpiredWaitlistEntries() {
		List<WaitingList> waitingList = waitingListRepository.findAll();
		List<WaitingList> expWaitingList = waitingList.stream().filter(wlDate -> {
			Instant expiryInstant = wlDate.getWaitingListClass().getExpiryDate().toInstant();
			return expiryInstant.isBefore(Instant.now());
		}).collect(Collectors.toList());

		waitingListRepository.deleteAll(expWaitingList);
		System.out.println("Run Schedule for waiting list ............" + Instant.now());
	}
}
