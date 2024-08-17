-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: uslugicykliczne
-- ------------------------------------------------------
-- Server version	8.0.37

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
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'jsm@interia.pl','John','121311111111111','Smith'),(2,'jan.kowalski@example.com	','Jan','123456789','Kowalski'),(3,'anna.nowak@example.com	','Anna','987654321','Nowak'),(4,'synan@gmail.com','Sylvester','5234325','Tynan '),(5,'mp@interia.pl','Markus','89898912','Persson'),(6,'bruh@interia.pl','Bruh','1213111111111','Bruhsky'),(7,'csv@gmail.com','Crazy','6661666','Steve'),(8,'mieszex@wp.pl','Mieszko','9660414','Pierwszy'),(9,'oraora@gmail.com','Jotaro','4323523515','Kujo'),(10,'logisticisthekey@gmail.com','Roboute ','35236512','Guilliman'),(11,'flyingman33@gmail.com','Michael ','312453215','Byrd'),(12,'goldenman@gmail.com','Big','432513424','E'),(13,'blackbeard42@gmail.com','Edward ','342532134','Teach'),(14,'itjustworks@gmail.com','Todd ','97321544','Howard'),(15,'topsecret@gmail.com','Johnny ','2345214313','English'),(16,'angus6@gmail.com','Angus','3214214','McSix'),(17,'angus5@gmail.com','Angus','42152134','McFife'),(18,'mithrandir@gmail.com','Gandalf','432514','the White');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping data for table `dysponenet`
--

LOCK TABLES `dysponenet` WRITE;
/*!40000 ALTER TABLE `dysponenet` DISABLE KEYS */;
INSERT INTO `dysponenet` VALUES (2,'12345','lulu','gaming','lgm@interia.pl','888666');
/*!40000 ALTER TABLE `dysponenet` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;







--
-- Dumping data for table `cyclical_service`
--

LOCK TABLES `cyclical_service` WRITE;
/*!40000 ALTER TABLE `cyclical_service` DISABLE KEYS */;
INSERT INTO `cyclical_service` VALUES (6,2,1,332.22,_binary '\0','2024-12-31 15:53:16.000000','2025-12-31 15:53:16.000000','Klient kupuje inniejsze coś','1, 0, 0'),(8,2,2,34432.22,_binary '\0','2024-12-31 15:53:16.000000','2027-12-31 15:53:16.000000','Klient kupuje inne coś','3, 0, 0'),(8,2,3,66.22,_binary '\0','2024-12-31 15:53:16.000000','2025-02-28 15:53:16.000000','Klient kupuje coś','0, 2, 0');
/*!40000 ALTER TABLE `cyclical_service` ENABLE KEYS */;
UNLOCK TABLES;


-- Dump completed on 2024-06-18 10:53:49