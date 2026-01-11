package com.wjc.codetest;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Product is not found. productId : " + productId);
    }
}
