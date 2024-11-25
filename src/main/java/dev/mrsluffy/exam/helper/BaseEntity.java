package dev.mrsluffy.exam.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * product
 *
 * @author John Andrew Camu <werdna.jac@gmail.com>
 * @version 1.0
 * @since 10/11/2024
 **/

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    @Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(columnDefinition = "timestamp NULL")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "timestamp NULL")
    private LocalDateTime deletedAt;

    @JsonProperty("deleted")
    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted = false;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final BaseEntity other = (BaseEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }

        return true;
    }
}
