package jp.ne.yonem.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO情報Service
 */
@Service
public class TodoService {

    private final TodoDAO dao;

    @Autowired
    public TodoService(
            TodoDAO dao
    ) {
        this.dao = dao;
    }

    public Page<TodoEntity> findAll(Pageable pageable) {
        return dao.findAll(pageable);
    }

    public Page<TodoEntity> findByCategoryId(int categoryId, Pageable pageable) {
        return dao.findByCategoryIdAndDeletedAtIsNull(categoryId, pageable);
    }

    public TodoEntity findById(int id) {
        return dao.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteById(int id) {
        dao.deleteById(id);
    }
}
