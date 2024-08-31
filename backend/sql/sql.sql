CREATE DATABASE IF NOT EXISTS oj_sys COLLATE utf8mb4_general_ci CHARACTER SET utf8mb4;
use oj_sys;

CREATE TABLE user
(
    id           BIGINT AUTO_INCREMENT COMMENT 'id'
        PRIMARY KEY,
    nickName     VARCHAR(30)  collate utf8mb4_unicode_ci NULL COMMENT '用户昵称',
    userEmail    VARCHAR(255) UNIQUE                    NULL COMMENT '用户邮箱',
    userPassword VARCHAR(60)                            NULL COMMENT '密码',
    githubId     BIGINT UNIQUE                          NULL COMMENT 'github用户Id',
    userRole     VARCHAR(256) DEFAULT 'user'            NOT NULL COMMENT '用户角色：user/admin',
    unionId      VARCHAR(256) UNIQUE                    NULL COMMENT '微信开放平台id',
    userPhone    VARCHAR(20) UNIQUE                     NULL COMMENT '用户手机号(后期允许拓展区号和国际号码）',
    mpOpenId     VARCHAR(256) UNIQUE                    NULL COMMENT '公众号openId',
    userGender   INT                                    NOT NULL COMMENT '用户性别',
    userAvatar   VARCHAR(1024)                          NULL COMMENT '用户头像',
    createTime   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updateTime   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isActive     TINYINT      DEFAULT 1                 NOT NULL COMMENT '是否激活',
    isDelete     TINYINT      DEFAULT 0                 NOT NULL COMMENT '是否删除'
) COMMENT '用户' COLLATE = utf8mb4_general_ci;