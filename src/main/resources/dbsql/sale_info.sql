/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : trade

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2019-01-27 21:37:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sale_info`
-- ----------------------------
DROP TABLE IF EXISTS `sale_info`;
CREATE TABLE `sale_info` (
  `sale_id` int(11) NOT NULL AUTO_INCREMENT,
  `cargo_id` varchar(255) DEFAULT NULL,
  `create_date_time` varchar(255) DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_type` varchar(255) DEFAULT NULL,
  `customer_pay_date` varchar(255) DEFAULT NULL,
  `customer_pay_money` double NOT NULL,
  `customer_pay_date2` varchar(255) DEFAULT NULL,
  `customer_pay_money2` double DEFAULT NULL,
  `customer_pay_date3` varchar(255) DEFAULT NULL,
  `customer_pay_money3` double DEFAULT NULL,
  `customer_pay_date4` varchar(255) DEFAULT NULL,
  `customer_pay_money4` double DEFAULT NULL,
  `customer_pay_date5` varchar(255) DEFAULT NULL,
  `customer_pay_money5` double DEFAULT NULL,
  `money_clear` int(11) DEFAULT NULL,
  `kaifapiao` int(11) DEFAULT NULL,
  `payment_diff` double NOT NULL,
  `pickup_date` varchar(255) DEFAULT NULL,
  `pickup_user` varchar(255) DEFAULT NULL,
  `profit` double NOT NULL,
  `real_sale_boxes` int(11) DEFAULT NULL,
  `real_sale_date` varchar(255) DEFAULT NULL,
  `real_sale_money` double NOT NULL,
  `real_sale_unit_price` double NOT NULL,
  `real_sale_weight` double NOT NULL,
  `remark` varchar(2000) DEFAULT NULL,
  `sale_contract_no` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `deposit_date` varchar(50) DEFAULT NULL,
  `deposit` double DEFAULT NULL,
  PRIMARY KEY (`sale_id`),
  KEY `one` (`cargo_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2353 DEFAULT CHARSET=utf8;
