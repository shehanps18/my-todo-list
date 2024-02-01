package tm;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor



public class ToDoTm {
    private String id;
    private String description;
    private String userId;

    @Override
    public String toString() {
        return getDescription();
    }
}
