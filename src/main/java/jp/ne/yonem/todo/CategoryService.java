package jp.ne.yonem.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * お知らせ情報Service
 */
@Service
public class CategoryService {

    private final CategoryDao repository;

    @Autowired
    public CategoryService(
            CategoryDao repository
    ) {
        this.repository = repository;
    }

    public Page<CategoryEntity> findAll(Pageable pageable) {
        return repository.findByDeletedAtIsNull(pageable);
    }

    public List<CategoryEntity> findAll(Sort sort) {
        return repository.findByDeletedAtIsNull(sort);
    }

    public CategoryEntity findById(int id) {
        return repository.findById(id).orElseThrow();
    }

    @Transactional
    public void logicalDeleteById(int id) {
        var category = findById(id);
        category.setDeletedAt(LocalDateTime.now());
        repository.save(category);
    }
}
