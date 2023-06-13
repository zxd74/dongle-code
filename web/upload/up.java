// 单文件上传
public void upload(HttpServletRequest request,MultipartFile file){

}

// 指定参数文件上传
public void upload(HttpServletRequest request,@RequestParams("file") MultipartFile file){

}

// 多文件上传必需指定参数
public void upload(HttpServletRequest request,@RequestParams("files") MultipartFile[] files){

}

// 不同参数的多文件上传
public void upload(HttpServletRequest request,@RequestParams("files") MultipartFile[] files,@RequestParams("files1") MultipartFile[] files1){

}

// 单/多文件上传
public void upload(HttpServletRequest request){
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    Map<String,MultipartFile> fileMap = multipartRequest.getFileMap();
    // map以参数名，值为文件关联
}
