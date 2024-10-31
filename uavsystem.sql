CREATE TABLE `地图数据表`  (
  `X` int NOT NULL,
  `Y` int NOT NULL,
  `地形类型` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `作物类型` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `是否可飞越` tinyint(1) NOT NULL,
  `是否可停靠` tinyint(1) NOT NULL,
  PRIMARY KEY (`X`, `Y`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `个人信息`  (
  `用户ID` int NOT NULL COMMENT 'ID号',
  `邮箱` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `密码` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `手机号` bigint NOT NULL COMMENT '手机号',
  `用户类型` smallint NOT NULL DEFAULT 0 COMMENT '用户类型，0表示普通用户，1表示管理员',
  PRIMARY KEY (`用户ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `历史记录表`  (
  `用户` int NOT NULL COMMENT '五位整数表示',
  `时间` datetime(0) NOT NULL,
  `内容` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`用户`, `时间`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `任务表`  (
  `任务ID` int NOT NULL COMMENT '四位整数表示',
  `任务下达时间` datetime(0) NOT NULL,
  `下达人` int NOT NULL,
  `任务内容` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `任务状态` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `任务完成时间` datetime(0) NULL DEFAULT NULL,
  `结果` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`任务ID`, `任务下达时间`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `日志`  (
  `时间` datetime(0) NOT NULL,
  `级别` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `来源` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `信息` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`时间`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `无人机返回数据表`  (
  `无人机` int NOT NULL COMMENT '四位整数表示',
  `返回时间` datetime(0) NOT NULL,
  `内容` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`无人机`, `返回时间`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `无人机故障记录`  (
  `无人机` int NOT NULL COMMENT 'ID,四位整数表示',
  `故障时间` date NOT NULL COMMENT '故障时间',
  `是否送修` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否送修',
  PRIMARY KEY (`无人机`, `故障时间`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `无人机任务表`  (
  `任务` int NOT NULL,
  `无人机` varchar(255) NOT NULL,
  PRIMARY KEY (`任务`, `无人机`)
);

CREATE TABLE `无人机维修记录`  (
  `无人机` int NOT NULL COMMENT 'ID,四位整数表示',
  `送修时间` datetime(0) NOT NULL COMMENT '送修时间',
  `问题` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '待确认' COMMENT '问题',
  `处理人` int NOT NULL COMMENT '处理人',
  `是否完成修理` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否完成修理',
  `修理完成时间` datetime(0) NULL DEFAULT NULL COMMENT '修理完成时间',
  PRIMARY KEY (`无人机`, `送修时间`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `无人机信息`  (
  `无人机ID` int NOT NULL COMMENT 'ID，四位整数表示',
  `型号` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '型号',
  `生产商` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '生产商',
  `续航` smallint NOT NULL COMMENT '续航',
  `图传距离` smallint NOT NULL COMMENT '图传距离',
  `购买时间` date NOT NULL COMMENT '购买时间',
  PRIMARY KEY (`无人机ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `无人机状态`  (
  `无人机` int NOT NULL COMMENT 'ID，四位整数表示',
  `电量` float NOT NULL COMMENT '电量',
  `位置坐标X` double NOT NULL COMMENT '位置坐标X',
  `位置坐标Y` double NOT NULL COMMENT '位置坐标Y',
  `高度坐标` double NOT NULL COMMENT '高度坐标',
  `当前动作` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '当前动作',
  PRIMARY KEY (`无人机`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `信息转传输情况表`  (
  `发送方` int NOT NULL,
  `转送方` int NOT NULL,
  `时间` datetime(0) NOT NULL,
  `是否成功传送给中心` bool NULL,
  PRIMARY KEY (`发送方`, `时间`)
);

CREATE TABLE `用户反馈表`  (
  `用户` int NOT NULL COMMENT '五位整数表示',
  `时间` datetime(0) NOT NULL,
  `内容` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`用户`, `时间`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

ALTER TABLE `历史记录表` ADD FOREIGN KEY (`用户`) REFERENCES `个人信息` (`用户ID`);
ALTER TABLE `任务表` ADD FOREIGN KEY (`下达人`) REFERENCES `个人信息` (`ID`);
ALTER TABLE `无人机返回数据表` ADD FOREIGN KEY (`无人机`) REFERENCES `无人机信息` (`无人机ID`);
ALTER TABLE `无人机故障记录` ADD FOREIGN KEY (`无人机`) REFERENCES `无人机信息` (`无人机ID`);
ALTER TABLE `无人机任务表` ADD FOREIGN KEY (`任务`) REFERENCES `任务表` (`任务id`);
ALTER TABLE `无人机维修记录` ADD FOREIGN KEY (`无人机`) REFERENCES `无人机信息` (`无人机ID`);
ALTER TABLE `无人机维修记录` ADD FOREIGN KEY (`处理人`) REFERENCES `个人信息` (`用户ID`);
ALTER TABLE `无人机状态` ADD FOREIGN KEY (`无人机`) REFERENCES `无人机信息` (`无人机ID`);
ALTER TABLE `信息转传输情况表` ADD FOREIGN KEY (`转送方`) REFERENCES `无人机信息` (`无人机ID`);
ALTER TABLE `信息转传输情况表` ADD FOREIGN KEY (`发送方`) REFERENCES `无人机信息` (`无人机ID`);
ALTER TABLE `用户反馈表` ADD FOREIGN KEY (`用户`) REFERENCES `个人信息` (`用户ID`);

