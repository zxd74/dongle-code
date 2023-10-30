package config

import (
	"database/sql"
	"fmt"
	"github.com/go-sql-driver/mysql"
)

var my *MySqlConnect

type MySqlConnect struct {
	db *sql.DB
}

func MysqlConnect(address string) *MySqlConnect {
	if my != nil {
		return my
	}

	config := mysql.Config{}
	config.FormatDSN()

	db, err := sql.Open("mysql", address)
	if err != nil {
		fmt.Println("mysql connect is error!", err)
		return nil
	}
	if err := db.Ping(); err != nil {
		fmt.Println("mysql not ping!")
		return nil
	}
	my = new(MySqlConnect)
	my.db = db
	return my
}

func (my *MySqlConnect) Query(sql string) {

}

func (my *MySqlConnect) Count(sql string) int {
	count := 0
	row := my.db.QueryRow(sql)

	if err := row.Scan(&count); err != nil {
		return count
	}
	return count
}

func (my *MySqlConnect) Close() {
	err := my.db.Close()
	if err != nil {
		return
	}
}
