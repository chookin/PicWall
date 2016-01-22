package cmri.snapshot.wall.server;

import cmri.snapshot.wall.server.helper.RestHandler;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhuyin on 1/6/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {PicWallApplication.class, WebMvcConfig.class})
@WebIntegrationTest(randomPort = true)
public abstract class WebAppTest {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    protected RestHandler rest = new RestHandler();
    // open inner web server
    //    private String base = "http://localhost:";
    //    @Value("${local.server.port}")
    //    private int port;

    public WebAppTest(){
    }

    protected void log(Object o){
        LOG.info(String.valueOf(o));
    }
}
