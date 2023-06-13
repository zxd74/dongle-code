
    @RequestMapping("export")
    public void export(HttpServletRequest request,HttpServletResponse response){
        try {
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition","attachment;filename=test.txt");

            // header 设置要在内容输出前配置
            response.getOutputStream().write(new byte[100]);

            response.getOutputStream().flush();
            response.getOutputStream().close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }