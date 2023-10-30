package go_type

import (
	"encoding/json"
	"fmt"
)

type JsonObj struct {
	Name string
	Age  int
}

var bytes []byte

func ToJson() {
	obj := JsonObj{
		Name: "Dongle",
		Age:  30,
	}
	bytes, _ = json.Marshal(obj)
	fmt.Println("转JSON：", string(bytes))
}

func ReadJson() {
	if bytes == nil {
		return
	}
	var obj JsonObj
	json.Unmarshal(bytes, &obj)

	fmt.Println("解析JSON：", obj.Name, obj.Age)
}
