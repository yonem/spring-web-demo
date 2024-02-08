package jp.ne.yonem.todo;

import jp.ne.yonem.util.LogicalDeleteForm;
import jp.ne.yonem.util.MessageUtil;
import jp.ne.yonem.util.PaginationService;
import jp.ne.yonem.util.URL;
import lombok.extern.slf4j.Slf4j;
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
 * TODO一覧Controller
 */
@Controller
@Slf4j
public class TodoController {

    private final TodoService service;
    private final PaginationService pagination;
    private final CategoryService categoryService;
    private final MessageSource messageSource;

    @Autowired
    public TodoController(
            TodoService service
            , CategoryService categoryService
            , PaginationService pagination
            , MessageSource messageSource
    ) {
        this.service = service;
        this.categoryService = categoryService;
        this.pagination = pagination;
        this.messageSource = messageSource;
    }

    @GetMapping(URL.HOME)
    String init(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "-1") int categoryId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var pageable = PageRequest.of(page, PaginationService.ROWS_PER_PAGE, Sort.by(Sort.Order.desc("updatedAt")));
        var categories = categoryService.findAll(Sort.by("id"));
        if (categoryId < 0 && !categories.isEmpty()) categoryId = categories.getFirst().getId();
        var todos = service.findByCategoryId(categoryId, pageable);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("title", "TODO一覧");
        model.addAttribute("url", URL.HOME);
        model.addAttribute("userName", authentication.getName());
        model.addAttribute("todos", todos);
        pagination.setPagination(model, todos, page);
        return URL.VIEW_TODO;
    }

    @GetMapping(URL.TODO_DETAIL)
    String modal(Model model, @RequestParam int todoId) {
        model.addAttribute("todoId", todoId);

        try {
            var todo = service.findById(todoId);
            model.addAttribute("todo", todo);

        } catch (Exception e) {
            var msg = MessageUtil.getMessage(messageSource, MessageUtil.E_201);
            log.error(msg, e);
            model.addAttribute("msg", msg);
            model.addAttribute("isError", true);
        }
        return URL.VIEW_TODO_DETAIL;
    }

    @PostMapping(URL.TODO_DELETE)
    String delete(Model model, LogicalDeleteForm form) {
        service.deleteById(form.getId());

        // TODO情報の削除後に再検索し、一覧を最新化する
        var pageable = PageRequest.of(form.getPage(), PaginationService.ROWS_PER_PAGE, Sort.by(Sort.Order.asc("id")));
        var todos = service.findAll(pageable);
        pagination.setPagination(model, todos, form.getPage());
        var page = model.getAttribute(PaginationService.CURRENT_PAGE);
        return "redirect:%s?page=%s".formatted(URL.HOME, page);
    }
}
