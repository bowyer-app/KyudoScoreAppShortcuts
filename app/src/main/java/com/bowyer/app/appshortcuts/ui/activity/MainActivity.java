package com.bowyer.app.appshortcuts.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

  private void handleShortcutsAction() {
    boolean hasExtra = getIntent().hasExtra(KEY_SHORTCUT_ID);
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N || !hasExtra) {
      return;
    }
    String shortcutId = getIntent().getStringExtra(KEY_SHORTCUT_ID);
    Page page = Page.forMenuId(shortcutId);
    switch (page.getMenuId()) {
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
    navigationView.setCheckedItem(page.getMenuId());
  }
}
