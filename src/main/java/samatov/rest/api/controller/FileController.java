package samatov.rest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import samatov.rest.api.dto.FileDTO;
import samatov.rest.api.service.FileService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@MultipartConfig
@RequiredArgsConstructor
@WebServlet("/rest/api/v1/files/*")
public class FileController extends HttpServlet {

    private final FileService fileService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<FileDTO> files = fileService.getAllFile();
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), files);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                Integer id = Integer.parseInt(splits[1]);
                FileDTO file = fileService.getFileById(id);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), file);
            } else if (splits.length == 3 && splits[2].equals("user")) {
                Integer userId = Integer.parseInt(splits[1]);
                List<FileDTO> files = fileService.getFilesByUserId(userId);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), files);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uploadDir = "src/main/files/";
        File file = new File(uploadDir, fileName);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath());
        }

        FileDTO fileDTO = FileDTO.builder()
                .name(fileName)
                .filePath(file.getAbsolutePath())
                .build();

        fileDTO = fileService.uploadFile(fileDTO);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), fileDTO);
    }
}
