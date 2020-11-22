-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        5.5.68-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- paydb 데이터베이스 구조 내보내기
DROP DATABASE IF EXISTS `paydb`;
CREATE DATABASE IF NOT EXISTS `paydb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `paydb`;

-- 함수 paydb.nextval 구조 내보내기
DROP FUNCTION IF EXISTS `nextval`;
DELIMITER //
CREATE FUNCTION `nextval`(seq_name VARCHAR(100)) RETURNS bigint(20)
BEGIN
     DECLARE cur_val int(20);
     
	  SELECT sequenceCurValue INTO cur_val
	    FROM SEQUENCE
	   WHERE sequenceName = seq_name; 
	   
     
      IF cur_val IS NOT NULL THEN
	 
	     UPDATE SEQUENCE
	        SET sequenceCurValue = IF ((sequenceCurValue + sequenceIncrement) > sequenceMaxValue,
	                                  IF (sequenceCycle = TRUE, sequenceMinValue, NULL), sequenceCurValue + sequenceIncrement)
	      WHERE sequenceName = seq_name;
	      
	 END IF;
	 
	 RETURN cur_val;
END//
DELIMITER ;

-- 테이블 paydb.room 구조 내보내기
DROP TABLE IF EXISTS `room`;
CREATE TABLE IF NOT EXISTS `room` (
  `room_id` varchar(50) NOT NULL,
  `user_id` int(9) NOT NULL,
  `reg_id` int(9) NOT NULL,
  `reg_date` datetime NOT NULL,
  `upd_id` int(11) NOT NULL,
  `upd_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 paydb.room:~0 rows (대략적) 내보내기
DELETE FROM `room`;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
/*!40000 ALTER TABLE `room` ENABLE KEYS */;

-- 테이블 paydb.sequence 구조 내보내기
DROP TABLE IF EXISTS `sequence`;
CREATE TABLE IF NOT EXISTS `sequence` (
  `sequenceName` varchar(100) NOT NULL,
  `sequenceIncrement` int(11) unsigned NOT NULL DEFAULT '1',
  `sequenceMinValue` int(11) unsigned NOT NULL DEFAULT '1',
  `sequenceMaxValue` bigint(20) unsigned NOT NULL DEFAULT '999999999',
  `sequenceCurValue` bigint(20) unsigned DEFAULT '1',
  `sequenceCycle` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 paydb.sequence:~1 rows (대략적) 내보내기
DELETE FROM `sequence`;
/*!40000 ALTER TABLE `sequence` DISABLE KEYS */;
INSERT INTO `sequence` (`sequenceName`, `sequenceIncrement`, `sequenceMinValue`, `sequenceMaxValue`, `sequenceCurValue`, `sequenceCycle`) VALUES
	('SPREAD', 1, 1, 999999999, 1, 0);
/*!40000 ALTER TABLE `sequence` ENABLE KEYS */;

-- 테이블 paydb.spread 구조 내보내기
DROP TABLE IF EXISTS `spread`;
CREATE TABLE IF NOT EXISTS `spread` (
  `spread_id` int(9) DEFAULT NULL,
  `owner_id` int(9) DEFAULT NULL,
  `room_id` varchar(50) DEFAULT NULL,
  `token_id` varchar(3) DEFAULT NULL,
  `spread_amounts` int(9) DEFAULT NULL,
  `friend_cnt` int(9) DEFAULT NULL,
  `reg_id` int(9) DEFAULT NULL,
  `reg_date` datetime DEFAULT NULL,
  `upd_id` int(9) DEFAULT NULL,
  `upd_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 paydb.spread:~0 rows (대략적) 내보내기
DELETE FROM `spread`;
/*!40000 ALTER TABLE `spread` DISABLE KEYS */;
/*!40000 ALTER TABLE `spread` ENABLE KEYS */;

-- 테이블 paydb.spread_friend 구조 내보내기
DROP TABLE IF EXISTS `spread_friend`;
CREATE TABLE IF NOT EXISTS `spread_friend` (
  `spread_id` int(9) NOT NULL,
  `friend_id` int(9) NOT NULL,
  `allocated_amount` int(9) NOT NULL,
  `get_yn` varchar(1) DEFAULT NULL,
  `reg_id` int(9) NOT NULL,
  `reg_date` datetime NOT NULL,
  `upd_id` int(9) NOT NULL,
  `upd_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 paydb.spread_friend:~0 rows (대략적) 내보내기
DELETE FROM `spread_friend`;
/*!40000 ALTER TABLE `spread_friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `spread_friend` ENABLE KEYS */;

-- 테이블 paydb.user 구조 내보내기
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) NOT NULL,
  `amounts` int(11) NOT NULL,
  `reg_id` int(11) NOT NULL,
  `reg_date` datetime NOT NULL,
  `upd_id` int(11) NOT NULL,
  `upd_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 paydb.user:~0 rows (대략적) 내보내기
DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
