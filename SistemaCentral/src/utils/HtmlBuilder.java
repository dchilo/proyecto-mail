/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author teito
 */
public class HtmlBuilder {

    private static final String HTML_OPEN = "<html>";
    private static final String HTML_CLOSE = "</html>";
    private static final String BODY_OPEN = "<body>";
    private static final String BODY_CLOSE = "</body>";

    public static String generateTableHelp(String title, String explanation, String[] headers, List<String[]> data) {
        // Add the image URL
        String imageUrl = "https://i.postimg.cc/qqx8Mrrz/logo.jpg";

        // Create the logo HTML
        String logoHtml = "<img src='" + imageUrl + "' alt='Logo' style='max-width: 400px; max-height: 200px; margin-bottom: 10px;'>";

        // Create table headers HTML
        String tableHeadersHtml = "";
        for (String header : headers) {
            tableHeadersHtml += "<th style=\"border: 1px solid #ddd; padding: 12px; background-color: #3498db; color: #fff;\">" + header + "</th>";
        }

        // Create table body HTML
        String tableBodyHtml = "";
        for (String[] element : data) {
            tableBodyHtml += "<tr style=\"border: 1px solid #ddd;\">";
            for (String value : element) {
                tableBodyHtml += "<td style=\"border: 1px solid #ddd; padding: 12px;\">" + value + "</td>";
            }
            tableBodyHtml += "</tr>";
        }

        // Create the complete HTML
        String html = 
            "<div style=\"max-width: 800px; margin: 0 auto; padding: 20px; background-color: #f8f8f8; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center;\">" +
                logoHtml +
                "<h2 style=\"color: #3498db; margin-bottom: 10px;\">" + title + "</h2>" +
                "<p style=\"font-size: 16px; color: #555; margin-bottom: 20px;\">" + explanation + "</p>" +
                "<table style=\"width: 100%; border-collapse: collapse;\">" +
                    "<thead style=\"background-color: #f2f2f2;\">" + tableHeadersHtml + "</thead>" +
                    "<tbody>" + tableBodyHtml + "</tbody>" +
                "</table>" +
            "</div>";

        return insertInHtml(html);
    }

    public static String generateTable(String title, String[] headers, List<String[]> data) {
        String tableHeadersHtml = "";
        for (String header : headers) {
            tableHeadersHtml += "<th style=\"border: 1px solid #ddd; padding: 12px; background-color: #3498db; color: #fff;\">" + header + "</th>";
        }

        String tableBodyHtml = "";
        int rowNum = 0;
        for (String[] element : data) {
            // Alternar colores de fondo para filas
            String rowColor = (rowNum % 2 == 0) ? "#f9f9f9" : "#ffffff";
            tableBodyHtml += "<tr style=\"border: 1px solid #ddd; background-color: " + rowColor + ";\">";
            for (String value : element) {
                tableBodyHtml += "<td style=\"border: 1px solid #ddd; padding: 12px;\">" + value + "</td>";
            }
            tableBodyHtml += "</tr>";
            rowNum++;
        }

        String logoHtml = "<img src=\"" + "https://i.postimg.cc/qqx8Mrrz/logo.jpg" + "\" alt=\"Company Logo\" style=\"max-width: 400px; max-height: 200px;\">";

        String html = 
            "<div style=\"max-width: 800px; margin: 0 auto; padding: 20px; background-color: #3498db; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">" +
                "<center>" + logoHtml + "</center>" +
                "<center><h2 style=\"color: #fff; margin-bottom: 10px;\">" + title + "</h2></center>" +
                "<table style=\"width: 100%; border-collapse: collapse;\">" +
                    "<thead style=\"background-color: #2c3e50; color: #fff;\">" + tableHeadersHtml + "</thead>" +
                    "<tbody>" + tableBodyHtml + "</tbody>" +
                "</table>" +
            "</div>";

        return insertInHtml(html);
    }


