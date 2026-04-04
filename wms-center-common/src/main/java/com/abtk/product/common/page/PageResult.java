package com.abtk.product.common.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Integer totalPages;
    private List<T> list;

    public PageResult(List<T> list, long total) {
        this.list = list;
        this.total = total;
        this.pageNum = 1;
        this.pageSize = (int) Math.ceil((double) total / this.list.size());
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }

    public <R> PageResult<R> map(Function<? super T, ? extends R> mapper) {
        List<R> newList = (list == null) ? Collections.emptyList()
                : list.stream().map(mapper).collect(Collectors.toList());
        PageResult<R> r = new PageResult<>();
        r.setTotal(this.total);
        r.setPageNum(this.pageNum);
        r.setPageSize(this.pageSize);
        r.setTotalPages(this.totalPages);
        r.setList(newList);
        return r;
    }
}
