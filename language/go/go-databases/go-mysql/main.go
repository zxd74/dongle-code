package main

import "dongle/go-mysql/config"

func main() {
	myConfig := config.MysqlConnect("root:123456@tcp(localhost:3306)/test")
	if myConfig != nil {
		myConfig.Close()
	}
}
