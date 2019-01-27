/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : trade

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2019-01-27 21:37:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cargo_info`
-- ----------------------------
DROP TABLE IF EXISTS `cargo_info`;
CREATE TABLE `cargo_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `boxes` int(11) DEFAULT NULL,
  `cargo_id` varchar(255) NOT NULL,
  `cargo_name` varchar(255) DEFAULT NULL,
  `cargo_no` varchar(255) DEFAULT NULL,
  `contract_amount` double NOT NULL,
  `contract_id` varchar(255) DEFAULT NULL,
  `contract_money` double NOT NULL,
  `cost_price` double NOT NULL,
  `create_date_time` varchar(255) DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `expect_store_boxes` int(11) DEFAULT NULL,
  `expect_store_weight` double NOT NULL,
  `invoice_amount` double NOT NULL,
  `invoice_money` double NOT NULL,
  `level` varchar(255) DEFAULT NULL,
  `real_store_boxes` int(11) DEFAULT NULL,
  `real_store_money` double NOT NULL,
  `real_store_weight` double NOT NULL,
  `unit_price` double NOT NULL,
  `business_mode` varchar(50) DEFAULT NULL,
  `company_no` varchar(255) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`,`cargo_id`),
  KEY `one` (`contract_id`,`cargo_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3287 DEFAULT CHARSET=utf8;
