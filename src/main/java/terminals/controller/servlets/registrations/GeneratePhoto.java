package terminals.controller.servlets.registrations;

import terminals.controller.logic.BarcodeGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class GeneratePhoto extends HttpServlet {
    private static final String PHOTO_FOLDER = "c:/terminals_resources/photos/";
    @Override
    public void init() throws ServletException {
        File folder = new File(PHOTO_FOLDER);
        File terminalsPhotoFolder = new File(PHOTO_FOLDER + "terminalsphoto/");
        File usersPhotoFolder = new File(PHOTO_FOLDER + "usersphoto/");
        if (!folder.exists()) {
            terminalsPhotoFolder.mkdirs();
            usersPhotoFolder.mkdirs();
        }

    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String folder = req.getParameter("folder");
        String fileName = req.getParameter("fileName");
        File photoFile = new File(PHOTO_FOLDER + folder + "/" + fileName);
        FileInputStream fileInputStream = new FileInputStream(photoFile);
        int bufferSize = 1024*100;
        byte[] buffer = new byte[bufferSize];
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        OutputStream outputStream = resp.getOutputStream();

        resp.setContentType("image/gif");
        while (bytesRead > 0) {
            outputStream.write(buffer);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }
        fileInputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
