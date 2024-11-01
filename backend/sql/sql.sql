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


create table if not exists question
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    answer      text                               null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    judgeCase   text                               null comment '判题用例（json 数组）',
    judgeConfig text                               null comment '判题配置（json 对象）',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    creatorId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (creatorId)
) comment '题目' collate = utf8mb4_unicode_ci;



-- 题目提交表
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    judgeInfo  text                               null comment '判题信息（json 对象）',
    status     int      default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目提交';