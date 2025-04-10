-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : jeu. 10 avr. 2025 à 23:16
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `epicerie`
--

-- --------------------------------------------------------

--
-- Structure de la table `lignevente`
--

CREATE TABLE `lignevente` (
  `id_ligne` int(11) NOT NULL,
  `id_produit` int(11) DEFAULT NULL,
  `id_vente` int(11) NOT NULL,
  `quantite` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `lignevente`
--

INSERT INTO `lignevente` (`id_ligne`, `id_produit`, `id_vente`, `quantite`) VALUES
(39, NULL, 24, 1),
(40, NULL, 25, 2),
(41, NULL, 26, 2),
(42, 37, 28, 2),
(43, 43, 28, 3);

-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

CREATE TABLE `produit` (
  `id_produit` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prix` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `produit`
--

INSERT INTO `produit` (`id_produit`, `nom`, `prix`) VALUES
(37, 'Lait', 4.5),
(38, 'Pain', 1.2),
(39, 'RedBull', 15),
(40, 'Sting', 6),
(42, 'Orange', 10),
(43, 'Ris', 3),
(45, 'lait122', 4.5);

-- --------------------------------------------------------

--
-- Structure de la table `vente`
--

CREATE TABLE `vente` (
  `id_vente` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `montant_total` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `vente`
--

INSERT INTO `vente` (`id_vente`, `date`, `montant_total`) VALUES
(1, '2025-04-01 17:50:11', 9.1),
(2, '2025-04-01 17:54:31', 6.5),
(3, '2025-04-02 11:24:54', 13),
(4, '2025-04-02 11:26:24', 12.5),
(5, '2025-04-02 16:46:45', 12),
(6, '2025-04-03 10:52:03', 7.5),
(7, '2025-04-03 11:32:47', 32.6),
(8, '2025-04-03 12:02:30', 4),
(9, '2025-04-05 19:10:55', 25.5),
(10, '2025-04-06 13:23:20', 3.5),
(11, '2025-04-06 22:08:03', 20),
(12, '2025-04-06 22:14:07', 20),
(13, '2025-04-06 22:15:45', 20),
(14, '2025-04-06 22:17:14', 20),
(15, '2025-04-08 17:02:37', 39),
(16, '2025-04-09 18:46:23', 2.95),
(17, '2025-04-09 18:58:29', 12.5),
(18, '2025-04-09 19:00:30', 16),
(19, '2025-04-09 19:32:02', 1998),
(20, '2025-04-09 19:35:19', 10),
(21, '2025-04-09 21:36:21', 1),
(22, '2025-04-09 23:11:53', 10),
(23, '2025-04-09 23:17:02', 646),
(24, '2025-04-10 00:05:22', 1.15),
(25, '2025-04-10 00:08:02', 16),
(26, '2025-04-10 01:03:35', 2000),
(27, '2025-04-10 01:04:42', 8),
(28, '2025-04-10 12:16:38', 18);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `lignevente`
--
ALTER TABLE `lignevente`
  ADD PRIMARY KEY (`id_ligne`),
  ADD KEY `idx_lignevente_vente` (`id_vente`),
  ADD KEY `idx_lignevente_produit` (`id_produit`);

--
-- Index pour la table `produit`
--
ALTER TABLE `produit`
  ADD PRIMARY KEY (`id_produit`),
  ADD KEY `idx_produit_nom` (`nom`);

--
-- Index pour la table `vente`
--
ALTER TABLE `vente`
  ADD PRIMARY KEY (`id_vente`),
  ADD KEY `idx_vente_date` (`date`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `lignevente`
--
ALTER TABLE `lignevente`
  MODIFY `id_ligne` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE `produit`
  MODIFY `id_produit` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT pour la table `vente`
--
ALTER TABLE `vente`
  MODIFY `id_vente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `lignevente`
--
ALTER TABLE `lignevente`
  ADD CONSTRAINT `lignevente_ibfk_1` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`) ON DELETE SET NULL,
  ADD CONSTRAINT `lignevente_ibfk_2` FOREIGN KEY (`id_vente`) REFERENCES `vente` (`id_vente`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
