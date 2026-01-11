package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *  문제 : 성능, 설계
 *  원인 : GenerationType.AUTO의 성능 문제 유발
 *        Entity에 Setter 사용으로 캡슐화 약화
 *        생성일, 수정일 등이 없어 히스토리 파악 불가
 *  개선안 : 아래 별도 주석으로 표기
 */

@Entity
@Getter
//@Setter
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "product_id")
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // GenerationType.AUTO는 요청당 id 생성을 위한 추가의 커넥션을 가져가기 때문에 풀사이즈 부족 가능성이 있음
    // GenerationType.IDENTITY로 설정. product_id 컬럼 Auto Increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable false 추가
    @Column(name = "category")
    private String category;

    // nullable false 추가
    @Column(name = "name")
    private String name;

    // 등록-수정자, 등록일시-수정일시 컬럼 추가
    @Column(name = "reg_id")
    private String regId;
    @Column(name = "reg_dtm")
    private LocalDateTime regDtm;
    @Column(name = "upd_id")
    private String updId;
    @Column(name = "upd_dtm")
    private LocalDateTime updDtm;

//    protected Product() {
//    }

    public Product(String category, String name, String myId) {
        this.category = category;
        this.name = name;
        // 등록 수정 이력 추가
        this.regDtm = LocalDateTime.now();
        this.updDtm = LocalDateTime.now();
        this.regId = myId;
        this.updId = myId;
    }

    public void update(String category, String name, String myId) {
        this.category = category;
        this.name = name;
        // 수정 이력 추가
        this.updDtm = LocalDateTime.now();
        this.updId = myId;
    }

    // Lombok @Getter 선언 되어있으므로 삭제
//    public String getCategory() {
//        return category;
//    }
//
//    public String getName() {
//        return name;
//    }
}
