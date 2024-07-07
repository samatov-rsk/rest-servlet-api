package samatov.rest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import samatov.rest.api.dto.FileDTO;
import samatov.rest.api.service.FileService;

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
@WebServlet("/rest/api/v1/files/*")
public class FileController extends HttpServlet {

    private final FileService fileService = new FileService();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void init() throws ServletException {
        super.init();
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
        String userId = req.getHeader("userId");
        if (userId == null || userId.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing userId header");
            return;
        }

        Part filePart = req.getPart("file");
        if (filePart == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing file part");
            return;
        }

        String fileName = fileService.getFileName(filePart);

        try (InputStream input = filePart.getInputStream()) {
            FileDTO fileDTO = fileService.uploadFile(userId, fileName, input);
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), fileDTO);
        } catch (Exception e) {
            log.error("Error uploading file", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error uploading file");
        }
    }
}
