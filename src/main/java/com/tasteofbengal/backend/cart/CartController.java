package com.tasteofbengal.backend.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping("")
	public ResponseEntity<List<CartItemResponse>> getAllCartProducts() {
		return ResponseEntity.status(HttpStatus.OK).body(cartService.getAllCartProducts());
	}

	@PostMapping("/items/{productId}")
	public ResponseEntity<String> addToCart(@PathVariable Integer productId) {

		return ResponseEntity.status(HttpStatus.OK).body(cartService.addToCart(productId));
	}

	@PutMapping("/items/{productId}")
	public ResponseEntity<String> updateCartWithProductQuantity(@PathVariable Integer productId,
			@RequestBody UpdateCartRequest updateCartRequest) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(cartService.updateCartWithProductQuantity(productId, updateCartRequest.getQuantity()));
	}

	@DeleteMapping("/items/{productId}")
	public ResponseEntity<String> deleteProductFromCart(@PathVariable Integer productId) {

		return ResponseEntity.status(HttpStatus.OK).body(cartService.deleteProductFromCart(productId));
	}

}
