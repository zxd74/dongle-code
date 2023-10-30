package utils

import (
	"fmt"
	"net/http"
)

func indexHandle(writer http.ResponseWriter, request *http.Request) {
	_, err := fmt.Fprintf(writer, "Welcom to my website!")
	if err != nil {
		return
	}
}

func staticHandle() http.Handler {
	fs := http.FileServer(http.Dir("../static/"))
	return http.StripPrefix("/static/", fs)
}

func httpHandle() {
	http.HandleFunc("/", indexHandle)       //首页
	http.Handle("/static/", staticHandle()) // 静态资源目录服务
}

func StartWeb(host string, port int) {
	if port <= 0 {
		port = 80
	}
	address := fmt.Sprintf("%s:%s", host, port)
	err := http.ListenAndServe(address, nil)
	if err != nil {
		fmt.Println("web start is error!", err)
		return
	}
}
