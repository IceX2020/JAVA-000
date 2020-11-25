***用户表:用户id、用户名称、密码、手机号、证件类型、证件号码、用户状态、创建时间、更新时间***   
CREATE TABLE users (
  userid bigint NOT NULL AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  password varchar(64) NOT NULL,
  phoneNumber varchar(15) NULL,
  identify_card_type varchar(16) NULL,
  identify_card_num varchar(64) NULL,
  user_status int(2) NOT NULL,
  create_date timestamp NOT NULL,
  update_date timestamp NULL,
  PRIMARY KEY (userid)
);

***店铺表：店铺id、名称、描述、店铺状态、创建时间、更新时间***   
CREATE TABLE stores (
    storeid int(11) NOT NULL AUTO_INCREMENT,
    storename varchar(64) NOT NULL,
    description varchar(1024) NOT NULL,
    store_status int(2) not null,
    create_date timestamp NOT NULL,
    update_date timestamp NULL,
    PRIMARY KEY (storeid)
);

***商品表：商品id、名称、描述、价格、重量、所属店铺id、所属店铺名称、商品状态、创建时间、更新时间、生效时间、失效时间***   
CREATE TABLE goods (
    goodid int(11) NOT NULL AUTO_INCREMENT,
    goodname varchar(16) NOT NULL,
    description varchar(1024) NOT NULL,
    price int(11) NOT NULL,
    weight int(11) NOT NULL,
    store_id int(11) NOT NULL,
    store_name varchar(16) NOT NULL,
    good_status int(2) not null,
    create_date timestamp NOT NULL,
    update_date timestamp NULL,
    good_effdate timestamp NOT NULL,
    good_expdate timestamp NOT NULL,
    PRIMARY KEY (goodid),
);

***订单表:订单id、用户id、商品列表、订单状态、物流状态、物流单号、物流地址、总价、创建时间、更新时间***   
CREATE TABLE orders (
    orderid bigint NOT NULL AUTO_INCREMENT,
    user_id int(11) NOT NULL,
    commodities varchar(10024) NOT NULL,
    order_status int(2) not null,
    deliver_status varchar(10024) NOT NULL,
    deliver_orderid varchar(10024) NULL,
    deliver_addr varchar(10024) NULL,
    total_price int(11) NOT NULL,
    create_date timestamp NOT NULL,
    update_date timestamp NULL,
    PRIMARY KEY (orderid),
);