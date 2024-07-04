package samatov.rest.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import samatov.rest.api.dto.FileDTO;
import samatov.rest.api.mapper.FileMapper;
import samatov.rest.api.model.File;
import samatov.rest.api.repository.FileRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;

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

    public List<FileDTO> getFilesByUserId(Integer userId) {
        // Реализовать метод для получения файлов по ID пользователя
        return null;
    }

    public FileDTO uploadFile(FileDTO fileDTO) {
        log.info("Загрузка файла: {}", fileDTO);
        File file = FileMapper.toFileEntity(fileDTO);
        file = fileRepository.save(file);
        log.info("Файл загружен: {}", fileDTO);
        return FileMapper.toFileDto(file);
    }
}
