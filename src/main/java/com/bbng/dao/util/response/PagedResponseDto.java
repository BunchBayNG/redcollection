package com.bbng.dao.util.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PagedResponseDto<T> {
    private int statusCode;
    private boolean status;
    private String message;
    private int currentPage;
    private int itemsPerPage;
    private long totalItems;
    private int totalPages;
    private List<T> items;
}
