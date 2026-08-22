package com.tasteofbengal.backend.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tasteofbengal.backend.cart.Cart;
import com.tasteofbengal.backend.cart.CartItem;
import com.tasteofbengal.backend.cart.CartItemRepo;
import com.tasteofbengal.backend.cart.CartRepo;
import com.tasteofbengal.backend.exception.BadRequestException;
import com.tasteofbengal.backend.exception.ConflictException;
import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.order.Order;
import com.tasteofbengal.backend.order.OrderItem;
import com.tasteofbengal.backend.order.OrderItemRepo;
import com.tasteofbengal.backend.order.OrderRepo;
import com.tasteofbengal.backend.order.OrderStatus;
import com.tasteofbengal.backend.product.Product;
import com.tasteofbengal.backend.product.ProductRepo;
import com.tasteofbengal.backend.security.SecurityUtil;

@Service
public class PaymentService {

	@Autowired
	private RazorpayService razorpayService;

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private CartItemRepo cartItemRepo;

	@Autowired
	private OrderItemRepo orderItemRepo;

	@Autowired
	private SecurityUtil securityUtil;

	@Transactional
	public String verifyPayment(PaymentVerifyRequest paymentVerifyRequest) {

		if (!razorpayService.verifySignature(paymentVerifyRequest.getRazorpayOrderId(),
				paymentVerifyRequest.getRazorpayPaymentId(), paymentVerifyRequest.getRazorpaySignature())) {
			throw new BadRequestException("Inavlid payment signature");
		}

		Payment payment = paymentRepo.findByRazorpayOrderId(paymentVerifyRequest.getRazorpayOrderId())
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

		Order order = payment.getOrder();

		Integer currentUserId = securityUtil.getCurrentUserId();

		if (!order.getUser().getId().equals(currentUserId)) {
			throw new AccessDeniedException("You are not authorized to access this order");
		}

		if (payment.getStatus() == PaymentStatus.SUCCESS) {
			return "Payment already processed";
		}

		if (order.getOrderStatus() == OrderStatus.PAID) {
			return "Order already paid";
		}

		Cart cart = cartRepo.findByUserId(currentUserId);

		if (cart == null) {
			throw new ResourceNotFoundException("Cart not found");
		}

		List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());

		if (cartItems.isEmpty()) {
			throw new BadRequestException("Cart is empty. Cannot verify payment.");
		}

		int total = 0;
		Map<Integer, Product> products = new HashMap<>();
		for (CartItem cartItem : cartItems) {

			Product product = productRepo.findByIdForUpdate(cartItem.getProduct().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

			if (!product.isActive()) {
				throw new ConflictException("Product is inactive");
			}

			if (product.getAvailableStock() < cartItem.getQuantity()) {
				throw new ConflictException("Product is having only stock of: " + product.getAvailableStock());
			}



			total += product.getPrice() * cartItem.getQuantity();

			products.put(product.getId(), product);

		}
		if (total * 100 != payment.getAmount()) {
			throw new BadRequestException("Cart amount has been changed.");
		}

		for (CartItem cartItem : cartItems) {
			Product product = products.get(cartItem.getProduct().getId());

			product.setAvailableStock(product.getAvailableStock() - cartItem.getQuantity());

			productRepo.save(product);

			OrderItem orderItem = new OrderItem();

			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setPrice(product.getPrice());

			orderItemRepo.save(orderItem);

			cartItemRepo.delete(cartItem);
		}

		payment.setRazorpayPaymentId(paymentVerifyRequest.getRazorpayPaymentId());

		payment.setStatus(PaymentStatus.SUCCESS);

		order.setOrderStatus(OrderStatus.PAID);

		paymentRepo.save(payment);
		orderRepo.save(order);

		return "Payment Successful";
	}

}
