package jp.ne.yonem.todo;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "todo")
@Data
public class TodoEntity {

    public TodoEntity() {
        createdAt = Timestamp.valueOf(LocalDateTime.now());
        updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private CategoryEntity category;

    private String subject;
    private String body;
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
