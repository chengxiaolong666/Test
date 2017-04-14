package com.xiaolonglib.cn.util;

import android.net.Uri;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaolong on 2017/4/14.
 */
public class StringUtil {

    public static Uri getFileUri(File file) {
        if (file == null || !file.exists()) return null;
        return Uri.parse("file://" + file.getAbsolutePath());
    }

    public static Uri getNetworkUri(String url) {
        Uri uri = null;
        if (!TextUtils.isEmpty(url)) {
            uri = Uri.parse(url);
        }
        return uri;
    }


    public static String parsePhoneNumber(String text) {
        if (TextUtils.isEmpty(text)) return null;
        String phoneTag1 = "<phone>";
        String phoneTag2 = "</phone>";
        if (text.indexOf(phoneTag1) == -1 || text.indexOf(phoneTag2) == -1) {
            return null;
        }
        int startIndex = text.indexOf(phoneTag1) + phoneTag1.length();
        int endIndex = text.indexOf(phoneTag2);
        if (startIndex < endIndex && startIndex < text.length() && endIndex <= text.length()) {
            return text.substring(startIndex, endIndex);
        }
        return null;
    }

    public static String parseWechatNumber(String text) {
        if (TextUtils.isEmpty(text)) return null;
        String wxTag1 = "<copy>";
        String wxTag2 = "</copy>";
        if (text.indexOf(wxTag1) == -1) {
            return null;
        }
        if (text.indexOf(wxTag2) == -1) {
            return null;
        }
        int startIndex = text.indexOf(wxTag1) + wxTag1.length();
        int endIndex = text.indexOf(wxTag2);
        if (startIndex < endIndex && startIndex < text.length() && endIndex <= text.length()) {
            return text.substring(startIndex, endIndex);
        }
        return null;
    }

    /**
     * InputStream转换为String
     *
     * @param inputstream
     * @return
     * @throws IOException
     */
    public static String inputStreamToString(InputStream inputstream) {
        return readFully(new InputStreamReader(inputstream, Charset.forName("UTF-8")));
    }

    private static String readFully(Reader reader) {
        StringWriter sw = new StringWriter();
        try {
            char ac[] = new char[1024];
            do {
                int i = reader.read(ac);
                if (i == -1)
                    break;
                sw.write(ac, 0, i);
            } while (true);

            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sw != null) {
                try {
                    sw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    /**
     * 算unicode字节,区分中英�?
     *
     * @param str
     * @return
     */
    public static int getChineseCount(CharSequence str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        int count = 0;
        if (!TextUtils.isEmpty(str)) {
            Pattern p = Pattern.compile(regEx);// 计算有几个unicode�?
            Matcher m = p.matcher(str);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count = count + 1;
                }
            }
            count += str.length();
        }
        return count;
    }

    /**
     * 把关键词的List集合转换成String
     *
     * @param join
     * @param strAry
     * @return
     */
    public static final String SPLIT_CHAR_KEYWORD = "#&#"; // 分隔�?"#\u0026#"

    public static String joinKeywords(List<String> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i == (list.size() - 1)) {
                    sb.append(list.get(i));
                } else {
                    sb.append(list.get(i)).append(SPLIT_CHAR_KEYWORD);
                }
            }
            return new String(sb);
        } else {
            return "";
        }
    }

    /**
     * 把String关键词转换成为List集合
     *
     * @param string
     * @param list
     * @return
     */
    public static List<String> splitKeywords(String string, List<String> list) {
        if (!TextUtils.isEmpty(string) && list != null) {
            String[] array = string.split(SPLIT_CHAR_KEYWORD);
            if (array != null) {
                for (int i = 0; i < array.length; i++) {
                    list.add(array[i]);
                }
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * @param
     * @return
     */
    public static String getFormattedCount(int count) {
        String numStr = count + "";
        if (count > 999 && count <= 99000) {
            if (count % 1000 == 0) {
                numStr = count / 1000 + "k";
            } else {
                float fNum = count;
                if (fNum >= 9999) {
                    numStr = String.format("%.0fk", fNum / 1000.0);
                } else {
                    numStr = String.format("%.1fk", fNum / 1000.0);
                }
            }
        } else if (count > 99000) {
            numStr = "99k";
        }
        return numStr;
    }

    /**
     * 把地点字段格式化，超过四个字的都格式化成四个�?
     *
     * @param location
     * @return
     */
    public static String setSubstringLocation(String location) {
        if (TextUtils.isEmpty(location)) return "";

        if (location.length() > 4) {
            location = location.substring(0, 4);
        }
        return location;
    }


    private static float getTextViewLength(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时�?,像素为多�?
        float textLength = paint.measureText(text);
        return textLength;
    }

    /**
     * 去掉字符串中的空格（针对网址�?
     *
     * @param str
     * @return
     */
    public static String removeAllSpaces(String str) {
        if (TextUtils.isEmpty(str)) return "";
        String s = str.replace(" ", "");
        return s;
    }

    /**
     * 处理印尼金额  三位 一个.
     *
     * @param str
     * @return
     */
    public static String handleInDoMoney(String str) {
        if(!TextUtils.isEmpty(str)) {
            StringBuilder stringBuilder = new StringBuilder();
            int len = str.length();
            if(len < 4) {
                return str;
            }
            for(int i = len -1; i > -1; i--) {
                stringBuilder.append(str.charAt(i));
                if((len - i) % 3 == 0) {
                    stringBuilder.append(".");
                }
            }
            str = stringBuilder.reverse().toString();
        }
        return str;
    }

    /**
     * 手机号码中间带星号
     * @param str
     * @return
     */
    public static String getPhoneFourStars(String str) {
        if(!TextUtils.isEmpty(str)) {
            int len = str.length() - 4;
            if(len < 1) {
                return str;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < len; i ++) {
                stringBuilder.append("*");
            }
            return stringBuilder.toString() + str.substring(len, len + 4);
        }
        return "";
    }

    /**
     * 获取时间  账单那里
     * @param time
     * @return
     */
    public static String getTime(long time) {
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d1=new Date(time);
        return format.format(d1);
    }
    /**
     * 获取时间  账单那里
     * @param time
     * @return
     */
    public static String getTime1(long time) {
        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
        Date d1=new Date(time);
        return format.format(d1);
    }
}
