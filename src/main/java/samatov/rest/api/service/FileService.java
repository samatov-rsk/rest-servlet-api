package samatov.rest.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import samatov.rest.api.dto.*;
import samatov.rest.api.mapper.FileMapper;
import samatov.rest.api.model.File;
import samatov.rest.api.repository.FileRepository;
import samatov.rest.api.repository.impl.FileRepositoryImpl;

import javax.servlet.http.Part;
import java.io.IOException;
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
            if (!file.getParentFile().mkdirs()) {
                log.error("Не удалось создать директорию: {}", file.getParentFile().getAbsolutePath());
                throw new IOException("Не удалось создать директорию для загрузки файлов");
            }
        }

        try {
            Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            log.info("Файл скопирован в: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("Ошибка копирования файла: {}", e.getMessage(), e);
            throw e;
        }

        FileDTO fileDTO = FileDTO.builder()
                .name(fileName)
                .filePath(file.getAbsolutePath())
                .build();

        File fileEntity = FileMapper.toFileEntity(fileDTO);
        fileEntity = fileRepository.save(fileEntity);

        if (fileEntity == null) {
            log.error("Не удалось сохранить файл в базу данных");
            throw new Exception("Не удалось сохранить файл в базу данных");
        }
        log.info("Файл сохранен в базу данных с ID: {}", fileEntity.getId());

        UserDTOWithOutEvents user;
        try {
            user = userService.getUserById(Integer.parseInt(userId));
            log.info("Получен пользователь с ID: {}", userId);
        } catch (NumberFormatException e) {
            log.error("Неверный формат userId: {}", userId);
            throw new Exception("Неверный формат userId", e);
        }

        if (user == null) {
            log.error("Пользователь с ID {} не найден", userId);
            throw new Exception("Пользователь не найден");
        }

        EventDTOWithOutUser event = EventDTOWithOutUser.builder()
                .user(user)
                .file(FileMapper.toFileDto(fileEntity))
                .build();

        try {
            eventService.createEvent(event);
            log.info("Событие успешно создано для файла с ID: {}", fileEntity.getId());
        } catch (Exception e) {
            log.error("Ошибка создания события: {}", e.getMessage(), e);
            throw new Exception("Ошибка создания события", e);
        }

        return FileMapper.toFileDto(fileEntity);
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
