package impl

type Hello struct {
	name string
	age  int
}

func (h *Hello) Hello() string {
	return "Hello!"
}
func (h *Hello) Name() {
	h.name = "kk"
}
func (h *Hello) Aage() {
	h.age = 10
}

type HelloWorld struct {
	name string
}

func (h *HelloWorld) Hello() string {
	return "Hello World!"
}
func (h *HelloWorld) Name() {
	h.name = "kk"
}

type Dt struct {
	//service.Dongle
	Hello
	h Hello
}
