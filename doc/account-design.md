
## 设计

> 设计模块

* 模块名称： ka(keep accounts)
* 数据表设计 
  * 数据表名：tbl_account
  * 字段：
    * trade_time datetime index '交易时间'
    * trade_type varchar(32) index '交易分类'
    * trade_obj  varchar(32) '交易对象'
    * obj_account varchar(32) '对方账号'
    * product varchar(128) '商品说明'
    * in_out varchar(4) index '收/支'
    * amount double index '金额'
    * pay_type varchar(32) '收付款方式'
    * trade_status varchar(4) '交易状态'
    * order_id varchar(128) '交易订单号'
    * seller_order_id varchar(128) '商家订单号'
    * trade_comment text '备注',
    * create_user_id bigint(20),
    * primary key (order_id, creat_user_id)
```mysql
create table tbl_account (
    order_id varchar(128) primary key,
    trade_time datetime not null,
    trade_type varchar(32),
    trade_obj varchar(32),
    obj_account varchar(32),
    product varchar(128),
    in_out varchar(4),
    amount double,
    pay_type varchar(32),
    trade_status varchar(16),
    seller_order_id varchar(128),
    trade_comment text,
    index (trade_time),
    index (trade_type),
    index (in_out),
    index (amount)
);
```
