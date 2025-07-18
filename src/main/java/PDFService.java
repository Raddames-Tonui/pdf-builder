import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * A utility class for generating PDF documents from HTML templates
 * using the OpenHTMLtoPDF library.
 */
public class PDFService {

    /**
     * Generates the P9 report PDF using the corresponding HTML template.
     * Output path: out/Sample BNG P9 Report.pdf
     */
    public static void generateP9Report() throws IOException {
        generatePdfFromTemplate("p-nine-report.html", "src/out/Sample BNG P9 Report.pdf");
    }

    /**
     * Generates the Account Statement PDF using the corresponding HTML template.
     * Output path: out/Sample Account Statements Report.pdf
     */
    public static void generateAccountStatement() throws IOException {
        generatePdfFromTemplate("account-statements.html", "src/out/Sample Account Statements Report.pdf");
    }

    /**
     * General-purpose method that reads an HTML template and renders it into a PDF.
     *
     * @param templateName The name of the HTML template file (located in resources/templates/)
     * @param outputPath   The path (relative to project root) where the PDF will be saved
     */
    private static void generatePdfFromTemplate(String templateName, String outputPath) throws IOException {
        // Read the HTML content from the resources/templates directory
        String htmlContent = readResourceFile("templates/" + templateName);

        // Prepare the output file path and ensure the parent directory exists
        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs(); // Creates 'out/' if it doesn’t exist

        // Create the output stream to write the PDF file
        try (OutputStream os = new FileOutputStream(outputFile)) {
            // Create the OpenHTMLtoPDF builder
            PdfRendererBuilder builder = new PdfRendererBuilder();

            // Optional: use optimized rendering mode for faster performance
            builder.useFastMode();

            // Supply HTML content to be converted
            builder.withHtmlContent(htmlContent, null);

            // Set the destination stream where the PDF will be written
            builder.toStream(os);

            // Set the page size to A4 landscape (11.69in x 8.27in)
            // This is the key part that ensures landscape orientation
            builder.useDefaultPageSize(11.69f, 8.27f, PdfRendererBuilder.PageSizeUnits.INCHES);


            // Start the PDF generation process
            builder.run();

            System.out.println("PDF generated at: " + outputPath);
        }
    }

    /**
     * Reads a file from the classpath (usually from src/main/resources).
     *
     * @param resourcePath Relative path inside resources (e.g. templates/p-nine-report.html)
     * @return The file's content as a UTF-8 encoded string
     * @throws IOException If the file is not found or reading fails
     */
    private static String readResourceFile(String resourcePath) throws IOException {
        // Open an input stream to the resource file
        try (InputStream is = PDFService.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException("Template not found: " + resourcePath);
            }

            // Read all bytes and convert to String using UTF-8 encoding
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
