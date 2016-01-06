package com.cmri.pic.wall.server.helper;

import cmri.utils.lang.MapAdapter;
import cmri.utils.web.HttpConstant;
import com.cmri.pic.wall.server.WebMvcConfig;
import com.cmri.pic.wall.server.domain.ResponseMessage;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuyin on 11/11/15.
 */
public class RestHandler {
    MapAdapter<String, Object> paras = new MapAdapter<>();
    String baseUrl = WebMvcConfig.baseUrl;
    String path;
    String method = HttpConstant.Method.POST;
    HttpHeaders headers = new HttpHeaders();

    public RestHandler() {
        initHeader();
    }

    void initHeader() {
        /* header("Accept-Charset", "utf-8");*/
        header("Content-type", "multipart/form-data; charset=utf-8");
    }

    public RestHandler setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * 设置请求的资源路径
     *
     * @param path 资源路径，不需要以"/"开头。
     **/
    public RestHandler setPath(String path) {
        this.path = path;
        return this;
    }

    public RestHandler add(String name, Object value) {
        paras.put(name, value);
        return this;
    }

    public RestHandler header(String key, String value) {
        this.headers.add(key, value);
        return this;
    }

    /**
     * 重置所有参数
     */
    public RestHandler reset() {
        this.paras.clear();
        this.headers.clear();
        initHeader();
        return this;
    }

    String getUrl() {
        if (baseUrl.endsWith("/")) {
            if (path.startsWith("/")) {
                return baseUrl + path.substring(1);
            } else {
                return baseUrl + path;
            }
        } else{
            if (path.startsWith("/")) {
                return baseUrl + path;
            } else {
                return baseUrl + "/" + path;
            }
        }
    }

    public ResponseMessage post() {
        updateSig();
        return justPost();
    }

    public ResponseMessage justPost() {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(getParas(), headers);
        // return getRestTemplate().postForObject(getUrl(), requestEntity, ResponseMessage.class);
        return getRestTemplate()
                .exchange(getUrl(), HttpMethod.POST, requestEntity, ResponseMessage.class).getBody();
    }

    public ResponseMessage get() {
        setMethod(HttpConstant.Method.GET);
        updateSig();
        String url = getUrl();
        if(this.paras.size() > 0){
            url = url + "?" + this.paras.join("=", "&");
        }
        return getRestTemplate()
                .getForObject(url, ResponseMessage.class);
    }

    RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new TestRestTemplate();
        replaceStringHttpMessageConverter(restTemplate);
        return restTemplate;
    }

    /**
     * 解决中文乱码问题，StringHttpMessageConverter默认使用的"ISO-8859-1",改为使用"utf-8".<br>
     * 若提交的是MultiValueMap，则RestTemplate 使用的是AllEncompassingFormHttpMessageConverter，此时，只修改RestTemplate的converters没有用. AllEncompassingFormHttpMessageConverter继承FormHttpMessageConverter，并由FormHttpMessageConverter.partConverters序列化请求参数。
     */
    void replaceStringHttpMessageConverter(RestTemplate template) {
        int index = 0;
        for (HttpMessageConverter converter : template.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                break;
            }
            ++index;
        }
        template.getMessageConverters().remove(index);
        template.getMessageConverters()
                .add(index, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        // replace AllEncompassingFormHttpMessageConverter
        index = 0;
        for (HttpMessageConverter converter : template.getMessageConverters()) {
            if (converter instanceof AllEncompassingFormHttpMessageConverter) {
                break;
            }
            ++index;
        }
        template.getMessageConverters().remove(index);
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new ByteArrayHttpMessageConverter());
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        converters.add(stringHttpMessageConverter);
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<>());
        if(ClassUtils.isPresent("javax.xml.bind.Binder", AllEncompassingFormHttpMessageConverter.class.getClassLoader())){
            converters.add(new Jaxb2RootElementHttpMessageConverter());
        }
        if(ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", AllEncompassingFormHttpMessageConverter.class.getClassLoader()) &&
                ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", AllEncompassingFormHttpMessageConverter.class.getClassLoader())){
            converters.add(new MappingJackson2HttpMessageConverter());
        }
        AllEncompassingFormHttpMessageConverter allEncompassingFormHttpMessageConverter = new AllEncompassingFormHttpMessageConverter();
        allEncompassingFormHttpMessageConverter.setPartConverters(converters);
        template.getMessageConverters().add(index, allEncompassingFormHttpMessageConverter);
    }

    MultiValueMap<String, Object> getParas() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.setAll(paras.get());
        return map;
    }

    RestHandler setMethod(String method) {
        this.method = method;
        return this;
    }

    RestHandler updateSig() {
        return this;
    }
}
