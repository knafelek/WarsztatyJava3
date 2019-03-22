package pl.hit.coderslab.workshops.school.servlets;

import org.apache.log4j.Logger;
import pl.hit.coderslab.workshops.school.dao.ExerciseDao;
import pl.hit.coderslab.workshops.school.dao.SolutionDao;
import pl.hit.coderslab.workshops.school.dao.UserDao;
import pl.hit.coderslab.workshops.school.model.Exercise;
import pl.hit.coderslab.workshops.school.model.Solution;
import pl.hit.coderslab.workshops.school.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class HomePageServlet extends HttpServlet {

    public static final Logger logger = Logger.getLogger(HomePageServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int solutionsNumber = getSolutionsNumber();
        List<Solution> solutions = SolutionDao.findAll(solutionsNumber);
        logger.debug("Pobrane rozwiązania:"  + solutions);

        for (Solution solution : solutions) {
            Exercise exercise = ExerciseDao.findById(solution.getExerciseId());
            User user = UserDao.findById(solution.getUserId());
            solution.setExercise(exercise);
            solution.setUser(user);
        }

        req.setAttribute("solutions", solutions);

        logger.debug("Przekazanie obsługi do strony /WEB-INF/views/index.jsp");
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }

    private int getSolutionsNumber() {
        String numberSolutionsParam = getServletContext().getInitParameter("number-solutions");
        try {
            return Integer.parseInt(numberSolutionsParam);
        } catch (NumberFormatException nfe) {
            logger.warn("Nie udalo się odczytać parametru `numer-solutions`", nfe);
            return 5;
        }
    }
}
