
-- ----------------------------
-- Table structure for `msn_addfriend`
-- ----------------------------

CREATE TABLE `msn_addfriend` (
  `requestSenderId` int(11) NOT NULL DEFAULT '0',
  `requestReceiverId` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`requestSenderId`,`requestReceiverId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of msn_addfriend
-- ----------------------------

-- ----------------------------
-- Table structure for `msn_addgroup`
-- ----------------------------

CREATE TABLE `msn_addgroup` (
  `userId` int(11) NOT NULL,
  `groupId` int(11) NOT NULL,
  PRIMARY KEY (`userId`,`groupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of msn_addgroup
-- ----------------------------

-- ----------------------------
-- Table structure for `msn_group`
-- ----------------------------

CREATE TABLE `msn_group` (
  `creater` int(11) DEFAULT NULL,
  `groupID` int(11) NOT NULL DEFAULT '0',
  `groupName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`groupID`),
  KEY `fk_creater` (`creater`),
  CONSTRAINT `fk_creater` FOREIGN KEY (`creater`) REFERENCES `msn_user` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of msn_group
-- ----------------------------
INSERT INTO `msn_group` VALUES ('1', '1', '西安学习群');

-- ----------------------------
-- Table structure for `msn_message`
-- ----------------------------

CREATE TABLE `msn_message` (
  `messageSender` int(11) DEFAULT NULL,
  `MessageID` int(11) NOT NULL DEFAULT '0',
  `MessageContent` longtext,
  `MessageSendTime` date DEFAULT NULL,
  `receiverID` int(11) DEFAULT NULL,
  `messageType` tinyblob,
  PRIMARY KEY (`MessageID`),
  KEY `fk_messageSender` (`messageSender`),
  CONSTRAINT `fk_messageSender` FOREIGN KEY (`messageSender`) REFERENCES `msn_user` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of msn_message
-- ----------------------------

-- ----------------------------
-- Table structure for `msn_user`
-- ----------------------------

CREATE TABLE `msn_user` (
  `UserID` int(11) NOT NULL DEFAULT '0',
  `UserNickname` varchar(25) DEFAULT NULL,
  `UserPassword` varchar(15) DEFAULT NULL,
  `UserSex` tinyblob,
  `isOnline` bit(1) DEFAULT NULL,
  PRIMARY KEY (`UserID`),
  KEY `uk_userId` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of msn_user
-- ----------------------------
INSERT INTO `msn_user` VALUES ('1', 'zhangxiaojing', '123456', null, '');
INSERT INTO `msn_user` VALUES ('2', 'tanqi', '123456', null, '');
