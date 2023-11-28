package jp.ne.yonem.todo;

import jp.ne.yonem.util.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static jp.ne.yonem.util.MessageUtil.E_201;
import static jp.ne.yonem.util.MessageUtil.getMessage;
import static jp.ne.yonem.util.PaginationService.ROWS_PER_PAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class TodoControllerTest {

    private final MockMvc mockMvc;
    private final CategoryDao categories;
    private final TodoDAO todos;
    private final MessageSource messageSource;
    private CategoryEntity category;
    private TodoEntity todo;

    @Autowired
    public TodoControllerTest(
            MockMvc mockMvc
            , MessageSource messageSource
            , CategoryDao categories
            , TodoDAO todos
    ) {
        this.mockMvc = mockMvc;
        this.messageSource = messageSource;
        this.categories = categories;
        this.todos = todos;
    }

    @BeforeEach
    void setUp() {
        this.category = new CategoryEntity();
        category.setName("Category");
        category.setDescription("Description");
        this.categories.save(category);

        var todos = new ArrayList<TodoEntity>();

        for (var i = 0; i < ROWS_PER_PAGE; i++) {
            var item = new TodoEntity();
            item.setCategory(category);
            item.setSubject("テストタイトル-%d".formatted(i));
            item.setBody("テスト本文-%d".formatted(i));
            todos.add(item);
        }
        this.todos.saveAll(todos);
        this.todo = todos.getFirst();
    }

    @Test
    @DisplayName("TODO一覧画面 - 初期表示")
    @WithMockUser(username = "test_user")
    void test1() throws Exception {
        var model = mockMvc.perform(get(URL.HOME))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "TODO一覧"))
                .andExpect(model().attribute("url", URL.HOME))
                .andExpect(model().attribute("categoryId", this.category.getId()))
                .andExpect(model().attribute("userName", "test_user"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attributeExists("categories"))
                .andReturn().getModelAndView().getModel();
        assertEquals(category.getId(), ((List<CategoryEntity>) model.get("categories")).getFirst().getId());
        assertEquals(ROWS_PER_PAGE, ((Page<TodoEntity>) model.get("todos")).getSize());
    }

    @Test
    @DisplayName("TODO一覧画面 - TODO詳細")
    @WithMockUser(username = "test_user")
    void test2() throws Exception {
        var model = mockMvc.perform(get(URL.TODO_DETAIL.concat("?todoId=%d".formatted(todo.getId()))))
                .andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();
        var actual = (TodoEntity) model.get("todo");
        assertEquals(todo.getId(), actual.getId());
        assertEquals(todo.getSubject(), actual.getSubject());
        assertEquals(todo.getBody(), actual.getBody());
        assertEquals(category.getId(), actual.getCategory().getId());
        assertEquals(category.getName(), actual.getCategory().getName());
        assertEquals(category.getDescription(), actual.getCategory().getDescription());
    }

    @Test
    @DisplayName("TODO一覧画面 - 存在しないTODO詳細")
    @WithMockUser(username = "test_user")
    void test3() throws Exception {
        var model = mockMvc.perform(get(URL.TODO_DETAIL.concat("?todoId=-1")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("msg", getMessage(messageSource, E_201)))
                .andExpect(model().attribute("isError", true))
                .andReturn().getModelAndView().getModel();
    }
}
