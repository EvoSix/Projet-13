-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 17 sep. 2025 à 11:07
-- Version du serveur : 8.3.0
-- Version de PHP : 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `poc_chat`
--

-- --------------------------------------------------------

--
-- Structure de la table `chats`
--

DROP TABLE IF EXISTS `chats`;
CREATE TABLE IF NOT EXISTS `chats` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `chats`
--

INSERT INTO `chats` (`id`, `content`, `created_at`, `updated_at`) VALUES
(40, 'je fais un test d\'usure', '2025-09-15 16:57:11', '2025-09-15 16:57:11'),
(41, 'je teste le chat web socket socet', '2025-09-15 16:58:00', '2025-09-15 16:58:00'),
(42, 'Moi aussi sur microsoft edge', '2025-09-15 16:58:40', '2025-09-15 16:58:40');

-- --------------------------------------------------------

--
-- Structure de la table `conversation`
--

DROP TABLE IF EXISTS `conversation`;
CREATE TABLE IF NOT EXISTS `conversation` (
  `user_id` bigint NOT NULL,
  `chat_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`chat_id`),
  KEY `idx_conversation_user` (`user_id`),
  KEY `idx_conversation_chat` (`chat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `conversation`
--

INSERT INTO `conversation` (`user_id`, `chat_id`) VALUES
(1, 40),
(1, 41),
(2, 42);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `firstname` varchar(25) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `lastname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`firstname`, `id`, `lastname`, `email`, `adresse`, `role`, `created_at`, `updated_at`, `password`) VALUES
('Test2', 1, 'Test2', 'opez@op.com', 'formula', 'USER', '2025-09-10 18:48:53', '2025-09-10 18:48:53', '$2a$10$N/AcZ1Y3AfA770xU6kr/8OgyhVtnqM7tE3SzS4bLKOKZ0QHMpvmNa'),
('Test1', 2, 'Test1', 'opey@op.com', 'formula2', 'ADMIN', '2025-09-10 18:49:38', '2025-09-10 18:49:38', '$2a$10$bDgRCipyACO3iUyP.DKO3.XwUHl/D24/uCoIGjAdD2SVS7AQ5M3hy'),
('sqdsqd', 3, 'sqdqsdd', 'a@a.com', 'sqdsqdsqdqsd', 'USER', '2025-09-15 16:44:25', '2025-09-15 16:44:25', '$2a$10$oG9rgpNbpXo5CIMXYdjJJ.wWgNwroHLqOla55li5VdpgY3wJhSh0W');

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `conversation`
--
ALTER TABLE `conversation`
  ADD CONSTRAINT `fk_conversation_chat` FOREIGN KEY (`chat_id`) REFERENCES `chats` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_conversation_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
