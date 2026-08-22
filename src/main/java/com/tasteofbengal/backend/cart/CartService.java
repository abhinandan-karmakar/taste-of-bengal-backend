package com.tasteofbengal.backend.cart;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.exception.BadRequestException;
import com.tasteofbengal.backend.exception.ConflictException;
import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.product.Product;
import com.tasteofbengal.backend.product.ProductRepo;
import com.tasteofbengal.backend.security.SecurityUtil;
import com.tasteofbengal.backend.user.User;
import com.tasteofbengal.backend.user.UserRepo;

@Service
public class CartService {

	private final CartRepo cartRepo;

	private final UserRepo userRepo;

	private final CartItemRepo cartItemRepo;

	private final ProductRepo productRepo;

	private final SecurityUtil securityUtil;

	public CartService(CartRepo cartRepo, UserRepo userRepo, CartItemRepo cartItemRepo, ProductRepo productRepo) {
		super();
		this.cartRepo = cartRepo;
		this.userRepo = userRepo;
		this.cartItemRepo = cartItemRepo;
		this.productRepo = productRepo;
		this.securityUtil = new SecurityUtil();
	}


	private Cart getOrCreateCart() {

		Integer userId = securityUtil.getCurrentUserId();

		Cart cart = cartRepo.findByUserId(userId);

		if (cart == null) {
			User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
			Cart newCart = new Cart();
			newCart.setUser(user);

			cart = cartRepo.save(newCart);

		}

		return cart;
	}

	public List<CartItemResponse> getAllCartProducts() {

		Cart cart = getOrCreateCart();

		List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());

		List<CartItemResponse> cartItemResponses = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			cartItemResponses.add(new CartItemResponse(cartItem.getProduct().getId(), cartItem.getProduct().getName(),
					cartItem.getProduct().getPrice(), cartItem.getProduct().getCategory().getName(),
					cartItem.getProduct().getDescription(), cartItem.getProduct().getImage_url(),
					cartItem.getQuantity()));
		}

		return cartItemResponses;
	}

	public String addToCart(Integer productId) {
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with Id : " + productId));
		
		Cart cart = getOrCreateCart();
		

		if(!product.isActive()) {
			throw new ConflictException("Product is inactive");
		}
		
		if (cartItemRepo.existsByProductIdAndCartId(productId, cart.getId())) {
			throw new ConflictException("Product is already present in cart");
		}

		if (product.getAvailableStock() < 1) {
			throw new ConflictException("Product is out of stock");
		}

		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setProduct(product);
		cartItem.setQuantity(1);

		cartItemRepo.save(cartItem);
		
		return "Added to cart";
	}

	public String updateCartWithProductQuantity(Integer productId, int quantity) {

		if (quantity <= 0) {
			throw new BadRequestException("Quantity should be more than 0");
		}

		Cart cart = getOrCreateCart();

		CartItem cartItem = cartItemRepo.findByProductIdAndCartId(productId, cart.getId());

		if (cartItem == null) {
			throw new ResourceNotFoundException("Product does not exist in the cart");
		}

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));


		if (product.getAvailableStock() < quantity) {
			throw new ConflictException("Product have only stock of : " + product.getAvailableStock());
		}

		cartItem.setQuantity(quantity);
		cartItemRepo.save(cartItem);

		return "Updated quantity successfully";
	}

	public String deleteProductFromCart(Integer productId) {

		Cart cart = getOrCreateCart();

		CartItem cartItem = cartItemRepo.findByProductIdAndCartId(productId, cart.getId());

		if (cartItem == null) {
			throw new ResourceNotFoundException("Product does not exist in cart");
		}

		cartItemRepo.delete(cartItem);

		return "Removed from cart successfully";
	}

}
