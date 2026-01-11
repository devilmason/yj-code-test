package com.wjc.codetest.product.service;

import com.wjc.codetest.ProductNotFoundException;
import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 문제 : 설계, 에러처리
 * 원인 : 트랜잭션 경계가 없어 롤백 보장 안됨,
 *       특정 프로덕트 검색 결과가 없을 시 일반적인 RuntimeException으로 처리하여 클라이언트에서 원인을 알 수 없음
 *       getListByCategory에 정렬방향, 정렬기준이 하드코딩되어 클라이언트에서 지정할 수 없음
 * 개선안 : 아래 주석에 별도 표시
 */

@Slf4j
@Service
@RequiredArgsConstructor
// 전체 클래스의 기본 트랜잭션은 read로 설정
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    // DB 데이터 변경이 있으므로 @Transactional 선언
    @Transactional
    public Product create(CreateProductRequest dto) {
        // 엔티티에 등록 수정 정보 추가로 인한 생성자 변경
        Product product = new Product(dto.getCategory(), dto.getName(), "system");
        return productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        // 소스 간결하게 변경
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
//        Optional<Product> productOptional = productRepository.findById(productId);
//        if (!productOptional.isPresent()) {
//            throw new RuntimeException("product not found");
//        }
//        return productOptional.get();
    }

    // DB 데이터 변경이 있으므로 @Transactional 선언
    @Transactional
    public Product update(UpdateProductRequest dto) {
        // dirty checking 이용하여 소스 단순화
        Product product = getProductById(dto.getId());
        product.update(dto.getCategory(), dto.getName(), "system");
        return product;
//        Product product = getProductById(dto.getId());
//        product.setCategory(dto.getCategory());
//        product.setName(dto.getName());
//        Product updatedProduct = productRepository.save(product);
//        return updatedProduct;
    }

    @Transactional
    public void deleteById(Long productId) {
        // 불필요한 조회 제거
        //Product product = getProductById(productId);
        // 아이디로만 삭제
        productRepository.deleteById(productId);
    }

    // 검색어가 없다면 전체 검색, 검색명이 있다면 해당 카테로리만 검색하도록 변경
    // public Page<Product> getListByCategory(GetProductListRequest dto) {
    public Page<Product> searchProductList(GetProductListRequest dto) {
        // 정렬기준, 정렬방향도 클라이언트에서 받는걸로 변경
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(),
                Sort.by("ASC".equals(dto.getSortDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC,
                        "".equals(dto.getOrderBy()) ? "id" : dto.getOrderBy()));
        // 카테고리 값이 없으면 전체 정렬
        if(dto.getCategory() == null || dto.getCategory().isEmpty()) {
            return productRepository.findAll(pageRequest);
        }
        // 카테고리 값이 있으면 특정 카테고리만 검색
        else {
            return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
        }
    }

    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}