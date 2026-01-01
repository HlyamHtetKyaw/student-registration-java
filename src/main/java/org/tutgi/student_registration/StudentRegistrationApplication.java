package org.tutgi.student_registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
public class StudentRegistrationApplication {
	public static void main(String[] args) {
		if (isDevEnvironment()) {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
			dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
			);
		}
		SpringApplication.run(StudentRegistrationApplication.class, args);
	}

	private static boolean isDevEnvironment() {
		String profile = System.getenv("SPRING_PROFILES_ACTIVE");
		return profile == null || profile.equals("dev");
	}
}
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
//import org.docx4j.wml.*;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//
//import java.io.File;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.math.RoundingMode;
//
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//
//import java.io.*;
//import java.util.regex.Pattern;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//import java.util.zip.ZipOutputStream;
//
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.w3c.dom.Document;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import java.io.*;
//import java.math.BigDecimal;
//import java.nio.charset.StandardCharsets;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//import java.util.zip.ZipOutputStream;
//
//public class StudentRegistrationApplication {
//
//    public static void main(String[] args) throws Exception {
//        Resource resource = new ClassPathResource("shell/Registration.docx");
//
//        File sanitizedDocx = sanitizeDocx(resource.getInputStream());
//        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(sanitizedDocx);
//
//        File output = new File("output.docx");
//        wordMLPackage.save(output);
//        System.out.println("✅ Fixed document saved as " + output.getAbsolutePath());
//    }
//
//    private static File sanitizeDocx(InputStream docxInputStream) throws Exception {
//        File tempFile = File.createTempFile("sanitized-", ".docx");
//
//        try (ZipInputStream zin = new ZipInputStream(docxInputStream);
//             ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(tempFile))) {
//
//            ZipEntry entry;
//            while ((entry = zin.getNextEntry()) != null) {
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                zin.transferTo(baos);
//                byte[] data = baos.toByteArray();
//
//                String entryName = entry.getName();
//
//                // ✅ Only sanitize /word/*.xml files
//                if (entryName.startsWith("word/") && entryName.endsWith(".xml")) {
//                    String cleanedXml = sanitizeXmlNumbers(new String(data, StandardCharsets.UTF_8));
//                    data = cleanedXml.getBytes(StandardCharsets.UTF_8);
//                }
//
//                zout.putNextEntry(new ZipEntry(entryName));
//                zout.write(data);
//                zout.closeEntry();
//            }
//        }
//
//        return tempFile;
//    }
//
//    private static String sanitizeXmlNumbers(String xml) throws Exception {
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        dbf.setNamespaceAware(true);
//        DocumentBuilder db = dbf.newDocumentBuilder();
//
//        Document doc;
//        try (ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
//            doc = db.parse(in);
//        }
//
//        // Traverse all attributes and fix decimal numbers
//        traverseAndFix(doc.getDocumentElement());
//
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer transformer = tf.newTransformer();
//        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//
//        StringWriter writer = new StringWriter();
//        transformer.transform(new DOMSource(doc), new StreamResult(writer));
//
//        return writer.toString();
//    }
//
//    private static void traverseAndFix(Node node) {
//        if (node.hasAttributes()) {
//            NamedNodeMap attrs = node.getAttributes();
//            for (int i = 0; i < attrs.getLength(); i++) {
//                Node attr = attrs.item(i);
//                String val = attr.getNodeValue();
//                if (val.matches("\\d+\\.\\d+")) { // e.g., 537.9599
//                    try {
//                        BigDecimal bd = new BigDecimal(val);
//                        attr.setNodeValue(String.valueOf(bd.setScale(0, BigDecimal.ROUND_HALF_UP).intValue()));
//                    } catch (Exception ignore) {
//                    }
//                }
//            }
//        }
//
//        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
//            traverseAndFix(node.getChildNodes().item(i));
//        }
//    }
//}




