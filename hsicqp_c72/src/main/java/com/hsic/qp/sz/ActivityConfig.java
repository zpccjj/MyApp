package com.hsic.qp.sz;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;

import com.hsic.fragment.FragmentConfig;

import hsic.ui.HsicActivity;

public class ActivityConfig extends HsicActivity{
	private final static String TITLE = "返回";
	private Context getContext() {
		return ActivityConfig.this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(TITLE);

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new FragmentConfig()).commit();
	}

}
