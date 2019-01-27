/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : trade

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2019-01-27 21:37:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `contract_base_info`
-- ----------------------------
DROP TABLE IF EXISTS `contract_base_info`;
CREATE TABLE `contract_base_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eta` varchar(255) DEFAULT NULL,
  `etd` varchar(255) DEFAULT NULL,
  `lcno` varchar(255) DEFAULT NULL,
  `qacertificate` int(11) DEFAULT NULL,
  `added_value_tax` double(20,2) DEFAULT '0.00',
  `added_value_tax1` double DEFAULT '0',
  `added_value_tax2` double DEFAULT '0',
  `added_value_tax3` double DEFAULT '0',
  `added_value_tax4` double DEFAULT '0',
  `added_value_tax5` double DEFAULT '0',
  `added_value_tax6` double DEFAULT '0',
  `agent` varchar(255) DEFAULT NULL,
  `agent_pass_date` varchar(255) DEFAULT NULL,
  `agent_send_date` varchar(255) DEFAULT NULL,
  `company_no` varchar(255) DEFAULT NULL,
  `container_no` varchar(255) DEFAULT NULL,
  `container_size` varchar(255) DEFAULT NULL,
  `contract_date` varchar(255) DEFAULT NULL,
  `contract_id` varchar(255) NOT NULL DEFAULT '',
  `create_date_time` varchar(255) DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `destination_port` varchar(255) DEFAULT NULL,
  `expect_sailing_date` varchar(255) DEFAULT NULL,
  `external_company` varchar(255) DEFAULT NULL,
  `external_contract` varchar(255) DEFAULT NULL,
  `final_payment` double NOT NULL,
  `final_payment_date` varchar(255) DEFAULT NULL,
  `final_rate` float NOT NULL,
  `inside_contract` varchar(255) DEFAULT NULL,
  `insurance_buy_date` varchar(255) DEFAULT NULL,
  `insurance_company` varchar(255) DEFAULT NULL,
  `insurance_money` double NOT NULL,
  `is_check_elec` int(11) DEFAULT NULL,
  `hasbaoguan` int(11) DEFAULT NULL,
  `is_need_insurance` int(11) DEFAULT NULL,
  `issuing_bank` varchar(255) DEFAULT NULL,
  `issuing_date` varchar(255) DEFAULT NULL,
  `ladingbill_no` varchar(255) DEFAULT NULL,
  `origin_country` varchar(255) DEFAULT NULL,
  `pay_type` varchar(255) DEFAULT NULL,
  `pre_payment` double NOT NULL,
  `pre_payment_date` varchar(255) DEFAULT NULL,
  `pre_rate` float NOT NULL,
  `price_condition` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `remittance_date` varchar(255) DEFAULT NULL,
  `remittance_rate` double NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `store_date` varchar(255) DEFAULT NULL,
  `tariff` double(20,2) DEFAULT '0.00',
  `tariff1` double DEFAULT '0',
  `tariff2` double DEFAULT '0',
  `tariff3` double DEFAULT '0',
  `tariff4` double DEFAULT '0',
  `tariff5` double DEFAULT '0',
  `tariff6` double DEFAULT '0',
  `tariff_no` varchar(255) DEFAULT NULL,
  `tax_pay_date` varchar(255) DEFAULT NULL,
  `total_boxes` int(11) DEFAULT NULL,
  `total_contract_amount` double NOT NULL,
  `total_contract_money` double NOT NULL,
  `total_invoice_amount` double NOT NULL,
  `total_invoice_money` double NOT NULL,
  `version` int(11) DEFAULT NULL,
  `warehouse` varchar(255) DEFAULT NULL,
  `yahuidaoqi_date` varchar(255) DEFAULT NULL,
  `exchange_rate` double DEFAULT NULL,
  `zhixiangfei` double DEFAULT NULL,
  `zhigangfei` double DEFAULT NULL,
  `is_yahui` int(11) DEFAULT NULL,
  `yahui_money` double DEFAULT NULL,
  `yahui_year_rate` double DEFAULT NULL,
  `yahui_day_rate` double DEFAULT NULL,
  `is_financing` int(11) DEFAULT NULL,
  `financing_money` double DEFAULT NULL,
  `financing_rate` double DEFAULT NULL,
  `financing_daoqi` varchar(255) DEFAULT NULL,
  `daoqi_rate` double DEFAULT NULL,
  `financing_bank` varchar(255) DEFAULT NULL,
  `final_pay_bank` varchar(255) DEFAULT NULL,
  `pre_pay_bank` varchar(255) DEFAULT NULL,
  `owner_company` varchar(255) DEFAULT NULL,
  `baoguandan` int(11) DEFAULT NULL,
  `storage_condition` varchar(255) DEFAULT NULL,
  `cargo_match` int(11) DEFAULT NULL,
  `cargo_type` varchar(50) DEFAULT NULL,
  `customer_name` varchar(100) DEFAULT NULL,
  `expect_sale_date` varchar(50) DEFAULT NULL,
  `expect_sale_unit_price` double DEFAULT NULL,
  `expect_sale_weight` double DEFAULT NULL,
  PRIMARY KEY (`id`,`contract_id`),
  KEY `one` (`contract_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=952 DEFAULT CHARSET=utf8;
