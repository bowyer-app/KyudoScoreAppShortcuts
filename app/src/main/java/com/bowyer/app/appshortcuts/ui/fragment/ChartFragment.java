package com.bowyer.app.appshortcuts.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bowyer.app.appshortcuts.R;

public class ChartFragment extends Fragment {
  public static ChartFragment newInstance() {
    return new ChartFragment();
  }

  @Bind(R.id.fragment_name) TextView fragmentName;

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_brank, container, false);
    ButterKnife.bind(this, rootView);
    fragmentName.setText(this.getClass().getSimpleName());
    return rootView;
  }
}
