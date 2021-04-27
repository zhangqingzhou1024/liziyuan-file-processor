## 简介
 本项目为文件处理项目，包括 excel、txt等文件类型等处理。  
目前支持excel的导入和导出，包括单元格合并、多sheet导出等  
 
## 技术栈
  - spring-boot
  - easyExcel
  
## 代码架构说明
- 1: controller 控制层
- 2: service 业务服务层
- 3: processor 文件处理层
- 4: core 代码基础层
    - constant 常量类
    - util 工具类
    - exception 异常
    - model 模型层
    
- 5: 项目启动入口 FileProcessApplication

## 相关文档
   - easyExcel  https://github.com/alibaba/easyexcel