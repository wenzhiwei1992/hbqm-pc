-- -------------------------------------------------------------
-- TablePlus 2.11.2(278)
--
-- https://tableplus.com/
--
-- Database: pcmaster_org
-- Generation Time: 2019-11-26 10:59:40.9140
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


DROP TABLE IF EXISTS `pc_org`;
CREATE TABLE `pc_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `createdByName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateModified` datetime DEFAULT NULL,
  `modifiedBy` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modifiedByName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `active` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `org_code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `org_desc` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `org_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `root_org` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `grade_num` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `org_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_other_org` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_within_org` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `pc_org` (`id`, `createdBy`, `createdByName`, `dateCreated`, `dateModified`, `modifiedBy`, `modifiedByName`, `org_id`, `active`, `org_code`, `org_desc`, `org_name`, `root_org`, `grade_num`, `org_no`, `parent_id`, `is_other_org`, `is_within_org`) VALUES ('1', 'admin', '系统管理员', '2019-08-13 11:11:24', '2019-08-15 10:40:07', 'admin', '系统管理员', NULL, NULL, 'VPS', 'VPS', 'VPS', '是', '0', NULL, NULL, '是', '是');


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;