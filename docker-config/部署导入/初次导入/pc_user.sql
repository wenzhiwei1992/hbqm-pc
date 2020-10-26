/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50643
 Source Host           : localhost:3306
 Source Schema         : pcmaster_ct

 Target Server Type    : MySQL
 Target Server Version : 50643
 File Encoding         : 65001

 Date: 14/01/2020 16:03:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pc_user
-- ----------------------------
DROP TABLE IF EXISTS `pc_user`;
CREATE TABLE `pc_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdByName` varchar(255) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateModified` datetime DEFAULT NULL,
  `modifiedBy` varchar(255) DEFAULT NULL,
  `modifiedByName` varchar(255) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `confirmPassword` varchar(255) DEFAULT NULL,
  `deptId` bigint(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `newPassword` varchar(255) DEFAULT NULL,
  `nickName` varchar(255) DEFAULT NULL,
  `org_ids` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `realName` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pc_user
-- ----------------------------
BEGIN;
INSERT INTO `pc_user` VALUES (1, NULL, NULL, NULL, '2019-12-18 15:31:33', 'admin', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1;', 'admin', NULL, NULL, NULL, 'admin', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
