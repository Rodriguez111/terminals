package terminals.controller.servlets.terminals;

import terminals.controller.logic.BarcodeGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GenerateBarcode extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BarcodeGenerator barcodeGenerator = new BarcodeGenerator();
        String barcodeText = req.getParameter("barcodeText");
        byte[] buffer = barcodeGenerator.getBarcodeImage(barcodeText).toByteArray();
        resp.setContentType("image/gif");
        resp.setContentLength(buffer.length);
        resp.getOutputStream().write(buffer);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
