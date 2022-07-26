package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "items_requests", schema = "public")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "Description не должно быть пустым")
    private String description;
    private int requestor;
    private LocalDateTime created;

    @Transient
    private Set<ShortItem> items;

    @Data
    @AllArgsConstructor
    public static class ShortItem {
        private int id;
        private String name;
        private String description;
        private Boolean available;
        private int requestId;
    }
}
