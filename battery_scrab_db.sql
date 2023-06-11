/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.5.5-10.4.25-MariaDB : Database - battery_scrab_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`battery_scrab_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `battery_scrab_db`;

/*Table structure for table `customers` */

DROP TABLE IF EXISTS `customers`;

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `contact1` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `customer_type` enum('CUSTOMER','VENDOR','CUSTOMER_VENDOR') NOT NULL,
  `emirates_id` varchar(255) DEFAULT NULL,
  `expiry_Date` date DEFAULT NULL,
  `contact2` varchar(45) DEFAULT NULL,
  `contact3` varchar(45) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `customer_id_UNIQUE` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `customers` */

/*Table structure for table `expense_category` */

DROP TABLE IF EXISTS `expense_category`;

CREATE TABLE `expense_category` (
  `expense_category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(45) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`expense_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `expense_category` */

/*Table structure for table `expenses` */

DROP TABLE IF EXISTS `expenses`;

CREATE TABLE `expenses` (
  `expense_id` int(11) NOT NULL AUTO_INCREMENT,
  `expense_category` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `ref_number` varchar(45) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `expense_date` date DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`expense_id`),
  KEY `expense_category` (`expense_category`),
  CONSTRAINT `expenses_ibfk_1` FOREIGN KEY (`expense_category`) REFERENCES `expense_category` (`expense_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `expenses` */

/*Table structure for table `ledger` */

DROP TABLE IF EXISTS `ledger`;

CREATE TABLE `ledger` (
  `ledger_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL,
  `order_number` varchar(255) DEFAULT NULL,
  `amount_paid` double DEFAULT NULL,
  `amount_remaining` double DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ledger_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `ledger_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `ledger` */

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `code` varchar(45) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `product_id_UNIQUE` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `product` */

/*Table structure for table `purchase` */

DROP TABLE IF EXISTS `purchase`;

CREATE TABLE `purchase` (
  `purchase_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `rec_number` varchar(45) DEFAULT NULL,
  `purchase_number` varchar(45) DEFAULT NULL,
  `purchase_date` datetime DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit` enum('KG','MT') DEFAULT NULL,
  `price` double DEFAULT NULL,
  `amount_paid` double DEFAULT NULL,
  `amount_remaining` double DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `payment_type` enum('CASH','CHEQUE','ONLINE') DEFAULT NULL,
  `is_taxable` tinyint(1) DEFAULT NULL,
  `tax_amount` double DEFAULT 0,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`purchase_id`),
  UNIQUE KEY `purchase_id_UNIQUE` (`purchase_id`),
  KEY `purchased_product_id` (`product_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `purchase_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  CONSTRAINT `purchased_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `purchase` */

/*Table structure for table `purchase_details` */

DROP TABLE IF EXISTS `purchase_details`;

CREATE TABLE `purchase_details` (
  `purchase_details_id` int(11) NOT NULL AUTO_INCREMENT,
  `purchase_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit` enum('MT','KG') DEFAULT 'MT',
  `price` double DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`purchase_details_id`),
  KEY `product_id` (`product_id`),
  KEY `purchase_id` (`purchase_id`),
  CONSTRAINT `purchase_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `purchase_details_ibfk_3` FOREIGN KEY (`purchase_id`) REFERENCES `purchase` (`purchase_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `purchase_details` */

/*Table structure for table `sale` */

DROP TABLE IF EXISTS `sale`;

CREATE TABLE `sale` (
  `sale_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `rec_number` varchar(45) DEFAULT NULL,
  `sale_number` varchar(45) DEFAULT NULL,
  `sale_date` datetime DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit` enum('KG','MT') DEFAULT NULL,
  `price` double DEFAULT NULL,
  `amount_paid` double DEFAULT NULL,
  `amount_remaining` double DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `payment_type` enum('CASH','CHEQUE','ONLINE') DEFAULT NULL,
  `is_taxable` tinyint(1) DEFAULT NULL,
  `tax_amount` double DEFAULT 0,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sale_id`),
  UNIQUE KEY `sale_id` (`sale_id`),
  KEY `purchased_product_id` (`product_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `sale_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  CONSTRAINT `sale_product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sale` */

/*Table structure for table `sale_details` */

DROP TABLE IF EXISTS `sale_details`;

CREATE TABLE `sale_details` (
  `sale_details_id` int(11) NOT NULL AUTO_INCREMENT,
  `sale_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit` enum('MT','KG') DEFAULT 'MT',
  `price` double DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sale_details_id`),
  KEY `product_id` (`product_id`),
  KEY `sale_id` (`sale_id`),
  CONSTRAINT `sale_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `sale_details_ibfk_3` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`sale_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sale_details` */

/*Table structure for table `sales` */

DROP TABLE IF EXISTS `sales`;

CREATE TABLE `sales` (
  `sales_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `sales_date` datetime DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit` enum('KG','MT') DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `sales_number` varchar(100) DEFAULT NULL,
  `amount_paid` int(11) DEFAULT NULL,
  `amount_remaining` int(11) DEFAULT NULL,
  `total_amount` int(11) DEFAULT NULL,
  `payment_type` enum('CASH','CHEQUE','ONLINE') DEFAULT NULL,
  `is_taxable` tinyint(1) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sales_id`),
  KEY `product_id` (`product_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `sales_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sales` */

/*Table structure for table `stock` */

DROP TABLE IF EXISTS `stock`;

CREATE TABLE `stock` (
  `stock_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL,
  `stock_date` date DEFAULT NULL,
  `purchased` int(11) DEFAULT 0,
  `sold` int(11) DEFAULT 0,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`stock_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `stock_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `stock` */

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `contact` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `emirates_id` varchar(100) DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `last_modified_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*Data for the table `users` */

insert  into `users`(`user_id`,`name`,`email`,`password`,`contact`,`address`,`emirates_id`,`expiry_date`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,'Admin','admin@gmail.com','admin','9743435556','UAE','EM-24345','2023-06-30','2023-06-11 18:48:56',NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
