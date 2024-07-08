package samatov.rest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import samatov.rest.api.dto.FileDTO;
import samatov.rest.api.repository.impl.EventRepositoryImpl;
import samatov.rest.api.repository.impl.FileRepositoryImpl;
import samatov.rest.api.repository.impl.UserRepositoryImpl;
import samatov.rest.api.service.EventService;
import samatov.rest.api.service.FileService;
import samatov.rest.api.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@MultipartConfig
@RequiredArgsConstructor
@WebServlet("/rest/api/v1/files/*")
public class FileController extends HttpServlet {

    private FileService fileService;
    private UserService userService;
    private EventService eventService;
    private ObjectMapper objectMapper;


    @Override
    public void init() throws ServletException {
        super.init();
        FileRepositoryImpl fileRepository = new FileRepositoryImpl();
        EventRepositoryImpl eventRepository = new EventRepositoryImpl();
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        this.eventService = new EventService(eventRepository);
        this.userService = new UserService(userRepository);
        this.fileService = new FileService(fileRepository,eventService,userService);
        this.objectMapper = new ObjectMapper();
        log.info("UserController servlet initialized");
    }

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
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String userId = req.getHeader("userId");
        if (userId == null || userId.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing userId header\"}");
            return;
        }

        Part filePart = req.getPart("file");
        if (filePart == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing file part\"}");
            return;
        }

        String fileName = fileService.getFileName(filePart);

        try (InputStream input = filePart.getInputStream()) {
            FileDTO fileDTO = fileService.uploadFile(userId, fileName, input);
            resp.setContentType("application/json; charset=UTF-8");
            objectMapper.writeValue(resp.getOutputStream(), fileDTO);
        } catch (Exception e) {
            log.error("Error uploading file", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Error uploading file: " + e.getMessage() + "\"}");
        }
    }
}
