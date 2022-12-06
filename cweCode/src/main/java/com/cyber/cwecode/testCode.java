package com.cyber.cwecode;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.NodeList;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.metadata.IIOMetadataNode;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.*;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Set;

public class testCode {
    private boolean isUnix = false;
    private static Logger logger = LoggerFactory.getLogger(testCode.class);
    @RequestMapping(value = "/")
    public void index(HttpServletResponse res, String value) {
        res.setHeader("Set-Cookie", value);  // Noncompliant
        Cookie cookie = new Cookie("jsessionid", value);  // Noncompliant
        res.addCookie(cookie);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String value = req.getParameter("value");
        resp.addHeader("X-Header", value); // Noncompliant
    }
    public boolean authenticate(javax.servlet.http.HttpServletRequest request, java.sql.Connection connection) throws SQLException {
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");

        String query = "SELECT * FROM users WHERE user = '" + user + "' AND pass = '" + pass + "'"; // Unsafe

        // If the special value "foo' OR 1=1 --" is passed as either the user or pass, authentication is bypassed
        // Indeed, if it is passed as a user, the query becomes:
        // SELECT * FROM users WHERE user = 'foo' OR 1=1 --' AND pass = '...'
        // As '--' is the comment till end of line syntax in SQL, this is equivalent to:
        // SELECT * FROM users WHERE user = 'foo' OR 1=1
        // which is equivalent to:
        // SELECT * FROM users WHERE 1=1
        // which is equivalent to:
        // SELECT * FROM users

        java.sql.Statement statement = connection.createStatement();
        java.sql.ResultSet resultSet = statement.executeQuery(query); // Noncompliant
        return resultSet.next();
    }


    public void runUnsafe(HttpServletRequest request) throws IOException {
        String cmd = request.getParameter("command");
        String arg = request.getParameter("arg");

        Runtime.getRuntime().exec(cmd+" "+arg); // Noncompliant
    }

    protected void doGet1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        PrintWriter out = resp.getWriter();
        out.write("Hello " + name); // Noncompliant
    }
    protected  void getPassword() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:derby:memory:myDB;create=true", "login", "");

    }
    protected static void logging(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        File tempDir;
        tempDir = File.createTempFile("", ".");
        tempDir.delete();
        tempDir.mkdir();

    }

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
    protected void dynamicCode(HttpServletRequest req, HttpServletResponse resp) throws IOException, ScriptException {
        String input = req.getParameter("input");

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.eval(input); // Noncompliant
    }

    public String applyCBC(String strKey, String plainText) throws Exception {
        byte[] bytesIV = "7cVgr5cbdCZVw5WY".getBytes("UTF-8");

        /* KEY + IV setting */
        IvParameterSpec iv = new IvParameterSpec(bytesIV);
        SecretKeySpec skeySpec = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");

        /* Ciphering */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);  // Noncompliant: the IV is hard coded and thus not generated with a secure random generator
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return printBase64Binary(bytesIV)
                + ";" + printBase64Binary(encryptedBytes);
    }
    protected String printBase64Binary( byte[] bytesIV ){
        return bytesIV.toString();
    }

    protected void hashesIssue(char[] chars){
        byte[] salt = "notrandom".getBytes();

        PBEParameterSpec cipherSpec = new PBEParameterSpec(salt, 10000); // Noncompliant, predictable salt
        PBEKeySpec spec = new PBEKeySpec(chars, salt, 10000, 256);
    }

    protected void streamer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = "{\"key\":\""+req.getParameter("value")+"\"}";
        FileOutputStream fos = new FileOutputStream("output.json");
        fos.write(json.getBytes(Charset.forName("UTF-8")));  // Noncompliant
    }

    protected void tester( IIOMetadataNode doc) throws SQLException, MarshalException, XMLSignatureException {
        NodeList signatureElement = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        DOMValidateContext valContext = new DOMValidateContext(new KeySelector() {
            @Override
            public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException {
                return null;
            }
        }, signatureElement.item(0)); // Noncompliant
        XMLSignature signature = fac.unmarshalXMLSignature(valContext);

        boolean signatureValidity = signature.validate(valContext);
    }

    protected void basicAuthenticate(URL url) throws IOException {
        String encoding = Base64.getEncoder().encodeToString(("login:passwd").getBytes());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Basic " + encoding);
    }
}
