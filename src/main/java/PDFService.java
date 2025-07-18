
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PDFService {

    public static void generateP9Report() throws IOException {
        generatePdfFromTemplate("p-nine-report.html", "out/Sample BNG P9 Report.pdf");
    }

    public static void generateAccountStatement() throws IOException {
        generatePdfFromTemplate("account-statements.html", "out/Sample Account Statements Report.pdf");
    }

    private static void generatePdfFromTemplate(String templateName, String outputPath) throws IOException {
        String htmlContent = readResourceFile("" + templateName);

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs(); // Ensure 'out/' exists

        try (OutputStream os = new FileOutputStream(outputFile)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(os);
            builder.run();
            System.out.println("PDF generated at: " + outputPath);
        }
    }

    private static String readResourceFile(String resourcePath) throws IOException {
        try (InputStream is = PDFService.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException("Template not found: " + resourcePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
