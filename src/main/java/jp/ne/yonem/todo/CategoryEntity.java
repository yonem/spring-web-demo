package jp.ne.yonem.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "categories")
@Data
public class CategoryEntity {

    public CategoryEntity() {
        createdAt = Timestamp.valueOf(LocalDateTime.now());
        updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = Timestamp.valueOf(createdAt);
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = Timestamp.valueOf(updatedAt);
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = Objects.isNull(deletedAt) ? null : Timestamp.valueOf(deletedAt);
    }
}
