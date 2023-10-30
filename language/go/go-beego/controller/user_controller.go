package controller

import "github.com/beego/beego/v2/server/web"

type UserController struct {
	web.Controller
}

func (u *UserController) HelloWorld() {
	u.Ctx.WriteString("hello, world")
}

func (uc *UserController) Get() {
	uc.Ctx.WriteString("get user")
}
