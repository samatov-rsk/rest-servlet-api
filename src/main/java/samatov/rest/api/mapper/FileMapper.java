package samatov.rest.api.mapper;

import samatov.rest.api.dto.FileDTO;
import samatov.rest.api.model.File;

public class FileMapper {

    public static File toFileEntity(FileDTO fileDTO){
        return File.builder()
                .id(fileDTO.getId())
                .name(fileDTO.getName())
                .filePath(fileDTO.getFilePath())
                .build();
    }

    public static FileDTO toFileDto(File file){
        return FileDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .filePath(file.getFilePath())
                .build();
    }
}
