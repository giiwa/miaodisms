package org.giiwa.miaodisms.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.giiwa.core.bean.X;
import org.giiwa.core.conf.Global;
import org.giiwa.core.json.JSON;
import org.giiwa.framework.bean.OpLog;
import org.giiwa.framework.noti.Sms;
import org.giiwa.framework.web.Language;

/**
 * 验证码通知短信接口
 * 
 * @ClassName: IndustrySMS
 * @Description: 验证码通知短信接口
 *
 */
public class IndustrySMS implements Sms.ISender {

  private static Log                 log       = LogFactory.getLog(IndustrySMS.class);

  private static String              operation = "/industrySMS/sendSMS";

  private static Map<String, String> templates = new HashMap<String, String>();

  private static long                updated   = 0;

  private static String _createCommonParam() {
    // 时间戳
    String timestamp = Language.getLanguage().format(System.currentTimeMillis(), "yyyyMMddHHmmss");

    // 签名
    String sig = DigestUtils.md5Hex(Global.getString("miaodi.account_sid", "cfb0704580734d7xxxxxxxxxxxx")
        + Global.getString("miaodi.auth_token", "db8892c673204820839aa9xxxxxxxxxx") + timestamp);

    return "&timestamp=" + timestamp + "&sig=" + sig + "&respDataType=json";
  }

  private static String post(String url, String body) {
    log.debug("url=" + url + ", body=" + body);

    String result = "";
    try {
      OutputStreamWriter out = null;
      BufferedReader in = null;
      URL realUrl = new URL(url);
      URLConnection conn = realUrl.openConnection();

      // 设置连接参数
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setConnectTimeout(5000);
      conn.setReadTimeout(20000);

      // 提交数据
      out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
      out.write(body);
      out.flush();

      // 读取返回数据
      in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
      String line = "";
      boolean firstLine = true; // 读第一行不加换行符
      while ((line = in.readLine()) != null) {
        if (firstLine) {
          firstLine = false;
        } else {
          result += System.lineSeparator();
        }
        result += line;
      }
    } catch (Exception e) {
      log.error(body, e);
    }
    return result;
  }

  @Override
  public boolean send(String phone, JSON jo) {
    reset();

    log.debug("sendsms, jo=" + jo);

    String template = jo.getString("template");
    String content = templates.get(template);
    if (!X.isEmpty(content)) {
      for (String name : jo.keySet()) {
        content = content.replaceAll("\\$" + name, jo.getString(name));
      }

      String url = Global.getString("miaodi.url", "https://api.miaodiyun.com/20150822") + operation;
      String body = "accountSid=" + Global.getString("miaodi.account_sid", "cfb0704580734d7xxxxxxxxxxxx") + "&to="
          + phone + "&smsContent=" + content + _createCommonParam();
      // 提交请求
      String result = post(url, body);
      JSON j1 = JSON.fromObject(result);
      log.debug("result=" + result + ", j1=" + j1);

      if (j1 != null && X.isSame("00000", j1.getString("respCode"))) {
        OpLog.info("sms", "send", "sendsms, jo=" + jo + ", result=" + result, null);
        return true;
      } else {
        OpLog.warn("sms", "send", "sendsms, jo=" + jo + ", result=" + result, null);
        return false;
      }
    } else {
      OpLog.info("sms", "[" + template + "] missed, jo=" + jo, null);
    }

    return false;
  }

  public static void reset() {
    if (System.currentTimeMillis() - updated > X.AMINUTE) {
      updated = System.currentTimeMillis();
      String s = Global.getString("miaodi.template", X.EMPTY);
      String[] ss = s.split("[;；]");
      Map<String, String> m1 = new HashMap<String, String>();
      for (String s1 : ss) {
        String[] s2 = s1.split("[=＝]");
        if (s2.length == 2) {
          m1.put(s2[0].trim(), s2[1].trim());
        }
      }
      log.debug("templates=" + m1);
      templates = m1;
    }
  }
}
