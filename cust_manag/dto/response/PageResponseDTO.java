package com.pramaindia.cust_manag.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PageResponseDTO<T> {
    private List<T> list;
    private Integer pageSize;
    private Integer totalSize;
    private Integer pageNum;
    private Integer totalNum;
    private String orderBy;

    public PageResponseDTO(List<T> list, Integer totalSize, Integer pageNum, Integer pageSize) {
        this.list = list;
        this.totalSize = totalSize;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalNum = (int) Math.ceil((double) totalSize / pageSize);
    }
}