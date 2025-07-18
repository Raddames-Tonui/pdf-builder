
# OpenHTMLtoPDF Java PDF Generator

## 📘 Overview

**OpenHTMLtoPDF** is a modern, Java-based library for rendering well-structured HTML and CSS content into PDF documents. It is a more actively maintained successor to the Flying Saucer project and integrates well with any JVM-based application.

Use cases include:

* Generating reports (invoices, receipts, certificates)
* Exporting styled HTML content to PDF
* Automating document generation from templates

---

## 🏗️ Project Structure

```
├── sample-pdfs/                    # Original PDF files for reference
│   ├── Sample Account Statements Report.pdf
│   └── Sample BNG P9 Report.pdf
├── src/
│   └── main/
│       └── java/
│           └── Main.java          # Main application entry point
│       └── resources/
│           ├── account-statements.html    # HTML template for account statements
│           └── p-nine-report.html         # HTML template for P9 report
├── out/
│   └── Sample BNG P9 Report.pdf   # Generated PDF output
└── test/                          # Test files (if any)
```


## 💻 Example: Simple PDF Generator in Java

```java
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PDFGenerator {
    public static void main(String[] args) {
        try {
            // Read HTML content from resources
            String template = readResourceFile("templates/report.html");

            // Create output stream for the generated PDF
            try (OutputStream os = new FileOutputStream("kra_output.pdf")) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(template, null);
                builder.toStream(os);
                builder.run();
                System.out.println("PDF generated successfully.");
            }
        } catch (IOException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }

    private static String readResourceFile(String resourcePath) throws IOException {
        try (InputStream is = PDFGenerator.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException("Template not found: " + resourcePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
```

---

## 📦 Maven Dependencies

Make sure to include these dependencies in your `pom.xml`:

```xml
<dependencies>
    <!-- Core engine: Required in all setups -->
    <dependency>
        <groupId>com.openhtmltopdf</groupId>
        <artifactId>openhtmltopdf-core</artifactId>
        <version>1.0.10</version>
    </dependency>

    <!-- PDF rendering backend using Apache PDFBox -->
    <dependency>
        <groupId>com.openhtmltopdf</groupId>
        <artifactId>openhtmltopdf-pdfbox</artifactId>
        <version>1.0.10</version>
    </dependency>

    <!-- Support for RTL languages and BiDi text -->
    <dependency>
        <groupId>com.openhtmltopdf</groupId>
        <artifactId>openhtmltopdf-rtl-support</artifactId>
        <version>1.0.10</version>
    </dependency>

    <!-- Logging via SLF4J -->
    <dependency>
        <groupId>com.openhtmltopdf</groupId>
        <artifactId>openhtmltopdf-slf4j</artifactId>
        <version>1.0.10</version>
    </dependency>

    <!-- Optional: SVG image rendering support -->
    <!-- <dependency>
        <groupId>com.openhtmltopdf</groupId>
        <artifactId>openhtmltopdf-svg-support</artifactId>
        <version>1.0.10</version>
    </dependency> -->

    <!-- Optional: MathML (math equations) rendering -->
    <dependency>
        <groupId>com.openhtmltopdf</groupId>
        <artifactId>openhtmltopdf-mathml-support</artifactId>
        <version>1.0.10</version>
    </dependency>

    <!-- Apache Commons Text utilities -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-text</artifactId>
        <version>1.10.0</version>
    </dependency>
</dependencies>
```

---

## 🧠 Key Concepts

| Feature                  | Description                                         |
| ------------------------ | --------------------------------------------------- |
| `useFastMode()`          | Enables optimized performance for large documents.  |
| `withHtmlContent()`      | Accepts raw HTML as string input.                   |
| `toStream(OutputStream)` | Sets the destination for the final PDF.             |
| `run()`                  | Starts the actual rendering and writing of the PDF. |

---

## ⚖️ Comparison with Other Tools

| Tool          | Language | HTML/CSS Support | Headless Browser | Output Quality |
| ------------- | -------- | ---------------- | ---------------- | -------------- |
| OpenHTMLtoPDF | Java     | HTML5 + CSS2.1   | ❌                | ✅ Good         |
| wkhtmltopdf   | C++      | HTML5 + CSS3     | ❌                | ✅ Great        |
| Puppeteer     | Node.js  | Full (Chrome)    | ✅                | 🌟 Excellent   |
| WeasyPrint    | Python   | HTML5 + CSS3     | ❌                | ✅ Good         |

---

## 🧩 Tips & Best Practices

* Use valid XHTML or well-formed HTML.
* Avoid JavaScript — it is not supported.
* Use inline or internal CSS (no external file fetching).
* Define `@page` rules in CSS for margin control.
* Embed fonts for reliable rendering.

---

## 🔚 Conclusion

OpenHTMLtoPDF is the ideal tool for Java developers needing precise and styled PDF output from HTML templates — without relying on external browser engines. It's fast, clean, and works well in enterprise Java environments.

> Looking to extend? Try integrating Freemarker, Thymeleaf, or Handlebars for dynamic HTML template rendering before passing content to OpenHTMLtoPDF.
