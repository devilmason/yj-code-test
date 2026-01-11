package com.wjc.codetest.product.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 문제 : 성능, validation 예외처리
 * 원인 : product 등록시 기본 생성자가 없어서 json -> 객체 변환시 에러 발생
 *       입력값 검증 어노테이션 누락
 * 개선안 : @NoArgsConstructor 추가, 혹은 기본 생성자 추가(public CreateProductRequest() {})
 *        -@NotBlank 추가하여 입력값 validation 적용
 */
@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequest {
    // 빈값이나 null값 입력 방지
    @NotBlank(message = "category is required")
    private String category;
    // 빈값이나 null값 입력 방지
    @NotBlank(message = "name is required")
    private String name;

    // 미사용 코드 제거
//    public CreateProductRequest(String category) {
//        this.category = category;
//    }
//
//    public CreateProductRequest(String category, String name) {
//        this.category = category;
//        this.name = name;
//    }
}

