package com.evan.util;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;


/**
 * @Description mysql中半结构化数据处理
 * @ClassName ClearTagUtils
 * @Author Evan
 * @date 2019.09.29 13:12
 */

@Slf4j
public class ClearTagUtils {

    public static String getClearTag(String source) {
        if (source == null) {
            return null;
        }

        String regExScript = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        String regExStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        String regExHtml = "<[^>]+>";
        String resource = source.replaceAll(regExScript, "");
        resource = resource.replaceAll(regExStyle, "");
        resource = resource.replaceAll(regExHtml, "");
        resource = resource.replaceAll(" ", "");
        resource = resource.replaceAll("\r\n|\r|\n|\t", "");
        resource = resource.replaceAll("¶", "");
        resource = resource.replaceAll("\"", "");

        String regExString ="\\d{4}(\\-|\\/|.)\\d{2}(\\-|\\/|.)\\d{4}(\\:)\\d{2}(\\:)\\d{2}(\\.)\\d{1}";

        if (Pattern.matches(regExString, resource)){
            DateTime dateTime = new DateTime(resource, "yyyy-MM-ddHH:mm:ss.SS");
            resource = dateTime.toString();
        }
        return resource;
    }

    public static void main(String[] args) {
        String test1 = "1、可对文件或文件夹设置自定义标签\n" +
                "2、通过标签可快速定位并访问到有该标签的文件或文件夹\n" +
                "3、性能要求与正常访问文件或文件夹一致";
        String test2 = "";

        String xx = "1、电子政务办，一期完结，二期的项目预定推动。¶2、电子政务办大数据应用解决方案沟通。¶3、金华市孝顺小学项目落地。¶4、金华市教育云 灾备平台项目沟通。|1、金华市教育局灾备。¶华三平台备份至华为平台。可以实现手动切换。¶2、解决方案制定。¶3、金华电子政务办，灾备情况沟通。";


        String xxx= "有\"\"被\"\"、\"\"受\"\"等被动词";
        String clearxxx = getClearTag(xxx);
        System.out.println(clearxxx);


//        String clearTag = getClearTag(test1);
//        System.out.println(clearTag);
//        String clearTag1 = getClearTag(test2);
//        System.out.println(clearTag1);
//        String clearTag2 = getClearTag(xx);
//        System.out.println(clearTag2);
    }

}
