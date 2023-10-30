package main

import (
	"dongle/go-redis/config"
	"fmt"
)

func main() {
	err := config.Conn("db.dongle.com", 6379, "123456")
	if err != nil {
		return
	}
	key := "dongle"
	val := config.Get(key)
	fmt.Println("get key:", key, val)
	config.Set("go-redis", "test")
}
