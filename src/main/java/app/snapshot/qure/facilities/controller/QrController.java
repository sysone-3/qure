package app.snapshot.qure.facilities.controller;

import app.snapshot.qure.facilities.dto.FacilityTagDto;
import app.snapshot.qure.facilities.service.FacilitiesService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Controller
@RequiredArgsConstructor
public class QrController {
    private final FacilitiesService facilitiesService;

    @GetMapping("/qr/{tagId}.png")
    public void qr(@PathVariable int tagId, HttpServletResponse resp) throws Exception {
        FacilityTagDto tag = facilitiesService.findTagById(tagId);
        if (tag == null || !"Y".equalsIgnoreCase(tag.getActive())) {
            resp.sendError(404);
            return;
        }
        String payload = tag.getCode(); // {도메인}/mobile/main/{tagId}
        BitMatrix matrix = new QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, 480, 480);

        BufferedImage img = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < matrix.getWidth(); x++) {
            for (int y = 0; y < matrix.getHeight(); y++) {
                img.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }
        resp.setContentType("image/png");
        ImageIO.write(img, "png", resp.getOutputStream());
    }
}
