package jp.ne.yonem.todo;

import jp.ne.yonem.util.LogicalDeleteForm;
import jp.ne.yonem.util.PaginationService;
import jp.ne.yonem.util.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * カテゴリ情報一覧Controller
 */
@Controller
public class CategoryController {

    private final CategoryService service;
    private final PaginationService pagination;
    private final MessageSource messageSource;

    @Autowired
    public CategoryController(
            CategoryService service
            , PaginationService pagination
            , MessageSource messageSource
    ) {
        this.service = service;
        this.pagination = pagination;
        this.messageSource = messageSource;
    }

    @GetMapping(URL.CATEGORY)
    String init(Model model, @RequestParam(defaultValue = "0") int page) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var pageable = PageRequest.of(page, PaginationService.ROWS_PER_PAGE, Sort.by(Sort.Order.asc("id")));
        var categories = service.findAll(pageable);
        model.addAttribute("title", "Category");
        model.addAttribute("userName", authentication.getName());
        model.addAttribute("categories", categories);
        pagination.setPagination(model, categories, page);
        return URL.VIEW_CATEGORY;
    }

    @PostMapping(URL.CATEGORY_DELETE)
    String delete(Model model, LogicalDeleteForm form) {
        service.logicalDeleteById(form.getId());

        // カテゴリの削除後に再検索し、一覧を最新化する
        var pageable = PageRequest.of(form.getPage(), PaginationService.ROWS_PER_PAGE, Sort.by(Sort.Order.asc("id")));
        var categories = service.findAll(pageable);
        pagination.setPagination(model, categories, form.getPage());
        var page = model.getAttribute(PaginationService.CURRENT_PAGE);
        return "redirect:%s?page=%s".formatted(URL.CATEGORY, page);
    }
}
