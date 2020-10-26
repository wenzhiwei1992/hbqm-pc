-- -------------------------------------------------------------
-- TablePlus 2.11.2(278)
--
-- https://tableplus.com/
--
-- Database: pcmaster_org
-- Generation Time: 2019-11-29 11:11:30.4480
-- -------------------------------------------------------------


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


DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdByName` varchar(255) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateModified` datetime DEFAULT NULL,
  `modifiedBy` varchar(255) DEFAULT NULL,
  `modifiedByName` varchar(255) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `code_belong` varchar(255) DEFAULT NULL,
  `code_name` varchar(255) DEFAULT NULL,
  `code_no` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sys_dict_d`;
CREATE TABLE `sys_dict_d` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdByName` varchar(255) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateModified` datetime DEFAULT NULL,
  `modifiedBy` varchar(255) DEFAULT NULL,
  `modifiedByName` varchar(255) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `code_name` varchar(255) DEFAULT NULL,
  `dict_code_no` varchar(255) DEFAULT NULL,
  `is_enabled` bit(1) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4;

INSERT INTO `sys_dict` (`id`, `createdBy`, `createdByName`, `dateCreated`, `dateModified`, `modifiedBy`, `modifiedByName`, `org_id`, `code_belong`, `code_name`, `code_no`, `remark`) VALUES ('1', 'admin', '系统管理员', '2019-11-20 11:27:12', '2019-11-20 11:27:12', 'admin', '系统管理员', '1', '生产模块', '构件状态', 'PRODUCT_PROCESS_STATUS', '构件状态控制,可以跳过和排序/启用'),
('3', 'admin', '系统管理员', '2019-11-26 10:39:53', '2019-11-26 10:39:53', 'admin', '系统管理员', '1', '生产模块', '未完成构件状态值', 'PRODUCT_STATUS_WWC', NULL),
('4', 'admin', '系统管理员', '2019-11-26 10:40:30', '2019-11-26 10:40:30', 'admin', '系统管理员', '1', '生产模块', '已完成构件状态值', 'PRODUCT_STATUS_ORDER_OVER', NULL),
('5', 'admin', '系统管理员', '2019-11-26 10:42:02', '2019-11-26 10:44:22', 'admin', '系统管理员', '1', '生产模块', '产出量', 'PRODUCT_STATUS_OUT_PUT', '状态值需要检查,已经很陈旧了'),
('6', 'admin', '系统管理员', '2019-11-26 10:42:25', '2019-11-26 10:43:11', 'admin', '系统管理员', '1', '生产模块', '未生产构件状态值', 'PRODUCT_STATUS_WSC', NULL),
('7', 'admin', '系统管理员', '2019-11-26 10:43:53', '2019-11-26 10:43:53', 'admin', '系统管理员', '1', '生产模块', '生产中构件状态值', 'PRODUCT_STATUS_SCZ', NULL),
('8', 'admin', '系统管理员', '2019-11-29 11:03:29', '2019-11-29 11:03:29', 'admin', '系统管理员', '1', '生产模块', '统计在库存中的构件状态', 'PRODUCT_STATUS_IN_STOCK', NULL),
('9', 'admin', '系统管理员', '2019-12-04 13:30:07', '2019-12-04 13:39:41', 'admin', '系统管理员', '1', '生产模块', '检验状态', 'PRODUCT_CHECK_STATUS', '检验状态控制,可以跳过和排序/启用'),
('10', 'admin', '系统管理员', '2019-12-04 15:01:54', '2019-12-04 15:01:54', 'admin','系统管理员', '1', '生产模块', '成品检所有的状态位', 'CHECK_RESULT_CPJ_ALL', NULL),
('11', 'admin', '系统管理员', '2019-12-04 15:03:40', '2019-12-04 15:03:40', 'admin','系统管理员', '1', '生产模块', '成品检返修状态位', 'CHECK_RESULT_CPJ_FX', NULL),
('12', 'admin', '系统管理员', '2019-12-04 15:24:39', '2019-12-04 15:24:39', 'admin','系统管理员',  '1', '生产模块', '成品检返修时构件对应状态', 'PRODUCT_STATUS_CPJ_FX', NULL),
('13', 'admin', '系统管理员', '2019-12-10 16:07:54', '2019-12-10 16:07:54', 'admin', '系统管理员', '1', '生产模块', '未发货', 'PRODUCT_STATUS_NOT_FH', NULL),
('14', 'admin', '系统管理员', '2020-03-06 14:58:51', '2020-03-06 14:59:25', 'admin', '系统管理员', '1', '生产模块', '已发货构件状态值', 'PRODUCT_STATUS_YFH', NULL),
('15', 'admin', '系统管理员', '2020-06-19 02:12:44', '2020-06-19 02:12:44', 'admin', '系统管理员', '1', '发货模块', '收货状态', 'RECEIPT_STATUS', NULL);

INSERT INTO `sys_dict_d` (`id`, `createdBy`, `createdByName`, `dateCreated`, `dateModified`, `modifiedBy`, `modifiedByName`, `org_id`, `code_name`, `dict_code_no`, `is_enabled`, `remark`, `sort`) VALUES ('41', 'admin', '系统管理员', '2019-11-26 10:39:53', '2019-11-26 10:39:53', 'admin', '系统管理员', '1', '待生产,待隐检,隐检返修,待成品检,返修,待报废审批,报废审批未通过,报废', 'PRODUCT_STATUS_WWC', b'1', '', '1'),
('47', 'admin', '系统管理员', '2019-11-26 10:42:41', '2019-11-26 10:42:41', 'admin', '系统管理员', '1', '待入库,成品入库,报废入库,发货待确认', 'PRODUCT_STATUS_ORDER_OVER', b'1', '', '1'),
('48', 'admin', '系统管理员', '2019-11-26 10:43:11', '2019-11-26 10:43:11', 'admin', '系统管理员', '1', '待生产,待隐检', 'PRODUCT_STATUS_WSC', b'1', '', '1'),
('49', 'admin', '系统管理员', '2019-11-26 10:43:53', '2019-11-26 10:43:53', 'admin', '系统管理员', '1', '隐检返修,待成品检,返修,待报废审批,报废审批未通过,报废', 'PRODUCT_STATUS_SCZ', b'1', '', '1'),
('50', 'admin', '系统管理员', '2019-11-26 10:44:22', '2019-11-26 10:44:22', 'admin', '系统管理员', '1', '待入库,成品入库,发货待确认,已发货', 'PRODUCT_STATUS_OUT_PUT', b'1', '', '1'),
('63', 'admin', '系统管理员', '2019-11-26 13:32:37', '2019-11-26 13:32:37', 'admin', '系统管理员', '1', '待隐检', 'PRODUCT_PROCESS_STATUS', b'1', '', '10'),
('64', 'admin', '系统管理员', '2019-11-26 13:32:37', '2019-11-26 13:32:37', 'admin', '系统管理员', '1', '待成品检', 'PRODUCT_PROCESS_STATUS', b'1', '', '30'),
('65', 'admin', '系统管理员', '2019-11-26 13:32:37', '2019-11-26 13:32:37', 'admin', '系统管理员', '1', '待入库', 'PRODUCT_PROCESS_STATUS', b'1', '', '50'),
('66', 'admin', '系统管理员', '2019-11-26 13:32:37', '2019-11-26 13:32:37', 'admin', '系统管理员', '1', '成品入库', 'PRODUCT_PROCESS_STATUS', b'1', '', '60'),
('67', 'admin', '系统管理员', '2019-11-26 13:32:37', '2019-11-26 13:32:37', 'admin', '系统管理员', '1', '已发货', 'PRODUCT_PROCESS_STATUS', b'1', '', '70'),
('68', 'admin', '系统管理员', '2019-11-26 13:32:37', '2019-11-26 13:32:37', 'admin', '系统管理员', '1', '浇筑确认', 'PRODUCT_PROCESS_STATUS', b'0', '', '20'),
('69', 'admin', '系统管理员', '2019-11-29 11:03:29', '2019-11-29 11:03:29', 'admin', '系统管理员', '1', '成品入库,报废入库', 'PRODUCT_STATUS_IN_STOCK', b'1', '', '1'),
('70', 'admin', '系统管理员', '2019-12-04 14:12:21', '2019-12-04 14:12:21', 'admin', '系统管理员', '1', '隐检返修', 'PRODUCT_CHECK_STATUS', b'1', 'YJFX', '10'),
('71', 'admin', '系统管理员', '2019-12-04 14:12:21', '2019-12-04 14:12:21', 'admin', '系统管理员', '1', '合格', 'PRODUCT_CHECK_STATUS', b'1', 'YJHE', '20'),
('72', 'admin', '系统管理员', '2019-12-04 14:12:21', '2019-12-04 14:12:21', 'admin', '系统管理员', '1', '返修', 'PRODUCT_CHECK_STATUS', b'1', 'CPJFX', '30'),
('73', 'admin', '系统管理员', '2019-12-04 14:12:21', '2019-12-04 14:12:21', 'admin', '系统管理员', '1', '合格', 'PRODUCT_CHECK_STATUS', b'1', 'CPJHG', '40'),
('74', 'admin', '系统管理员', '2019-12-04 14:12:21', '2019-12-04 14:12:21', 'admin', '系统管理员', '1', '出厂返修', 'PRODUCT_CHECK_STATUS', b'1', 'CPJCCFX', '50'),
('75', 'admin', '系统管理员', '2019-12-04 15:03:57', '2019-12-04 15:03:57', 'admin', '系统管理员', '1', '返修,出厂返修', 'CHECK_RESULT_CPJ_FX', b'1', '', '1'),
('76', 'admin', '系统管理员', '2019-12-04 15:04:01', '2019-12-04 15:04:01', 'admin', '系统管理员', '1', '返修,合格,出厂返修', 'CHECK_RESULT_CPJ_ALL', b'1', '', '1'),
('77', 'admin', '系统管理员', '2019-12-04 15:24:59', '2019-12-04 15:24:59', 'admin', '系统管理员', '1', '待入库,成品入库', 'PRODUCT_STATUS_CPJ_FX', b'1', '', '1'),
('78', 'admin', '系统管理员', '2019-12-10 16:07:54', '2019-12-10 16:07:54', 'admin', '系统管理员', '1', '待生产,待隐检,隐检返修,待成品检,返修,待报废审批,报废审批未通过,报废,待入库,成品入库,报废入库,发货待确认,出厂返修,出厂报废', 'PRODUCT_STATUS_NOT_FH', b'1', '', '1'),
('79', 'admin', '系统管理员', '2020-03-06 15:16:05', '2020-03-06 15:16:05', 'admin', '系统管理员', '1', '已发货,出厂返修,出厂报废报废', 'PRODUCT_STATUS_YFH', b'1', '', '999'),
('80', 'admin', '系统管理员', '2020-06-19 02:12:44', '2020-06-19 02:12:44', 'admin', '系统管理员', '1', '收货', 'RECEIPT_STATUS', b'1', '', 0),
('81', 'admin', '系统管理员', '2020-06-19 02:12:44', '2020-06-19 02:12:44', 'admin', '系统管理员', '1', '部分收货', 'RECEIPT_STATUS', b'1', '', 10);




/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;