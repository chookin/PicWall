# 咔嚓墙

## 部署
* package <pre>mvn package -DskipTests</pre>
* copy, edit...
* execute
<pre>java -cp conf/:lib/:pic-wall-server-1.0.jar com.cmri.pic.wall.server.Application</pre>

## 访问
* 上传图片
http://111.13.47.169:8081/pages/index.html
* 获取图片地址
http://111.13.47.169:8081/image/get
http://111.13.47.169:8081/image/get?since=1452069197084