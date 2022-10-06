package com.accountant.webrest;

import com.accountant.dao.TaskDAO;
import com.accountant.model.Task;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/gll_all_records"})
public class GetAllRecordsServlet extends HttpServlet {
    private static final TaskDAO taskDAO = new TaskDAO();
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        List<Task> taskList = taskDAO.getAll();
        String json = gson.toJson(taskList);
        resp.getWriter().write(json);
    }
}
