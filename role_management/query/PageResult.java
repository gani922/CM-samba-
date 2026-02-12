package com.pramaindia.role_management.query;

import lombok.Data;

@Data
public class PageResult<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Integer totalPages;
    private T list;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }
}