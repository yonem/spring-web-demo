package jp.ne.yonem.util;

import jp.ne.yonem.todo.CategoryDao;
import jp.ne.yonem.todo.CategoryEntity;
import jp.ne.yonem.todo.TodoDAO;
import jp.ne.yonem.todo.TodoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static jp.ne.yonem.util.PaginationService.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class PaginationServiceTest {

    private final MockMvc mockMvc;
    private final TodoDAO todos;
    private final CategoryDao categories;

    @Autowired
    public PaginationServiceTest(
            MockMvc mockMvc
            , TodoDAO todos
            , CategoryDao categories
    ) {
        this.mockMvc = mockMvc;
        this.todos = todos;
        this.categories = categories;
    }

    @Test
    @DisplayName("ページネーション")
    @WithMockUser(username = "test_user")
    void test1() throws Exception {
        var category = new CategoryEntity();
        category.setName("Category");
        this.categories.save(category);

        var todos = new ArrayList<TodoEntity>();

        for (var i = 0; i < ROWS_PER_PAGE * 2 + 1; i++) {
            var item = new TodoEntity();
            item.setCategory(category);
            item.setSubject("テストタイトル-%d".formatted(i));
            item.setBody("テスト本文-%d".formatted(i));
            todos.add(item);
        }
        this.todos.saveAll(todos);
        var noticeSize = todos.size();

        // 1/3
        mockMvc.perform(get(URL.HOME))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute(SUMMARY, SUMMARY_TEMPLATE.formatted(1, ROWS_PER_PAGE, noticeSize)))
                .andExpect(model().attribute(CURRENT_PAGE, 0))
                .andExpect(model().attribute(TOTAL_PAGES, 3))
                .andExpect(model().attribute(IS_PAGE_BEGIN, true))
                .andExpect(model().attribute(IS_PAGE_END, false));

        // 2/3
        mockMvc.perform(get(URL.HOME.concat("?page=%d".formatted(1))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute(SUMMARY, SUMMARY_TEMPLATE.formatted(ROWS_PER_PAGE + 1, ROWS_PER_PAGE * 2, noticeSize)))
                .andExpect(model().attribute(CURRENT_PAGE, 1))
                .andExpect(model().attribute(TOTAL_PAGES, 3))
                .andExpect(model().attribute(IS_PAGE_BEGIN, false))
                .andExpect(model().attribute(IS_PAGE_END, false));

        // 3/3
        mockMvc.perform(get(URL.HOME.concat("?page=%d".formatted(2))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute(SUMMARY, SUMMARY_TEMPLATE.formatted(noticeSize, noticeSize, noticeSize)))
                .andExpect(model().attribute(CURRENT_PAGE, 2))
                .andExpect(model().attribute(TOTAL_PAGES, 3))
                .andExpect(model().attribute(IS_PAGE_BEGIN, false))
                .andExpect(model().attribute(IS_PAGE_END, true));

        this.todos.deleteById(todos.getFirst().getId());

        // 3/3 -> 2/2
        mockMvc.perform(get(URL.HOME.concat("?page=%d".formatted(2))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute(SUMMARY, SUMMARY_TEMPLATE.formatted(ROWS_PER_PAGE + 1, noticeSize - 1, noticeSize - 1)))
                .andExpect(model().attribute(CURRENT_PAGE, 1))
                .andExpect(model().attribute(TOTAL_PAGES, 2))
                .andExpect(model().attribute(IS_PAGE_BEGIN, false))
                .andExpect(model().attribute(IS_PAGE_END, true));
    }
}