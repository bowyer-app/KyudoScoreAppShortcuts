package com.bowyer.app.appshortcuts.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bowyer.app.appshortcuts.R;

public class RecordTabFragment extends Fragment {
  public static RecordTabFragment newInstance() {
    return new RecordTabFragment();
  }

  @Bind(R.id.fragment_name) TextView fragmentName;

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_brank, container, false);
    ButterKnife.bind(this, rootView);
    fragmentName.setText(this.getClass().getSimpleName());
    return rootView;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    setHasOptionsMenu(true);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_record, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_calender:
        Snackbar.make(fragmentName, R.string.message_calendar, Snackbar.LENGTH_SHORT).show();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
