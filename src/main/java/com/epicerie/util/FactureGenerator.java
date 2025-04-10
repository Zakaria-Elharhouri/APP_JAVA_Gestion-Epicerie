package com.epicerie.util;

import com.epicerie.model.LigneVente;
import com.epicerie.model.Vente;
import java.awt.*;
import java.awt.print.*;
import java.text.SimpleDateFormat;

public class FactureGenerator implements Printable {
    
    private Vente vente;
    private Font headerFont, normalFont, boldFont, footerFont;
    
    public FactureGenerator(Vente vente) {
        this.vente = vente;
        this.headerFont = new Font("Arial", Font.BOLD, 18);
        this.normalFont = new Font("Arial", Font.PLAIN, 12);
        this.boldFont = new Font("Arial", Font.BOLD, 12);
        this.footerFont = new Font("Arial", Font.ITALIC, 10);
    }
    
    public void genererEtImprimer() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                System.err.println("Erreur d'impression: " + e.getMessage());
            }
        }
    }
    
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) return NO_SUCH_PAGE;
        
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        int y = 30;
        
        
        g2d.setColor(new Color(0, 120, 215));
        g2d.fillRect(0, 0, (int)pageFormat.getImageableWidth(), 60);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(headerFont);
        g2d.drawString("ÉPICERIE DU QUARTIER", 50, y);
        y += 20;
        g2d.drawString("FACTURE N°" + vente.getId(), 50, y);
        y += 30;
        
        
        g2d.setColor(Color.BLACK);
        g2d.setFont(normalFont);
        g2d.drawString("Date: " + sdf.format(vente.getDate()), 50, y);
        y += 20;
        
        g2d.drawLine(50, y, 450, y);
        y += 20;
        

        g2d.setFont(boldFont);
        g2d.drawString("PRODUIT", 50, y);
        g2d.drawString("QTE", 250, y);
        g2d.drawString("PRIX", 300, y);
        g2d.drawString("TOTAL", 380, y);
        y += 5;
        
        // Ligne séparatrice
        g2d.drawLine(50, y, 450, y);
        y += 15;
        
        
        g2d.setFont(normalFont);
        boolean alternate = false;
        for (LigneVente ligne : vente.getLignesVente()) {
            if (alternate) {
                g2d.setColor(new Color(240, 240, 240));
                g2d.fillRect(50, y-15, 400, 20);
            }
            g2d.setColor(Color.BLACK);
            
            g2d.drawString(ligne.getProduit().getNom(), 50, y);
            g2d.drawString(String.valueOf(ligne.getQuantite()), 250, y);
            g2d.drawString(String.format("%.2f DH", ligne.getProduit().getPrix()), 300, y);
            g2d.drawString(String.format("%.2f DH", ligne.getSousTotal()), 380, y);
            
            y += 20;
            alternate = !alternate;
        }
        
        g2d.drawLine(50, y, 450, y);
        y += 20;
        
        g2d.setFont(boldFont);
        g2d.drawString("TOTAL À PAYER: " + String.format("%.2f DH", vente.getMontantTotal()), 300, y);
        y += 40;
        
        g2d.setFont(footerFont);
        g2d.drawString("Merci de votre visite et à bientôt!", 50, y);
        y += 15;
        g2d.drawString("Tél: 05 00 00 00 00", 50, y);
        
        return PAGE_EXISTS;
    }
}