package org.giiwa.miaodisms.web.admin;

import org.giiwa.app.web.admin.setting;
import org.giiwa.core.bean.X;
import org.giiwa.core.conf.Global;
import org.giiwa.miaodisms.web.IndustrySMS;

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

    Global.setConfig("miaodi.seq", this.getInt("seq"));
    Global.setConfig("miaodi.account_sid", this.getString("account_sid"));
    Global.setConfig("miaodi.auth_token", this.getString("auth_token"));
    Global.setConfig("miaodi.url", this.getString("url"));
    Global.setConfig("miaodi.template", this.getHtml("template"));

    IndustrySMS.reset();
    
    this.set(X.MESSAGE, lang.get("save.success"));

    get();
  }

  @Override
  public void get() {
    this.set("page", "/admin/setting.miaodisms.html");
  }

}
