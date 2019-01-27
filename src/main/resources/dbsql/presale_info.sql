/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : trade

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2019-01-27 21:37:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `presale_info`
-- ----------------------------
DROP TABLE IF EXISTS `presale_info`;
CREATE TABLE `presale_info` (
  `sale_id` int(11) NOT NULL AUTO_INCREMENT,
  `cargo_id` varchar(255) DEFAULT NULL,
  `create_date_time` varchar(255) DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `expect_sale_date` varchar(255) DEFAULT NULL,
  `pickup_user` varchar(255) DEFAULT NULL,
  `expect_sale_unit_price` double NOT NULL,
  `expect_sale_weight` double NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sale_id`),
  KEY `one` (`cargo_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of presale_info
-- ----------------------------
