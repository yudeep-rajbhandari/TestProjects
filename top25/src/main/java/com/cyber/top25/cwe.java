package com.cyber.top25;

import ch.qos.logback.core.util.SystemInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;


public class cwe {


    private CSVPrinter csvPrinter = null;
    private CSVFormat csvFormat = null;

    public void print(Object value, String filePath) {
        if (csvPrinter == null) {
            try {
                csvPrinter = csvFormat.print(new PrintWriter(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            csvPrinter.print(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void integerOverflow(int seconds) {
        float twoThirds = 2 / 3; // Noncompliant; int division. Yields 0.0
        long millisInYear = 1_000 * 3_600 * 24 * 365; // Noncompliant; int multiplication. Yields 1471228928
        long bigNum = Integer.MAX_VALUE + 2; // Noncompliant. Yields -2147483647
        long bigNegNum = Integer.MIN_VALUE - 1; //Noncompliant, gives a positive result instead of a negative one.
        Date myDate = new Date(seconds * 1_000); //Noncompliant, won't produce the expected result if seconds > 2_147_483

    }

    public long compute(int factor) {
        return factor * 10_000;  //Noncompliant, won't produce the expected result if factor > 214_748
    }

    public float compute2(long factor) {
        return factor / 123;  //Noncompliant, will be rounded to closest long integer
    }

    private void cwe611() throws TransformerConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // Noncompliant

        SAXParserFactory factory1 = SAXParserFactory.newInstance(); // Noncompliant

        XMLInputFactory factory2 = XMLInputFactory.newInstance(); // Noncompliant


        SchemaFactory factory4 = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);  // Noncompliant
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        Integer properties = 0;
        if (properties != null) {
            transformer.setOutputProperties(new Properties());
        }

    }

    private Connection getConnection(String endpoint, int queryTimeout) throws SQLException {
        Connection connection =

                DriverManager.getConnection(
                        "endpoint + getParam(null, queryTimeout)",
                        System.getProperty("User", "root"),
                        System.getProperty("Password", "root"));
        connection.close();
        return connection;
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    public String metricFindQuery() {
        JSONObject root = new JSONObject();
        List<String> columnsName = new ArrayList<>();
        try {
            columnsName.add("databaseConnectService.getMetaData()");

        } catch (Exception e) {

        }
        return columnsName.get(0);
    }
    public boolean authenticate(javax.servlet.http.HttpServletRequest request, java.sql.Connection connection) throws SQLException {
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        String query = "SELECT * FROM users WHERE user = '" + user + "' AND pass = '" + pass + "'"; // Unsafe
        java.sql.Statement statement = connection.createStatement();
        java.sql.ResultSet resultSet = statement.executeQuery(query); // Noncompliant
        return resultSet.next();
    }

}
