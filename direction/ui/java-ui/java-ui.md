# 尺寸管理
* 初始大小`setSize`
* 最终大小`setPreferredSize`

## 内边距实现方案
* 通过Border实现
  * 使用`EmptyBorder`设置内边框`BorderFactory.createEmptyBorder(0, 20, 0, 0)`
  * 使用`CompoundBorder`组合边框`BorderFactory.createCompoundBorder(border1,border2,...)`,第一个最外层，依次内推
* 通过布局管理器设置
  * `GridBagLayout`通过`Insets`实现
    ```java
    gbc.insets = new Insets(0, 20, 0, 0); // 左内边距20px
    gbc.anchor = GridBagConstraints.WEST;  // 左对齐
    ```
  * `BoxLayout`
    ```java
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(Box.createHorizontalStrut(20)); // 左偏移20px
    ```
* 自定义 JPanel 重写 paintComponent
```java
JPanel panel = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        Insets insets = new Insets(0, 20, 0, 0); // 左内边距20px
        setInsets(insets);
        super.paintComponent(g);
    }
};
```
* 使用空透明组件作为占位符
```java
JPanel panel = new JPanel(new BorderLayout());
panel.add(new JPanel() {{
    setPreferredSize(new Dimension(20, 0)); // 20px宽度的占位符
    setOpaque(false);
}}, BorderLayout.WEST);
panel.add(yourComponent, BorderLayout.CENTER);
```
* **最佳实践建议**
  * **简单场景**：使用 setBorder 方法最简单直接
  * **需要保留原有边框**：使用 CompoundBorder
  * **精确控制布局**：使用 GridBagLayout 的 insets 属性
  * **动态调整**：可以考虑覆盖 paintComponent 方法
  * **复杂布局**：使用空组件作为占位符更灵活


## Border管理
### 边框控制单方向显隐
* `MatteBorder`:`BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK)`
* **自定义** `LineBorder`
```java
public class MyLineBorder extends LineBorder {
    public MyLineBorder(Color color) {
        super(color);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getLineColor());
        g2.drawLine(x, y, x + width - 1, y); // 只画顶部线条
    }
}
```
* `CompoundBorder` 组合边框
* `TitledBorder` 简化实现`((TitledBorder)titledBorder).setTitleJustification(TitledBorder.LEFT);`
* **完全自定义** `Border`
```java
class TopLineBorder extends AbstractBorder {
    private final Color color;
    private final int thickness;
    
    public TopLineBorder(Color color, int thickness) {
        this.color = color;
        this.thickness = thickness;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(color);
        for (int i = 0; i < thickness; i++) {
            g2.drawLine(x, y + i, x + width - 1, y + i);
        }
        g2.dispose();
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, 0, 0, 0);
    }
}
```

**最佳实践建议**
* **简单需求**：使用 `MatteBorder` 最简单直接
* **复杂样式**：自定义 `Border` 类提供最大灵活性
* **性能考虑**：避免在 `paintBorder` 方法中创建新对象
* **UI一致性**：使用系统的默认边框颜色 `UIManager.getColor("Separator.foreground")`

### 自定义线条
```java
// 虚线边框
Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, 
                              BasicStroke.JOIN_BEVEL, 0, 
                              new float[]{3}, 0);
panel.setBorder(new TopLineBorder(Color.BLACK, 1) {
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(dashed);
        super.paintBorder(c, g2, x, y, width, height);
        g2.dispose();
    }
});
```
