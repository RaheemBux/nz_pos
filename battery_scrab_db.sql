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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

/*Data for the table `customers` */

insert  into `customers`(`customer_id`,`name`,`email`,`contact1`,`address`,`customer_type`,`emirates_id`,`expiry_Date`,`contact2`,`contact3`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,'Shahid Khan','test@gmail.com','78978789789','Habbibb','CUSTOMER_VENDOR','EM435535','2023-05-31','4252557','25252255',NULL,NULL,'2023-06-10 02:26:43',NULL),(3,'Hashim','vendor1@gmail.com','09242425','UAE','CUSTOMER_VENDOR','Em546','2023-05-31','325252','2525255',NULL,NULL,'2023-06-10 02:26:21',NULL),(4,'Yaseen and Sons','vendor2@gmail.com','09242425232','DUBAI','VENDOR','ET-2233','2023-06-07','0313131244','0314555338',NULL,NULL,'2023-06-10 02:26:32',NULL),(5,'Sanaullah','sanaullah@gmail.com','23482782784','Abu Dhabi','VENDOR',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,'Shakeel','shak@nisum.com','03131312442','Dubai','CUSTOMER_VENDOR','EM333231','2023-05-02','03444444422','034253523523','2023-05-29 22:19:31',NULL,NULL,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

/*Data for the table `expense_category` */

insert  into `expense_category`(`expense_category_id`,`category`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,'Logistics','2023-05-31 23:52:40',NULL,'2023-06-01 00:52:42',NULL),(3,'Salary','2023-06-01 00:51:34',NULL,NULL,NULL),(4,'Fuel','2023-06-01 00:51:38',NULL,NULL,NULL),(5,'Utitlity','2023-06-01 00:52:02',NULL,NULL,NULL),(6,'Daily','2023-06-01 00:52:16',NULL,NULL,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

/*Data for the table `expenses` */

insert  into `expenses`(`expense_id`,`expense_category`,`name`,`ref_number`,`amount`,`expense_date`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,1,'Kiraya','SF-777',889,'2023-06-16','2023-06-01 00:50:47',NULL,'2023-06-01 00:53:51',NULL),(2,3,'Maqsood','LG-5555',2000,'2023-06-01','2023-06-01 00:53:25',NULL,NULL,NULL),(3,3,'Habib','787887',1000,'2023-06-04','2023-06-04 22:33:02',NULL,NULL,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

/*Data for the table `ledger` */

insert  into `ledger`(`ledger_id`,`customer_id`,`order_number`,`amount_paid`,`amount_remaining`,`total_amount`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,3,'NM-20230609211909',15750,0,15750,'2023-06-09 21:19:51',NULL,NULL,NULL),(2,4,'NM-20230609214902',15750,0,15750,'2023-06-09 21:49:37',NULL,NULL,NULL),(3,1,'NM-20230609222854',15750,0,15750,'2023-06-09 22:29:27',NULL,NULL,NULL),(4,3,'NM-20230610000336',7875,0,7875,'2023-06-10 00:04:15',NULL,NULL,NULL),(5,3,'NM-20230610001608',15750,0,15750,'2023-06-10 00:16:39',NULL,NULL,NULL),(6,5,'NM-20230610015850',9450,0,9450,'2023-06-10 01:59:21',NULL,NULL,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

/*Data for the table `product` */

insert  into `product`(`product_id`,`name`,`code`,`quantity`,`price`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,'Battery','B35345',6,0,'2023-06-10 00:14:38',NULL,'2023-06-10 01:59:20',NULL),(2,'Lens','L4323',6,0,'2023-06-10 00:14:49',NULL,'2023-06-10 01:59:20',NULL),(3,'IC','I244',6,0,'2023-06-10 00:14:58',NULL,'2023-06-10 01:59:20',NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `purchase` */

insert  into `purchase`(`purchase_id`,`product_id`,`customer_id`,`rec_number`,`purchase_number`,`purchase_date`,`quantity`,`unit`,`price`,`amount_paid`,`amount_remaining`,`total_amount`,`payment_type`,`is_taxable`,`tax_amount`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,NULL,3,'43255','NM-20230610001608','2023-06-10 00:00:00',NULL,NULL,NULL,15750,0,15750,'CASH',1,750,'2023-06-10 00:16:37',NULL,NULL,NULL),(2,NULL,5,'2424','NM-20230610015850','2023-06-07 00:00:00',NULL,NULL,NULL,9450,0,9450,'CASH',1,450,'2023-06-10 01:59:20',NULL,NULL,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

/*Data for the table `purchase_details` */

insert  into `purchase_details`(`purchase_details_id`,`purchase_id`,`product_id`,`quantity`,`unit`,`price`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,1,1,5,'MT',1000,'2023-06-10 00:16:37',NULL,NULL,NULL),(2,1,2,5,'MT',1000,'2023-06-10 00:16:37',NULL,NULL,NULL),(3,1,3,5,'MT',1000,'2023-06-10 00:16:37',NULL,NULL,NULL),(4,2,1,3,'MT',1000,'2023-06-10 01:59:20',NULL,NULL,NULL),(5,2,2,3,'MT',1000,'2023-06-10 01:59:20',NULL,NULL,NULL),(6,2,3,3,'MT',1000,'2023-06-10 01:59:20',NULL,NULL,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*Data for the table `sale` */

insert  into `sale`(`sale_id`,`product_id`,`customer_id`,`rec_number`,`sale_number`,`sale_date`,`quantity`,`unit`,`price`,`amount_paid`,`amount_remaining`,`total_amount`,`payment_type`,`is_taxable`,`tax_amount`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,NULL,7,'7866','NM-20230610001729','2023-06-10 00:00:00',NULL,NULL,NULL,7560,0,7560,'CASH',1,360,'2023-06-10 00:18:00',NULL,NULL,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

/*Data for the table `sale_details` */

insert  into `sale_details`(`sale_details_id`,`sale_id`,`product_id`,`quantity`,`unit`,`price`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,1,1,2,'MT',1200,'2023-06-10 00:18:00',NULL,NULL,NULL),(2,1,2,2,'MT',1200,'2023-06-10 00:18:00',NULL,NULL,NULL),(3,1,3,2,'MT',1200,'2023-06-10 00:18:00',NULL,NULL,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

/*Data for the table `stock` */

insert  into `stock`(`stock_id`,`product_id`,`stock_date`,`purchased`,`sold`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (1,1,'2023-06-10',5,0,'2023-06-10 00:16:37',NULL,NULL,NULL),(2,2,'2023-06-10',5,0,'2023-06-10 00:16:37',NULL,NULL,NULL),(3,3,'2023-06-10',5,0,'2023-06-10 00:16:37',NULL,NULL,NULL),(4,1,'2023-06-10',0,2,'2023-06-10 00:18:00',NULL,NULL,NULL),(5,2,'2023-06-10',0,2,'2023-06-10 00:18:00',NULL,NULL,NULL),(6,3,'2023-06-10',0,2,'2023-06-10 00:18:00',NULL,NULL,NULL),(7,1,'2023-06-07',3,0,'2023-06-10 01:59:20',NULL,NULL,NULL),(8,2,'2023-06-07',3,0,'2023-06-10 01:59:20',NULL,NULL,NULL),(9,3,'2023-06-07',3,0,'2023-06-10 01:59:20',NULL,NULL,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

/*Data for the table `users` */

insert  into `users`(`user_id`,`name`,`email`,`password`,`contact`,`address`,`emirates_id`,`expiry_date`,`created_date`,`created_by`,`last_modified_date`,`last_modified_by`) values (2,'Admin','admin@gmail.com','admin','009414124','UAE','EM43535','2023-05-31',NULL,NULL,NULL,NULL),(3,'Azeem','azeem@gmail.com','azeem','7897842323','Dubai','EM55554','2023-05-31',NULL,NULL,NULL,NULL),(5,'Rahim','rahim@gmail.com','admin','78789789789','Pakistan','EM345353','2023-05-31',NULL,NULL,NULL,NULL),(9,'Rahim','rahim44@gmail.com','admin','78789789789','Pakistan','EM6677777','2023-05-31',NULL,NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
