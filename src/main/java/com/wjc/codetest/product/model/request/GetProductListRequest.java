package com.wjc.codetest.product.model.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductListRequest {

    private String category;

    // order 기준, order 방향 추가
    @Pattern(regexp = "^(id|name|category)$", message = "Invalid orderBy field")
    private String orderBy = "id";
    @Pattern(regexp = "^(ASC|DESC)$", message = "Invalid sort direction")
    private String sortDirection = "ASC";

    private int page;
    private int size;
}