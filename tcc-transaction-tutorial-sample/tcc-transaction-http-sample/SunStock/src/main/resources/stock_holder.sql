/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : stock

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-05-04 23:45:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for stock_holder
-- ----------------------------
DROP TABLE IF EXISTS `stock_holder`;
CREATE TABLE `stock_holder` (
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `count` double DEFAULT NULL,
  `rate` varchar(255) DEFAULT NULL,
  `changeState` varchar(255) DEFAULT NULL,
  `changeCount` double DEFAULT NULL,
  `stockName` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
