-- -------------------------------------------------------------
-- TablePlus 2.12(282)
--
-- https://tableplus.com/
--
-- Database: pcmaster_org
-- Generation Time: 2019-12-19 17:14:34.5760
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


DROP TABLE IF EXISTS `pc_permission`;
CREATE TABLE `pc_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alias` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;

INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('1', '组织管理', '2019-12-18 07:12:34', 'ORG_ALL', '0');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('2', '组织查询', '2019-12-18 07:12:34', 'ORG_SELECT', '1');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('3', '组织修改', '2019-12-18 07:12:34', 'ORG_UPDATE', '1');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('4', '组织删除', '2019-12-18 07:12:34', 'ORG_DELETE', '1');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('5', '组织新增', '2019-12-18 07:12:34', 'ORG_ADD', '1');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('6', '用户管理', NULL, 'USER_ALL', '0');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('7', '用户查询', NULL, 'USER_SELECT', '6');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('8', '用户新增', NULL, 'USER_ADD', '6');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('9', '用户删除', NULL, 'USER_DELETE', '6');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('11', '同步企业微信用户', NULL, 'USER_SYNC_QYWX', '6');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('12', '更新用户密码', NULL, 'USER_UPDATE_PWD', '6');
INSERT INTO `pc_permission` (`id`, `alias`, `create_time`, `name`, `pid`) VALUES ('13', '更新用户', NULL, 'USER_UPDATE', '6');


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;