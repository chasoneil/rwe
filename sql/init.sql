-- MySQL dump 10.13  Distrib 5.7.30, for linux-glibc2.12 (x86_64)
--
-- Host: 192.168.12.217    Database: rwe
-- ------------------------------------------------------
-- Server version	5.7.31-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
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
INSERT INTO `sys_dict` VALUES (1979,'基础主题','basic','cms_theme','站点主题',10,0,1,NULL,1,NULL,NULL,'0'),(1980,'蓝色主题','blue','cms_theme','站点主题',20,0,1,NULL,1,NULL,NULL,'1'),(1981,'红色主题','red','cms_theme','站点主题',30,0,1,NULL,1,NULL,NULL,'1'),(1982,'红色','red','color','颜色值',10,0,1,NULL,1,NULL,NULL,'0'),(1983,'绿色','green','color','颜色值',20,0,1,NULL,1,NULL,NULL,'0'),(1984,'蓝色','blue','color','颜色值',30,0,1,NULL,1,NULL,NULL,'0'),(1985,'黄色','yellow','color','颜色值',40,0,1,NULL,1,NULL,NULL,'0'),(1986,'橙色','orange','color','颜色值',50,0,1,NULL,1,NULL,NULL,'0'),(1999,'删除','0','del_flag','删除标记',NULL,NULL,NULL,NULL,NULL,NULL,'',''),(2000,'正常','0','del_flag','删除标记',10,0,1,NULL,1,NULL,NULL,'0'),(2004,'增删改查','crud','gen_category','代码生成分类',10,0,1,NULL,1,NULL,NULL,'1'),(2005,'增删改查（包含从表）','crud_many','gen_category','代码生成分类',20,0,1,NULL,1,NULL,NULL,'1'),(2006,'树结构','tree','gen_category','代码生成分类',30,0,1,NULL,1,NULL,NULL,'1'),(2007,'仅持久层','dao','gen_category','代码生成分类\0\0',40,0,1,NULL,1,NULL,NULL,'1'),(2008,'String','String','gen_java_type','Java类型',10,0,1,NULL,1,NULL,NULL,'1'),(2009,'Long','Long','gen_java_type','Java类型',20,0,1,NULL,1,NULL,NULL,'1'),(2010,'Integer','Integer','gen_java_type','Java类型',30,0,1,NULL,1,NULL,NULL,'1'),(2011,'Double','Double','gen_java_type','Java类型',40,0,1,NULL,1,NULL,NULL,'1'),(2012,'Date','java.util.Date','gen_java_type','Java类型',50,0,1,NULL,1,NULL,NULL,'1'),(2013,'Custom','Custom','gen_java_type','Java类型',90,0,1,NULL,1,NULL,NULL,'1'),(2014,'=','=','gen_query_type','查询方式',10,0,1,NULL,1,NULL,NULL,'1'),(2015,'!=','!=','gen_query_type','查询方式',20,0,1,NULL,1,NULL,NULL,'1'),(2016,'&gt;','&gt;','gen_query_type','查询方式',30,0,1,NULL,1,NULL,NULL,'1'),(2017,'&lt;','&lt;','gen_query_type','查询方式',40,0,1,NULL,1,NULL,NULL,'1'),(2018,'Between','between','gen_query_type','查询方式',50,0,1,NULL,1,NULL,NULL,'1'),(2019,'Like','like','gen_query_type','查询方式',60,0,1,NULL,1,NULL,NULL,'1'),(2020,'Left Like','left_like','gen_query_type','查询方式',70,0,1,NULL,1,NULL,NULL,'1'),(2021,'Right Like','right_like','gen_query_type','查询方式',80,0,1,NULL,1,NULL,NULL,'1'),(2022,'文本框','input','gen_show_type','字段生成方案',10,0,1,NULL,1,NULL,NULL,'1'),(2023,'文本域','textarea','gen_show_type','字段生成方案',20,0,1,NULL,1,NULL,NULL,'1'),(2024,'下拉框','select','gen_show_type','字段生成方案',30,0,1,NULL,1,NULL,NULL,'1'),(2025,'复选框','checkbox','gen_show_type','字段生成方案',40,0,1,NULL,1,NULL,NULL,'1'),(2026,'单选框','radiobox','gen_show_type','字段生成方案',50,0,1,NULL,1,NULL,NULL,'1'),(2027,'日期选择','dateselect','gen_show_type','字段生成方案',60,0,1,NULL,1,NULL,NULL,'1'),(2028,'人员选择','userselect','gen_show_type','字段生成方案',70,0,1,NULL,1,NULL,NULL,'1'),(2029,'部门选择','officeselect','gen_show_type','字段生成方案',80,0,1,NULL,1,NULL,NULL,'1'),(2030,'区域选择','areaselect','gen_show_type','字段生成方案',90,0,1,NULL,1,NULL,NULL,'1'),(2032,'系统数据处理','dataCheckJob','job_group','计划任务分类',2,NULL,NULL,NULL,NULL,NULL,'',NULL),(2033,'公休','1','oa_leave_type','请假类型',10,0,1,NULL,1,NULL,NULL,'0'),(2034,'病假','2','oa_leave_type','请假类型',20,0,1,NULL,1,NULL,NULL,'0'),(2035,'事假','3','oa_leave_type','请假类型',30,0,1,NULL,1,NULL,NULL,'0'),(2036,'调休','4','oa_leave_type','请假类型',40,0,1,NULL,1,NULL,NULL,'0'),(2037,'婚假','5','oa_leave_type','请假类型',60,0,1,NULL,1,NULL,NULL,'0'),(2038,'未读','0','oa_notify_read','通知阅知状态',10,0,1,NULL,1,NULL,'','0'),(2039,'已读','1','oa_notify_read','通知阅知状态',20,0,1,NULL,1,NULL,'','0'),(2040,'草稿','0','oa_notify_status','通知通告状态',10,0,1,NULL,1,NULL,NULL,'0'),(2041,'草稿','0','oa_notify_status','通知通告状态',10,0,1,NULL,1,NULL,'','0'),(2042,'发布','1','oa_notify_status','通知通告状态',20,0,1,NULL,1,NULL,NULL,'0'),(2043,'信息提示','1','oa_notify_type','通知公告类型',10,NULL,NULL,NULL,NULL,NULL,'',NULL),(2044,'活动通告','3','oa_notify_type','通知通告类型',30,0,1,NULL,1,NULL,NULL,'0'),(2045,'男','1','sex','性别',10,0,1,NULL,1,NULL,NULL,'0'),(2046,'女','2','sex','性别',20,0,1,NULL,1,NULL,NULL,'0'),(2047,'显示','1','show_hide','显示/隐藏',10,0,1,NULL,1,NULL,NULL,'0'),(2048,'隐藏','0','show_hide','显示/隐藏',20,0,1,NULL,1,NULL,NULL,'0'),(2049,'国家','1','sys_area_type','区域类型',10,0,1,NULL,1,NULL,NULL,'0'),(2050,'省份、直辖市','2','sys_area_type','区域类型',20,0,1,NULL,1,NULL,NULL,'0'),(2051,'地市','3','sys_area_type','区域类型',30,0,1,NULL,1,NULL,NULL,'0'),(2052,'区县','4','sys_area_type','区域类型',40,0,1,NULL,1,NULL,NULL,'0'),(2053,'所有数据','1','sys_data_scope','数据范围',10,0,1,NULL,1,NULL,NULL,'0'),(2054,'所在公司及以下数据','2','sys_data_scope','数据范围',20,0,1,NULL,1,NULL,NULL,'0'),(2055,'所在公司数据','3','sys_data_scope','数据范围',30,0,1,NULL,1,NULL,NULL,'0'),(2056,'所在部门及以下数据','4','sys_data_scope','数据范围',40,0,1,NULL,1,NULL,NULL,'0'),(2057,'所在部门数据','5','sys_data_scope','数据范围',50,0,1,NULL,1,NULL,NULL,'0'),(2058,'仅本人数据','8','sys_data_scope','数据范围',90,0,1,NULL,1,NULL,NULL,'0'),(2059,'按明细设置','9','sys_data_scope','数据范围',100,0,1,NULL,1,NULL,NULL,'0'),(2060,'接入日志','1','sys_log_type','日志类型',30,0,1,NULL,1,NULL,NULL,'0'),(2061,'异常日志','2','sys_log_type','日志类型',40,0,1,NULL,1,NULL,NULL,'0'),(2062,'综合部','1','sys_office_common','快捷通用部门',30,0,1,NULL,1,NULL,NULL,'0'),(2063,'开发部','2','sys_office_common','快捷通用部门',40,0,1,NULL,1,NULL,NULL,'0'),(2064,'人力部','3','sys_office_common','快捷通用部门',50,0,1,NULL,1,NULL,NULL,'0'),(2065,'一级','1','sys_office_grade','机构等级',10,0,1,NULL,1,NULL,NULL,'0'),(2066,'二级','2','sys_office_grade','机构等级',20,0,1,NULL,1,NULL,NULL,'0'),(2067,'三级','3','sys_office_grade','机构等级',30,0,1,NULL,1,NULL,NULL,'0'),(2068,'四级','4','sys_office_grade','机构等级',40,0,1,NULL,1,NULL,NULL,'0'),(2069,'公司','1','sys_office_type','机构类型',60,0,1,NULL,1,NULL,NULL,'0'),(2070,'部门','2','sys_office_type','机构类型',70,0,1,NULL,1,NULL,NULL,'0'),(2071,'小组','3','sys_office_type','机构类型',80,0,1,NULL,1,NULL,NULL,'0'),(2072,'其它','4','sys_office_type','机构类型',90,0,1,NULL,1,NULL,NULL,'0'),(2073,'系统管理','1','sys_user_type','用户类型',10,0,1,NULL,1,NULL,NULL,'0'),(2074,'部门经理','2','sys_user_type','用户类型',20,0,1,NULL,1,NULL,NULL,'0'),(2075,'普通用户','3','sys_user_type','用户类型',30,0,1,NULL,1,NULL,NULL,'0'),(2076,'默认主题','default','theme','主题方案',10,0,1,NULL,1,NULL,NULL,'0'),(2077,'天蓝主题','cerulean','theme','主题方案',20,0,1,NULL,1,NULL,NULL,'0'),(2078,'橙色主题','readable','theme','主题方案',30,0,1,NULL,1,NULL,NULL,'0'),(2079,'红色主题','united','theme','主题方案',40,0,1,NULL,1,NULL,NULL,'0'),(2080,'Flat主题','flat','theme','主题方案',60,0,1,NULL,1,NULL,NULL,'0'),(2081,'星期一','1','week_num','星期分类',1,NULL,NULL,NULL,NULL,NULL,'',NULL),(2082,'星期二','2','week_num','星期分类',2,NULL,NULL,NULL,NULL,NULL,'',NULL),(2083,'星期三','3','week_num','星期分类',3,NULL,NULL,NULL,NULL,NULL,'',NULL),(2084,'星期四','4','week_num','星期分类',4,NULL,NULL,NULL,NULL,NULL,'',NULL),(2085,'星期五','5','week_num','星期分类',5,NULL,NULL,NULL,NULL,NULL,'',NULL),(2086,'星期六','6','week_num','星期分类',6,NULL,NULL,NULL,NULL,NULL,'',NULL),(2087,'星期日','7','week_num','星期分类',7,NULL,NULL,NULL,NULL,NULL,'',NULL),(2088,'是','1','yes_no','是/否',10,0,1,NULL,1,NULL,NULL,'0'),(2089,'否','0','yes_no','是/否',20,0,1,NULL,1,NULL,NULL,'0'),(2090,'工作日单','odd','policy_work_type','定时策略类型',NULL,NULL,NULL,NULL,NULL,NULL,'',NULL),(2091,'工作日双','even','policy_work_type','定时策略类型',NULL,NULL,NULL,NULL,NULL,NULL,'',NULL),(2092,'不限定','other','policy_work_type','定时策略类型',NULL,NULL,NULL,'2019-04-04 17:54:19',NULL,'2019-04-04 17:54:23','',''),(2093,'超级管理员','super','role_type','角色权限类型',40,NULL,NULL,NULL,NULL,NULL,'',NULL),(2094,'普通用户权限','normal','role_type','角色权限类型',40,NULL,NULL,NULL,NULL,NULL,'',NULL),(2095,'管理员','admin','role_type','角色权限类型',40,NULL,NULL,NULL,NULL,NULL,'',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=758 DEFAULT CHARSET=utf8 COMMENT='系统日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=192 DEFAULT CHARSET=utf8 COMMENT='菜单管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'基础管理','','',0,'fa fa-bars',95,'2017-08-09 22:49:47',NULL),(2,3,'系统菜单','sys/menu/','sys:menu:menu',1,'fa fa-th-list',2,'2017-08-09 22:55:15',NULL),(3,0,'系统管理','','',0,'fa fa-desktop',99,'2017-08-09 23:06:55','2017-08-14 14:13:43'),(6,3,'用户管理','sys/user/','sys:user:user',1,'fa fa-user',0,'2017-08-10 14:12:11',NULL),(7,3,'角色管理','sys/role','sys:role:role',1,'fa fa-paw',1,'2017-08-10 14:13:19',NULL),(12,6,'新增','','sys:user:add',2,'',0,'2017-08-14 10:51:35',NULL),(13,6,'编辑','','sys:user:edit',2,'',0,'2017-08-14 10:52:06',NULL),(14,6,'删除',NULL,'sys:user:remove',2,NULL,0,'2017-08-14 10:52:24',NULL),(15,7,'新增','','sys:role:add',2,'',0,'2017-08-14 10:56:37',NULL),(20,2,'新增','','sys:menu:add',2,'',0,'2017-08-14 10:59:32',NULL),(21,2,'编辑','','sys:menu:edit',2,'',0,'2017-08-14 10:59:56',NULL),(22,2,'删除','','sys:menu:remove',2,'',0,'2017-08-14 11:00:26',NULL),(24,6,'批量删除','','sys:user:batchRemove',2,'',0,'2017-08-14 17:27:18',NULL),(25,6,'停用',NULL,'sys:user:disable',2,NULL,0,'2017-08-14 17:27:43',NULL),(26,6,'重置密码','','sys:user:resetPwd',2,'',0,'2017-08-14 17:28:34',NULL),(27,91,'系统日志','common/log','common:log',1,'fa fa-warning',0,'2017-08-14 22:11:53',NULL),(28,27,'刷新',NULL,'sys:log:list',2,NULL,0,'2017-08-14 22:30:22',NULL),(29,27,'删除',NULL,'sys:log:remove',2,NULL,0,'2017-08-14 22:30:43',NULL),(30,27,'清空',NULL,'sys:log:clear',2,NULL,0,'2017-08-14 22:31:02',NULL),(55,7,'编辑','','sys:role:edit',2,'',NULL,NULL,NULL),(56,7,'删除','','sys:role:remove',2,NULL,NULL,NULL,NULL),(57,91,'运行监控','/druid/index.html','',1,'fa fa-caret-square-o-right',1,NULL,NULL),(61,2,'批量删除','','sys:menu:batchRemove',2,NULL,NULL,NULL,NULL),(62,7,'批量删除','','sys:role:batchRemove',2,NULL,NULL,NULL,NULL),(71,1,'文件管理','/common/sysFile','common:sysFile:sysFile',1,'fa fa-folder-open',2,NULL,NULL),(73,3,'部门管理','/system/sysDept','system:sysDept:sysDept',1,'fa fa-users',3,NULL,NULL),(74,73,'增加','/system/sysDept/add','system:sysDept:add',2,'fa fa-wifi',1,NULL,NULL),(75,73,'刪除','system/sysDept/remove','system:sysDept:remove',2,NULL,2,NULL,NULL),(76,73,'编辑','/system/sysDept/edit','system:sysDept:edit',2,NULL,3,NULL,NULL),(78,1,'数据字典','/common/sysDict','common:sysDict:sysDict',1,'fa fa-book',1,NULL,NULL),(79,78,'增加','/common/sysDict/add','common:sysDict:add',2,NULL,2,NULL,NULL),(80,78,'编辑','/common/sysDict/edit','common:sysDict:edit',2,NULL,2,NULL,NULL),(81,78,'删除','/common/sysDict/remove','common:sysDict:remove',2,'',3,NULL,NULL),(83,78,'批量删除','/common/sysDict/batchRemove','common:sysDict:batchRemove',2,'',4,NULL,NULL),(91,0,'系统监控','','',0,'fa fa-video-camera',90,NULL,NULL),(92,91,'在线用户','sys/online','',1,'fa fa-user',NULL,NULL,NULL),(147,0,'任务管理','','',0,'fa fa-gears',60,NULL,NULL),(157,147,'计划任务','common/job','common:taskScheduleJob',1,'fa fa-hourglass-1',8,NULL,NULL),(179,0,'背单词','','',0,'fa fa-book',30,NULL,NULL),(180,179,'选课背','/rwe/lesson','rwe:lesson',1,'fa fa-pencil-square-o',NULL,NULL,NULL),(184,179,'随机测验','/rwe/lesson/exam','rwe:lesson',1,'fa fa-calendar-check-o',NULL,NULL,NULL),(185,180,'添加课程','/rwe/lesson/add','rwe:lesson:add',2,'',NULL,NULL,NULL),(186,0,'账单管理','','',1,'fa fa-calendar',35,NULL,NULL),(187,186,'我的账单','/rwe/trade/index','',1,'fa fa-building',2,NULL,NULL),(188,186,'记账','/rwe/keep_account','',1,'fa fa-calendar',3,NULL,NULL),(189,186,'分类和预算设置','/rwe/consume_category','',1,'fa fa-server',4,NULL,NULL),(191,186,'账单字典设置','/rwe/account/dict','',1,'fa fa-book',10,NULL,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=8270 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (367,44,1),(368,44,32),(369,44,33),(370,44,34),(371,44,35),(372,44,28),(373,44,29),(374,44,30),(375,44,38),(376,44,4),(377,44,27),(378,45,38),(379,46,3),(380,46,20),(381,46,21),(382,46,22),(383,46,23),(384,46,11),(385,46,12),(386,46,13),(387,46,14),(388,46,24),(389,46,25),(390,46,26),(391,46,15),(392,46,2),(393,46,6),(394,46,7),(632,38,42),(1064,54,53),(1095,55,2),(1096,55,6),(1097,55,7),(1098,55,3),(1099,55,50),(1100,55,49),(1101,55,1),(1856,53,28),(1857,53,29),(1858,53,30),(1859,53,27),(1860,53,57),(1861,53,71),(1862,53,48),(1863,53,72),(1864,53,1),(1865,53,7),(1866,53,55),(1867,53,56),(1868,53,62),(1869,53,15),(1870,53,2),(1871,53,61),(1872,53,20),(1873,53,21),(1874,53,22),(2247,63,-1),(2248,63,84),(2249,63,85),(2250,63,88),(2251,63,87),(2252,64,84),(2253,64,89),(2254,64,88),(2255,64,87),(2256,64,86),(2257,64,85),(2258,65,89),(2259,65,88),(2260,65,86),(2262,67,48),(2263,68,88),(2264,68,87),(2265,69,89),(2266,69,88),(2267,69,86),(2268,69,87),(2269,69,85),(2270,69,84),(2271,70,85),(2272,70,89),(2273,70,88),(2274,70,87),(2275,70,86),(2276,70,84),(2277,71,87),(2278,72,59),(2279,73,48),(2280,74,88),(2281,74,87),(2282,75,88),(2283,75,87),(2284,76,85),(2285,76,89),(2286,76,88),(2287,76,87),(2288,76,86),(2289,76,84),(2292,78,88),(2293,78,87),(2294,78,NULL),(2295,78,NULL),(2296,78,NULL),(2308,80,87),(2309,80,86),(2310,80,-1),(2311,80,84),(2312,80,85),(2328,79,72),(2329,79,48),(2330,79,77),(2331,79,84),(2332,79,89),(2333,79,88),(2334,79,87),(2335,79,86),(2336,79,85),(2337,79,-1),(2338,77,89),(2339,77,88),(2340,77,87),(2341,77,86),(2342,77,85),(2343,77,84),(2344,77,72),(2345,77,-1),(2346,77,77),(8157,1,185),(8158,1,184),(8159,1,187),(8160,1,188),(8161,1,189),(8162,1,190),(8163,1,157),(8164,1,92),(8165,1,28),(8166,1,29),(8167,1,30),(8168,1,57),(8169,1,79),(8170,1,80),(8171,1,81),(8172,1,83),(8173,1,71),(8174,1,12),(8175,1,13),(8176,1,14),(8177,1,24),(8178,1,25),(8179,1,26),(8180,1,55),(8181,1,56),(8182,1,62),(8183,1,15),(8184,1,61),(8185,1,20),(8186,1,21),(8187,1,22),(8188,1,74),(8189,1,75),(8190,1,76),(8191,1,180),(8192,1,179),(8193,1,147),(8194,1,27),(8195,1,91),(8196,1,78),(8197,1,1),(8198,1,6),(8199,1,7),(8200,1,2),(8201,1,73),(8202,1,3),(8203,1,191),(8204,1,186),(8205,1,-1),(8217,49,185),(8218,49,184),(8219,49,187),(8220,49,188),(8221,49,189),(8222,49,190),(8223,49,92),(8224,49,28),(8225,49,29),(8226,49,30),(8227,49,57),(8228,49,79),(8229,49,80),(8230,49,81),(8231,49,83),(8232,49,71),(8233,49,12),(8234,49,13),(8235,49,14),(8236,49,24),(8237,49,25),(8238,49,26),(8239,49,55),(8240,49,56),(8241,49,62),(8242,49,15),(8243,49,61),(8244,49,20),(8245,49,21),(8246,49,22),(8247,49,74),(8248,49,75),(8249,49,76),(8250,49,180),(8251,49,179),(8252,49,27),(8253,49,91),(8254,49,78),(8255,49,1),(8256,49,6),(8257,49,7),(8258,49,2),(8259,49,73),(8260,49,3),(8261,49,191),(8262,49,186),(8263,49,-1),(8264,52,187),(8265,52,188),(8266,52,189),(8267,52,191),(8268,52,186),(8269,52,-1);
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
INSERT INTO `sys_task` VALUES (14,'0 0 20 * * ? *',NULL,NULL,'每日20点自动备份数据库',NULL,'com.chason.common.task.JobBackupDb',NULL,'0','dataCheckJob',NULL,NULL,NULL,'数据备份');
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
INSERT INTO `sys_user` VALUES (1,'admin','超级管理员','d1e2292b8991e896b272a37e1c9be3ad',9,'admin@example.com','123456',1,1,'2017-08-15 21:40:39','2017-08-15 21:41:00',NULL,NULL,170,NULL,NULL,NULL,NULL,NULL),(139,'manager','管理员','18e9a6e0b2e8abab4fbb09a24e7098b9',9,'manager@example.com',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(140,'user01','测试用户','f811545e9532d73b88dfd07799f35aab',11,'user01@example.com',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(141,'chason','chason','9f8efd748ef4f7338d41fb1e492f1cae',9,'chason001@126.com',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (73,30,48),(74,30,49),(75,30,50),(76,31,48),(77,31,49),(78,31,52),(79,32,48),(80,32,49),(81,32,50),(82,32,51),(83,32,52),(84,33,38),(85,33,49),(86,33,52),(87,34,50),(88,34,51),(89,34,52),(124,NULL,48),(154,141,52),(156,1,1),(157,139,49),(158,140,52);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_account_dict`
--

DROP TABLE IF EXISTS `tbl_account_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_account_dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `dict_name` varchar(32) NOT NULL COMMENT '数据字典名称',
  `dict_value` varchar(32) NOT NULL COMMENT '选项值',
  `dict_remark` varchar(128) DEFAULT NULL COMMENT '字典备注',
  `sys` smallint(6) NOT NULL DEFAULT '0' COMMENT '是否系统字典',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `dict_name` (`dict_name`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_account_dict`
--

LOCK TABLES `tbl_account_dict` WRITE;
/*!40000 ALTER TABLE `tbl_account_dict` DISABLE KEYS */;
INSERT INTO `tbl_account_dict` VALUES (25,141,'深度支出分类','基本生存支出',NULL,0),(26,141,'深度支出分类','生活改善支出',NULL,0),(27,141,'深度支出分类','提升愉悦支出',NULL,0),(28,141,'深度支出分类','一次性支出',NULL,0),(29,141,'消费人','C',NULL,0),(30,141,'消费人','D',NULL,0),(32,141,'消费人','CF',NULL,0),(33,141,'消费人','DM',NULL,0),(34,141,'消费人','DF',NULL,0),(35,141,'消费人','DMF',NULL,0),(36,141,'账单周期','月',NULL,0),(37,141,'账单周期','日',NULL,0),(38,141,'账单周期','年',NULL,0),(39,141,'账单周期','季度',NULL,0),(40,141,'账单周期','一次性',NULL,0),(41,141,'账单周期','半年',NULL,0),(44,141,'支付方式','支付宝',NULL,0),(45,141,'支付方式','微信',NULL,0),(46,141,'支付方式','信用卡',NULL,0),(47,141,'支付方式','储蓄卡',NULL,0),(48,141,'支付方式','现金',NULL,0),(49,141,'支付方式','其他',NULL,1),(50,141,'支付账户','其他账户',NULL,1),(52,141,'深度支出分类','其他分类',NULL,1),(53,141,'消费人','其他',NULL,1),(54,141,'账单周期','其他',NULL,1);
/*!40000 ALTER TABLE `tbl_account_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_consume_category`
--

DROP TABLE IF EXISTS `tbl_consume_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_consume_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `category_name` varchar(32) NOT NULL COMMENT '一级分类',
  `category_type` varchar(32) NOT NULL COMMENT '二级分类',
  `bill_period` varchar(8) DEFAULT NULL COMMENT '账单周期',
  `deep_type` varchar(32) NOT NULL COMMENT '深度支出分类',
  `level` int(11) NOT NULL DEFAULT '1' COMMENT '菜单级别',
  `parent_id` int(11) NOT NULL DEFAULT '0',
  `in_out` varchar(8) NOT NULL DEFAULT '支出',
  `budget` double DEFAULT NULL COMMENT '预算',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `category_name` (`category_name`),
  KEY `bill_type` (`bill_period`),
  KEY `deep_type` (`deep_type`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_consume_category`
--

LOCK TABLES `tbl_consume_category` WRITE;
/*!40000 ALTER TABLE `tbl_consume_category` DISABLE KEYS */;
INSERT INTO `tbl_consume_category` VALUES (18,141,'服装配饰','服装配饰','-','-',1,0,'支出',NULL),(19,141,'出行支出','出行支出','-','-',1,0,'支出',NULL),(20,141,'居住支出','居住支出','-','-',1,0,'支出',NULL),(30,141,'食品支出','食品支出','-','-',1,0,'支出',12),(31,141,'食品支出','零食','月','生活改善支出',2,30,'支出',NULL),(32,141,'食品支出','外卖','月','基本生存支出',2,30,'支出',NULL),(33,141,'工资','工资','-','-',1,0,'收入',NULL),(34,141,'工资','工资收入','月','-',2,33,'收入',NULL),(35,141,'食品支出','外食','月','生活改善支出',2,30,'支出',NULL),(36,141,'食品支出','食品采购','月','基本生存支出',2,30,'支出',NULL),(37,141,'居住支出','物业费','年','基本生存支出',2,20,'支出',NULL),(38,141,'居住支出','水电燃','月','基本生存支出',2,20,'支出',NULL),(39,141,'居住支出','房租','月','基本生存支出',2,20,'支出',NULL),(40,141,'居住支出','房贷','月','基本生存支出',2,20,'支出',NULL),(41,141,'居住支出','家财险','年','基本生存支出',2,20,'支出',NULL),(42,141,'退款','退款','-','-',1,0,'收入',NULL),(43,141,'红包','红包','-','-',1,0,'收入',NULL),(44,141,'人情往来','人情往来','-','-',1,0,'支出',NULL),(45,141,'人情往来','家人支出','一次性','一次性支出',2,44,'支出',NULL),(46,141,'形象管理','形象管理','-','-',1,0,'支出',NULL),(47,141,'形象管理','理发','月','基本生存支出',2,46,'支出',NULL),(48,141,'文化体育','文化体育','-','-',1,0,'支出',NULL),(49,141,'文化体育','健身器材','一次性','生活改善支出',2,48,'支出',NULL);
/*!40000 ALTER TABLE `tbl_consume_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_keep_account`
--

DROP TABLE IF EXISTS `tbl_keep_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_keep_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `amount` double NOT NULL COMMENT '金额',
  `trade_time` datetime NOT NULL COMMENT '交易时间',
  `trade_variety` varchar(32) NOT NULL COMMENT '交易所属一级菜单',
  `trade_type` varchar(32) NOT NULL COMMENT '交易所属二级菜单',
  `trade_statistics` text NOT NULL COMMENT '交易所属深度支出分类',
  `consumer` varchar(32) DEFAULT NULL,
  `trade_status` varchar(16) NOT NULL COMMENT '交易状态',
  `trade_comment` text COMMENT '备注',
  `checked` int(11) DEFAULT '0' COMMENT '是否checked',
  `trade_period` varchar(8) DEFAULT NULL,
  `in_out` varchar(8) NOT NULL DEFAULT '支出',
  `trade_detail` varchar(256) DEFAULT NULL COMMENT '交易详情',
  `pay_account` varchar(32) DEFAULT NULL,
  `pay_method` varchar(32) DEFAULT NULL COMMENT '付款方式',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `trade_variety` (`trade_variety`),
  KEY `trade_type` (`trade_type`),
  KEY `time_amount` (`trade_time`,`amount`),
  KEY `pay_account` (`pay_account`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_keep_account`
--

LOCK TABLES `tbl_keep_account` WRITE;
/*!40000 ALTER TABLE `tbl_keep_account` DISABLE KEYS */;
INSERT INTO `tbl_keep_account` VALUES (1,141,4,'2025-01-08 00:00:00','食品支出','外卖','基本生存支出','C','交易成功',NULL,0,'月','支出','早餐','C',NULL),(2,141,13.8,'2025-01-08 00:00:00','食品支出','外卖','基本生存支出','C','交易成功',NULL,0,'月','支出','午餐','C',NULL),(3,141,46.73,'2025-01-08 00:00:00','食品支出','食品采购','基本生存支出','C','交易成功',NULL,0,'月','支出','叮咚买菜','C',NULL),(4,141,279.7,'2025-01-08 00:00:00','居住支出','水电燃','基本生存支出','C','交易成功',NULL,0,'月','支出','金领12月电费','C',NULL),(5,141,188,'2025-01-09 00:00:00','退款','退款','-','C','交易成功',NULL,0,'-','收入','携程火车票退款','C',NULL),(6,141,188,'2025-01-09 00:00:00','人情往来','家人支出','一次性支出','CS','交易成功',NULL,0,'一次性','支出','帮姐姐抢票','C',NULL),(7,141,8,'2025-01-10 00:00:00','食品支出','外卖','基本生存支出','C','交易成功',NULL,0,'月','支出','早餐','C',NULL),(8,141,14.8,'2025-01-10 00:00:00','食品支出','外卖','基本生存支出','C','交易成功',NULL,0,'月','支出','全家','C',NULL),(9,141,40,'2025-01-11 00:00:00','形象管理','理发','基本生存支出','C','交易成功',NULL,0,'月','支出','理发','C',NULL),(10,141,73.9,'2025-01-11 00:00:00','食品支出','外卖','基本生存支出','CD','交易成功',NULL,0,'月','支出','早午饭 必胜客','C',NULL),(11,141,300,'2025-01-11 00:00:00','文化体育','健身器材','生活改善支出','CD','交易成功',NULL,0,'一次性','支出','磁力单车','C',NULL),(12,141,4,'2025-01-13 00:00:00','食品支出','外卖','基本生存支出','C','交易成功','',0,'月','支出','早餐','CA',''),(13,141,14.5,'2025-01-13 00:00:00','食品支出','外卖','基本生存支出','C,D','交易成功','',0,'月','支出','午餐','CA','支付宝');
/*!40000 ALTER TABLE `tbl_keep_account` ENABLE KEYS */;
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
  `last_learn_time` datetime DEFAULT NULL COMMENT '上次学习时间',
  PRIMARY KEY (`lesson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_lesson`
--

LOCK TABLES `tbl_lesson` WRITE;
/*!40000 ALTER TABLE `tbl_lesson` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_trade`
--

DROP TABLE IF EXISTS `tbl_trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_trade` (
  `order_id` varchar(128) NOT NULL COMMENT '订单号',
  `trade_time` datetime NOT NULL COMMENT '交易时间',
  `trade_type` varchar(32) DEFAULT NULL COMMENT '交易类型',
  `trade_obj` varchar(64) DEFAULT NULL,
  `obj_account` varchar(32) DEFAULT NULL COMMENT '对方账户',
  `product` varchar(128) DEFAULT NULL COMMENT '商品信息',
  `in_out` varchar(4) DEFAULT NULL COMMENT '收入支出',
  `amount` double DEFAULT NULL COMMENT '金额',
  `pay_type` varchar(32) DEFAULT NULL COMMENT '付款方式',
  `trade_status` varchar(16) DEFAULT NULL COMMENT '交易状态',
  `seller_order_id` varchar(128) DEFAULT NULL COMMENT '商户订单号',
  `trade_comment` text COMMENT '备注',
  `platform` varchar(16) DEFAULT NULL COMMENT '消费平台',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建用户id',
  `checked` int(11) DEFAULT '0' COMMENT '是否checked',
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`order_id`,`create_user_id`),
  KEY `trade_time` (`trade_time`),
  KEY `trade_type` (`trade_type`),
  KEY `in_out` (`in_out`),
  KEY `money` (`amount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_trade`
--

LOCK TABLES `tbl_trade` WRITE;
/*!40000 ALTER TABLE `tbl_trade` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_trade` ENABLE KEYS */;
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

-- Dump completed on 2025-01-14  9:32:41
