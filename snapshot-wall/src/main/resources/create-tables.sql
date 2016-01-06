DROP DATABASE if exists snapshot_wall;
create DATABASE if not exists snapshot_wall default character set utf8;
grant all on snapshot_wall.* to 'wall'@'localhost' identified by 'wall_cm';

use snapshot_wall;
DROP TABLE IF EXISTS pic;
CREATE TABLE pic (
  id BIGINT NOT NULL auto_increment,
  title varchar(64) COMMENT '标题',
  path varchar(512) NOT NULL COMMENT '图片的服务端存储地址',
  upload_time DATETIME COMMENT '上传时间',
  PRIMARY KEY  (id)
);