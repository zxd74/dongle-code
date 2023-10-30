import baostock as bs
import pandas as pd

history_filed = "date,code,open,high,low,close,preclose,volume,amount,adjustflag,turn,tradestatus,pctChg,peTTM,pbMRQ," \
                "psTTM,pcfNcfTTM,isST "


def history_k_data(stock_code: str):
    rs = bs.query_history_k_data_plus(stock_code,
                                      history_filed,
                                      start_date='2023-01-01', end_date='2023-04-10',
                                      frequency="d", adjustflag="3")
    if rs.error_code != '0':
        print('query_history_k_data_plus respond code:%s,msg:%s' % (rs.error_code, rs.error_msg))
        return None
    return handle_data(rs)


def handle_data(rs):
    data_list = []
    while (rs.error_code == '0') & rs.next():
        # 获取一条记录，将记录合并在一起
        data_list.append(rs.get_row_data())
    return pd.DataFrame(data_list, columns=rs.fields)


def login():
    lg = bs.login()
    if lg is None: return False
    if lg.error_code != '0':
        print('login respond code:%s,msg:%s' % (lg.error_code, lg.error_msg))
        return False
    return True


def track_history_k_data(codes,filepath):
    if login() is False:
        return
    result = []
    for code in codes:
        # 打印结果集
        rs = bs.query_history_k_data_plus(code,
                                          history_filed,
                                          start_date='2023-01-01', end_date='2023-04-10',
                                          frequency="d", adjustflag="3")
        if rs.error_code != '0':
            print('query_history_k_data_plus respond code:%s,msg:%s' % (rs.error_code, rs.error_msg))
            return
        result.append(handle_data(rs))
    result = pd.concat(result, ignore_index=True)
    result.to_csv("%s\\history_stock_k_data_by_bao.csv" % filepath, index=False)
    bs.logout()


def track_stock_info(codes,filepath):
    if login() is False:
        return
    result = []
    for code in codes:
        # 获取证券基本资料
        rs = bs.query_stock_basic(code)
        # rs = bs.query_stock_basic(code_name="浦发银行")  # 支持模糊查询
        if rs.error_code != '0':
            print('query_stock_basic respond code:%s,msg:%s' % (rs.error_code, rs.error_msg))
            return
        result.append(handle_data(rs))
    result = pd.concat(result,ignore_index=True)
    result.to_csv("%s\\stock_info_by_bao.csv" % filepath, index=False)
    bs.logout()


def track_all_stock_code(day,filepath):
    if login() is False:
        return
    handle_data(bs.query_all_stock(day=day)).to_csv("%s\\all_stock_by_bao.csv" % filepath, index=False)
    bs.logout()