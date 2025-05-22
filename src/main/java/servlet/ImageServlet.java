package servlet;

import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.MutterDAO;
import model.Mutter;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mutterIdStr = request.getParameter("id");
        if (mutterIdStr == null || mutterIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'id' parameter.");
            return;
        }

        int mutterId;
        try {
            mutterId = Integer.parseInt(mutterIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'id' parameter format.");
            return;
        }

        MutterDAO mutterDAO = new MutterDAO();
        // Assuming we need the whole Mutter object to get user context for findByIdAndUserId,
        // or if findByIdAndUserId is the most suitable existing method.
        // If there's a more direct way to get a Mutter by ID alone, that could be used.
        // For this task, we don't have a direct getMutterById method,
        // and findByIdAndUserId requires userId, which we don't have here.
        // This highlights a potential need for a new DAO method: findById(int mutterId).
        // For now, let's adapt. A common approach would be to fetch without user ID if the image is public.
        // Or, if images are tied to viewing permissions, this servlet would need user session checks.

        // TEMPORARY WORKAROUND: Since MutterDAO.findByIdAndUserId needs userId,
        // and this servlet doesn't have it, we cannot directly use it.
        // A proper solution would be to add a `Mutter findById(int id)` method to MutterDAO.
        // For the purpose of this subtask, we will simulate fetching a Mutter object.
        // In a real scenario, this part MUST be implemented correctly with proper DB access.

        // Let's try to fetch all and filter. This is INEFFICIENT and NOT FOR PRODUCTION.
        // This is only to make the servlet structure complete for the task.
        // The correct way is: Mutter mutter = mutterDAO.findById(mutterId);
        Mutter mutter = null;
        java.util.List<Mutter> mutters = mutterDAO.findAll(); // Inefficient
        for (Mutter m : mutters) {
            if (m.getId() == mutterId) {
                mutter = m;
                break;
            }
        }

        if (mutter == null || mutter.getImageData() == null || mutter.getImageData().length == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found.");
            return;
        }

        // TODO: Determine content type dynamically if possible.
        // For now, defaulting to "image/jpeg". A better solution would be to store
        // the MIME type alongside the image data in the database.
        response.setContentType("image/jpeg");
        response.setContentLength(mutter.getImageData().length);

        try (OutputStream out = response.getOutputStream()) {
            out.write(mutter.getImageData());
        }
    }
}
