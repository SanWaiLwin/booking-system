package com.swl.booking.system.request.packages;

public class PaymentCardRequest {

	private String paymentMethod;

	public PaymentCardRequest(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	} 
}
