package org.giiwa.miaodisms.web;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.giiwa.app.web.admin.setting;
import org.giiwa.core.conf.Global;
import org.giiwa.core.noti.Sms;
import org.giiwa.framework.web.IListener;
import org.giiwa.framework.web.Module;
import org.giiwa.miaodisms.web.admin.miaodisms;

public class MiaodismsListener implements IListener {

  static Log log = LogFactory.getLog(MiaodismsListener.class);

  @Override
  public void onStart(Configuration conf, Module m) {
    // TODO Auto-generated method stub
    log.info("miaodisms is starting ...");

    // setting
    setting.register("miaodisms", miaodisms.class);

    Sms.register(Global.getInt("miaodi.seq", 0), new IndustrySMS());

  }

  @Override
  public void onStop() {
    // TODO Auto-generated method stub

  }

  @Override
  public void uninstall(Configuration conf, Module m) {
    // TODO Auto-generated method stub

  }

  @Override
  public void upgrade(Configuration conf, Module m) {
    // TODO Auto-generated method stub

  }

}
