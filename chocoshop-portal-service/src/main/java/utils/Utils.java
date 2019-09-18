package utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.regex.Pattern;

public class Utils {

    public static String uploadSingle(MultipartFile file, String pathString, String fileName, boolean hasType) {
        if(!file.isEmpty() && file.getContentType().split("/")[0].equals("image")){

            try {
                String imgUrlPrefix = pathString;
                String imgUrlSuffix = "";
                if(!hasType) imgUrlSuffix = fileName + "." + file.getContentType().split("/")[1];
                else imgUrlSuffix = fileName;
                File upload = new File(ResourceUtils.getURL("file:C:/upload").getPath(), pathString);
                if(!upload.exists()) upload.mkdirs();
                String UPLOAD_FOLDER = upload.getAbsolutePath();
                String fileFullPath = UPLOAD_FOLDER + "/" + imgUrlSuffix;
                byte[] bytes = file.getBytes();
                Path path = Paths.get(fileFullPath);
                System.out.println(path.toAbsolutePath().toString());
                Files.write(path, bytes);
                return imgUrlPrefix+"/"+imgUrlSuffix;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 盐值为md5(pwd+date) 2次
     * @param pwd
     * @return
     */
    public static String generateSalt(String pwd){
        return new SimpleHash("md5", pwd, ByteSource.Util.bytes(pwd + new Date()), 2).toHex();
    }

    /**
     * 密码
     * @param pwd
     * @param salt
     * @return
     */
    public static String generatePwd(String pwd, String salt){
        return new SimpleHash("md5", pwd, ByteSource.Util.bytes(salt), 2).toHex();
    }

    /**
     * refer: https://my.oschina.net/AaronDMC/blog/848408
     */
    public static class Validator {
        /**
         * 正则表达式：验证用户名
         */
        public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

        /**
         * 正则表达式：验证密码
         */
        public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

        /**
         * 正则表达式：验证手机号
         */
        public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

        /**
         * 正则表达式：验证邮箱
         */
        public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        /**
         * refer: https://juejin.im/entry/582d5649c4c97100543e1810
         * 正则表达式：验证用户名
         * 取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
         */
        public static final String REGEX_NICKNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";

        /**
         * 正则表达式：yyyy-MM-dd格式的日期校验，已考虑平闰年
         */
        public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";

        /**
         * 校验用户名
         *
         * @param username
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isUsername(String username) {
            return Pattern.matches(REGEX_USERNAME, username);
        }

        /**
         * 校验密码
         *
         * @param password
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isPassword(String password) {
            return Pattern.matches(REGEX_PASSWORD, password);
        }

        /**
         * 校验手机号
         *
         * @param mobile
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isMobile(String mobile) {
            return Pattern.matches(REGEX_MOBILE, mobile);
        }

        /**
         * 校验邮箱
         *
         * @param email
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isEmail(String email) {
            return Pattern.matches(REGEX_EMAIL, email);
        }

        /**
         * yyyy-MM-dd格式的日期校验，已考虑平闰年
         *
         * @param date
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isDate(String date) {
            return Pattern.matches(REGEX_DATE, date);
        }

    }
}
