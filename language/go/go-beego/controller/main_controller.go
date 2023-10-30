package controller

import (
	"fmt"
	"github.com/beego/beego/v2/server/web"
	"github.com/beego/beego/v2/server/web/context"
)

type MainController struct {
	web.Controller
}

func (mc *MainController) Home() {
	mc.Ctx.WriteString("this is home")
}

func Health(ctx *context.Context) {
	ctx.WriteString("health")
}

func handlerV1NS() {
	uc := &UserController{}
	// initiate namespace
	ns := web.NewNamespace("/v1",
		web.NSCtrlGet("/home", (*MainController).Home),
		web.NSRouter("/user", uc),
		web.NSAutoRouter(uc),
		web.NSGet("/health", Health),
		// 嵌套 namespace
		web.NSNamespace("/admin",
			web.NSRouter("/user", uc),
		),
	)
	web.AddNamespace(ns)
	fmt.Println("config v1 web namespace")
}

func HandlerController() {
	handlerV1NS()

	web.AutoRouter(&UserController{})
	web.Get("/hello", func(ctx *context.Context) {
		ctx.WriteString("hello, world")
	})
	fmt.Println("config web interface")
}
