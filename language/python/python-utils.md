# FileUtils
```python
# 读取文件
def read_file(path):
    with open(path, 'r') as f:
        return f.read()

# 按行读取文件
def read_file_line(path):
    with open(path, 'r') as f:
        for line in f:
            yield line


# 写入文件
def write_file(path, content):
    with open(path, 'w') as f:
        f.write(content)
# 分行写入：批次写入行数据，或使用换行符\n全部写入

import os
# 删除文件
def delete_file(path):
    os.remove(path)

# 判断文件是否存在
def is_file_exist(path):
    return os.path.exists(path)

# 获取文件大小
def get_file_size(path):
    return os.path.getsize(path)

# 获取文件后缀名
def get_file_extension(path):
    return os.path.splitext(path)[1]

# 获取目录下所有文件
def get_all_files(path):
    return os.listdir(path)

# 重命名
def rename_file(old_path, new_path):
    os.rename(old_path, new_path)
```

# DataUtils
```python
import datetime
from datetime import datetime as dtc
class DataUtil:
    # TODO 考虑归为enum对象
    DATE_FORMAT = "%Y-%m-%d"
    SHORT_DATA_FORMAT = "%Y%m%d"
    LONG_DATA_FORMAT = "%Y-%m-%d %H:%M:%S"

    @staticmethod
    def date(dt: dtc = dtc.now(), dif=0):
        return dt if dif == 0 else dt - datetime.timedelta(days=dif) if dif < 0 else dt + datetime.timedelta(days=dif)

    @staticmethod
    def format_date(dt: dtc = dtc.now(), ft=DATE_FORMAT, dif=0):
        return DateUtil.date(dt, dif).strftime(ft)

    @staticmethod
    def date_format(dataStr: str, ft=DATE_FORMAT):
        return dtc.strptime(dataStr, ft)

    @staticmethod
    def compare(d1: dtc, d2: dtc):
        return 1 if d1 > d2 else 0 if d1 == d2 else -1

    @staticmethod
    def compareLess(d1: dtc, d2: dtc):
        return 1 if d1 < d2 else -1

    @staticmethod
    def compareGreater(d1: dtc, d2: dtc):
        return 1 if d1 > d2 else -1

    @staticmethod
    def dif_days(d1: dtc, d2: dtc):
        return d1.day - d2.day

    @staticmethod
    def timestamp(dt: dtc = dtc.now(), tp: int = 0):
        """
        @param dt:
        @param tp:  0 秒级；1，毫秒；2.微秒
        @return:
        """
        dt = dt.timestamp()
        p = pow(10,(tp * 3))
        return int(dt * p)

    @staticmethod
    def localdate(ts:int,tp:int = 0):
        """
        @param ts: 时间戳
        @param tp:  0 秒级；1，毫秒；2.微秒
        @return:
        """
        p = pow(10,(tp * 3))
        return  datetime.datetime.fromtimestamp((ts/p))
```
# JsonUtils
```python
import json
class JsonUtil:
    @staticmethod
    def convertJsonFromJsonCallBack(content, jsonCallBack=None):
        if jsonCallBack:
            return content.removeprefix(jsonCallBack)[1:-1:]
        return content[content.find("(") + 1:-1:]

    @staticmethod
    def obj_2_json(obj):
        """
        # 自定义类转json需要自定义一个转换成python基本类型的方法
        def obj_2_json(obj):
            return {
                "name":obj.name
            }
        """
        return json.dumps(obj, default=lambda obj: obj.__dict__)

    @staticmethod
    def json_2_obj(jsonStr, handler=None, pairsHandler=None):
        return json.loads(jsonStr, object_hook=handler, object_pairs_hook=pairsHandler)

    @staticmethod
    def obj_2_json_to_file(data, fp):
        with open(fp, 'w') as file:
            json.dump(data, file, default=lambda obj: obj.__dict__)

    @staticmethod
    def json_2_obj_from_file(fp, handler=None):
        with open(fp, 'r') as file:
            return json.load(file, object_hook=handler)
```

# DbUtils
    简单封装，功能不全
```python
import pymysql
class MySQLOption:
    def __init__(self):
        self.__db = pymysql.connect(host='db.dongle.com', user='root', passwd='Dongle@123', port=3306, db="dongle-data")
        self.__cursor = self.__db.cursor()

    def close(self):
        self.__cursor.close()
        self.__db.close()

    def executeSql(self, sql):
        try:
            self.__cursor.execute(sql)
            self.__db.commit()
        except Exception as ex:
            print("sql execute is error![%s]" % sql, ex)
            self.__db.rollback()

    def querySql(self, sql):
        self.__cursor.execute(sql)
        return self.__cursor.fetchall()
```

# AesUtils
```python
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad

import base64

def aes_ecb_encrypt(key, text):
    """
    AES-128-ECB AES标准对称加密算法
    :param key: 加密密钥
    :param plaintext: 传入的明文，带加密内容
    :return: 加密结果
    """
    # 创建AES并设置加密模式为ECB
    aes = AES.new(key.encode(), AES.MODE_ECB)
    # 明文填充,保证数据被填充为16位，AES强制要求
    padded_text= pad(text.encode(), AES.block_size)
    # 加密
    encrypt_aes = aes.encrypt(padded_text)
    return base64.encodebytes(encrypt_aes).decode()

def aes_cbc_encrypt(key, text, iv):
    """
    AES-128-CBC AES标准对称加密算法
    :param key: 加密密钥
    :param iv: 初始化向量,CBC模式需要
    :param plaintext: 传入的明文，带加密内容
    :return: 加密结果(密文)
    """
    # 创建AES并设置加密模式为cbc
    aes = AES.new(key.encode(), AES.MODE_CBC,iv.encode())
    # 明文填充,保证数据被填充为16位，AES强制要求
    padded_text= pad(text.encode(), AES.block_size)
    # 加密
    encrypt_aes = aes.encrypt(padded_text)
    return base64.encodebytes(encrypt_aes).decode()


def aes_ecb_decrypt(key, text):
    """
    AES-128-ECB AES标准对称解密算法
    :param key: 加密密钥
    :param text: 密文
    :return: 解密结果(明文)
    """
    text = base64.b64decode(text)
    aes = AES.new(key.encode(), AES.MODE_ECB)
    decrypt_text = aes.decrypt(text).decode()
    return decrypt_text.strip()

def aes_cbc_decrypt(key, text, iv):
    """
    AES-128-CBC AES标准对称解密算法
    :param key: 加密密钥
    :param iv: 初始化向量,CBC模式需要
    :param text: 密文
    :return: 解密结果(明文)
    """
    text = base64.b64decode(text)
    aes = AES.new(key.encode(), AES.MODE_CBC,iv.encode())
    decrypt_text = aes.decrypt(text).decode()
    return decrypt_text.strip()

```
