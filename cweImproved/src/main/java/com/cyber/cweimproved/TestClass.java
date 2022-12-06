package com.cyber.cweimproved;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.NodeList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class TestClass {
    private static Logger logger = LoggerFactory.getLogger(TestClass.class);
    private boolean isUnix = false;
    List<String> whiteList = new ArrayList<>();

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void index(HttpServletResponse res, String value) {
        res.setHeader("X-Data", value);
        Cookie cookie = new Cookie("data", value);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        res.addCookie(cookie);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String location = req.getParameter("url");

        List<String> allowedHosts = new ArrayList<>();
        allowedHosts.add("https://www.domain1.com/");
        allowedHosts.add("https://www.domain2.com/");

        if (allowedHosts.contains(location))
            resp.sendRedirect(location);
    }
    public boolean authenticate(javax.servlet.http.HttpServletRequest request, java.sql.Connection connection) throws SQLException {
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");

        String query = "SELECT * FROM users WHERE user = ? AND pass = ?"; // Safe even if authenticate() method is still vulnerable to brute-force attack in this specific case

       try ( java.sql.PreparedStatement statement = connection.prepareStatement(query);){
           statement.setString(1, user); // Will be properly escaped
           statement.setString(2, pass);
           java.sql.ResultSet resultSet = statement.executeQuery();
           return resultSet.next();
       }

    }
    public void runUnsafe(HttpServletRequest request) throws IOException {
        String cmd = request.getParameter("command");
        String arg = request.getParameter("arg");

        if(cmd.equals("/usr/bin/ls") || cmd.equals("/usr/bin/cat"))
        {
            // only ls or cat command are authorized
            String cmdarray[] =  new String[] { cmd, arg };
            Runtime.getRuntime().exec(cmdarray); // Compliant
        }
    }
    protected void doGet1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String encodedName = org.owasp.encoder.Encode.forHtml(name);
        PrintWriter out = resp.getWriter();
        out.write("Hello " + encodedName);
    }
    protected void getPassword() throws SQLException {
        String password = System.getProperty("database.password");
        try ( Connection conn = DriverManager.getConnection("jdbc:derby:memory:myDB;create=true", "login", password)){
            logger.debug(conn.toString());
        }
    }
    protected void logger1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(isUnix) {
            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));
            Files.createTempFile("prefix", "suffix", attr); // Compliant
        }
        else {
            File f = Files.createTempFile("prefix", "suffix").toFile();  // Compliant
           boolean j = f.setReadable(true, true);
           if(j){
              boolean p =  f.setWritable(true, true);
              if(p){
                  boolean nn = f.setExecutable(true, true);
                  if(nn){
                        logger.debug("the status is {}",nn);
                  }
              }

              }
           }
    }

    protected void dynamicCode(HttpServletRequest req, HttpServletResponse resp) throws IOException, ScriptException {

        String input = req.getParameter("input");

        // Match the input against a whitelist
        if (!whiteList.contains(input))
            throw new IOException();

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.eval(input);
    }

    SecureRandom random = new SecureRandom();

    public String applyCBC(String strKey, String plainText) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] bytesIV = new byte[16];
        random.nextBytes(bytesIV);

        /* KEY + IV setting */
        IvParameterSpec iv = new IvParameterSpec(bytesIV);
        SecretKeySpec skeySpec = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");

        /* Ciphering */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv); // Compliant
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return printBase64Binary(bytesIV)
                + ";" + printBase64Binary(encryptedBytes);
    }
    protected String printBase64Binary( byte[] bytesIV ){
        return Arrays.toString(bytesIV);
    }
    protected void hashesIssue(char[] chars){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
    }
    protected void streamer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        try(JsonGenerator generator = jsonFactory.createGenerator(OutputStream.nullOutputStream());){
            generator.writeStartObject();
            generator.writeFieldName("key");
            generator.writeString(req.getParameter("value"));
        }

    }

    protected void sqlite() throws SQLException {
        try(Connection connection =DriverManager.getConnection("jdbc:sqlite:sample.db") ) {
            try (Statement statement = connection.createStatement();){
                statement.setQueryTimeout(30);
            }
        }
    }
    protected void tester( IIOMetadataNode doc)  {
        NodeList signatureElement = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");

        DOMValidateContext valContext = new DOMValidateContext(new KeySelector() {
            @Override
            public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod method, XMLCryptoContext context) throws KeySelectorException {
                return null;
            }
        }, signatureElement.item(0));
        valContext.setProperty("org.jcp.xml.dsig.secureValidation", Boolean.TRUE);
    }
    protected void basicAuthenticate(URL url,String rawPassword) throws IOException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization",   encodedPassword);
    }
}
