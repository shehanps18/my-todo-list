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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
