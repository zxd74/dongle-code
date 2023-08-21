# 二维码生成

## java(com.google.zxing)
```xml
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>core</artifactId>
    <version>3.4.0</version>
</dependency>
```
```java
    private static final int QRCODE_HEIGHT = 100;
    private static final int QRCODE_WIDTH = 100;
    //FRONT_COLOR：二维码前景色，0x000000 表示黑色
    private static final int FRONT_COLOR = 0x000000;
    //BACKGROUND_COLOR：二维码背景色，0xFFFFFF 表示白色
    //演示用 16 进制表示，和前端页面 CSS 的取色是一样的，注意前后景颜色应该对比明显，如常见的黑白
    private static final int BACKGROUND_COLOR = 0xFFFFFF;

    public BufferedImage buildQrCode(String content) throws WriterException {
        // 使用EncodeHintType设定额外配置，详情见com.google.zxing.core.Writer
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
        //
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT,hints);
        BufferedImage bufferedImage =  new BufferedImage(QRCODE_WIDTH, QRCODE_HEIGHT,BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < QRCODE_WIDTH; x++) {
            for (int y = 0; y < QRCODE_HEIGHT; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? FRONT_COLOR : BACKGROUND_COLOR);
            }
        }

        return bufferedImage;
    }
```