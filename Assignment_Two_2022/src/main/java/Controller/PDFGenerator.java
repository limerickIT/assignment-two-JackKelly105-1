/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Model.Beer;
import Model.Brewery;
import Model.Category;
import Model.Style;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Jack Kelly
 */
public class PDFGenerator {

    private static Logger logger = LoggerFactory.getLogger(PDFGenerator.class);

    public static ByteArrayInputStream customerPDFReport(Beer beer, Brewery brewery) throws IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 20, BaseColor.BLACK);
            Paragraph para = new Paragraph("Beer Brochure", font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);
            
            File g = new File("src/main/resources/static/assets/images/large/" +beer.getId() + ".jpg" + "src/main/resources/static/assets/images/large/noimage.jpg" );
//           
//            Image image1 = Image.getInstance(g.toURI().toString());
//
//            image1.setAlignment(Element.ALIGN_CENTER);
//
//            image1.scaleAbsolute(400, 250);
//            
//
//            document.add(image1);
            
            document.add(Chunk.NEWLINE);
            Font e = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            Paragraph c = new Paragraph("Name : " + beer.getName(), e);
            c.setAlignment(Element.ALIGN_CENTER);
            document.add(c);
            document.add(Chunk.NEWLINE);

            Font fce = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            Paragraph pc = new Paragraph("ABV : " + beer.getAbv(), fce);
            pc.setAlignment(Element.ALIGN_CENTER);
            document.add(pc);
            document.add(Chunk.NEWLINE);

            Font fonte = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 13, BaseColor.BLACK);
            Paragraph parae = new Paragraph("Description : " + beer.getDescription(), fonte);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(parae);
            document.add(Chunk.NEWLINE);

            document.add(Chunk.NEWLINE);
            Font pr = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.BLACK);
            Paragraph p = new Paragraph("Price : €" + beer.getSell_price(), pr);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            document.add(Chunk.NEWLINE);

            Font f = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            Paragraph b = new Paragraph("Brewery Name : " + brewery.getName(), f);
            b.setAlignment(Element.ALIGN_CENTER);
            document.add(b);
            document.add(Chunk.NEWLINE);
            
            Font a = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            Paragraph r = new Paragraph("Brewery Website : " + brewery.getWebsite(), a);
            r.setAlignment(Element.ALIGN_CENTER);
            document.add(r);
            document.add(Chunk.NEWLINE);
            
//            Font h = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
//            Paragraph v = new Paragraph("Beer category : " + category.getCat_name(), h);
//            v.setAlignment(Element.ALIGN_CENTER);
//            document.add(v);
//            document.add(Chunk.NEWLINE);
//            
//            Font k = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
//            Paragraph x = new Paragraph("Beer style : " + style.getStyle_name(), k);
//            x.setAlignment(Element.ALIGN_CENTER);
//            document.add(x);
//            document.add(Chunk.NEWLINE);
            
            
            document.close();
        } catch (DocumentException e) {
            logger.error(e.toString());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
