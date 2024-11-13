package com.swl.booking.system.response.booking;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class WaitingListResponse implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -5597370916181738265L;

	private List<WaitingListData> waitingList;
}
