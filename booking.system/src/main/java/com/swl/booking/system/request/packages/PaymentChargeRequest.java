package com.swl.booking.system.request.packages;

public class PaymentChargeRequest {
	private double price;
	private String paymentMethod;

	public PaymentChargeRequest(double price, String paymentMethod) {
		this.price = price;
		this.paymentMethod = paymentMethod;
	}

	public double getPrice() {
		return price;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}
}