    public static String generateText(String[] args) {
        String imageUrl = "https://i.postimg.cc/qqx8Mrrz/logo.jpg";
        String accumulatedHtml = "<div style=\"border: 2px solid #e74c3c; padding: 20px; background-color: #f9d9d9; color: #e74c3c; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">"
                + "<center><img src=\"" + imageUrl + "\" alt=\"Logo\" style=\"width: 400px; height: 200px;\">"
                + "<h2 style=\"color: #e74c3c; font-size: 24px;\">Informe de petición</h2></center>";

        for (int i = 0; i < args.length; i++) {
            accumulatedHtml += "<p style=\"color: #e74c3c;\">" + args[i] + "</p>";
        }

        accumulatedHtml += "</div>";
        return insertInHtml(accumulatedHtml);
    }

    public static String generateTextPago(String[] args) {
        System.out.println(args[0] + " " + args[1] + " " + args[2] + " " + args[3]);
        String imageUrl = "https://i.postimg.cc/qqx8Mrrz/logo.jpg";
        String qrCodeUrl = "https://i.postimg.cc/pVDqGSwH/Whats-App-Image-2023-12-04-at-20-20-35-23097198.jpg";
    
        String accumulatedHtml = "<div style=\"border: 2px solid #3498db; padding: 20px; background-color: #d4e6f1; color: #3498db; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">"
                + "<center><img src=\"" + imageUrl + "\" alt=\"Logo\" style=\"width: 400px; height: 200px;\">"
                + "<h2 style=\"color: #3498db; font-size: 24px;\">Informe de pago</h2></center>";
    
        // Agrega información sobre el tipo de pago y el monto
        if (args.length >= 2) {
            accumulatedHtml += "<p style=\"color: #3498db;\">Tipo de pago: " + args[0] + "</p>";
            accumulatedHtml += "<p style=\"color: #3498db;\">Monto: $" + args[1] + "</p>";
        }
    
        // Agrega el código QR al HTML
        accumulatedHtml += "<center><img src=\"" + qrCodeUrl + "\" alt=\"QR Code\" style=\"width: 200px; height: 200px;\"></center>";
    
        accumulatedHtml += "</div>";
        return insertInHtml(accumulatedHtml);
    }

    public static String generateCharBar(String[] args) {
        String acumulative = "<center><h2>" + args[0] + "</h2></center>";
        for (int i = 1; i < args.length; i++) {
            acumulative += "<center><h3>" + args[i] + "</h3></center>";
        }

        String chartScript = "<script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n"
                + "<canvas id=\"myChart\"></canvas>\n"
                + "<script>\n"
                + "var ctx = document.getElementById('myChart').getContext('2d');\n"
                + "new Chart(ctx, {\n"
                + "type: 'bar',\n"
                + "data: {\n"
                + "labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],\n"
                + "datasets: [{\n"
                + "label: '# of Votes',\n"
                + "data: [12, 19, 3, 5, 2, 3],\n"
                + "borderWidth: 1\n"
                + "}]\n"
                + "},\n"
                + "options: {\n"
                + "scales: {\n"
                + "y: {\n"
                + "beginAtZero: true\n"
                + "}\n"
                + "}\n"
                + "}\n"
                + "});\n"
                + "</script>";

        return insertInHtml(acumulative);
    }

    public static String generateChartPieImage() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Product A", 10);
        dataset.setValue("Product B", 20);
        dataset.setValue("Product C", 30);

        JFreeChart chart = ChartFactory.createPieChart("Chart Title", dataset);

