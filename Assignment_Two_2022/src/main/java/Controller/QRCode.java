/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Brewery;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jack Kelly
 */
public class QRCode {
    
        public static BufferedImage generatedQRCode(Brewery urlText) throws Exception{
    
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(" Brewery Name :"+urlText.getName() + "PhoneNumber: "+ urlText.getPhone() + "Address 1: "+ urlText.getAddress1()+ "Address 2: "+ urlText.getAddress2() + "City: "+ urlText.getCity()+ "State: "+ urlText.getState()+ "Code: "+ urlText.getCode()+ "Email: "+ urlText.getEmail()+ "Wesite: "+ urlText.getWebsite(), BarcodeFormat.QR_CODE, 200,200);
    
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
