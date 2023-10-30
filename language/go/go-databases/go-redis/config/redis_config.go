package config

import (
	"fmt"

	"github.com/go-redis/redis"
)

var rdb *redis.Client

func Conn(host string, port int, passwd string) error {
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

func Set(key string, val string) error {
	err := rdb.Set(key, val, 0).Err()
	if err != nil {
		fmt.Println("set Key is Error!", key, err)
	}
	return err
}
