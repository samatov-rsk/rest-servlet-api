package samatov.rest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import samatov.rest.api.dto.EventDTO;
import samatov.rest.api.service.EventService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@WebServlet("/rest/api/v1/events/*")
public class EventController extends HttpServlet {

    private final EventService eventService = new EventService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        log.info("EventController servlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<EventDTO> events = eventService.getAllEvents();
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), events);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                Integer id = Integer.parseInt(splits[1]);
                EventDTO event = eventService.getEventById(id);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), event);
            }
        }
    }
}
