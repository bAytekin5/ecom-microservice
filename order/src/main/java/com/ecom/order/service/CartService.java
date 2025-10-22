package com.ecom.order.service;

import com.ecom.order.dto.CartItemRequest;
import com.ecom.order.model.CartItem;
import com.ecom.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
//        // ürünü bul
//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//        if (productOpt.isEmpty()) {
//            return false;
//        }
//        Product product = productOpt.get();
//        if (product.getStockQuantity() < request.getQuantity()) {
//            return false;
//        }
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty()) {
//            return false;
//        }
//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existingCartItem != null) {//miktarı arttır
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
//            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        } else { // yeni cartItem
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
//            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
//        return userRepository.findById(Long.valueOf(userId))
//                .map(cartItemRepository::findByUser)
//                .orElseGet(List::of);
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        //userRepository.findById(Long.valueOf(userId)).ifPresent(cartItemRepository::deleteByUser);
        cartItemRepository.deleteByUserId(userId);

    }
}
