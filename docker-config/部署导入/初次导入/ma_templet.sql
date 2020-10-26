/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50643
 Source Host           : localhost:3306
 Source Schema         : pcmaster_org

 Target Server Type    : MySQL
 Target Server Version : 50643
 File Encoding         : 65001

 Date: 02/03/2020 09:56:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ma_templet
-- ----------------------------
DROP TABLE IF EXISTS `ma_templet`;
CREATE TABLE `ma_templet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdBy` varchar(255) DEFAULT NULL,
  `createdByName` varchar(255) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateModified` datetime DEFAULT NULL,
  `modifiedBy` varchar(255) DEFAULT NULL,
  `modifiedByName` varchar(255) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `templet_name` varchar(4000) DEFAULT NULL,
  `templet_json` varchar(4000) DEFAULT NULL,
  `templet_no` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ma_templet
-- ----------------------------
BEGIN;
INSERT INTO `ma_templet` VALUES (2, 'admin', 'admin', '2020-02-20 15:40:24', '2020-02-24 14:24:17', 'admin', 'admin', 1, NULL, '隐检模版', '[{\"name\":\"模具检测长度\",\"result\":\"合格\"}, {\"name\":\"对角线\",\"result\":\"合格\"} ,{\"name\":\"接缝缝隙\",\"result\":\"合格\"},{\"name\":\"截面尺寸\",\"result\":\"合格\"},{\"name\":\"侧向弯曲\",\"result\":\"合格\"},{\"name\":\"露石剂\",\"result\":\"合格\"},{\"name\":\"钢筋牌号\",\"result\":\"合格\"},{\"name\":\"钢筋数量\",\"result\":\"合格\"},{\"name\":\"钢筋间距\",\"result\":\"合格\"},{\"name\":\"钢筋规格\",\"result\":\"合格\"},{\"name\":\"钢筋位置\",\"result\":\"合格\"},{\"name\":\"钢筋排距\",\"result\":\"合格\"},{\"name\":\"连接方式\",\"result\":\"合格\"},{\"name\":\"接头质量\",\"result\":\"合格\"},{\"name\":\"搭接长度\",\"result\":\"合格\"},{\"name\":\"接头位置\",\"result\":\"合格\"},{\"name\":\"接头面积\",\"result\":\"合格\"},{\"name\":\"保护层\",\"result\":\"合格\"},{\"name\":\"箍筋、横向钢筋牌号\",\"result\":\"合格\"},{\"name\":\"箍筋、横向钢筋数量\",\"result\":\"合格\"},{\"name\":\"箍筋间距\",\"result\":\"合格\"},{\"name\":\"箍筋、横向钢筋规格\",\"result\":\"合格\"},{\"name\":\"箍筋、横向钢筋位置\",\"result\":\"合格\"},{\"name\":\"箍筋弯钩角度平直段长度\",\"result\":\"合格\"},{\"name\":\"预埋规格\",\"result\":\"合格\"},{\"name\":\"预埋位置\",\"result\":\"合格\"},{\"name\":\"对位螺栓固定措施\",\"result\":\"合格\"},{\"name\":\"预埋数量\",\"result\":\"合格\"},{\"name\":\"预埋件固定措施\",\"result\":\"合格\"},{\"name\":\"预埋吊环固定措施\",\"result\":\"合格\"},{\"name\":\"灌浆套筒预留孔洞\",\"result\":\"合格\"},{\"name\":\"灌浆套筒数量\",\"result\":\"合格\"},{\"name\":\"灌浆套筒垂直度\",\"result\":\"合格\"},{\"name\":\"灌浆套筒规格\",\"result\":\"合格\"},{\"name\":\"灌浆套筒中心线位置\",\"result\":\"合格\"},{\"name\":\"夹心外墙保温层\",\"result\":\"合格\"},{\"name\":\"夹心外墙拉结件规格\",\"result\":\"合格\"},{\"name\":\"夹心外墙位置\",\"result\":\"合格\"},{\"name\":\"夹心外墙厚度\",\"result\":\"合格\"},{\"name\":\"夹心外墙数量\",\"result\":\"合格\"},{\"name\":\"预留孔洞位置尺寸\",\"result\":\"合格\"},{\"name\":\"预留孔洞规格\",\"result\":\"合格\"},{\"name\":\"线盒规格\",\"result\":\"合格\"},{\"name\":\"线盒位置及方向\",\"result\":\"合格\"},{\"name\":\"线盒固定措施\",\"result\":\"合格\"},{\"name\":\"线盒数量\",\"result\":\"合格\"},{\"name\":\"线盒是否流畅\",\"result\":\"合格\"},{\"name\":\"保温板铺设\",\"result\":\"合格\"}]', 'YJ');
INSERT INTO `ma_templet` VALUES (3, 'admin', 'admin', '2020-02-20 17:14:15', '2020-02-24 14:54:06', 'admin', 'admin', 1, '', '成品检模版', '[{\"name\":\"混凝土\",\"result\":\"合格\"},{\"name\":\"养护\",\"result\":\"合格\"},{\"name\":\"灌浆导管\",\"result\":\"合格\"},{\"name\":\"洞口\",\"result\":\"合格\"},{\"name\":\"标签、质检章、堆放\",\"result\":\"合格\"},{\"name\":\"起吊件\",\"result\":\"合格\"},{\"name\":\"尺寸\",\"result\":\"合格\"},{\"name\":\"外观质量\",\"result\":\"合格\"},{\"name\":\"预埋件\",\"result\":\"合格\"},{\"name\":\"套筒\",\"result\":\"合格\"},{\"name\":\"钢筋\",\"result\":\"合格\"},{\"name\":\"钢筋保护层\",\"result\":\"合格\"}]', 'CPJ');
INSERT INTO `ma_templet` VALUES (4, 'admin', 'admin', '2020-02-20 17:15:11', '2020-02-24 14:40:51', 'admin', 'admin', 1, '', '浇筑确认模版', '[{\"name\": \"浇筑确认\", \"result\": \"合格\"}]', 'JZQR');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
