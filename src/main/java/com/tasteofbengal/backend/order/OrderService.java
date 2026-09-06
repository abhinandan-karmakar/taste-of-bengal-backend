package com.tasteofbengal.backend.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tasteofbengal.backend.address.Address;
import com.tasteofbengal.backend.address.AddressRepo;
import com.tasteofbengal.backend.cart.Cart;
import com.tasteofbengal.backend.cart.CartItem;
import com.tasteofbengal.backend.cart.CartItemRepo;
import com.tasteofbengal.backend.cart.CartRepo;
import com.tasteofbengal.backend.exception.BadRequestException;
import com.tasteofbengal.backend.exception.ConflictException;
import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.payment.OrderPaymentResponse;
import com.tasteofbengal.backend.payment.Payment;
import com.tasteofbengal.backend.payment.PaymentRepo;
import com.tasteofbengal.backend.payment.PaymentStatus;
import com.tasteofbengal.backend.payment.RazorpayService;
import com.tasteofbengal.backend.product.Product;
import com.tasteofbengal.backend.product.ProductRepo;
import com.tasteofbengal.backend.security.SecurityUtil;

@Service
public class OrderService {

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private CartItemRepo cartItemRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private RazorpayService razorpayService;

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	private OrderItemRepo orderItemRepo;

	@Autowired
	private AddressRepo addressRepo;

	@Autowired
	private SecurityUtil securityUtil;

	private Cart getCart() {

		Integer userId = securityUtil.getCurrentUserId();

		Cart cart = cartRepo.findByUserId(userId);

		if (cart == null) {
			throw new ResourceNotFoundException("The user don't have any existing cart");
		}

		return cart;
	}

	@Transactional
	public OrderPaymentResponse createOrder(OrderRequest orderRequest) {
		
		Address address = addressRepo.findById(orderRequest.getAddressId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Address does not exsist with id : " + orderRequest.getAddressId()));

		if (address.getUser().getId() != securityUtil.getCurrentUserId()) {
			throw new BadRequestException("Address does not exist");
		}

		Cart cart = getCart();

		List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());

		if (cartItems.isEmpty()) {
			throw new BadRequestException("Cart is empty, can't place an order");
		}

		int total = 0;

		for (CartItem cartItem : cartItems) {

			Product product = productRepo.findByIdForUpdate(cartItem.getProduct().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

			if (!product.isActive()) {
				throw new ConflictException("Product is inactive");
			}

			if (product.getAvailableStock() < cartItem.getQuantity()) {
				throw new ConflictException(
						"Product is having only stock of : " + product.getAvailableStock());
			}

			total += product.getPrice() * cartItem.getQuantity();

		}

		Order order = new Order();
		order.setUser(cart.getUser());
		order.setTotalAmount(total);
		order.setAddress(address);
		Order newOrder = orderRepo.save(order);

		int amountInPaise = total * 100;

		String razorpayOrderId = razorpayService.createRazorpayOrder(amountInPaise);

		Payment payment = new Payment();
		payment.setOrder(newOrder);
		payment.setRazorpayOrderId(razorpayOrderId);
		payment.setAmount(amountInPaise);
		payment.setStatus(PaymentStatus.CREATED);

		paymentRepo.save(payment);

		return new OrderPaymentResponse(newOrder.getId(), total, "INR", razorpayOrderId);
	}

	private OrderResponse mapToOrderResponse(Order order, List<OrderItem> items) {
		
		List<OrderedProductResponse> orderedProducts = new ArrayList<>();
		
		for(OrderItem item : items) {
			orderedProducts.add(new OrderedProductResponse(item.getProduct().getId(), item.getProduct().getName(), item.getPrice(), item.getProduct().getCategory().getName(), item.getProduct().getDescription(), item.getProduct().getImage_url(), item.getQuantity()));
		}
		
		return new OrderResponse(order.getId(), order.getTotalAmount(), order.getOrderStatus().toString(),
				order.getOrderedAt(), orderedProducts);
	}

	public List<OrderResponse> getAllOrders() {

		Integer userId = securityUtil.getCurrentUserId();
		List<Order> orders = orderRepo.findByUserIdAndOrderStatusInOrderByOrderedAtDesc(userId,
				List.of(OrderStatus.PAID, OrderStatus.CASH_ON_DELIVERY, OrderStatus.CANCELLED));

		List<OrderResponse> response = new ArrayList<>();

		for (Order order : orders) {
			List<OrderItem> items = orderItemRepo.findByOrderId(order.getId());
			response.add(mapToOrderResponse(order, items));
		}

		return response;
	}

	public OrderResponse getOrder(Integer id) {
		Order order = orderRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No order found with id : " + id));

		if (!order.getUser().getId().equals(securityUtil.getCurrentUserId())) {
			throw new ResourceNotFoundException("No order found with id : " + id);
		}

		if (!(order.getOrderStatus().equals(OrderStatus.PAID)
				|| order.getOrderStatus().equals(OrderStatus.CASH_ON_DELIVERY)
				|| order.getOrderStatus().equals(OrderStatus.CANCELLED))) {
			throw new ResourceNotFoundException("No Order found with id : " + id);
		}

		List<OrderItem> items = orderItemRepo.findByOrderId(order.getId());

		return mapToOrderResponse(order, items);
	}

	@Transactional
	public String createOrderOfCOD(OrderRequest orderRequest) {
		Address address = addressRepo.findById(orderRequest.getAddressId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Address does not exsist with id : " + orderRequest.getAddressId()));

		if (address.getUser().getId() != securityUtil.getCurrentUserId()) {
			throw new BadRequestException("Address does not exist");
		}

		Cart cart = getCart();

		List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());

		if (cartItems.isEmpty()) {
			throw new BadRequestException("Cart is empty, can't place an order");
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
				throw new ConflictException("Product is having only stock of : " + product.getAvailableStock());
			}

			total += product.getPrice() * cartItem.getQuantity();

			products.put(product.getId(), product);

		}

		Order order = new Order();
		order.setUser(cart.getUser());
		order.setTotalAmount(total);
		order.setAddress(address);
		order.setOrderStatus(OrderStatus.CASH_ON_DELIVERY);
		Order newOrder = orderRepo.save(order);

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

		return "Order Successful";
	}

	public String cancelOrder(Integer id) {

		Order order = orderRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No order found with id : " + id));

		Integer userId = securityUtil.getCurrentUserId();

		if (userId != order.getUser().getId()) {
			throw new ResourceNotFoundException("No Order found with the id : " + id);
		}

		if (order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
			throw new ConflictException("Order is alreadt cancelled");
		}

		order.setOrderStatus(OrderStatus.CANCELLED);
		orderRepo.save(order);

		return "Order cancelled successfully";
	}

}
