package com.cmri.pic.wall.server.controller;

import cmri.utils.configuration.ConfigFileManager;
import com.cmri.pic.wall.server.WebAppTest;
import com.cmri.pic.wall.server.domain.ResponseMessage;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

/**
 * Created by zhuyin on 1/6/16.
 */
public class ImageControllerTest extends WebAppTest {
    @Test
    public void testUpload() throws Exception {
        String file = ConfigFileManager.dump("pic/sandy-2009.jpg");
        ResponseMessage response = rest.reset()
                .setPath("image/upload")
                .add("img", new FileSystemResource(new File(file)))
                // .add("title", "test")
                .post();
        log(response);
        Assert.assertTrue(response.isSucceed());
    }
    @Test
    public void testGet() throws Exception {
        ResponseMessage response = rest.reset()
                .setPath("image/get")
                .get();
        log(response);
        Assert.assertTrue(response.isSucceed());
    }
    @Test
     public void testGetSince() throws Exception {
        ResponseMessage response = rest.reset()
                .setPath("image/get")
                .add("since", "1452069197084")
                .get();
        log(response);
        Assert.assertTrue(response.isSucceed());
    }
}