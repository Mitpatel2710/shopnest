package com.shopnest.util;

import java.util.Collections;
import java.util.List;

// T = the type of items in the page — Product, Order, User, etc.
public class Page<T> {

    // ── Fields ────────────────────────────────────────────
    private final List<T> content;          // items on this page
    private final int pageNumber;           // 0-based current page
    private final int pageSize;             // how many per page
    private final long totalElements;       // total records in DB
    private final int totalPages;           // calculated total pages

    // ── Constructor ───────────────────────────────────────
    public Page(List<T> content, int pageNumber, int pageSize, long totalElements) {
        if (content == null)    throw new IllegalArgumentException("Content cannot be null");
        if (pageNumber < 0)     throw new IllegalArgumentException("Page number cannot be negative");
        if (pageSize <= 0)      throw new IllegalArgumentException("Page size must be positive");
        if (totalElements < 0)  throw new IllegalArgumentException("Total elements cannot be negative");

        this.content       = content;
        this.pageNumber    = pageNumber;
        this.pageSize      = pageSize;
        this.totalElements = totalElements;
        this.totalPages    = pageSize > 0
                ? (int) Math.ceil((double) totalElements / pageSize)
                : 0;
    }

    // ── Static factory — convenient creation ──────────────
    public static <T> Page<T> of(List<T> content, int pageNumber,
                                 int pageSize, long totalElements) {
        return new Page<>(content, pageNumber, pageSize, totalElements);
    }

    // Empty page
    public static <T> Page<T> empty(int pageSize) {
        return new Page<>(Collections.emptyList(), 0, pageSize, 0);
    }

    // ── Navigation helpers ────────────────────────────────
    public boolean isFirst()        { return pageNumber == 0; }
    public boolean isLast()         { return pageNumber >= totalPages - 1; }
    public boolean hasNext()        { return !isLast() && totalPages > 0; }
    public boolean hasPrevious()    { return pageNumber > 0; }
    public boolean isEmpty()        { return content.isEmpty(); }
    public int getNumberOfElements(){ return content.size(); }

    // ── Getters ───────────────────────────────────────────
    public List<T> getContent()     { return Collections.unmodifiableList(content); }
    public int getPageNumber()      { return pageNumber; }
    public int getPageSize()        { return pageSize; }
    public long getTotalElements()  { return totalElements; }
    public int getTotalPages()      { return totalPages; }

    @Override
    public String toString() {
        return "Page{" +
                "page=" + pageNumber +
                "/" + (totalPages - 1) +
                ", size=" + pageSize +
                ", elements=" + content.size() +
                "/" + totalElements +
                ", first=" + isFirst() +
                ", last=" + isLast() +
                '}';
    }
}
