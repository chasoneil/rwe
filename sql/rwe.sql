-- MySQL dump 10.13  Distrib 5.7.31, for Win64 (x86_64)
--
-- Host: localhost    Database: zmanager
-- ------------------------------------------------------
-- Server version	5.7.31-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES UTF8MB4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `oa_notify`
--

DROP TABLE IF EXISTS `oa_notify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oa_notify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `title` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '标题',
  `content` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '内容',
  `files` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '附件',
  `status` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `oa_notify_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='通知通告';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oa_notify`
--

LOCK TABLES `oa_notify` WRITE;
/*!40000 ALTER TABLE `oa_notify` DISABLE KEYS */;
/*!40000 ALTER TABLE `oa_notify` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oa_notify_record`
--

DROP TABLE IF EXISTS `oa_notify_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oa_notify_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `notify_id` bigint(20) DEFAULT NULL COMMENT '通知通告ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '接受人',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '阅读标记',
  `read_date` date DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`id`),
  KEY `oa_notify_record_notify_id` (`notify_id`),
  KEY `oa_notify_record_user_id` (`user_id`),
  KEY `oa_notify_record_read_flag` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='通知通告发送记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oa_notify_record`
--

LOCK TABLES `oa_notify_record` WRITE;
/*!40000 ALTER TABLE `oa_notify_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `oa_notify_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='部门管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (9,0,'经理室',2,1),(11,0,'人力资源部',3,1),(15,0,'财务部',4,1),(16,0,'销售部',5,1),(17,0,'品牌宣传部',6,1),(18,16,'销售1部',1,1),(19,16,'销售2部',2,1);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dict` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '标签名',
  `value` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '数据值',
  `type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `description` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `sort` decimal(10,0) DEFAULT NULL COMMENT '排序（升序）',
  `parent_id` bigint(64) DEFAULT '0' COMMENT '父级编号',
  `create_by` int(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`name`),
  KEY `sys_dict_del_flag` (`del_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=2096 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES (1979,'基础主题','basic','cms_theme','站点主题',10,0,1,NULL,1,NULL,NULL,'0'),(1980,'蓝色主题','blue','cms_theme','站点主题',20,0,1,NULL,1,NULL,NULL,'1'),(1981,'红色主题','red','cms_theme','站点主题',30,0,1,NULL,1,NULL,NULL,'1'),(1982,'红色','red','color','颜色值',10,0,1,NULL,1,NULL,NULL,'0'),(1983,'绿色','green','color','颜色值',20,0,1,NULL,1,NULL,NULL,'0'),(1984,'蓝色','blue','color','颜色值',30,0,1,NULL,1,NULL,NULL,'0'),(1985,'黄色','yellow','color','颜色值',40,0,1,NULL,1,NULL,NULL,'0'),(1986,'橙色','orange','color','颜色值',50,0,1,NULL,1,NULL,NULL,'0'),(1999,'删除','0','del_flag','删除标记',NULL,NULL,NULL,NULL,NULL,NULL,'',''),(2000,'正常','0','del_flag','删除标记',10,0,1,NULL,1,NULL,NULL,'0'),(2001,'小乙网控终端','ZTOWER','device_type','终端类型',1,NULL,NULL,NULL,NULL,NULL,'',NULL),(2004,'增删改查','crud','gen_category','代码生成分类',10,0,1,NULL,1,NULL,NULL,'1'),(2005,'增删改查（包含从表）','crud_many','gen_category','代码生成分类',20,0,1,NULL,1,NULL,NULL,'1'),(2006,'树结构','tree','gen_category','代码生成分类',30,0,1,NULL,1,NULL,NULL,'1'),(2007,'仅持久层','dao','gen_category','代码生成分类\0\0',40,0,1,NULL,1,NULL,NULL,'1'),(2008,'String','String','gen_java_type','Java类型',10,0,1,NULL,1,NULL,NULL,'1'),(2009,'Long','Long','gen_java_type','Java类型',20,0,1,NULL,1,NULL,NULL,'1'),(2010,'Integer','Integer','gen_java_type','Java类型',30,0,1,NULL,1,NULL,NULL,'1'),(2011,'Double','Double','gen_java_type','Java类型',40,0,1,NULL,1,NULL,NULL,'1'),(2012,'Date','java.util.Date','gen_java_type','Java类型',50,0,1,NULL,1,NULL,NULL,'1'),(2013,'Custom','Custom','gen_java_type','Java类型',90,0,1,NULL,1,NULL,NULL,'1'),(2014,'=','=','gen_query_type','查询方式',10,0,1,NULL,1,NULL,NULL,'1'),(2015,'!=','!=','gen_query_type','查询方式',20,0,1,NULL,1,NULL,NULL,'1'),(2016,'&gt;','&gt;','gen_query_type','查询方式',30,0,1,NULL,1,NULL,NULL,'1'),(2017,'&lt;','&lt;','gen_query_type','查询方式',40,0,1,NULL,1,NULL,NULL,'1'),(2018,'Between','between','gen_query_type','查询方式',50,0,1,NULL,1,NULL,NULL,'1'),(2019,'Like','like','gen_query_type','查询方式',60,0,1,NULL,1,NULL,NULL,'1'),(2020,'Left Like','left_like','gen_query_type','查询方式',70,0,1,NULL,1,NULL,NULL,'1'),(2021,'Right Like','right_like','gen_query_type','查询方式',80,0,1,NULL,1,NULL,NULL,'1'),(2022,'文本框','input','gen_show_type','字段生成方案',10,0,1,NULL,1,NULL,NULL,'1'),(2023,'文本域','textarea','gen_show_type','字段生成方案',20,0,1,NULL,1,NULL,NULL,'1'),(2024,'下拉框','select','gen_show_type','字段生成方案',30,0,1,NULL,1,NULL,NULL,'1'),(2025,'复选框','checkbox','gen_show_type','字段生成方案',40,0,1,NULL,1,NULL,NULL,'1'),(2026,'单选框','radiobox','gen_show_type','字段生成方案',50,0,1,NULL,1,NULL,NULL,'1'),(2027,'日期选择','dateselect','gen_show_type','字段生成方案',60,0,1,NULL,1,NULL,NULL,'1'),(2028,'人员选择','userselect','gen_show_type','字段生成方案',70,0,1,NULL,1,NULL,NULL,'1'),(2029,'部门选择','officeselect','gen_show_type','字段生成方案',80,0,1,NULL,1,NULL,NULL,'1'),(2030,'区域选择','areaselect','gen_show_type','字段生成方案',90,0,1,NULL,1,NULL,NULL,'1'),(2031,'设备定时通电/关电','switchChangeJob','job_group','计划任务分类',1,NULL,NULL,NULL,NULL,NULL,'',NULL),(2032,'系统数据处理','dataCheckJob','job_group','计划任务分类',2,NULL,NULL,NULL,NULL,NULL,'',NULL),(2033,'公休','1','oa_leave_type','请假类型',10,0,1,NULL,1,NULL,NULL,'0'),(2034,'病假','2','oa_leave_type','请假类型',20,0,1,NULL,1,NULL,NULL,'0'),(2035,'事假','3','oa_leave_type','请假类型',30,0,1,NULL,1,NULL,NULL,'0'),(2036,'调休','4','oa_leave_type','请假类型',40,0,1,NULL,1,NULL,NULL,'0'),(2037,'婚假','5','oa_leave_type','请假类型',60,0,1,NULL,1,NULL,NULL,'0'),(2038,'未读','0','oa_notify_read','通知阅知状态',10,0,1,NULL,1,NULL,'','0'),(2039,'已读','1','oa_notify_read','通知阅知状态',20,0,1,NULL,1,NULL,'','0'),(2040,'草稿','0','oa_notify_status','通知通告状态',10,0,1,NULL,1,NULL,NULL,'0'),(2041,'草稿','0','oa_notify_status','通知通告状态',10,0,1,NULL,1,NULL,'','0'),(2042,'发布','1','oa_notify_status','通知通告状态',20,0,1,NULL,1,NULL,NULL,'0'),(2043,'信息提示','1','oa_notify_type','通知公告类型',10,NULL,NULL,NULL,NULL,NULL,'',NULL),(2044,'活动通告','3','oa_notify_type','通知通告类型',30,0,1,NULL,1,NULL,NULL,'0'),(2045,'男','1','sex','性别',10,0,1,NULL,1,NULL,NULL,'0'),(2046,'女','2','sex','性别',20,0,1,NULL,1,NULL,NULL,'0'),(2047,'显示','1','show_hide','显示/隐藏',10,0,1,NULL,1,NULL,NULL,'0'),(2048,'隐藏','0','show_hide','显示/隐藏',20,0,1,NULL,1,NULL,NULL,'0'),(2049,'国家','1','sys_area_type','区域类型',10,0,1,NULL,1,NULL,NULL,'0'),(2050,'省份、直辖市','2','sys_area_type','区域类型',20,0,1,NULL,1,NULL,NULL,'0'),(2051,'地市','3','sys_area_type','区域类型',30,0,1,NULL,1,NULL,NULL,'0'),(2052,'区县','4','sys_area_type','区域类型',40,0,1,NULL,1,NULL,NULL,'0'),(2053,'所有数据','1','sys_data_scope','数据范围',10,0,1,NULL,1,NULL,NULL,'0'),(2054,'所在公司及以下数据','2','sys_data_scope','数据范围',20,0,1,NULL,1,NULL,NULL,'0'),(2055,'所在公司数据','3','sys_data_scope','数据范围',30,0,1,NULL,1,NULL,NULL,'0'),(2056,'所在部门及以下数据','4','sys_data_scope','数据范围',40,0,1,NULL,1,NULL,NULL,'0'),(2057,'所在部门数据','5','sys_data_scope','数据范围',50,0,1,NULL,1,NULL,NULL,'0'),(2058,'仅本人数据','8','sys_data_scope','数据范围',90,0,1,NULL,1,NULL,NULL,'0'),(2059,'按明细设置','9','sys_data_scope','数据范围',100,0,1,NULL,1,NULL,NULL,'0'),(2060,'接入日志','1','sys_log_type','日志类型',30,0,1,NULL,1,NULL,NULL,'0'),(2061,'异常日志','2','sys_log_type','日志类型',40,0,1,NULL,1,NULL,NULL,'0'),(2062,'综合部','1','sys_office_common','快捷通用部门',30,0,1,NULL,1,NULL,NULL,'0'),(2063,'开发部','2','sys_office_common','快捷通用部门',40,0,1,NULL,1,NULL,NULL,'0'),(2064,'人力部','3','sys_office_common','快捷通用部门',50,0,1,NULL,1,NULL,NULL,'0'),(2065,'一级','1','sys_office_grade','机构等级',10,0,1,NULL,1,NULL,NULL,'0'),(2066,'二级','2','sys_office_grade','机构等级',20,0,1,NULL,1,NULL,NULL,'0'),(2067,'三级','3','sys_office_grade','机构等级',30,0,1,NULL,1,NULL,NULL,'0'),(2068,'四级','4','sys_office_grade','机构等级',40,0,1,NULL,1,NULL,NULL,'0'),(2069,'公司','1','sys_office_type','机构类型',60,0,1,NULL,1,NULL,NULL,'0'),(2070,'部门','2','sys_office_type','机构类型',70,0,1,NULL,1,NULL,NULL,'0'),(2071,'小组','3','sys_office_type','机构类型',80,0,1,NULL,1,NULL,NULL,'0'),(2072,'其它','4','sys_office_type','机构类型',90,0,1,NULL,1,NULL,NULL,'0'),(2073,'系统管理','1','sys_user_type','用户类型',10,0,1,NULL,1,NULL,NULL,'0'),(2074,'部门经理','2','sys_user_type','用户类型',20,0,1,NULL,1,NULL,NULL,'0'),(2075,'普通用户','3','sys_user_type','用户类型',30,0,1,NULL,1,NULL,NULL,'0'),(2076,'默认主题','default','theme','主题方案',10,0,1,NULL,1,NULL,NULL,'0'),(2077,'天蓝主题','cerulean','theme','主题方案',20,0,1,NULL,1,NULL,NULL,'0'),(2078,'橙色主题','readable','theme','主题方案',30,0,1,NULL,1,NULL,NULL,'0'),(2079,'红色主题','united','theme','主题方案',40,0,1,NULL,1,NULL,NULL,'0'),(2080,'Flat主题','flat','theme','主题方案',60,0,1,NULL,1,NULL,NULL,'0'),(2081,'星期一','1','week_num','星期分类',1,NULL,NULL,NULL,NULL,NULL,'',NULL),(2082,'星期二','2','week_num','星期分类',2,NULL,NULL,NULL,NULL,NULL,'',NULL),(2083,'星期三','3','week_num','星期分类',3,NULL,NULL,NULL,NULL,NULL,'',NULL),(2084,'星期四','4','week_num','星期分类',4,NULL,NULL,NULL,NULL,NULL,'',NULL),(2085,'星期五','5','week_num','星期分类',5,NULL,NULL,NULL,NULL,NULL,'',NULL),(2086,'星期六','6','week_num','星期分类',6,NULL,NULL,NULL,NULL,NULL,'',NULL),(2087,'星期日','7','week_num','星期分类',7,NULL,NULL,NULL,NULL,NULL,'',NULL),(2088,'是','1','yes_no','是/否',10,0,1,NULL,1,NULL,NULL,'0'),(2089,'否','0','yes_no','是/否',20,0,1,NULL,1,NULL,NULL,'0'),(2090,'工作日单','odd','policy_work_type','定时策略类型',NULL,NULL,NULL,NULL,NULL,NULL,'',NULL),(2091,'工作日双','even','policy_work_type','定时策略类型',NULL,NULL,NULL,NULL,NULL,NULL,'',NULL),(2092,'不限定','other','policy_work_type','定时策略类型',NULL,NULL,NULL,'2019-04-04 17:54:19',NULL,'2019-04-04 17:54:23','',''),(2093,'超级管理员','super','role_type','角色权限类型',40,NULL,NULL,NULL,NULL,NULL,'',NULL),(2094,'普通用户权限','normal','role_type','角色权限类型',40,NULL,NULL,NULL,NULL,NULL,'',NULL),(2095,'管理员','admin','role_type','角色权限类型',40,NULL,NULL,NULL,NULL,NULL,'',NULL);
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_file`
--

DROP TABLE IF EXISTS `sys_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '文件类型',
  `url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `file_name` varchar(200) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件上传';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_file`
--

LOCK TABLES `sys_file` WRITE;
/*!40000 ALTER TABLE `sys_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `time` int(11) DEFAULT NULL COMMENT '响应时间',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='系统日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
INSERT INTO `sys_log` VALUES (1,141,'chason','登录',10,'com.chason.system.controller.LoginController.ajaxLogin()',NULL,'127.0.0.1','2024-08-15 16:00:26'),(2,141,'chason','请求访问主页',45,'com.chason.system.controller.LoginController.index()',NULL,'127.0.0.1','2024-08-15 16:00:26'),(3,141,'chason','访问主页',4,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 16:00:27'),(4,141,'chason','访问主页',0,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 16:00:27'),(5,141,'chason','登录',0,'com.chason.system.controller.LoginController.ajaxLogin()',NULL,'127.0.0.1','2024-08-15 16:21:52'),(6,141,'chason','请求访问主页',41,'com.chason.system.controller.LoginController.index()',NULL,'127.0.0.1','2024-08-15 16:21:52'),(7,141,'chason','访问主页',4,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 16:21:52'),(8,141,'chason','访问主页',0,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 16:21:52'),(9,141,'chason','编辑菜单',9,'com.chason.system.controller.MenuController.edit()',NULL,'127.0.0.1','2024-08-15 16:24:34'),(10,141,'chason','更新菜单',11,'com.chason.system.controller.MenuController.update()',NULL,'127.0.0.1','2024-08-15 16:24:39'),(11,141,'chason','编辑菜单',12,'com.chason.system.controller.MenuController.edit()',NULL,'127.0.0.1','2024-08-15 16:24:42'),(12,141,'chason','更新菜单',7,'com.chason.system.controller.MenuController.update()',NULL,'127.0.0.1','2024-08-15 16:24:52'),(13,141,'chason','编辑菜单',7,'com.chason.system.controller.MenuController.edit()',NULL,'127.0.0.1','2024-08-15 16:24:56'),(14,141,'chason','更新菜单',8,'com.chason.system.controller.MenuController.update()',NULL,'127.0.0.1','2024-08-15 16:25:05'),(15,141,'chason','登录',8,'com.chason.system.controller.LoginController.ajaxLogin()',NULL,'127.0.0.1','2024-08-15 16:29:26'),(16,141,'chason','请求访问主页',46,'com.chason.system.controller.LoginController.index()',NULL,'127.0.0.1','2024-08-15 16:29:26'),(17,141,'chason','访问主页',4,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 16:29:26'),(18,141,'chason','访问主页',0,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 16:29:26'),(19,141,'chason','编辑菜单',14,'com.chason.system.controller.MenuController.edit()',NULL,'127.0.0.1','2024-08-15 17:04:50'),(20,141,'chason','更新菜单',11,'com.chason.system.controller.MenuController.update()',NULL,'127.0.0.1','2024-08-15 17:05:10'),(21,141,'chason','编辑菜单',9,'com.chason.system.controller.MenuController.edit()',NULL,'127.0.0.1','2024-08-15 17:05:17'),(22,141,'chason','更新菜单',6,'com.chason.system.controller.MenuController.update()',NULL,'127.0.0.1','2024-08-15 17:05:27'),(23,141,'chason','编辑菜单',10,'com.chason.system.controller.MenuController.edit()',NULL,'127.0.0.1','2024-08-15 17:05:32'),(24,141,'chason','更新菜单',6,'com.chason.system.controller.MenuController.update()',NULL,'127.0.0.1','2024-08-15 17:05:36'),(25,141,'chason','编辑菜单',6,'com.chason.system.controller.MenuController.edit()',NULL,'127.0.0.1','2024-08-15 17:05:40'),(26,141,'chason','更新菜单',8,'com.chason.system.controller.MenuController.update()',NULL,'127.0.0.1','2024-08-15 17:05:45'),(27,141,'chason','登录',9,'com.chason.system.controller.LoginController.ajaxLogin()',NULL,'127.0.0.1','2024-08-15 17:10:06'),(28,141,'chason','请求访问主页',44,'com.chason.system.controller.LoginController.index()',NULL,'127.0.0.1','2024-08-15 17:10:06'),(29,141,'chason','访问主页',7,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 17:10:06'),(30,141,'chason','访问主页',0,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 17:10:06'),(31,141,'chason','编辑菜单',12,'com.chason.system.controller.MenuController.edit()',NULL,'127.0.0.1','2024-08-15 17:10:49'),(32,141,'chason','更新菜单',14,'com.chason.system.controller.MenuController.update()',NULL,'127.0.0.1','2024-08-15 17:10:54'),(33,141,'chason','登录',9,'com.chason.system.controller.LoginController.ajaxLogin()',NULL,'127.0.0.1','2024-08-15 17:11:27'),(34,141,'chason','请求访问主页',39,'com.chason.system.controller.LoginController.index()',NULL,'127.0.0.1','2024-08-15 17:11:27'),(35,141,'chason','访问主页',3,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 17:11:27'),(36,141,'chason','访问主页',0,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 17:11:27'),(37,141,'chason','请求访问主页',12,'com.chason.system.controller.LoginController.index()',NULL,'127.0.0.1','2024-08-15 17:14:33'),(38,141,'chason','访问主页',0,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 17:14:33'),(39,141,'chason','访问主页',0,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 17:14:33'),(40,141,'chason','登录',7,'com.chason.system.controller.LoginController.ajaxLogin()',NULL,'127.0.0.1','2024-08-15 17:38:41'),(41,141,'chason','请求访问主页',38,'com.chason.system.controller.LoginController.index()',NULL,'127.0.0.1','2024-08-15 17:38:41'),(42,141,'chason','访问主页',6,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 17:38:41'),(43,141,'chason','访问主页',0,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 17:38:41'),(44,141,'chason','登录',9,'com.chason.system.controller.LoginController.ajaxLogin()',NULL,'127.0.0.1','2024-08-15 17:59:08'),(45,141,'chason','请求访问主页',37,'com.chason.system.controller.LoginController.index()',NULL,'127.0.0.1','2024-08-15 17:59:08'),(46,141,'chason','访问主页',4,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 17:59:08'),(47,141,'chason','访问主页',0,'com.chason.rwe.controller.MainController.main()',NULL,'127.0.0.1','2024-08-15 17:59:08');
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=185 DEFAULT CHARSET=utf8 COMMENT='菜单管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'基础管理','','',0,'fa fa-bars',95,'2017-08-09 22:49:47',NULL),(2,3,'系统菜单','sys/menu/','sys:menu:menu',1,'fa fa-th-list',2,'2017-08-09 22:55:15',NULL),(3,0,'系统管理','','',0,'fa fa-desktop',99,'2017-08-09 23:06:55','2017-08-14 14:13:43'),(6,3,'用户管理','sys/user/','sys:user:user',1,'fa fa-user',0,'2017-08-10 14:12:11',NULL),(7,3,'角色管理','sys/role','sys:role:role',1,'fa fa-paw',1,'2017-08-10 14:13:19',NULL),(12,6,'新增','','sys:user:add',2,'',0,'2017-08-14 10:51:35',NULL),(13,6,'编辑','','sys:user:edit',2,'',0,'2017-08-14 10:52:06',NULL),(14,6,'删除',NULL,'sys:user:remove',2,NULL,0,'2017-08-14 10:52:24',NULL),(15,7,'新增','','sys:role:add',2,'',0,'2017-08-14 10:56:37',NULL),(20,2,'新增','','sys:menu:add',2,'',0,'2017-08-14 10:59:32',NULL),(21,2,'编辑','','sys:menu:edit',2,'',0,'2017-08-14 10:59:56',NULL),(22,2,'删除','','sys:menu:remove',2,'',0,'2017-08-14 11:00:26',NULL),(24,6,'批量删除','','sys:user:batchRemove',2,'',0,'2017-08-14 17:27:18',NULL),(25,6,'停用',NULL,'sys:user:disable',2,NULL,0,'2017-08-14 17:27:43',NULL),(26,6,'重置密码','','sys:user:resetPwd',2,'',0,'2017-08-14 17:28:34',NULL),(27,91,'系统日志','common/log','common:log',1,'fa fa-warning',0,'2017-08-14 22:11:53',NULL),(28,27,'刷新',NULL,'sys:log:list',2,NULL,0,'2017-08-14 22:30:22',NULL),(29,27,'删除',NULL,'sys:log:remove',2,NULL,0,'2017-08-14 22:30:43',NULL),(30,27,'清空',NULL,'sys:log:clear',2,NULL,0,'2017-08-14 22:31:02',NULL),(55,7,'编辑','','sys:role:edit',2,'',NULL,NULL,NULL),(56,7,'删除','','sys:role:remove',2,NULL,NULL,NULL,NULL),(57,91,'运行监控','/druid/index.html','',1,'fa fa-caret-square-o-right',1,NULL,NULL),(61,2,'批量删除','','sys:menu:batchRemove',2,NULL,NULL,NULL,NULL),(62,7,'批量删除','','sys:role:batchRemove',2,NULL,NULL,NULL,NULL),(71,1,'文件管理','/common/sysFile','common:sysFile:sysFile',1,'fa fa-folder-open',2,NULL,NULL),(73,3,'部门管理','/system/sysDept','system:sysDept:sysDept',1,'fa fa-users',3,NULL,NULL),(74,73,'增加','/system/sysDept/add','system:sysDept:add',2,'fa fa-wifi',1,NULL,NULL),(75,73,'刪除','system/sysDept/remove','system:sysDept:remove',2,NULL,2,NULL,NULL),(76,73,'编辑','/system/sysDept/edit','system:sysDept:edit',2,NULL,3,NULL,NULL),(78,1,'数据字典','/common/sysDict','common:sysDict:sysDict',1,'fa fa-book',1,NULL,NULL),(79,78,'增加','/common/sysDict/add','common:sysDict:add',2,NULL,2,NULL,NULL),(80,78,'编辑','/common/sysDict/edit','common:sysDict:edit',2,NULL,2,NULL,NULL),(81,78,'删除','/common/sysDict/remove','common:sysDict:remove',2,'',3,NULL,NULL),(83,78,'批量删除','/common/sysDict/batchRemove','common:sysDict:batchRemove',2,'',4,NULL,NULL),(91,0,'系统监控','','',0,'fa fa-video-camera',90,NULL,NULL),(92,91,'在线用户','sys/online','',1,'fa fa-user',NULL,NULL,NULL),(146,0,'设备发现','','',0,'fa fa-laptop',10,NULL,NULL),(147,0,'任务管理','','',0,'fa fa-gears',60,NULL,NULL),(148,146,'设备发现','/rtm/device/register','rtm:device:register',1,'fa fa-newspaper-o',0,NULL,NULL),(157,147,'计划任务','common/job','common:taskScheduleJob',1,'fa fa-hourglass-1',8,NULL,NULL),(159,0,'设备运维','','',0,'fa fa-briefcase',20,NULL,NULL),(160,159,'设备分组','/rtm/device/group','rwe:device:group',1,'fa fa-bars',2,NULL,NULL),(166,159,'分组设置','/rtm/space','rwe:space:space',1,'fa fa-bars',1,NULL,NULL),(168,166,'空间信息操作','','rtm:space:update',2,'fa fa-circle-o',NULL,NULL,NULL),(170,159,'定时策略','/rtm/policy','rwe:policy:policy',1,'',2,NULL,NULL),(175,166,'添加','','rtm:space:add',2,'',NULL,NULL,NULL),(176,166,'删除','','rtm:space:delete',2,'',NULL,NULL,NULL),(177,170,'添加','','rtm:policy:add',2,'',NULL,NULL,NULL),(178,170,'删除','','rtm:policy:remove',2,'',NULL,NULL,NULL),(179,0,'背单词','','',0,'fa fa-book',30,NULL,NULL),(180,179,'选课背','/rwe/lesson','rwe:lesson',1,'fa fa-pencil-square-o',NULL,NULL,NULL),(182,0,'企业信息','','',0,'fa fa-tag',40,NULL,NULL),(183,182,'企业信息列表','/rtm/company','',1,'',NULL,NULL,NULL),(184,179,'随机测验','/rwe/lesson/exam','rwe:lesson',1,'fa fa-calendar-check-o',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `role_sign` varchar(100) DEFAULT NULL COMMENT '角色标识',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `user_id_create` bigint(255) DEFAULT NULL COMMENT '创建用户id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COMMENT='角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','super','最高权限',2,'2017-08-12 00:43:52','2017-08-12 19:14:59'),(49,'普通管理员','admin','基本管理权限',NULL,NULL,NULL),(52,'普通用户','normal','普通用户',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7546 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (367,44,1),(368,44,32),(369,44,33),(370,44,34),(371,44,35),(372,44,28),(373,44,29),(374,44,30),(375,44,38),(376,44,4),(377,44,27),(378,45,38),(379,46,3),(380,46,20),(381,46,21),(382,46,22),(383,46,23),(384,46,11),(385,46,12),(386,46,13),(387,46,14),(388,46,24),(389,46,25),(390,46,26),(391,46,15),(392,46,2),(393,46,6),(394,46,7),(632,38,42),(1064,54,53),(1095,55,2),(1096,55,6),(1097,55,7),(1098,55,3),(1099,55,50),(1100,55,49),(1101,55,1),(1856,53,28),(1857,53,29),(1858,53,30),(1859,53,27),(1860,53,57),(1861,53,71),(1862,53,48),(1863,53,72),(1864,53,1),(1865,53,7),(1866,53,55),(1867,53,56),(1868,53,62),(1869,53,15),(1870,53,2),(1871,53,61),(1872,53,20),(1873,53,21),(1874,53,22),(2247,63,-1),(2248,63,84),(2249,63,85),(2250,63,88),(2251,63,87),(2252,64,84),(2253,64,89),(2254,64,88),(2255,64,87),(2256,64,86),(2257,64,85),(2258,65,89),(2259,65,88),(2260,65,86),(2262,67,48),(2263,68,88),(2264,68,87),(2265,69,89),(2266,69,88),(2267,69,86),(2268,69,87),(2269,69,85),(2270,69,84),(2271,70,85),(2272,70,89),(2273,70,88),(2274,70,87),(2275,70,86),(2276,70,84),(2277,71,87),(2278,72,59),(2279,73,48),(2280,74,88),(2281,74,87),(2282,75,88),(2283,75,87),(2284,76,85),(2285,76,89),(2286,76,88),(2287,76,87),(2288,76,86),(2289,76,84),(2292,78,88),(2293,78,87),(2294,78,NULL),(2295,78,NULL),(2296,78,NULL),(2308,80,87),(2309,80,86),(2310,80,-1),(2311,80,84),(2312,80,85),(2328,79,72),(2329,79,48),(2330,79,77),(2331,79,84),(2332,79,89),(2333,79,88),(2334,79,87),(2335,79,86),(2336,79,85),(2337,79,-1),(2338,77,89),(2339,77,88),(2340,77,87),(2341,77,86),(2342,77,85),(2343,77,84),(2344,77,72),(2345,77,-1),(2346,77,77),(7419,49,148),(7420,49,168),(7421,49,175),(7422,49,176),(7423,49,160),(7424,49,177),(7425,49,178),(7426,49,180),(7427,49,181),(7428,49,183),(7429,49,157),(7430,49,79),(7431,49,80),(7432,49,81),(7433,49,83),(7434,49,71),(7435,49,12),(7436,49,13),(7437,49,25),(7438,49,26),(7439,49,74),(7440,49,75),(7441,49,76),(7442,49,146),(7443,49,166),(7444,49,170),(7445,49,159),(7446,49,179),(7447,49,182),(7448,49,147),(7449,49,78),(7450,49,1),(7451,49,73),(7452,49,7),(7453,49,55),(7454,49,56),(7455,49,62),(7456,49,15),(7457,49,2),(7458,49,61),(7459,49,20),(7460,49,21),(7461,49,22),(7462,49,-1),(7463,49,28),(7464,49,29),(7465,49,30),(7466,49,92),(7467,49,27),(7468,49,57),(7469,49,14),(7470,49,24),(7471,49,6),(7472,49,91),(7473,49,3),(7474,52,146),(7475,52,148),(7476,52,159),(7477,52,168),(7478,52,175),(7479,52,176),(7480,52,177),(7481,52,178),(7482,52,166),(7483,52,160),(7484,52,170),(7485,52,179),(7486,52,180),(7487,52,181),(7488,52,182),(7489,52,183),(7490,52,-1),(7491,1,148),(7492,1,168),(7493,1,175),(7494,1,176),(7495,1,160),(7496,1,177),(7497,1,178),(7498,1,180),(7499,1,183),(7500,1,157),(7501,1,92),(7502,1,28),(7503,1,29),(7504,1,30),(7505,1,57),(7506,1,79),(7507,1,80),(7508,1,81),(7509,1,83),(7510,1,71),(7511,1,12),(7512,1,13),(7513,1,14),(7514,1,24),(7515,1,25),(7516,1,26),(7517,1,55),(7518,1,56),(7519,1,62),(7520,1,15),(7521,1,61),(7522,1,20),(7523,1,21),(7524,1,22),(7525,1,74),(7526,1,75),(7527,1,76),(7528,1,146),(7529,1,166),(7530,1,170),(7531,1,159),(7532,1,182),(7533,1,147),(7534,1,27),(7535,1,91),(7536,1,78),(7537,1,1),(7538,1,6),(7539,1,7),(7540,1,2),(7541,1,73),(7542,1,3),(7543,1,184),(7544,1,179),(7545,1,-1);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_task`
--

DROP TABLE IF EXISTS `sys_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron表达式',
  `method_name` varchar(255) DEFAULT NULL COMMENT '任务调用的方法名',
  `is_concurrent` varchar(255) DEFAULT NULL COMMENT '任务是否有状态',
  `description` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `bean_class` varchar(255) DEFAULT NULL COMMENT '任务执行时调用哪个类的方法 包名+类名',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `job_status` varchar(255) DEFAULT NULL COMMENT '任务状态',
  `job_group` varchar(255) DEFAULT NULL COMMENT '任务分组',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `spring_bean` varchar(255) DEFAULT NULL COMMENT 'Spring bean',
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_task`
--

LOCK TABLES `sys_task` WRITE;
/*!40000 ALTER TABLE `sys_task` DISABLE KEYS */;
INSERT INTO `sys_task` VALUES (14,'0 0 20 * * ? *',NULL,NULL,'每日20点自动备份数据库',NULL,'com.chason.common.task.JobBackupDb',NULL,'0','dataCheckJob',NULL,NULL,NULL,'数据备份'),(20,'0 0/1 * * * ? ',NULL,NULL,'每一个小时校时一次',NULL,'com.chason.common.task.JobDeviceCheckTime',NULL,'0','dataCheckJob',NULL,NULL,NULL,'延时设置');
/*!40000 ALTER TABLE `sys_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `dept_id` bigint(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(255) DEFAULT NULL COMMENT '状态 0:禁用，1:正常',
  `user_id_create` bigint(255) DEFAULT NULL COMMENT '创建用户id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `sex` bigint(32) DEFAULT NULL COMMENT '性别',
  `birth` datetime DEFAULT NULL COMMENT '出身日期',
  `pic_id` bigint(32) DEFAULT NULL,
  `live_address` varchar(500) DEFAULT NULL COMMENT '现居住地',
  `hobby` varchar(255) DEFAULT NULL COMMENT '爱好',
  `province` varchar(255) DEFAULT NULL COMMENT '省份',
  `city` varchar(255) DEFAULT NULL COMMENT '所在城市',
  `district` varchar(255) DEFAULT NULL COMMENT '所在地区',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','总经理','d1e2292b8991e896b272a37e1c9be3ad',9,'admin@example.com','123456',1,1,'2017-08-15 21:40:39','2017-08-15 21:41:00',NULL,NULL,170,NULL,NULL,NULL,NULL,NULL),(139,'manager','副总经理','18e9a6e0b2e8abab4fbb09a24e7098b9',9,'manager@example.com',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(140,'user01','普通用户','f811545e9532d73b88dfd07799f35aab',11,'user01@example.com',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(141,'chason','chason','9f8efd748ef4f7338d41fb1e492f1cae',9,'chason001@126.com',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (73,30,48),(74,30,49),(75,30,50),(76,31,48),(77,31,49),(78,31,52),(79,32,48),(80,32,49),(81,32,50),(82,32,51),(83,32,52),(84,33,38),(85,33,49),(86,33,52),(87,34,50),(88,34,51),(89,34,52),(124,NULL,48),(149,139,49),(150,1,1),(151,141,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_cluster`
--

DROP TABLE IF EXISTS `tbl_cluster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_cluster` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `cluster_name` varchar(64) NOT NULL,
  `cluster_note` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `delete_time` datetime DEFAULT NULL,
  `is_deleted` int(11) NOT NULL,
  `creator` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_cluster`
--

LOCK TABLES `tbl_cluster` WRITE;
/*!40000 ALTER TABLE `tbl_cluster` DISABLE KEYS */;
INSERT INTO `tbl_cluster` VALUES ('c10fbf8b-93c3-4817-9a3f-9e14f9e5d32b','d80demo_ldr','同城集群',1,'2021-06-11 14:40:02',NULL,0,'admin'),('f4c0022d-13f6-4834-b432-d9154444c064','d80demo_prd','生产集群',0,'2021-06-11 14:38:12',NULL,0,'admin');
/*!40000 ALTER TABLE `tbl_cluster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_company`
--

DROP TABLE IF EXISTS `tbl_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_company` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `company_code` varchar(255) NOT NULL,
  `company_name` varchar(64) NOT NULL,
  `business_nature` varchar(64) NOT NULL,
  `representative` varchar(32) NOT NULL COMMENT '法人代表',
  `representative_phone` varchar(32) NOT NULL,
  `connector` varchar(32) DEFAULT NULL,
  `connector_phone` varchar(32) DEFAULT NULL,
  `company_nature` varchar(64) NOT NULL,
  `staff_number` int(11) NOT NULL,
  `regist_address` varchar(255) DEFAULT NULL,
  `product` varchar(32) DEFAULT NULL,
  `is_list` varchar(32) NOT NULL,
  `list_note` varchar(255) NOT NULL,
  `social_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_company`
--

LOCK TABLES `tbl_company` WRITE;
/*!40000 ALTER TABLE `tbl_company` DISABLE KEYS */;
INSERT INTO `tbl_company` VALUES ('249b0f5c-9c0b-44a8-a89a-820788343b40','JG1000001','杭州阿里巴巴','互联网','马云','13100010001','小王','13900020002','互联网',20065,NULL,NULL,'已上市','阿里巴巴于纳斯达克上市							',18000),('9a229a6b-235d-4438-90cc-ff0642841bc9','JM0232212','北京京东','互联网','刘强东','13909918898','章泽天','13909918897','互联网',15688,NULL,NULL,'已上市','已于纳斯达克上市						',14700),('e5b9dee7-6b41-4cf5-af96-c7d25a5325f8','GC0000014','上海新能源有限公司','新能源','陈胜','18321810247','李锂','13909918897','新能源',3,NULL,NULL,'计划上市','小公司，慢慢来，先做大做强		',3);
/*!40000 ALTER TABLE `tbl_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_device`
--

DROP TABLE IF EXISTS `tbl_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_device` (
  `dev_id` varchar(255) NOT NULL COMMENT '主键',
  `dev_code` varchar(255) DEFAULT NULL COMMENT '设备代号',
  `dev_number` varchar(255) DEFAULT NULL COMMENT '设备序列号',
  `dev_group_code` varchar(255) DEFAULT NULL COMMENT '设备分组号',
  `dev_type` varchar(255) DEFAULT NULL COMMENT '设备类型',
  `dev_reg_by` varchar(255) DEFAULT NULL COMMENT '注册人',
  `dev_reg_time` datetime DEFAULT NULL COMMENT '注册时间',
  `dev_online_last_time` datetime DEFAULT NULL COMMENT '最近在线时间',
  `dev_status` varchar(255) DEFAULT NULL COMMENT '设备状态',
  `dev_switch_mode` varchar(255) DEFAULT NULL,
  `dev_policy` text,
  `dev_switch_hand_time` datetime DEFAULT NULL,
  `dev_random` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dev_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_device`
--

LOCK TABLES `tbl_device` WRITE;
/*!40000 ALTER TABLE `tbl_device` DISABLE KEYS */;
INSERT INTO `tbl_device` VALUES ('638160ddc0a8053526cf3032a0c0c081','192.168.5.225','515338122187','f59665b5c0a805346f17d9e4ceb64468,FM0101,FM01','ZTOWER','自动获取','2019-06-17 11:35:51','2019-06-17 11:43:46','11','自动','[\"16:00~16:10\",\"16:15~16:25\"]','2019-06-17 11:38:18','fab19801c0a805344993e127bdd76a06,fb0bdca7c0a805346a6cd7ba8d42eee3'),('63819859c0a80535364c52f8eb07fbf3','192.168.5.226','525564392087','f59695f6c0a805341e5de53ab97107e0,FM0101,FM01','ZTOWER','自动获取','2019-06-17 11:36:05','2019-06-17 11:43:48','11','自动','[\"16:00~16:10\",\"16:15~16:25\"]','2019-06-17 11:38:20','fab19801c0a805344993e127bdd76a06,fb0bdca7c0a805346a6cd7ba8d42eee3'),('6381d46ac0a8053576fb89507bc88d91','192.168.5.228','495028341687','f596be10c0a805343a08d98f0f998e99,FM0101,FM01','ZTOWER','自动获取','2019-06-17 11:36:21','2019-06-17 11:43:48','11','自动','[\"16:00~16:10\",\"16:15~16:25\"]','2019-06-17 11:38:20','fab19801c0a805344993e127bdd76a06,fb0bdca7c0a805346a6cd7ba8d42eee3'),('638208dfc0a805355bf3dfa9817dfa6e','192.168.5.229','525555242387','f596ebf1c0a80534752ef338b0ec9369,FM0101,FM01','ZTOWER','自动获取','2019-06-17 11:36:34','2019-06-17 11:43:46','11','自动','[\"16:00~16:10\",\"16:15~16:25\"]','2019-06-17 11:38:18','fab19801c0a805344993e127bdd76a06,fb0bdca7c0a805346a6cd7ba8d42eee3'),('63824f00c0a8053547033c5f91ad4d17','192.168.5.230','525541672487',NULL,'ZTOWER','自动获取','2019-06-17 11:36:52','2019-06-17 11:43:45','11','自动','-','2019-06-17 11:38:18',NULL),('6382777ac0a805357ba659bdb77a4553','192.168.5.231','525732391687',NULL,'ZTOWER','自动获取','2019-06-17 11:37:03','2019-06-17 11:43:47','11','自动','-','2019-06-17 11:38:19',NULL),('6382b668c0a80535398b161d42497075','192.168.5.15','495038221787',NULL,'ZTOWER','自动获取','2019-06-17 11:37:19','2019-06-28 09:33:01','11','自动','-','2019-06-28 09:32:21',NULL),('6382e9b8c0a80535195a659f8fb9ecb3','192.168.5.233','504973541467',NULL,'ZTOWER','自动获取','2019-06-17 11:37:32','2019-06-17 11:43:48','11','自动','-','2019-06-17 11:38:21',NULL),('63831bf1c0a8053538bb4c9d4db09532','192.168.5.234','525752201787',NULL,'ZTOWER','自动获取','2019-06-17 11:37:45','2019-06-17 11:43:46','11','自动','-','2019-06-17 11:38:18',NULL);
/*!40000 ALTER TABLE `tbl_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_lesson`
--

DROP TABLE IF EXISTS `tbl_lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_lesson` (
  `lesson_id` int(11) NOT NULL COMMENT '主键',
  `count` int(11) NOT NULL COMMENT '一共有多少单词',
  `learned` int(11) NOT NULL COMMENT '已经掌握的单词数',
  `learned_time` int(11) NOT NULL COMMENT '课程被学习的次数',
  `passed` int(11) NOT NULL COMMENT '已经通过考试的单词数',
  `pass_time` datetime DEFAULT NULL COMMENT '整个课程通过考试的时间',
  `lesson` varchar(32) NOT NULL COMMENT 'lesson 名称',
  PRIMARY KEY (`lesson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_lesson`
--

LOCK TABLES `tbl_lesson` WRITE;
/*!40000 ALTER TABLE `tbl_lesson` DISABLE KEYS */;
INSERT INTO `tbl_lesson` VALUES (1,0,0,0,0,'2024-08-15 00:00:00','Lesson1');
/*!40000 ALTER TABLE `tbl_lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_policy`
--

DROP TABLE IF EXISTS `tbl_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_policy` (
  `policy_id` varchar(255) NOT NULL,
  `policy_name` varchar(255) DEFAULT NULL,
  `policy_type` varchar(255) DEFAULT NULL,
  `policy_week` varchar(255) DEFAULT NULL,
  `policy_start_time` time DEFAULT NULL,
  `policy_dur` int(11) DEFAULT NULL,
  `policy_space_keys` text,
  PRIMARY KEY (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_policy`
--

LOCK TABLES `tbl_policy` WRITE;
/*!40000 ALTER TABLE `tbl_policy` DISABLE KEYS */;
INSERT INTO `tbl_policy` VALUES ('fab19801c0a805344993e127bdd76a06','随机策略1','随机策略','2,3,4,5,6','16:00:00',10,'f59665b5c0a805346f17d9e4ceb64468,f59695f6c0a805341e5de53ab97107e0,f596be10c0a805343a08d98f0f998e99,f596ebf1c0a80534752ef338b0ec9369,e27f7e17c0a8053404c0c0d2d69f6849,-1,e26f4451c0a805347cdb8819b3e51f3a,'),('fb0bdca7c0a805346a6cd7ba8d42eee3','随机策略2','随机策略','2,3,4,5,6','16:15:00',10,'e27f7e17c0a8053404c0c0d2d69f6849,f59665b5c0a805346f17d9e4ceb64468,f59695f6c0a805341e5de53ab97107e0,f596be10c0a805343a08d98f0f998e99,f596ebf1c0a80534752ef338b0ec9369,-1,e26f4451c0a805347cdb8819b3e51f3a,');
/*!40000 ALTER TABLE `tbl_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_space`
--

DROP TABLE IF EXISTS `tbl_space`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_space` (
  `space_id` varchar(255) NOT NULL,
  `space_parent_id` varchar(255) DEFAULT NULL,
  `space_code` varchar(255) DEFAULT NULL,
  `space_address` varchar(255) DEFAULT NULL,
  `space_manager_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`space_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_space`
--

LOCK TABLES `tbl_space` WRITE;
/*!40000 ALTER TABLE `tbl_space` DISABLE KEYS */;
INSERT INTO `tbl_space` VALUES ('e26f4451c0a805347cdb8819b3e51f3a','','FM01','女生宿舍12号楼','139,1,140,9,11,-1,'),('e27d7be7c0a805345f0646d6a5ca10f8','','FM02','女生宿舍2号楼','9,139,1,-1,'),('e27f7e17c0a8053404c0c0d2d69f6849','e26f4451c0a805347cdb8819b3e51f3a','FM0101','女12号楼1层','139,1,9,-1,'),('e27fbb99c0a805344d62e17ddaddfc24','e26f4451c0a805347cdb8819b3e51f3a','FM0102','女12号楼2层','139,1,9,-1,'),('e27ff5a4c0a8053419f57dd29f6e8f74','e26f4451c0a805347cdb8819b3e51f3a','FM0103','女12号楼3层','139,1,9,-1,'),('e2806956c0a805344ac37ef13051ad8b','e26f4451c0a805347cdb8819b3e51f3a','FM0104','女12号楼4层','139,1,9,-1,'),('e280b159c0a805344c29d6c39f45a8fe','e26f4451c0a805347cdb8819b3e51f3a','FM0105','女12号楼5层','139,1,9,-1,'),('e28482b8c0a80534674ddcae3d6de376','e26f4451c0a805347cdb8819b3e51f3a','FM0106','女12号楼6层','139,1,9,-1,'),('e2850654c0a8053471f505aa76045cc7','e26f4451c0a805347cdb8819b3e51f3a','FM0107','女12号楼7层','139,1,9,-1,'),('e2854f8fc0a8053449a795b69a512a02','e27d7be7c0a805345f0646d6a5ca10f8','FM0201','女2号楼1层','139,1,9,-1,'),('e2859cc9c0a8053461949f579189b56b','e27d7be7c0a805345f0646d6a5ca10f8','FM0202','女2号楼2层','139,1,9,-1,'),('e62ddc44dbdcdde450d1cf51698df899','e27d7be7c0a805345f0646d6a5ca10f8','FM0203','女2号楼3层','139,1,9,-1,'),('e62e02a4dbdcdde40b044523693fb789','e27d7be7c0a805345f0646d6a5ca10f8','FM0204','女2号楼4层','139,1,9,-1,'),('e62e2baddbdcdde4116ed93b545dde51','e27d7be7c0a805345f0646d6a5ca10f8','FM0205','女2号楼5层','139,1,9,-1,'),('e62e4be7dbdcdde44174866881a89ed9','e27d7be7c0a805345f0646d6a5ca10f8','FM0206','女2号楼6层','139,1,9,-1,'),('e62e7aa8dbdcdde445b896a0cade77eb','e27d7be7c0a805345f0646d6a5ca10f8','FM0207','女2号楼7层','139,1,9,-1,'),('f59665b5c0a805346f17d9e4ceb64468','e27f7e17c0a8053404c0c0d2d69f6849','f59665b5c0a805346f17d9e4ceb64468','女12号楼1层-1单元','-1,139,1,140,9,11,13,'),('f59695f6c0a805341e5de53ab97107e0','e27f7e17c0a8053404c0c0d2d69f6849','f59695f6c0a805341e5de53ab97107e0','女12号楼1层-2单元','-1,139,1,140,9,11,13,'),('f596be10c0a805343a08d98f0f998e99','e27f7e17c0a8053404c0c0d2d69f6849','f596be10c0a805343a08d98f0f998e99','女12号楼1层-3单元','-1,139,1,140,9,11,13,'),('f596ebf1c0a80534752ef338b0ec9369','e27f7e17c0a8053404c0c0d2d69f6849','f596ebf1c0a80534752ef338b0ec9369','女12号楼1层-4单元','-1,139,1,140,9,11,13,');
/*!40000 ALTER TABLE `tbl_space` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_table`
--

DROP TABLE IF EXISTS `tbl_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_table` (
  `id` int(11) NOT NULL,
  `schema_name` varchar(32) NOT NULL,
  `table_name` varchar(32) NOT NULL,
  `table_type` varchar(32) NOT NULL,
  `shards` int(11) NOT NULL,
  `split_key` varchar(32) NOT NULL,
  `rule_name` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_table`
--

LOCK TABLES `tbl_table` WRITE;
/*!40000 ALTER TABLE `tbl_table` DISABLE KEYS */;
INSERT INTO `tbl_table` VALUES (1,'demo','t_list','split',8,'col1','r_list'),(2,'demo','t_hash','split',8,'col1','r_hash'),(3,'demo','t_range','split',8,'col1','r_range'),(4,'demo','t_day','split',8,'col1','r_day');
/*!40000 ALTER TABLE `tbl_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_word`
--

DROP TABLE IF EXISTS `tbl_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_word` (
  `word` varchar(255) NOT NULL COMMENT '英语单词',
  `type` varchar(32) NOT NULL COMMENT '类型',
  `lesson` int(11) DEFAULT NULL COMMENT '第几课',
  `zh_mean` varchar(255) NOT NULL COMMENT '中文含义',
  `learn_time` int(11) NOT NULL DEFAULT '0' COMMENT '学习次数',
  `learned` int(11) NOT NULL COMMENT '是否学会',
  `last_review_time` datetime DEFAULT NULL COMMENT '上次复习时间',
  `create_time` datetime DEFAULT NULL COMMENT '录入时间',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `idx_lesson` (`lesson`),
  KEY `idx_review` (`last_review_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_word`
--

LOCK TABLES `tbl_word` WRITE;
/*!40000 ALTER TABLE `tbl_word` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_word` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-15 18:02:51
