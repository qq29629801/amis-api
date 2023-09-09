/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : talk-lowcode

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 09/09/2023 14:51:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_menu`;
CREATE TABLE `base_menu` (
  `id` bigint NOT NULL,
  `visible` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `orderNum` int DEFAULT NULL,
  `menuName` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `isCache` varchar(255) DEFAULT NULL,
  `component` varchar(255) DEFAULT NULL,
  `isFrame` varchar(255) DEFAULT NULL,
  `menuType` varchar(255) DEFAULT NULL,
  `perms` varchar(255) DEFAULT NULL,
  `queryParam` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of base_menu
-- ----------------------------
BEGIN;
INSERT INTO `base_menu` (`id`, `visible`, `icon`, `orderNum`, `menuName`, `remark`, `path`, `isCache`, `component`, `isFrame`, `menuType`, `perms`, `queryParam`, `status`, `parent_id`) VALUES (1, '0', NULL, 0, '系统管理', NULL, 'system', '0', NULL, '1', 'M', NULL, NULL, '0', 0);
INSERT INTO `base_menu` (`id`, `visible`, `icon`, `orderNum`, `menuName`, `remark`, `path`, `isCache`, `component`, `isFrame`, `menuType`, `perms`, `queryParam`, `status`, `parent_id`) VALUES (2, '0', 'user', 1, '用户管理', NULL, 'user', '0', 'system/user/index', '1', 'C', 'base:User:search', NULL, '0', 1);
INSERT INTO `base_menu` (`id`, `visible`, `icon`, `orderNum`, `menuName`, `remark`, `path`, `isCache`, `component`, `isFrame`, `menuType`, `perms`, `queryParam`, `status`, `parent_id`) VALUES (3, '0', 'tree-table', 0, '菜单管理', NULL, 'menu', '0', 'system/menu/index', '1', 'C', 'base:Menu:search', NULL, '0', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
