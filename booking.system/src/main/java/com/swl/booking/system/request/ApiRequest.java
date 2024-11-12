package com.swl.booking.system.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiRequest<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4009557924588181070L;
	private T data;

	public ApiRequest() {
		this.data = null;
	}

	public ApiRequest(T data) {
		this.data = data;
	}

}