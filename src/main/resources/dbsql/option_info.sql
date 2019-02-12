/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : trade

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-02-12 08:54:01
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=330 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of option_info
-- ----------------------------
INSERT INTO `option_info` VALUES ('255', '保乐肩', 'cargo');
INSERT INTO `option_info` VALUES ('256', '板腱', 'cargo');
INSERT INTO `option_info` VALUES ('257', '脖骨', 'cargo');
INSERT INTO `option_info` VALUES ('258', '脖肉', 'cargo');
INSERT INTO `option_info` VALUES ('259', '板筋', 'cargo');
INSERT INTO `option_info` VALUES ('260', '背肩', 'cargo');
INSERT INTO `option_info` VALUES ('261', '背肋排', 'cargo');
INSERT INTO `option_info` VALUES ('262', '边肉', 'cargo');
INSERT INTO `option_info` VALUES ('263', '大米龙', 'cargo');
INSERT INTO `option_info` VALUES ('264', '带骨胸排', 'cargo');
INSERT INTO `option_info` VALUES ('265', '腹肉心', 'cargo');
INSERT INTO `option_info` VALUES ('266', '方切肩', 'cargo');
INSERT INTO `option_info` VALUES ('267', '隔膜', 'cargo');
INSERT INTO `option_info` VALUES ('268', '混柜', 'cargo');
INSERT INTO `option_info` VALUES ('269', '厚裙', 'cargo');
INSERT INTO `option_info` VALUES ('270', '后胸', 'cargo');
INSERT INTO `option_info` VALUES ('271', '肩胛仔骨', 'cargo');
INSERT INTO `option_info` VALUES ('272', '肩胛背肩', 'cargo');
INSERT INTO `option_info` VALUES ('273', '肩胛', 'cargo');
INSERT INTO `option_info` VALUES ('274', '肩肉', 'cargo');
INSERT INTO `option_info` VALUES ('275', '肋排边', 'cargo');
INSERT INTO `option_info` VALUES ('276', '肋条肉', 'cargo');
INSERT INTO `option_info` VALUES ('277', '肋排肉', 'cargo');
INSERT INTO `option_info` VALUES ('278', '肋脊肉', 'cargo');
INSERT INTO `option_info` VALUES ('279', '脸肉', 'cargo');
INSERT INTO `option_info` VALUES ('280', '内裙', 'cargo');
INSERT INTO `option_info` VALUES ('281', '牛霖', 'cargo');
INSERT INTO `option_info` VALUES ('282', '牛腱', 'cargo');
INSERT INTO `option_info` VALUES ('283', '牛腩', 'cargo');
INSERT INTO `option_info` VALUES ('284', '牛腩排', 'cargo');
INSERT INTO `option_info` VALUES ('285', '牛前', 'cargo');
INSERT INTO `option_info` VALUES ('286', '牛柳', 'cargo');
INSERT INTO `option_info` VALUES ('287', '牛心', 'cargo');
INSERT INTO `option_info` VALUES ('288', '牛肾', 'cargo');
INSERT INTO `option_info` VALUES ('289', '牛鞭', 'cargo');
INSERT INTO `option_info` VALUES ('290', '牛尾', 'cargo');
INSERT INTO `option_info` VALUES ('291', '牛唇', 'cargo');
INSERT INTO `option_info` VALUES ('292', '牛舌', 'cargo');
INSERT INTO `option_info` VALUES ('293', '嫩肩', 'cargo');
INSERT INTO `option_info` VALUES ('294', '腩筋', 'cargo');
INSERT INTO `option_info` VALUES ('295', '排骨', 'cargo');
INSERT INTO `option_info` VALUES ('296', '前胸', 'cargo');
INSERT INTO `option_info` VALUES ('297', '去盖臀肉', 'cargo');
INSERT INTO `option_info` VALUES ('298', '脐膯板', 'cargo');
INSERT INTO `option_info` VALUES ('299', '裙膜', 'cargo');
INSERT INTO `option_info` VALUES ('300', '上脑', 'cargo');
INSERT INTO `option_info` VALUES ('301', '上脑脖肉', 'cargo');
INSERT INTO `option_info` VALUES ('302', '上脑心', 'cargo');
INSERT INTO `option_info` VALUES ('303', '三角肩肉', 'cargo');
INSERT INTO `option_info` VALUES ('304', '三角肉', 'cargo');
INSERT INTO `option_info` VALUES ('305', '臀肉', 'cargo');
INSERT INTO `option_info` VALUES ('306', '臀腰肉盖', 'cargo');
INSERT INTO `option_info` VALUES ('307', '臀腰肉心', 'cargo');
INSERT INTO `option_info` VALUES ('308', '碎肉', 'cargo');
INSERT INTO `option_info` VALUES ('309', '腿骨', 'cargo');
INSERT INTO `option_info` VALUES ('310', '蹄筋', 'cargo');
INSERT INTO `option_info` VALUES ('311', '窝骨', 'cargo');
INSERT INTO `option_info` VALUES ('312', '外裙', 'cargo');
INSERT INTO `option_info` VALUES ('313', '西冷', 'cargo');
INSERT INTO `option_info` VALUES ('314', '胸肋排', 'cargo');
INSERT INTO `option_info` VALUES ('315', '胸肉', 'cargo');
INSERT INTO `option_info` VALUES ('316', '胸肉块', 'cargo');
INSERT INTO `option_info` VALUES ('317', '胸排肉', 'cargo');
INSERT INTO `option_info` VALUES ('318', '胸口油', 'cargo');
INSERT INTO `option_info` VALUES ('319', '小排', 'cargo');
INSERT INTO `option_info` VALUES ('320', '小米龙', 'cargo');
INSERT INTO `option_info` VALUES ('321', '心片', 'cargo');
INSERT INTO `option_info` VALUES ('322', '心管', 'cargo');
INSERT INTO `option_info` VALUES ('323', '翼板肉', 'cargo');
INSERT INTO `option_info` VALUES ('324', '眼肉', 'cargo');
INSERT INTO `option_info` VALUES ('325', '眼肉盖', 'cargo');
INSERT INTO `option_info` VALUES ('326', '仔骨', 'cargo');
INSERT INTO `option_info` VALUES ('327', '战斧', 'cargo');
INSERT INTO `option_info` VALUES ('328', '椎骨', 'cargo');
INSERT INTO `option_info` VALUES ('329', '脂肪', 'cargo');
