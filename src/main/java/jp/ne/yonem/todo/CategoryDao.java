package jp.ne.yonem.todo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryDao extends JpaRepository<CategoryEntity, Integer> {

    public Page<CategoryEntity> findByDeletedAtIsNull(Pageable page);

    public List<CategoryEntity> findByDeletedAtIsNull(Sort sort);
}
