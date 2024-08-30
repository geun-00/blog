package com.spring.blog.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.IntStream;

@Data
public class PageResponse<E> {

    private List<E> dataList;
    private List<Integer> pageNumList;
    private boolean prev, next;
    private int totalCount, currentPage, prevPage, nextPage;

    @Builder(builderMethodName = "withAll")
    public PageResponse(List<E> dataList, int currentPage, int pageSize, long totalCount) {

        this.dataList = dataList;
        this.totalCount = (int) totalCount;
        this.currentPage = currentPage;

        int end = (int) (Math.ceil(currentPage / 10.0)) * 10;
        int start = end - 9;

        int last = (int) Math.ceil(totalCount / (double) pageSize);

        end = Math.min(end, last);

        this.prev = start > 1;
        this.next = (int) totalCount > end * pageSize;

        this.prevPage = prev ? start - 1 : 0;
        this.nextPage = next ? end + 1 : 0;

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().toList();
    }
}