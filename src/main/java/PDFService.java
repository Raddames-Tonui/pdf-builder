import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

/**
 * A utility class for generating PDF documents from HTML templates
 * using the OpenHTMLtoPDF library.
 */
public class PDFService {

    private static final DecimalFormat df = new DecimalFormat("#,##0.00");

    /**
     * Generates the P9 report PDF using the corresponding HTML template.
     */
    public static void generateP9Report() throws IOException {
        generateStaticPdfFromTemplate("p-nine-report.html", "src/out/Sample BNG P9 Report.pdf");
    }

    /**
     * Generates the Account Statement PDF from a dynamic HTML template + CSV data.
     */
    public static void generateAccountStatement() throws IOException {
        // Load HTML template
        String html = loadResourceAsString("templates/account-statements.html");

        // Read CSV and compute financials
        List<String> rows = new ArrayList<>();
        BigDecimal totalCredits = BigDecimal.ZERO;
        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal openingBalance = null;
        BigDecimal closingBalance = BigDecimal.ZERO;

        try (InputStream is = PDFService.class.getClassLoader().getResourceAsStream("assets/account_statement_extracted.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8))) {

            String line;
            boolean header = true;

            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                String date = parts[0];
                String docNo = parts[1];
                String description = parts[2];
                BigDecimal credit = parse(parts[3]);
                BigDecimal debit = parse(parts[4]);
                BigDecimal balance = parse(parts[5]);

                if (openingBalance == null) openingBalance = balance;

                totalCredits = totalCredits.add(credit);
                totalDebits = totalDebits.add(debit);
                closingBalance = balance;

                rows.add(transactionRow(date, docNo, description, credit, debit, balance));
            }
        }

        // Replace placeholders in template with actual data
        html = html.replace("{{CLIENT_NAME}}", "Mr. John Juma")
                .replace("{{PHONE}}", "+254722368794")
                .replace("{{STATEMENT_PERIOD}}", "1 Jan 2022 to 2 June 2022")
                .replace("{{ACCOUNT_NAME}}", "FOSA Savings Account")
                .replace("{{ACCOUNT_NUMBER}}", "5050123278000")
                .replace("{{DATE_ISSUED}}", "18 July 2025")
                .replace("{{OPENING_BALANCE}}", format(openingBalance))
                .replace("{{CREDITS}}", "<span class='green'>" + format(totalCredits) + "</span>")
                .replace("{{DEBITS}}", "<span class='red'>" + format(totalDebits) + "</span>")
                .replace("{{CLOSING_BALANCE}}", format(closingBalance))
                .replace("{{TRANSACTIONS}}", String.join("\n", rows));

        // Generate final PDF
        generatePdfFromHtml(html, "src/out/Sample Account Statements Report.pdf");
    }

    /**
     * For simple, static HTML-based PDFs (no dynamic data).
     */
    private static void generateStaticPdfFromTemplate(String templateName, String outputPath) throws IOException {
        String html = loadResourceAsString("templates/" + templateName);
        generatePdfFromHtml(html, outputPath);
    }

    /**
     * Converts HTML content to a PDF and saves it to disk.
     */
    private static void generatePdfFromHtml(String htmlContent, String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();

        try (OutputStream os = new FileOutputStream(outputFile)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.useSVGDrawer(new BatikSVGDrawer());

            // Make image paths (e.g. assets/logo.png) resolve from resources
            builder.useUriResolver((baseUri, uri) -> {
                if (uri.startsWith("assets/")) {
                    return PDFService.class.getClassLoader().getResource(uri).toString();
                }
                return uri;
            });

            builder.withHtmlContent(htmlContent, null);
            builder.toStream(os);

            // Set the page size to A4 landscape (11.69in x 8.27in)
            // This is the key part that ensures landscape orientation
            builder.useDefaultPageSize(11.69f, 8.27f, PdfRendererBuilder.PageSizeUnits.INCHES);
            builder.run();

            System.out.println("✅ PDF generated at: " + outputPath);
        }
    }

    /**
     * Loads a resource file (like an HTML template) into a string.
     */
    private static String loadResourceAsString(String path) throws IOException {
        try (InputStream is = PDFService.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) throw new FileNotFoundException("Resource not found: " + path);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Parses a numeric value from CSV, removing commas and handling blanks safely.
     */
    private static BigDecimal parse(String value) {
        try {
            return new BigDecimal(value.trim().replace(",", ""));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Formats a number into "#,##0.00" format for rendering.
     */
    private static String format(BigDecimal value) {
        return df.format(value != null ? value : BigDecimal.ZERO);
    }

    /**
     * Generates a table row for one transaction in HTML.
     */
    private static String transactionRow(String date, String docNo, String desc, BigDecimal credit, BigDecimal debit, BigDecimal balance) {
        return String.format("""
            <tr>
              <td class="date">%s</td>
              <td class="docno">%s</td>
              <td class="description">%s</td>
              <td class="credit">%s</td>
              <td class="debit">%s</td>
              <td>%s</td>
            </tr>
        """, date, docNo, desc, format(credit), format(debit), format(balance));
    }
}
