package com.accountant.webrest;

import com.accountant.dao.TaskDAO;
import com.accountant.model.Task;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(urlPatterns = {"/get_user_by_id/*"})
public class GetRecordsByUserIdByDateServlet extends HttpServlet {
    private static final TaskDAO taskDAO = new TaskDAO();
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        int user = Integer.parseInt(req.getParameter("user_id"));
        long days = Long.parseLong(req.getParameter("days"));
        LocalDate date = LocalDate.now().minusDays(days);
        List<Task> taskList = taskDAO.findTaskByUserIdAndDate(user, date);
        String json = gson.toJson(taskList);
        resp.getWriter().write(json);
    }
}
