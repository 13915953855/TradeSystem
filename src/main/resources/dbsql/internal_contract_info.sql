/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : trade

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2019-01-27 21:37:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `internal_contract_info`
-- ----------------------------
DROP TABLE IF EXISTS `internal_contract_info`;
CREATE TABLE `internal_contract_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contract_no` varchar(255) DEFAULT NULL,
  `contract_date` varchar(255) DEFAULT NULL,
  `contract_id` varchar(255) DEFAULT NULL,
  `supplier` varchar(255) DEFAULT NULL,
  `container_no` varchar(255) DEFAULT NULL,
  `pay_date` varchar(255) DEFAULT NULL,
  `pay_money` double NOT NULL,
  `receipt_date` varchar(255) DEFAULT NULL,
  `create_date_time` varchar(255) DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_boxes` int(11) DEFAULT NULL,
  `total_amount` double NOT NULL,
  `total_money` double NOT NULL,
  `real_amount` double DEFAULT NULL,
  `real_money` double DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `warehouse` varchar(255) DEFAULT NULL,
  `store_date` varchar(255) DEFAULT NULL,
  `owner_company` varchar(255) DEFAULT NULL,
  `import_contract_no` varchar(255) DEFAULT NULL,
  `cargo_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=368 DEFAULT CHARSET=utf8;
