package com.wjc.codetest.product.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductListRequest {

    @NotNull(message = "category is required")
    private String category;

    // order 기준, order 방향 추가
    private String orderBy;
    private String sortDirection;

    private int page;
    private int size;
}