package jp.ne.yonem.todo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoDAO extends JpaRepository<TodoEntity, Integer> {

    public Page<TodoEntity> findByCategoryIdAndDeletedAtIsNull(int categoryId, Pageable page);

    public Page<TodoEntity> findByDeletedAtIsNull(Pageable page);
}
