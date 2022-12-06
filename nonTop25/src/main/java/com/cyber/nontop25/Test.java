package com.cyber.nontop25;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Test {
    private boolean isUnix = false;
    private static Logger logger = LoggerFactory.getLogger(Test.class);

    protected void logger1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isUnix) {
            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));
            Files.createTempFile("prefix", "suffix", attr); // Compliant
        } else {
            File f = Files.createTempFile("prefix", "suffix").toFile();  // Compliant
            boolean j = f.setReadable(true, true);

            boolean p = f.setWritable(true, true);

            boolean nn = f.setExecutable(true, true);

            logger.debug("the status is {}", nn);


        }
    }
    protected void streamer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = "{\"key\":\""+req.getParameter("value")+"\"}";
        FileOutputStream fos = new FileOutputStream("output.json");
                fos.write(json.getBytes(Charset.forName("UTF-8")));  // Noncompliant
    }
    protected static void logging(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        File tempDir;
        tempDir = File.createTempFile("", ".");
        tempDir.delete();
        tempDir.mkdir();

    }

    public static String run(String input) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String cmd = "/usr/games/cowsay '" + input + "'";
        System.out.println(cmd);
        processBuilder.command("bash", "-c", cmd);

        StringBuilder output = new StringBuilder();
        return output.toString();
    }
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/comments", method = RequestMethod.GET, produces = "application/json")
    List<Comment> comments(@RequestHeader(value="x-auth-token") String token) {
        return new ArrayList<>();
    }

    public static Boolean delete(String id,Connection con) {
        try {
            String sql = "DELETE FROM comments where id = ?";
            PreparedStatement pStatement = con.prepareStatement(sql);
            pStatement.setString(1, id);
            return 1 == pStatement.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();

        } finally {
            return false;
        }
    }
    }
