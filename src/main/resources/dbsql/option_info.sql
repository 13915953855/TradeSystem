/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : trade

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-03-25 08:45:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for option_info
-- ----------------------------
DROP TABLE IF EXISTS `option_info`;
CREATE TABLE `option_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `group` varchar(40) NOT NULL,
  `field` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=382 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of option_info
-- ----------------------------
INSERT INTO `option_info` VALUES ('255', '保乐肩', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('256', '板腱', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('257', '脖骨', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('258', '脖肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('259', '板筋', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('260', '背肩', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('261', '背肋排', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('262', '边肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('263', '大米龙', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('264', '带骨胸排', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('265', '腹肉心', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('266', '方切肩', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('267', '隔膜', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('268', '混柜', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('269', '厚裙', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('270', '后胸', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('271', '肩胛仔骨', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('272', '肩胛背肩', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('273', '肩胛', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('274', '肩肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('275', '肋排边', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('276', '肋条肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('277', '肋排肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('278', '肋脊肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('279', '脸肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('280', '内裙', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('281', '牛霖', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('282', '牛腱', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('283', '牛腩', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('284', '牛腩排', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('285', '牛前', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('286', '牛柳', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('287', '牛心', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('288', '牛肾', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('289', '牛鞭', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('290', '牛尾', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('291', '牛唇', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('292', '牛舌', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('293', '嫩肩', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('294', '腩筋', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('295', '排骨', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('296', '前胸', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('297', '去盖臀肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('298', '脐膯板', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('299', '裙膜', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('300', '上脑', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('301', '上脑脖肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('302', '上脑心', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('303', '三角肩肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('304', '三角肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('305', '臀肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('306', '臀腰肉盖', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('307', '臀腰肉心', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('308', '碎肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('309', '腿骨', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('310', '蹄筋', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('311', '窝骨', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('312', '外裙', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('313', '西冷', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('314', '胸肋排', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('315', '胸肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('316', '胸肉块', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('317', '胸排肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('318', '胸口油', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('319', '小排', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('320', '小米龙', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('321', '心片', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('322', '心管', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('323', '翼板肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('324', '眼肉', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('325', '眼肉盖', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('326', '仔骨', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('327', '战斧', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('328', '椎骨', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('329', '脂肪', '牛产品', 'cargoName');
INSERT INTO `option_info` VALUES ('330', '肋排', '猪产品', 'cargoName');
INSERT INTO `option_info` VALUES ('331', '口条', '猪产品', 'cargoName');
INSERT INTO `option_info` VALUES ('332', '大排', '猪产品', 'cargoName');
INSERT INTO `option_info` VALUES ('333', '前肘', '猪产品', 'cargoName');
INSERT INTO `option_info` VALUES ('334', '后腿', '猪产品', 'cargoName');
INSERT INTO `option_info` VALUES ('335', '猪头', '猪产品', 'cargoName');
INSERT INTO `option_info` VALUES ('336', 'TEYS AUS', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('337', 'SANGER', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('338', 'FRISA', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('339', 'JBS AUS', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('340', 'KPC HK', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('341', 'JOC AUS', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('342', 'STANBROKE', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('343', 'THOMAS', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('344', 'HARVEY', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('345', 'MOGILEV MEAT', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('346', 'MINEVER', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('347', 'MATABOI', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('348', 'MARFRIG', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('349', 'MTT', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('350', 'GORINA', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('351', 'CARGILL', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('352', 'ARREBEEF', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('353', 'TOMEX', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('354', 'ASHLEIGH PARK BEEF', '牛产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('355', 'CINCOVILLAS', '猪产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('356', 'NORWEST', '猪产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('357', 'PATEL', '猪产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('358', 'TOMEX', '猪产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('359', 'WESTFORT', '猪产品', 'externalCompany');
INSERT INTO `option_info` VALUES ('360', '澳大利亚', '牛产品', 'originCountry');
INSERT INTO `option_info` VALUES ('361', '乌拉圭', '牛产品', 'originCountry');
INSERT INTO `option_info` VALUES ('362', '巴西', '牛产品', 'originCountry');
INSERT INTO `option_info` VALUES ('363', '加拿大', '牛产品', 'originCountry');
INSERT INTO `option_info` VALUES ('364', '阿根廷', '牛产品', 'originCountry');
INSERT INTO `option_info` VALUES ('365', '白俄罗斯', '牛产品', 'originCountry');
INSERT INTO `option_info` VALUES ('366', '新西兰', '牛产品', 'originCountry');
INSERT INTO `option_info` VALUES ('367', '法国', '牛产品', 'originCountry');
INSERT INTO `option_info` VALUES ('368', '爱尔兰', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('369', '巴西', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('370', '比利时', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('371', '丹麦', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('372', '德国', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('373', '法国', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('374', '荷兰', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('375', '加拿大', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('376', '美国', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('377', '葡萄牙', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('378', '西班牙', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('379', '匈牙利', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('380', '英国', '猪产品', 'originCountry');
INSERT INTO `option_info` VALUES ('381', '智利', '猪产品', 'originCountry');
