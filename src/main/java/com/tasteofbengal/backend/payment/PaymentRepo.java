package com.tasteofbengal.backend.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {

	Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

	Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);
}
