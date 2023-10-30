package service

type Dongle interface {
	Hello() string
}

type Kk interface {
	Test()
}

type Kevin interface {
	Hello() string
	Test()
}

type Dd struct {
}

func (d *Dd) Hello() string {
	return ""
}

func (d *Dd) Test() {
}
