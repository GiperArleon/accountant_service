package com.accountant.webrest;

import com.accountant.dao.TaskDAO;
import com.accountant.model.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@WebServlet(urlPatterns = {"/create_record"})
public class CreateRecordServlet extends HttpServlet {
    private static final TaskDAO taskDAO = new TaskDAO();
    private static final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = req.getReader();
            while((line = reader.readLine()) != null)
                jb.append(line);
            JsonObject data = gson.fromJson(jb.toString(), JsonObject.class); //.fromJson(req.getReader(), JsonObject.class);
            Task task = new Task();
            task.setUserId(data.get("userId").getAsInt());
            task.setDate(LocalDate.now());
            task.setDescription(data.get("description").getAsString());
            task.setHours(data.get("hours").getAsInt());
            task.setMinutes(data.get("minutes").getAsInt());
            taskDAO.create(task);
            resp.setStatus(200);
            resp.getWriter().write("record inserted");
        } catch(Exception e) {
            log.error(e.getMessage());
            resp.setStatus(400);
            resp.getWriter().write("error in record insert");
        }
    }
}
