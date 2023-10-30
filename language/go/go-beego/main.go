package main

import (
	"dongle/go-web/controller"
	"github.com/beego/beego/v2/server/web"
)

func main() {

	controller.HandlerController()

	// 启动Admin Service
	//web.BConfig.Listen.EnableAdmin = true
	//web.BConfig.Listen.AdminAddr = "localhost"
	//web.BConfig.Listen.AdminPort = 8088

	// 输出全部路由
	//web.BConfig.RouterCaseSensitive = false
	//tree := web.PrintTree()
	//methods := tree["Data"].(web.M)
	//for k, v := range methods {
	//	fmt.Printf("%s => %v\n", k, v)
	//}

	web.Run()
}
