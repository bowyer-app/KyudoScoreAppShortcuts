package com.bowyer.app.appshortcuts.ui.model;

import android.support.v4.app.Fragment;
import android.view.MenuItem;
import com.bowyer.app.appshortcuts.R;
import com.bowyer.app.appshortcuts.ui.fragment.ChartFragment;
import com.bowyer.app.appshortcuts.ui.fragment.FortuneFragment;
import com.bowyer.app.appshortcuts.ui.fragment.InputTabFragment;
import com.bowyer.app.appshortcuts.ui.fragment.MemoListFragment;
import com.bowyer.app.appshortcuts.ui.fragment.NotificationFragment;
import com.bowyer.app.appshortcuts.ui.fragment.RecordTabFragment;

public enum Page {

  HOME(R.id.nav_input, InputTabFragment.class.getSimpleName()),
  RECORD(R.id.nav_record, RecordTabFragment.class.getSimpleName()),
  MEMO(R.id.nav_memo, MemoListFragment.class.getSimpleName()),
  CHART(R.id.nav_chart, ChartFragment.class.getSimpleName()),
  NOTIFICATION(R.id.nav_notification, NotificationFragment.class.getSimpleName()),
  FORTUNE(R.id.nav_fortune, FortuneFragment.class.getSimpleName());

  private final int menuId;
  private final String pageName;

  Page(int menuId, String pageName) {
    this.menuId = menuId;
    this.pageName = pageName;
  }

  public static Page forMenuId(MenuItem item) {
    int id = item.getItemId();
    return forMenuId(id);
  }

  public static Page forMenuId(int id) {
    for (Page page : values()) {
      if (page.menuId == id) {
        return page;
      }
    }
    throw new AssertionError("no menu enum found for the id. you forgot to implement?");
  }

  public static Page forName(Fragment fragment) {
    String name = fragment.getClass().getSimpleName();
    for (Page page : values()) {
      if (page.pageName.equals(name)) {
        return page;
      }
    }
    throw new AssertionError("no menu enum found for the id. you forgot to implement?");
  }

  public int getMenuId() {
    return menuId;
  }
}
