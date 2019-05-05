/**
  * Copyright 2017 bejson.com 
  */
package us.xingkong.retrofitrealmpicload.bean;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Auto-generated: 2017-07-08 22:51:22
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class AppInfo extends RealmObject{

    public static AppInfo appInfo = null;

    private int apps_count;//get
    private int page_size;
    private RealmList<Items> items;
    public void setApps_count(int apps_count) {
         this.apps_count = apps_count;
     }
    public int getApps_count() {
         return apps_count;
     }

    public void setPage_size(int page_size) {
         this.page_size = page_size;
     }
    public int getPage_size() {
         return page_size;
     }

    public void setItems(RealmList<Items> items) {
         this.items = items;
     }
    public List<Items> getItems() {
         return items;
     }
}