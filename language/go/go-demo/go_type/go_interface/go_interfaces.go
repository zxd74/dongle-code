package go_interface

type Hello struct{}

func (h *Hello) Hello() string {
	return "Hello!"
}

type HelloWorld struct{}

func (h *HelloWorld) Hello() string {
	return "Hello World!"
}
