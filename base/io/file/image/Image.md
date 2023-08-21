# 绘图
## java(base)
```java
    public static final int OFFSET_HEIGHT = 20;

    public BufferedImage drawImage(String title){
        //新建模板图片
        BufferedImage bufferedImage = new BufferedImage(100, OFFSET_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        //绘制矩形背景
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, 100, 100);
        //绘制描述信息
        Font font = new Font(title, Font.PLAIN, 22);
        graphics.setColor(Color.black);
        graphics.setFont(font);
        graphics.drawString(title, 20, OFFSET_HEIGHT - 2);
        graphics.dispose();
        return bufferedImage;
    }

    /**
     * 为原图增加内容
     * @param source
     * @param title
     * @return
     */
    public BufferedImage drawImage(BufferedImage source,String title){
        BufferedImage bufferedImage = new BufferedImage(100, source.getHeight() + OFFSET_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        //绘制矩形背景
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, source.getWidth(), OFFSET_HEIGHT);
        //绘制描述信息
        Font font = new Font(title, Font.PLAIN, 22);
        graphics.setColor(Color.black);
        graphics.setFont(font);
        graphics.drawString(title, 20, OFFSET_HEIGHT - 2);
        // 绘制新图
        graphics.drawImage(source, 0, OFFSET_HEIGHT, source.getWidth(), source.getWidth(), null);
        graphics.dispose();
        return bufferedImage;
    }
```