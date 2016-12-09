package com.bowyer.app.appshortcuts.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bowyer.app.appshortcuts.R;
import com.bowyer.app.appshortcuts.ui.fragment.ChartFragment;
import com.bowyer.app.appshortcuts.ui.fragment.FortuneFragment;
import com.bowyer.app.appshortcuts.ui.fragment.InputTabFragment;
import com.bowyer.app.appshortcuts.ui.fragment.MemoListFragment;
import com.bowyer.app.appshortcuts.ui.fragment.NotificationFragment;
import com.bowyer.app.appshortcuts.ui.fragment.RecordTabFragment;
import com.bowyer.app.appshortcuts.ui.model.Page;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements FragmentManager.OnBackStackChangedListener {

  private static final String KEY_SHORTCUT_ID = "shortcut_id";
  private static final long DRAWER_CLOSE_DELAY_MILLS = 300L;

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    context.startActivity(intent);
  }

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
  @Bind(R.id.navigation_view) NavigationView navigationView;

  ImageView coverImage;
  private ActionBarDrawerToggle drawerToggle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    createShortCut();
    ButterKnife.bind(this);

    View headerLayout = navigationView.getHeaderView(0);
    coverImage = (ImageView) headerLayout.findViewById(R.id.coverImage);
    if (savedInstanceState == null) {
      addFragmentTab();
    }
    initToolbar();
    setUpNavigation();
    initDrawer();
    getSupportFragmentManager().addOnBackStackChangedListener(this);
    handleShortcutsAction();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    getSupportFragmentManager().removeOnBackStackChangedListener(this);
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    drawerToggle.syncState();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    drawerToggle.onConfigurationChanged(newConfig);
  }

  @Override public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
      return;
    }
    FragmentManager fm = getSupportFragmentManager();
    if (fm.getBackStackEntryCount() > 0) {
      fm.popBackStack();
      return;
    }
    super.onBackPressed();
  }

  @Override public void onBackStackChanged() {
    FragmentManager fm = getSupportFragmentManager();
    Fragment current = fm.findFragmentById(R.id.content);
    if (current == null) {
      finish();
      return;
    }
    Page page = Page.forName(current);
    navigationView.setCheckedItem(page.getMenuId());
  }

  private void initToolbar() {
    setToolbar(toolbar);
    ActionBar bar = getSupportActionBar();
    if (bar != null) {
      bar.setDisplayHomeAsUpEnabled(true);
      bar.setDisplayShowHomeEnabled(true);
      bar.setDisplayShowTitleEnabled(false);
      bar.setHomeButtonEnabled(true);
    }
  }

  public void setToolbar(Toolbar toolbar) {
    setSupportActionBar(toolbar);
  }

  private void initDrawer() {
    drawerToggle =
        new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
    drawerToggle.setDrawerIndicatorEnabled(true);
    drawerLayout.addDrawerListener(drawerToggle);
    Picasso.with(this).load(R.drawable.header).into(coverImage);
  }

  private void addFragmentTab() {
    replaceFragment(InputTabFragment.newInstance());
    navigationView.setCheckedItem(R.id.nav_input);
  }

  private void setUpNavigation() {
    navigationView.setNavigationItemSelectedListener(item -> {
      switchFragment(item);
      return false;
    });
  }

  private void switchFragment(MenuItem item) {
    int id = item.getItemId();

    boolean checked = false;
    switch (id) {
      case R.id.nav_input:
        drawerLayout.closeDrawer(GravityCompat.START);
        MainActivity.startActivity(this);
        finish();
        break;
      case R.id.nav_record:
        pageChange(RecordTabFragment.newInstance());
        checked = true;
        break;
      case R.id.nav_memo:
        pageChange(MemoListFragment.newInstance());
        checked = true;
        break;
      case R.id.nav_chart:
        pageChange(ChartFragment.newInstance());
        checked = true;
        break;
      case R.id.nav_notification:
        pageChange(NotificationFragment.newInstance());
        checked = true;
        break;
      case R.id.nav_fortune:
        pageChange(FortuneFragment.newInstance());
        checked = true;
        break;
      case R.id.nav_review:
        Snackbar.make(drawerLayout, R.string.message_review, Snackbar.LENGTH_SHORT).show();
        break;
      case R.id.nav_tell_friend:
        Snackbar.make(drawerLayout, R.string.message_share, Snackbar.LENGTH_SHORT).show();
        break;
      case R.id.nav_setting:
        Snackbar.make(drawerLayout, R.string.message_setting, Snackbar.LENGTH_SHORT).show();
        break;
    }
    drawerLayout.closeDrawer(GravityCompat.START);
    item.setChecked(checked);
  }

  private void pageChange(Fragment fragment) {
    new Handler().postDelayed(() -> {
      replaceFragment(fragment);
    }, DRAWER_CLOSE_DELAY_MILLS);
  }

  private void replaceFragment(Fragment fragment) {
    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
    ft.replace(R.id.content, fragment, fragment.getClass().getSimpleName());
    ft.addToBackStack(null);
    ft.commit();
  }

  private Intent createIntent() {
    Intent intent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    return intent;
  }

  @TargetApi(25) private void createShortCut() {
    if ((Build.VERSION.SDK_INT <= Build.VERSION_CODES.N)) {
      return;
    }

    ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
    List<ShortcutInfo> shortcutInfos = new ArrayList<>();

    Intent recordIntent = createIntent();
    String recordTitle = getString(R.string.shortcut_record);
    recordIntent.putExtra(KEY_SHORTCUT_ID, R.id.nav_record);

    ShortcutInfo record = new ShortcutInfo.Builder(this, recordTitle).setShortLabel(recordTitle)
        .setLongLabel(recordTitle)
        .setIcon(Icon.createWithResource(this, R.drawable.ic_short_record))
        .setIntent(recordIntent)
        .setRank(1)
        .build();

    shortcutInfos.add(record);

    Intent memoIntent = createIntent();
    String memoTitle = getString(R.string.shortcut_memo_list);
    memoIntent.putExtra(KEY_SHORTCUT_ID, R.id.nav_memo);
    ShortcutInfo memo = new ShortcutInfo.Builder(this, memoTitle).setShortLabel(memoTitle)
        .setLongLabel(memoTitle)
        .setIcon(Icon.createWithResource(this, R.drawable.ic_short_memo))
        .setIntent(memoIntent)
        .setRank(2)
        .build();

    shortcutInfos.add(memo);

    Intent graphIntent = createIntent();
    String graphTitle = getString(R.string.shortcut_graph);
    graphIntent.putExtra(KEY_SHORTCUT_ID, R.id.nav_chart);
    ShortcutInfo graph = new ShortcutInfo.Builder(this, graphTitle).setShortLabel(graphTitle)
        .setLongLabel(graphTitle)
        .setIcon(Icon.createWithResource(this, R.drawable.ic_short_chart))
        .setIntent(graphIntent)
        .setRank(3)
        .build();

    shortcutInfos.add(graph);

    Intent fortuneIntent = createIntent();
    String fortuneTitle = getString(R.string.shortcut_fortune);
    fortuneIntent.putExtra(KEY_SHORTCUT_ID, R.id.nav_fortune);
    ShortcutInfo fortune = new ShortcutInfo.Builder(this, fortuneTitle).setShortLabel(fortuneTitle)
        .setLongLabel(fortuneTitle)
        .setIcon(Icon.createWithResource(this, R.drawable.ic_short_fortune))
        .setIntent(fortuneIntent)
        .setRank(4)
        .build();

    shortcutInfos.add(fortune);

    shortcutManager.setDynamicShortcuts(shortcutInfos);
  }

  private void handleShortcutsAction() {

    if ((Build.VERSION.SDK_INT <= Build.VERSION_CODES.N)) {
      return;
    }

    int shortcutId = getIntent().getIntExtra(KEY_SHORTCUT_ID, 0);
    switch (shortcutId) {
      case R.id.nav_record:
        pageChange(RecordTabFragment.newInstance());
        break;
      case R.id.nav_memo:
        pageChange(MemoListFragment.newInstance());
        break;
      case R.id.nav_chart:
        pageChange(ChartFragment.newInstance());
        break;
      case R.id.nav_notification:
        pageChange(NotificationFragment.newInstance());
        break;
      case R.id.nav_fortune:
        pageChange(FortuneFragment.newInstance());
        break;
    }
    navigationView.setCheckedItem(shortcutId);
  }
}