        // Guarda la imagen del gráfico en un archivo temporal
        try {
            Path imagePath = Files.createTempFile("chart", ".png");
            ChartUtils.saveChartAsPNG(imagePath.toFile(), chart, 600, 400);

            // Lee el archivo de imagen en bytes
            byte[] imageBytes = Files.readAllBytes(imagePath);

            // Convierte los bytes de la imagen a Base64
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
            
            System.out.println("Imagen del gráfico en formato Base64: " + imageBase64);
            return imageBase64;
            // Borra el archivo temporal
//            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String generateTableForSimpleData(String title, String[] headers, String[] data) {
        String acumulative = "";

        for (int i = 0; i < headers.length; i++) {
            acumulative
                    += "<tr>"
                    + "<td>" + headers[i] + "</td>"
                    + "<td>" + data[i] + "</td>"
                    + "</tr>";
        }

        String table
                = "<div align=\"center\">\n"
                + "<h2>" + title + "<br>\n"
                + "</h2>\n"
                + "</div>\n"
                + "<table width=\"250\"  border=\"1\" align=\"center\" cellpadding=\"2\" cellspacing=\"2\" bgcolor=\"#CCCCCC\">\n"
                + acumulative
                + "</table>";

        return insertInHtml(table);
    }

    public static String generateGraficaPago(String title, List<String[]> data) {
        String encabezados = "";
        String valores = "";
        String encabezados2 = "";

        String[] nombresMeses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        for (String[] element : data) {
            // Asegúrate de que el número del mes sea válido
            int numeroMes = Integer.parseInt(element[0]);
            if (numeroMes >= 1 && numeroMes <= 12) {
                String nombreMes = nombresMeses[numeroMes - 1];

                // Usa el segundo elemento del array como monto total ganado
                String montoGanado = element[1];

                valores += montoGanado + ",";
                encabezados += nombreMes + "|";
                encabezados2 += nombreMes + "%28" + montoGanado + "+%29|";
            } else {
                // Manejo de error si el número del mes no es válido
                // Puedes agregar aquí el código necesario para manejar la situación.
            }
        }

        valores = valores.substring(0, valores.length() - 1);
        encabezados = encabezados.substring(0, encabezados.length() - 1);
        encabezados2 = encabezados2.substring(0, encabezados2.length() - 1);

        String graficas = "<h2> " + title + "</h2>"
                + "<div>"
                + "<img src=\"http://chart.apis.google.com/chart?chs=600x200&cht=p&chd=t:" + valores + "&chl=" + encabezados + "\" width=\"600\" height=\"200\">"
                + "</div>\n"
                + "<div>"
                + "<img src=\"http://chart.apis.google.com/chart?chs=600x200&cht=bhg&chco=e5f867|aaaaaa|596605&chd=t:" + valores + "&chdl=" + encabezados2 + "\" width=\"600\" height=\"200\">"
                + "</div>\n";

        return insertInHtml(graficas);
    }
 
    public static String generateGrafica(String title, List<String[]> data) {
        String encabezados = "";
        String valores = "";
        String encabezados2 = "";
        for (String[] element : data) {

            valores += element[1] + ",";

            encabezados += element[0] + "|";
            encabezados2 += element[0] + "%28" + element[1] + "+veces%29|";
        }
        valores = valores.substring(0, valores.length() - 1);
        encabezados = encabezados.substring(0, encabezados.length() - 1);
        encabezados2 = encabezados2.substring(0, encabezados2.length() - 1);
        String graficas = "<h2> " + title + "</h2>"
                + "<div>"
                + "<img src=\"http://chart.apis.google.com/chart?chs=600x200&cht=p&chd=t:" + valores + "&chl=" + encabezados + "\" width=\"600\" height=\"200\">"
                + "</div>\n"
                + "<div>"                
                + "<img src=\"http://chart.apis.google.com/chart?chs=600x200&cht=bhg&chco=e5f867|aaaaaa|596605&chd=t:" + valores + "&chdl=" + encabezados2 + "\" width=\"600\" height=\"200\">"
                + "</div>\n";
        //String body=BalnearioHTML(graficas);
        return insertInHtml(graficas);
    }

    private static String insertInHtml(String data) {
        return HTML_OPEN + BODY_OPEN + data + BODY_CLOSE + HTML_CLOSE;
    }
}
