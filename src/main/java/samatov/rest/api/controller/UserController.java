package samatov.rest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import samatov.rest.api.dto.UserDTO;
import samatov.rest.api.repository.impl.UserRepositoryImpl;
import samatov.rest.api.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/rest/api/v1/users/*")
public class UserController extends HttpServlet {

    private UserService userService;
    private ObjectMapper objectMapper;

    public UserController() {
    }

    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        this.userService = new UserService(userRepository);
        this.objectMapper = new ObjectMapper();
        log.info("UserController servlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Received GET request with path info: {}", req.getPathInfo());
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            getAllUsers(req, resp);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                getUserById(req, resp, splits[1]);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private void getAllUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<UserDTO> users = userService.getAllUser();
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), users);
    }

    private void getUserById(HttpServletRequest req, HttpServletResponse resp, String userId) throws IOException {
        try {
            Integer id = Integer.parseInt(userId);
            UserDTO user = userService.getUserById(id);
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), user);
        } catch (NumberFormatException e) {
            log.error("Invalid user ID format: {}", userId);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid user ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Received POST request");
        UserDTO userDTO = objectMapper.readValue(req.getInputStream(), UserDTO.class);
        UserDTO newUser = userService.createUser(userDTO);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getOutputStream(), newUser);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Received PUT request with path info: {}", req.getPathInfo());
        String pathInfo = req.getPathInfo();
        String[] splits = pathInfo.split("/");
        if (splits.length == 2) {
            Integer id = Integer.parseInt(splits[1]);
            UserDTO userDTO = objectMapper.readValue(req.getInputStream(), UserDTO.class);
            userDTO.setId(id);
            UserDTO updatedUser = userService.updateUser(userDTO);
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), updatedUser);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Received DELETE request with path info: {}", req.getPathInfo());
        String pathInfo = req.getPathInfo();
        String[] splits = pathInfo.split("/");
        if (splits.length == 2) {
            Integer id = Integer.parseInt(splits[1]);
            userService.deleteUserById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
