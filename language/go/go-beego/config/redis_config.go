package config

import (
	"fmt"
	"github.com/go-redis/redis"
)

var rdb *redis.Client

func Con(host string, port int, passwd string) error {
	address := fmt.Sprintf("%s:%d", host, port)
	rdb = redis.NewClient(&redis.Options{
		Addr:     address,
		Password: passwd,
		DB:       0,
	})

	_, err := rdb.Ping().Result()
	if err != nil {
		fmt.Println("Connect Redis is Error!", err)
	}
	return err
}

func Get(key string) string {
	val, err := rdb.Get(key).Result()
	if err != nil {
		fmt.Println("Get Key is Error!", key, err)
	}
	return val
}

//func main() {
//	err := config.Con("db.dongle.com", 6379, "123456")
//	if err != nil {
//		return
//	}
//	fmt.Println(config.Get("dongle"))
//}
