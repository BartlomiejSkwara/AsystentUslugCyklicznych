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
delete from status_change;
delete from address;
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
INSERT INTO `account_data` (id_login_credentials,role,username,hashed_password) VALUES (1,'ROLE_admin','admin','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO'),(2,'ROLE_editor','Krisent','$2a$10$Frl2aoDXlhUWbr47Bex.je5HakgHbAe0fc90D.d8TUIsPnYkDnTKO'),(3,'ROLE_user','user','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO'),(4,'ROLE_user','emps','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO'),(5,'ROLE_user','kug','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO'),(6,'ROLE_user','aar','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO'),(7,'ROLE_user','rob','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO'),(8,'ROLE_user','san','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO'),(9,'ROLE_user','crag','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO'),(10,'ROLE_user','karlach','$2a$10$Rzzrw7bukifg80Xddf6qOOMSfSrgnFvdx.n59DS2ZeIh8TlGT0UfO');
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
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address`(address_id,apartment_number,locality,postal_code,property_number,street) VALUE (1,3,'Warszawa','00-049',1,'Kmyrańskiego'), (2,9297,'Danville','94506',2,'Polly Parkway'), (3,213,'Dallas','75243',3,'McKinney Avenue'), (4,21,'Chiba','100-8677',4,'Chibaminato'),(5,55,'Tokyo','100-8606',5,' Kotto-dori Ave'),(6,233,'Irvine','333-222',6,' Bruh Avenue');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping data for table `business`
--

LOCK TABLES `business` WRITE;
/*!40000 ALTER TABLE `business` DISABLE KEYS */;
INSERT INTO `business` (contact_data_id,id_business,nip,regon,name,comments,address_address_id) VALUES (1,1,'2142151','3214125125','Huta Szkła Ozdobnego',NULL,1),(2,2,'2142151','3214125125','Doofenshmirtz Evil INC','Blah blah blah',2),(6,3,'235143215','6345315','Petrochem',NULL,3),(7,4,'6134531','1343125','Akaromi BioCorp',NULL,4),(8,5,'32415','12351','Arasaka Corporation',NULL,5),(9,6,'12421421','6132432','Blizzard Entertainment','Po prostu nie :>',6);
/*!40000 ALTER TABLE `business` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cyclical_service`
--

LOCK TABLES `cyclical_service` WRITE;
/*!40000 ALTER TABLE `cyclical_service` DISABLE KEYS */;
INSERT INTO `cyclical_service` (business_id_business,id_cyclical_service,one_time,service_user_id_service_user,agreement_number,description,status) VALUES (2,1,_binary '\0',1,'1234124','Pięć ton eternitu w wiórach',256),(2,2,_binary '',1,'42425331551','Dwie tony rudy ołowiu',256),(5,3,_binary '\0',7,'5142','Amortyzacja dla chomika dżungarskiego',256),(3,4,_binary '\0',9,'51324','4 tony stali nierdzewnej',512),(1,5,_binary '\0',2,'21532153','800 kg rudy sfalerytu',512),(2,6,_binary '\0',4,'9243124','300 kg auremitu',512);
/*!40000 ALTER TABLE `cyclical_service` ENABLE KEYS */;
UNLOCK TABLES;






--
-- Dumping data for table `certificate`
--

LOCK TABLES `certificate` WRITE;
/*!40000 ALTER TABLE `certificate` DISABLE KEYS */;
INSERT INTO `certificate` (id_certificate,id_cyclical_service,renewal_message_sent,most_recent,valid_from,valid_to,card_number,card_type,certificate_serial_number,name_in_organisation) VALUES (1,1,_binary '',_binary '\0',current_date-interval 5 year ,current_date-interval 4 year ,'4215213',1,'25213412421142214',NULL),(2,2,_binary '\0',_binary '',current_date-interval 1 year ,current_date+interval 4 day ,'945322155213',1,'32623523632',NULL),(3,1,_binary '',_binary '\0',current_date-interval 4 year ,current_date-interval 3 year ,'4215213',1,'235213412421142214','CEO'),(4,1,_binary '',_binary '\0',current_date-interval 3 year ,current_date-interval 2 year ,'4215213',1,'35213412421142214','CEO'),(5,1,_binary '',_binary '\0',current_date-interval 2 year ,current_date-interval 1 year,'4215213',2,'45213412421142214','CEO'),(6,1,_binary '',_binary '\0',current_date-interval 1 year ,current_date-interval 0 year,'4215213',2,'55213412421142214','CEO'),(7,1,_binary '\0',_binary '',current_date,current_date+interval 3 year ,'4215213',2,'65213412421142214','CEO'),(8,3,_binary '',_binary '\0',current_date-interval 4 year ,current_date-interval 2 year ,'4215213',2,'75213412421142214',NULL),(9,3,_binary '',_binary '\0',current_date-interval 2 year ,current_date-interval 1 year ,'4215213',3,'85213412421142214','CEO'),(10,3,_binary '\0',_binary '',current_date-interval 2 year ,current_date+interval 15 day ,'4215213',3,'95213412421142214','CEO'),(11,4,_binary '\0',_binary '',current_date-interval 2 year ,current_date+interval 25 day ,'4215213',3,'43324234',NULL),(12,5,_binary '\0',_binary '',current_date-interval 1 year ,current_date+interval 14 day ,'251235132',4,'1613451',NULL),(13,6,_binary '\0',_binary '',current_date-interval 1 year ,current_date+interval 2 day ,'8675987',4,'4123124',NULL);
/*!40000 ALTER TABLE `certificate` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping data for table `service_user`
--

LOCK TABLES `service_user` WRITE;
/*!40000 ALTER TABLE `service_user` DISABLE KEYS */;
INSERT INTO `service_user` (contact_data_id, has_polishpesel, id_service_user, name, surname,tax_identification_number, comments,account_data) VALUES (3,_binary '\0',1,'crazy','steve',NULL,NULL,1),(4,_binary '',2,'Mieszko','Pierwszy','96696696611',NULL,2),(5,_binary '\0',3,'Michael','Byrd',NULL,'Mostly orders a suspiciously large amounts of feathers',3),(10,_binary '\0',4,'Big','E',NULL,NULL,4),(11,_binary '\0',5,'Ku\'gath','Plaguefather ',NULL,NULL,5),(12,_binary '\0',6,'Aaron','Burr ',NULL,NULL,6),(13,_binary '\0',7,'Roboute','Guilliman',NULL,NULL,7),(14,_binary '\0',8,'Sandro','the Great',NULL,NULL,8),(15,_binary '\0',9,'Crag','Hack',NULL,NULL,9),(16,_binary '\0',10,'Karlach','Cliffgate',NULL,NULL,10);
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
