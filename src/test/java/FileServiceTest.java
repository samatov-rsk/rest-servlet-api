import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import samatov.rest.api.dto.EventDTOWithOutUser;
import samatov.rest.api.dto.FileDTO;
import samatov.rest.api.dto.UserDTOWithOutEvents;
import samatov.rest.api.exception.NotFoundException;
import samatov.rest.api.mapper.FileMapper;
import samatov.rest.api.model.File;
import samatov.rest.api.repository.FileRepository;
import samatov.rest.api.service.EventService;
import samatov.rest.api.service.FileService;
import samatov.rest.api.service.UserService;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Когда getAllFiles вызывается, то успешный результат")
    void getAllFilesTest() {
        List<File> expectedFiles = Arrays.asList(new File(1, "file1.txt", "path1"),
                new File(2, "file2.txt", "path2"));
        when(fileRepository.findAll()).thenReturn(expectedFiles);

        List<FileDTO> actualFiles = fileService.getAllFile();

        assertEquals(expectedFiles.size(), actualFiles.size());
        verify(fileRepository).findAll();
    }

    @Test
    @DisplayName("Когда getFileById вызывается, то успешный результат")
    void getFileByIdTest() {
        File expectedFile = new File(1, "file1.txt", "path1");
        when(fileRepository.findById(anyInt())).thenReturn(expectedFile);

        FileDTO actualFile = fileService.getFileById(1);

        assertEquals(expectedFile.getId(), actualFile.getId());
        verify(fileRepository).findById(anyInt());
    }

    @Test
    @DisplayName("Когда getFileById вызывается, то NotFoundException")
    void throwExceptionWhenFileNotFound() {
        when(fileRepository.findById(anyInt())).thenThrow(new NotFoundException("Ошибка, файл с таким id не найден"));

        assertThrows(NotFoundException.class, () -> fileService.getFileById(1));
        verify(fileRepository).findById(anyInt());
    }

    @Test
    @DisplayName("Когда uploadFile вызывается, то успешный результат")
    void uploadFileTest() throws Exception {
        FileDTO fileDTO = new FileDTO();
        InputStream inputStream = mock(InputStream.class);

        when(userService.getUserById(anyInt())).thenReturn(new UserDTOWithOutEvents());
        when(fileRepository.save(any(File.class))).thenReturn(FileMapper.toFileEntity(fileDTO));

        FileDTO uploadedFileDTO = fileService.uploadFile("1", "test.txt", inputStream);

        assertNotNull(uploadedFileDTO);
        verify(fileRepository).save(any(File.class));
        verify(eventService).createEvent(any(EventDTOWithOutUser.class));
    }

    @Test
    @DisplayName("Когда uploadFile вызывается, то FileException")
    void throwExceptionWhenUploadFileFails() {
        InputStream inputStream = mock(InputStream.class);

        when(fileRepository.save(any(File.class))).thenThrow(new RuntimeException("Ошибка запроса, файл не удалось сохранить..."));

        assertThrows(RuntimeException.class, () -> fileService.uploadFile("1", "test.txt", inputStream));
        verify(fileRepository).save(any(File.class));
    }
}
