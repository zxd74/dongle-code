# 根据条件(没有则以主键为条件)查询最新一条数据
1. 根据ID最大查询
2. 根据ID倒序查询一条
```sql
select * from tb where id=(select max(id) from tb);

select * from tb order by id desc limit 1;
```
3. 根据row id查询(待)


# 根据查询结果导入新表
```sql
insert into tb_new select * from tb_old where id in (select id from tb_new);

insert into tb_new select * from tb_old where id not in (select id from tb_new);

insert into tb_new select * from tb_old
```
