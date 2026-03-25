# 通用扩展
## 坐标轴不自动缩放
**基本原理**：以数据长度作为有序值代替数据本身，然后将数据长度对应数据值，从而实现坐标轴自动缩放
```py
plt.bar(range(len(x_data)),y_data)
plt.xticks(range(len(x_data)),x_data) # 设置x轴刻度
```
# 一个坐标系多图形
* 共用一个坐标系，但不同图形
  * 多次调用绘制方法`plt.plot`、`plt.bar`等
```py
plt.figure(figsize=(10, 5),dpi=100) # 通过一个坐标系
# 绘制多个图形
plt.plot(x, y_d1,color='r',label='label1') # color = 'r'
plt.plot(x, y_d2,color='b',label='label2')
# ...
plt.show()
```
* 亦可使用多坐标系创建一个坐标系
```py
fig,axes = plt.subplots(figsize=(20, 8),dpi=50) # nrows,ncols默认1
```

# 多坐标系
* 将一个画布分割多个区域，每个区域代表一个坐标系
  * 使用`plt.subplots`创建画布，并返回`fig`、`ax`对象，可指定坐标系个数`nrows`、`ncols`
```py
fig,axes = plt.subplots(nrows=2,ncols=2,figsize=(20, 8),dpi=50) 

```

# 水平柱状图`barh`
* 使用`axes.barh`绘制水平柱状图
  * `axes.bar_label` 为条状区段添加label
## 绘制简易水平柱状图
案例要求：
1. 在y轴上显示条柱
2. 每个条柱颜色不同
3. 每个条柱宽度根据数值大小不同
```py
import matplotlib.pyplot as plt
import numpy as np

labels = ['Question 1', 'Question 2', 'Question 3', 'Question 4', 'Question 5', 'Question 6']
data = np.array([10, 15, 17, 32, 26, 28])
label_colors = plt.colormaps['RdYlGn'](np.linspace(0.15, 0.85, data.shape[0]))

fig, ax = plt.subplots(figsize=(9.2, 5))
ax.set_xlim(0, np.sum(data, axis=0).max()) # 限定x边界，0~max，axis=0代表每行最大值

rects = ax.barh(y=labels, width=data, color=category_colors) # 绘制条柱图,# 返回BarContainer容器
ax.bar_label(rects, label_type='center') # 为条状区段添加label，label_type 数值显示位置，center位于条带居中，edge位于条带边缘(右侧)
plt.show()
```

## 绘制水平堆叠柱状图
案例要求:
1. 在y轴上显示堆叠柱状图
2. 水平堆叠内容做条带分段
3. 水平堆叠每个条带分段颜色不同

案例实现：
* 将数据值进行**累加**`ndarray.cumsum`，从而计算条状图中每个分段的**结束位置**。
  * 同理`start`起时位置即**累加值减去当前值**
```py
import matplotlib.pyplot as plt
import numpy as np

category_names = ['Strongly disagree', 'Disagree',
                  'Neither agree nor disagree', 'Agree', 'Strongly agree']
results = {
    'Question 1': [10, 15, 17, 32, 26],
    'Question 2': [26, 22, 29, 10, 13],
    'Question 3': [35, 37, 7, 2, 19],
    'Question 4': [32, 11, 9, 15, 33],
    'Question 5': [21, 29, 5, 5, 40],
    'Question 6': [8, 19, 5, 30, 38]
}

labels = list(results.keys())
data = np.array(list(results.values()))
data_cum = data.cumsum(axis=1) # 通过cumsum累加方式确定每个类别的结束位置
category_colors = plt.colormaps['RdYlGn'](np.linspace(0.15, 0.85, data.shape[1]))

fig, ax = plt.subplots(figsize=(9.2, 5))
ax.invert_yaxis() # 将y轴数值反转，例如1-6，改为6-1
ax.xaxis.set_visible(False) # 不显示x轴
ax.set_xlim(0, np.sum(data, axis=1).max()) # 限定x边界，0~max，axis=1代表每列最大值

for i, (colname, color) in enumerate(zip(category_names, category_colors)):
    widths = data[:, i]
    starts = data_cum[:, i] - widths
    print(data[:, i],data_cum[:, i],starts)
    # start 换一个思路：第一个肯定都是[0,0,0,0,0]，下一个位置是前一个的data_cum对应值，依次类推
    rects = ax.barh(labels, widths, left=starts, height=0.5,label=colname, color=color) # 绘制水平柱状图

    r, g, b, _ = color
    text_color = 'white' if r * g * b < 0.5 else 'darkgrey'
    ax.bar_label(rects, label_type='center', color=text_color) # 水平柱状图的标签(数值)
ax.legend(ncols=len(category_names), bbox_to_anchor=(0, 1),loc='lower left', fontsize='small') # 添加图例

plt.show()
```