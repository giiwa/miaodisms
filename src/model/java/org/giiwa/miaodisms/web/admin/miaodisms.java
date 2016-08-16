package org.giiwa.miaodisms.web.admin;

import org.giiwa.app.web.admin.setting;
import org.giiwa.core.bean.X;
import org.giiwa.core.conf.Global;
import org.giiwa.miaodisms.web.MiaodiSms;

/**
 * web api: /admin/setting/[method]/sync
 * <p>
 * used to set syncing configuration
 * 
 * @author joe
 *
 */
public class miaodisms extends setting {

  @Override
  public void reset() {
  }

  @Override
  public void set() {

    Global.setConfig("aliyu.sign", this.getString("aliyu_sign"));
    Global.setConfig("aliyu.appkey", this.getString("aliyu_appkey"));
    Global.setConfig("aliyu.secret", this.getString("aliyu_secret"));
    Global.setConfig("aliyu.templatecode", this.getString("aliyu_templatecode"));

    MiaodiSms.reset();

    this.set(X.MESSAGE, "修改成功！");

    get();
  }

  @Override
  public void get() {

    // public final static String url =
    // "http://gw.api.taobao.com/router/rest";
    // public static String appkey = "23365917";
    // public static String secret = "6270908d7574497bcbbdd11b23e6bcf3";

    this.set("aliyu_url", MiaodiSms.url);
    this.set("aliyu_sign", Global.getString("aliyu.sign", null));
    this.set("aliyu_appkey", Global.getString("aliyu.appkey", null));
    this.set("aliyu_secret", Global.getString("aliyu.secret", null));
    this.set("aliyu_templatecode", Global.getString("aliyu.templatecode", null));

    this.set("page", "/admin/setting.aliyusms.html");
  }

}
