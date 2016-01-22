package cmri.snapshot.wall.server.helper;

import cmri.utils.lang.MapAdapter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhuyin on 11/10/15.
 */
public class ParasHelper {
    public static MapAdapter<String, Object> getParas(HttpServletRequest request) {
        MapAdapter<String, Object> returnMap = new MapAdapter<>();
        for(Map.Entry entry: request.getParameterMap().entrySet()){
            String value;
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                value = StringUtils.join((String[])valueObj, ",");
            }else{
                value = valueObj.toString();
            }
            returnMap.put((String) entry.getKey(), value);
        }

        return returnMap;
    }

    public static TreeMap<String, Object> getParasWithoutSig(HttpServletRequest request){
        return ParasHelper.getParas(request).remove("sig").getSorted();
    }
}
