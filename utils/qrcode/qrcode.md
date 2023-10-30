# zxing/core
1. 导入依赖
```xml
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>core</artifactId>
    <version>3.4.1</version>
</dependency>
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>javase</artifactId>
    <version>3.4.1</version>
</dependency>
```
2. 生成/读取二维码
```java
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

public class QrCodeUtils {

    public static void main(String[] args) throws IOException {
        String content = "123456";
        generateQrCode(content, "D:\\qrcode.png");
    }

    public static void generateQrCode(String content, String path) throws IOException{
        BufferedImage image = generateQrCode(content);
        ImageIO.write(image, "png", new File(path));
    }

    public static BufferedImage generateQrCode(String content) {
        // 指定生成图片类型
        BarcodeFormat format = BarcodeFormat.QR_CODE;
        // 指定图片尺寸
        int width = 300;
        int height = 300;

        // 额外参数，用户替换默认配置
        final Map<EncodeHintType,Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.AZTEC_LAYERS, -1);

        // 生成图片数据源
        BitMatrix bitMatrix = new AztecWriter().encode(content, format, width, height, hints);
        // 生成二进制图片数据，并配置图片配置(如颜色)
        return MatrixToImageWriter.toBufferedImage(bitMatrix,new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF));
    }
    

    public static String readQrCode(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("not read image file");
        }
        return readQrCode(image);
    }

    public static String readQrCode(BufferedImage image) {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        try{
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            return result.getText();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}

```