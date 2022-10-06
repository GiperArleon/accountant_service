package com.accountant.webrest;

import com.accountant.dao.TaskDAO;
import com.accountant.model.Task;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@WebServlet(urlPatterns = {"/gll_all_records_for_days/*"})
public class GetAllRecordsByDateServlet extends HttpServlet {
    private static final TaskDAO taskDAO = new TaskDAO();
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        long days = Long.parseLong(req.getParameter("days"));
        LocalDate date = LocalDate.now().minusDays(days);
        List<Task> taskList = taskDAO.getAllByDate(date);
        String json = gson.toJson(taskList);
        resp.getWriter().write(json);
    }
}
