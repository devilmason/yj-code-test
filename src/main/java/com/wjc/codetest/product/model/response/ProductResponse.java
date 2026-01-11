package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Builder;
import lombok.Getter;

/**
 * 문제 : 보안, 설계
 * 원인 : 엔티티 전체 노출
 * 개선안 : 별도의 Response용 클래스 선언 및 필요한 정보만 노출
 */
@Getter
@Builder
public class ProductResponse {
    private Long id;
    private String category;
    private String name;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder().
                id(product.getId()).
                category(product.getCategory()).
                name(product.getName()).
                build();
    }
}
