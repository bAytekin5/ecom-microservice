package com.ecom.order.controller;

import com.ecom.order.dto.CartItemRequest;
import com.ecom.order.model.CartItem;
import com.ecom.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    @PostMapping()
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId,
                                            @RequestBody CartItemRequest request) {

        if (!cartService.addToCart(userId, request)) {
            return ResponseEntity.badRequest().body("Product Out of Stock or User Not found or Product not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId,
                                               @PathVariable String productId) {

        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<List<CartItem>> retrieveAllCartItems(@RequestHeader("X-User-ID") String userId) {
        log.debug("Retrieving all cart items userId={}", userId);
        return ResponseEntity.ok(cartService.getCart(userId));
    }

}
