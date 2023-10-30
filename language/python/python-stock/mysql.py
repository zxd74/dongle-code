import pymysql
import csv


def save_history_data(result):
    db = pymysql.connect(host='db.dongle.com', user='root', passwd='Dongle@123', port=3306, db="dongle-data")
    cursor = db.cursor()

    sql = "insert into history_data(`date`,`code`,`open`,`high`,`low`,`close`,`preclose`,`volume`,`amount`,`adjustflag`,`turn`,`tradestatus`,`pctChg`,`peTTM`,`pbMRQ`,`psTTM`,`pcfNcfTTM`,`isST`) values "
    for row in result:
        sql += "(" + str(row).removeprefix("[").removesuffix("]") + "),"
    sql = sql.removesuffix(",")
    try:
        cursor.execute(sql)
        db.commit()
    except:
        db.rollback()

    # 关闭数据库连接
    db.close()


def save_stock_data(result):
    db = pymysql.connect(host='db.dongle.com', user='root', passwd='Dongle@123', port=3306, db="dongle-data")
    cursor = db.cursor()

    sql = "insert into stock_info(  `code`,  `code_name`,  `ipoDate`,  `outDate`,  `type`,  `status`) values "
    for row in result:
        sql += "(" + str(row).removeprefix("[").removesuffix("]") + "),"
    sql = sql.removesuffix(",")
    try:
        cursor.execute(sql)
        db.commit()
    except:
        db.rollback()
    # 关闭数据库连接
    db.close()


def readExcel(file):
    data = []
    with open(file, 'r', encoding='utf-8') as csv_file:
        reader = csv.reader(csv_file)
        next(reader)
        for row in reader:
            data.append(row)
    return data
