package samatov.rest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  UserDTO {
    private Integer id;
    private String name;
    private List<EventDTOWithOutUser> events;
}
