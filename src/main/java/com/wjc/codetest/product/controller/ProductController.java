package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.model.response.ProductResponse;
import com.wjc.codetest.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 문제 : 설계
 * 원인 : 메서드의 의미가 url 경로에 포함, 불필요한 단어 추가
 *       적절한 어노테이션을 사용하지 않음
 *       동일한 명칭의 function 존재하지만 기능은 다름
 *       서비스단에서의 정렬기준과 정렬방향이 하드코딩
 * 개선안 : 아래에 별도 주석으로 표시
 */

@RestController
// @RequestMapping
// 공통경로 설정 추가, versioning 추가
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // @GetMapping(value = "/get/product/by/{productId}")
    // url 수정
    @GetMapping(value = "/product/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    // @PostMapping(value = "/create/product")
    // url 수정
    @PostMapping(value = "/product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest dto){
        Product product = productService.create(dto);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    // @PostMapping(value = "/delete/product/{productId}")
    // 어노테이션 수정, url 수정
    @DeleteMapping(value = "/product/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteById(productId);
        return ResponseEntity.ok(true);
    }

    // @PostMapping(value = "/update/product")
    // 어노테이션 수정, url 수정
    @PutMapping(value = "/product")
    public ResponseEntity<ProductResponse> updateProduct(@Valid @RequestBody UpdateProductRequest dto){
        Product product = productService.update(dto);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    // 리스트 검색의 기능이 들어가야되는 것 같은데 function 명도 아래와 겹치고 기능도 모호함
    // request에 카테고리명과 페이징만 설정되어 있는데 카테고리명만으로 검색기능을 넣는다고 했을때는
    // 검색어가 없다면 전체 검색, 검색명이 있다면 해당 카테로리만 검색하도록 변경이 필요할 것으로 생각됨
    // Request 부분에 정렬기준, 정렬방향도 클라이언트에서 받도록 설정
    // @PostMapping(value = "/product/list")
    // url 수정
    @PostMapping(value = "/product/search")
    // public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
    // function 명 변경
    public ResponseEntity<ProductListResponse> searchProduct(@Valid @RequestBody GetProductListRequest dto){
        //   Page<Product> productList = productService.getListByCategory(dto);
        // service member function 명도 변경
        Page<Product> productList = productService.searchProductList(dto);
        List<ProductResponse> response = productList.stream().map(ProductResponse::from).toList();
        return ResponseEntity.ok(new ProductListResponse(response, productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}