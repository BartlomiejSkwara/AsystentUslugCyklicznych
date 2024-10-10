SET FOREIGN_KEY_CHECKS=0;
SET SQL_SAFE_UPDATES = 0;

delete from account_data;
delete from email;
delete from phone_number;
delete from certificate;
delete from cyclical_service;
delete from business;
delete from service_user;
delete from contact_data;
delete from status_type;
delete from status_change;
# delete from cyclical_service_status_change_entities;
SET SQL_SAFE_UPDATES = 1;

-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: uslugi_cykliczne
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `account_data`
--

LOCK TABLES `account_data` WRITE;
/*!40000 ALTER TABLE `account_data` DISABLE KEYS */;
INSERT INTO `account_data` (id_login_credentials,role,username,hashed_password) VALUES (1,'ROLE_admin','admin','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO'),(2,'ROLE_editor','Krisent','$2a$10$Frl2aoDXlhUWbr47Bex.je5HakgHbAe0fc90D.d8TUIsPnYkDnTKO'),(3,'ROLE_user','user','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO');
/*!40000 ALTER TABLE `account_data` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `contact_data`
--

LOCK TABLES `contact_data` WRITE;
/*!40000 ALTER TABLE `contact_data` DISABLE KEYS */;
INSERT INTO `contact_data` VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15),(16);
/*!40000 ALTER TABLE `contact_data` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `email`
--

LOCK TABLES `email` WRITE;
/*!40000 ALTER TABLE `email` DISABLE KEYS */;
INSERT INTO `email` (id_contact_data, id_email, email) VALUES (1,1,'hsk@gmail.com'),(1,2,'huta@interia.pl'),(2,3,'doof@gmail.com'),(2,4,'evil@gmail.com'),(2,5,'sdafewafwea@gmail.com'),(3,6,'csv@gmail.com'),(3,7,'steve@interia.pl'),(4,8,'mieszex@interia.pl'),(4,9,'ms@wp.pl'),(5,10,'flyingman@gmail.com'),(5,11,'mich2@gmail.com'),(5,12,'byrd@gmail.com'),(6,13,'pchem@gmail.com'),(6,14,'petro@gmail.com'),(7,15,'akr@gmail.com'),(7,16,'biocor@gmail.com'),(8,17,'aras@gmail.com'),(8,18,'acor@gmail.com'),(9,19,'blizz@gmail.com'),(10,20,'emps@gmail.com'),(11,21,'bigsmellyguy@gmail.com'),(12,22,'waitingtime@gmail.com'),(13,23,'logisticsisthekey@gmail.com'),(14,24,'evilguy@gmail.com'),(14,25,'necroMancer@gmail.com'),(15,26,'rageragerage@gmail.com'),(15,27,'barbariab@gmail.com'),(16,28,'ihatezariel@gmail.com'),(16,29,'frickzariel@gmail.com');
/*!40000 ALTER TABLE `email` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `phone_number`
--

LOCK TABLES `phone_number` WRITE;
/*!40000 ALTER TABLE `phone_number` DISABLE KEYS */;
INSERT INTO `phone_number` (id_contact_data, id_phone_number, number) VALUES (1,1,'12235352'),(2,2,'231325151'),(2,3,'235234'),(3,4,'9211111111'),(4,5,'966'),(4,6,'922'),(5,7,'24214125'),(6,8,'32523152'),(7,9,'23515'),(8,10,'432152'),(9,11,'1514'),(9,12,'235234'),(10,13,'4124215'),(11,14,'2516136'),(11,15,'213125124'),(12,16,'12412'),(12,17,'523412'),(13,18,'1535136'),(13,19,'731515'),(14,20,'125423162'),(14,21,'8534515'),(15,22,'5423512'),(15,23,'1346435'),(16,24,'12351325'),(16,25,'3245215');
/*!40000 ALTER TABLE `phone_number` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `business`
--

LOCK TABLES `business` WRITE;
/*!40000 ALTER TABLE `business` DISABLE KEYS */;
INSERT INTO `business` (contact_data_id,id_business,nip,regon,adres,name,comments) VALUES (1,1,'2142151','3214125125','Warszawa ul. Kmyrańskiego 33',' Huta Szkła Ozdobnego',NULL),(2,2,'2142151','3214125125','Danville Polly Parkway 9297 ','Doofenshmirtz Evil INC','Blah blah blah'),(6,3,'235143215','6345315','Dallas Texas','Petrochem',NULL),(7,4,'6134531','1343125','Chiba Japan','Akaromi BioCorp',NULL),(8,5,'32415','12351','Chu-ku, Tokyo, Japan','Arasaka Corporation',NULL),(9,6,'12421421','6132432','Irvine, California, United States','Blizzard Entertainment','Po prostu nie :>');
/*!40000 ALTER TABLE `business` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cyclical_service`
--

LOCK TABLES `cyclical_service` WRITE;
/*!40000 ALTER TABLE `cyclical_service` DISABLE KEYS */;
INSERT INTO `cyclical_service` (business_id_business,id_cyclical_service,one_time,price,service_user_id_service_user,agreement_number,description,status,assigned_account_data_entity_id_login_credentials) VALUES (2,1,_binary '\0',22.4,1,'1234124','Pięć ton eternitu w wiórach',256,1),(2,2,_binary '',5022.4,1,'42425331551','Dwie tony rudy ołowiu',256,1),(5,3,_binary '\0',332122.4,7,'5142','Amortyzacja dla chomika dżungarskiego',256,2),(3,4,_binary '\0',523.3,9,'51324','4 tony stali nierdzewnej',256,2),(1,5,_binary '\0',3000.4,2,'21532153','800 kg rudy sfalerytu',256,3),(2,6,_binary '\0',4213124.23,4,'9243124','300 kg auremitu',256,3);
/*!40000 ALTER TABLE `cyclical_service` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `certificate`
--

LOCK TABLES `certificate` WRITE;
/*!40000 ALTER TABLE `certificate` DISABLE KEYS */;
INSERT INTO `certificate` (id_certificate,id_cyclical_service,renewal_message_sent,most_recent,valid_from,valid_to,card_number,card_type,certificate_serial_number,name_in_organisation) VALUES (1,1,_binary '',_binary '\0','2024-12-31 15:53:16.000000','2026-12-31 15:53:16.000000','4215213','PHYSICAL','15213412421142214',NULL),(2,2,_binary '\0',_binary '','2024-12-31 15:53:16.000000',current_date+interval 70 day,'945322155213','PHYSICAL','32623523632',NULL),(3,1,_binary '',_binary '\0','2024-12-31 15:53:16.000000','2024-08-10 15:53:16.000000','4215213','PHYSICAL','15213412421142214','CEO'),(4,1,_binary '',_binary '\0','2024-12-31 15:53:16.000000','2024-08-10 15:53:16.000000','4215213','PHYSICAL','15213412421142214','CEO'),(5,1,_binary '',_binary '\0','2024-12-31 15:53:16.000000','2024-08-10 15:53:16.000000','4215213','PHYSICAL','15213412421142214','CEO'),(6,1,_binary '',_binary '\0','2024-12-31 15:53:16.000000','2024-08-10 15:53:16.000000','4215213','PHYSICAL','15213412421142214','CEO'),(7,1,_binary '\0',_binary '','2024-12-31 15:53:16.000000',current_date+interval 4 day,'4215213','PHYSICAL','15213412421142214','CEO'),(8,3,_binary '',_binary '\0','2024-12-31 15:53:16.000000','2026-12-31 15:53:16.000000','4215213','PHYSICAL','15213412421142214',NULL),(9,3,_binary '',_binary '\0','2024-12-31 15:53:16.000000','2024-08-10 15:53:16.000000','4215213','PHYSICAL','15213412421142214','CEO'),(10,3,_binary '\0',_binary '','2024-12-31 15:53:16.000000',current_date+interval 6 day,'4215213','PHYSICAL','15213412421142214','CEO'),(11,4,_binary '\0',_binary '','2024-12-31 15:53:16.000000',current_date+interval 9 day,'4215213','PHYSICAL','15213412421142214',NULL),(12,5,_binary '\0',_binary '','2024-12-31 15:53:16.000000',current_date+interval 15 day,'251235132','PHYSICAL','1613451',NULL),(13,6,_binary '\0',_binary '','2024-12-31 15:53:16.000000',current_date+interval 31 day,'8675987','PHYSICAL','4123124',NULL);
/*!40000 ALTER TABLE `certificate` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `status_type`
--
LOCK TABLES `status_type` WRITE;
/*!40000 ALTER TABLE `status_type` DISABLE KEYS */;
INSERT INTO `status_type` (id_status_type,status_name) VALUES (1,"AWAITING_RENEWAL"),(2,"PRO_FORM_SENT"),(4,"MARKED_FOR_CANCEL"),(8,"CANCELED"),(16,"MARKED_AS_NON_RENEWABLE"),(32,"RENEWED_ELSEWHERE"),(64,"PAYMENT_DONE"),(128,"INVOICE_SENT"),(256,"RENEWED");
/*!40000 ALTER TABLE `status_type` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping data for table `service_user`
--

LOCK TABLES `service_user` WRITE;
/*!40000 ALTER TABLE `service_user` DISABLE KEYS */;
INSERT INTO `service_user` (contact_data_id, has_polishpesel, id_service_user, name, surname,tax_identification_number, comments) VALUES (3,_binary '\0',1,'crazy','steve',NULL,NULL),(4,_binary '',2,'Mieszko','Pierwszy','96696696611',NULL),(5,_binary '\0',3,'Michael','Byrd',NULL,'Mostly orders a suspiciously large amounts of feathers'),(10,_binary '\0',4,'Big','E',NULL,NULL),(11,_binary '\0',5,'Ku\'gath','Plaguefather ',NULL,NULL),(12,_binary '\0',6,'Aaron','Burr ',NULL,NULL),(13,_binary '\0',7,'Roboute','Guilliman',NULL,NULL),(14,_binary '\0',8,'Sandro','the Great',NULL,NULL),(15,_binary '\0',9,'Crag','Hack',NULL,NULL),(16,_binary '\0',10,'Karlach','Cliffgate',NULL,NULL);
UNLOCK TABLES;

SET FOREIGN_KEY_CHECKS=1;

/*!40000 ALTER TABLE `service_user` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-20  9:36:42
