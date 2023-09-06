```vue
<!-- data为绑定数据，prop 绑定属性字段，label显示列名称 -->
<!-- 可以使用template自定义单元格内容，slot-scope代码代码格绑定row数据定义，
    使用 其.row.属性读取行数据属性值；
    使用其.$index读取行序列
    
    -->
<el-table :data="data" >
    <el-table-item prop="name" label="列名" />
    <el-table-item label="列名"> 
        <template slot-scope="subdata">
            {{subdata.row.name}}
        </template>
    </el-table-item>
</el-table>
```