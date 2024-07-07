package samatov.rest.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import samatov.rest.api.dto.EventDTO;
import samatov.rest.api.dto.FileDTO;
import samatov.rest.api.dto.UserDTO;
import samatov.rest.api.mapper.FileMapper;
import samatov.rest.api.model.File;
import samatov.rest.api.repository.FileRepository;
import samatov.rest.api.repository.impl.FileRepositoryImpl;

import javax.servlet.http.Part;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;
    private final EventService eventService;
    private final UserService userService;

    public FileService() {
        this.fileRepository = new FileRepositoryImpl();
        this.eventService = new EventService();
        this.userService = new UserService();
    }

    public List<FileDTO> getAllFile() {
        log.info("Получение всех файлов");
        List<File> files = fileRepository.findAll();
        log.info("Найдено {} файлов", files.size());
        return files.stream()
                .map(FileMapper::toFileDto)
                .collect(Collectors.toList());
    }

    public FileDTO getFileById(Integer id) {
        log.info("Получение файла по его id: {}", id);
        File file = fileRepository.findById(id);
        log.info("Получен файл c id: {}", id);
        return FileMapper.toFileDto(file);
    }


    public FileDTO uploadFile(String userId, String fileName, InputStream fileContent) throws Exception {
        String uploadDir = "src/main/files/";

        java.io.File file = new java.io.File(uploadDir, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        FileDTO fileDTO = FileDTO.builder()
                .name(fileName)
                .filePath(file.getAbsolutePath())
                .build();
        File fileEntity = FileMapper.toFileEntity(fileDTO);
        fileEntity = fileRepository.save(fileEntity);
        fileDTO = FileMapper.toFileDto(fileEntity);

        UserDTO user = userService.getUserById(Integer.parseInt(userId));
        EventDTO event = EventDTO.builder()
                .user(user)
                .file(fileDTO)
                .build();
        eventService.createEvent(event);

        return fileDTO;
    }

    public String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}
