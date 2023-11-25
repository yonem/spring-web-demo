package jp.ne.yonem.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * ページネーション用の共通サービス
 */
@Service
public class PaginationService {

    public static final int ROWS_PER_PAGE = 10;
    public static final String SUMMARY = "summary";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String TOTAL_PAGES = "totalPages";
    public static final String IS_PAGE_BEGIN = "isPageBegin";
    public static final String IS_PAGE_END = "isPageEnd";
    public static final String SUMMARY_TEMPLATE = "Showing %d to %d of %d results.";

    public void setPagination(Model model, Page rows, int currentPage) {
        var totalPages = rows.getTotalPages();
        if (totalPages <= currentPage && 0 < currentPage) currentPage--;
        var totalElements = rows.getTotalElements();
        var begin = currentPage * ROWS_PER_PAGE;
        var end = begin + ROWS_PER_PAGE;
        model.addAttribute(SUMMARY, SUMMARY_TEMPLATE.formatted(
                totalElements == 0 ? 0 : begin + 1
                , totalElements < end ? totalElements : end
                , totalElements
        ));
        model.addAttribute(CURRENT_PAGE, currentPage);
        model.addAttribute(TOTAL_PAGES, totalPages);
        model.addAttribute(IS_PAGE_BEGIN, currentPage == 0);
        model.addAttribute(IS_PAGE_END, totalPages - 1 <= currentPage);
    }
}
