package com.shopnest.util;

import java.util.Collections;
import java.util.List;

public class PaginationUtil {

    // Private constructor — utility class, no instances needed
    private PaginationUtil() {}

    // Slice any List<T> into a Page<T>
    // pageNumber is 0-based (page 0 = first page)
    public static <T> Page<T> paginate(List<T> allItems, int pageNumber, int pageSize) {
        if (allItems == null || allItems.isEmpty())
            return Page.empty(pageSize);

        long totalElements = allItems.size();
        int fromIndex = pageNumber * pageSize;

        // Requested page is beyond available data
        if (fromIndex >= totalElements)
            return Page.of(Collections.emptyList(), pageNumber, pageSize, totalElements);

        int toIndex = (int) Math.min((long) fromIndex + pageSize, totalElements);
        List<T> pageContent = allItems.subList(fromIndex, toIndex);

        return Page.of(pageContent, pageNumber, pageSize, totalElements);
    }
}