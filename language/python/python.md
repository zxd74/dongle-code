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