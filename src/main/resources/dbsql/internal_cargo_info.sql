/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : trade

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2019-01-27 21:37:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `internal_cargo_info`
-- ----------------------------
DROP TABLE IF EXISTS `internal_cargo_info`;
CREATE TABLE `internal_cargo_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `boxes` int(11) DEFAULT NULL,
  `cargo_id` varchar(255) NOT NULL,
  `cargo_name` varchar(255) DEFAULT NULL,
  `cargo_no` varchar(255) DEFAULT NULL,
  `contract_id` varchar(255) DEFAULT NULL,
  `cost_price` double NOT NULL,
  `create_date_time` varchar(255) DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `amount` double NOT NULL,
  `level` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `unit_price` double NOT NULL,
  `company_no` varchar(255) DEFAULT NULL,
  `warehouse` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`cargo_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1506 DEFAULT CHARSET=utf8;
