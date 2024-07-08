package samatov.rest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTOWithOutUser {
    private Integer id;
    private UserDTOWithOutEvents user;
    private FileDTO file;
}
