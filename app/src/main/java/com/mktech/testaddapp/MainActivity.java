package com.mktech.testaddapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mktech.testaddapp.dao.AppinfoHandle;
import com.mktech.testaddapp.entity.HotAppInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES;
import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity {
	private RecyclerView				mRecyclerView;
	private LinearLayoutManager			mLayoutManager;
	private List<HotAppInfo>			mList;
	private PackageManager				mPackageManager;
	private Button						mButton;
	private CommonAdapter<HotAppInfo>	mAdapter;
	private HeaderAndFooterWrapper		mHeaderAndFooterWrapper;
	private LoadMoreWrapper				mLoadMoreWrapper;
	private List<HotAppInfo>			mData;
	private AppinfoHandle mAppinfoHandle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mRecyclerView = (RecyclerView) findViewById(R.id.recyler);
		mPackageManager = getPackageManager();
		mButton= (Button) findViewById(R.id.buttonadd);
		initData();
		initRecycle();
	}

	private void initHeaderAndFooter() {
		mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
		Button button = new Button(this);
		button.setBackgroundResource(R.mipmap.add);
		mHeaderAndFooterWrapper.addFootView(button);
	}

	private void initRecycle() {
		mLayoutManager = new LinearLayoutManager(this);
		mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new CommonAdapter<HotAppInfo>(this, R.layout.test, mList) {
			@Override
			protected void convert(ViewHolder holder, HotAppInfo hotAppInfo, int position) {
				holder.setImageDrawable(R.id.image, hotAppInfo.getDrawableIcon());
				BitmapDrawable bitmapDrawable= (BitmapDrawable) hotAppInfo.getDrawableIcon();
				Bitmap bitmap=bitmapDrawable.getBitmap();
				holder.setImageBitmap(R.id.image,ImageUtil.getRoundedCornerBitmap(bitmap,5));
				holder.getConvertView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							CommonMethods.zoomOut(v, Constants.SIZE_BIG);
						} else {
							CommonMethods.zoomIn(v, Constants.SIZE_NORMAL);
						}
					}
				});

			}
		};

		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

				startApp(mList.get(position).getPackageName());

			}

			@Override
			public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {

				if (mList.get(position).getPackageName().equals("com.excellence.youtube"))
				{
					return false;
				}
				mAdapter.notifyItemRemoved(position);
				mList.remove(position);
				mAdapter.notifyDataSetChanged();
				if (mAdapter.getItemCount()<8)
				{
					mButton.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

	}

	private void initData() {
		mAppinfoHandle=new AppinfoHandle(this);
		mList = new ArrayList<>();
		mList.clear();
		mList=mAppinfoHandle.getHotAppinfo();
		Log.i("test",mList.size()+"");
		if (mList.size()==0)
		{
			mList.add(obtainAppinfo("com.excellence.youtube"));
			mAppinfoHandle.addHotAppinfo(obtainAppinfo("com.excellence.youtube"));
		}else {
			for (int i=0;i<mList.size();i++)
			{
				HotAppInfo hotAppInfo=mList.get(i);
				hotAppInfo=obtainAppinfo(hotAppInfo.getPackageName());
				mList.remove(i);
				mList.add(i,hotAppInfo);
			}
		}


	}

	private HotAppInfo obtainAppinfo(String name) {
		HotAppInfo hotAppInfo = null;
		try {
			ApplicationInfo applicationInfo = mPackageManager.getApplicationInfo(name, 0);
			hotAppInfo = new HotAppInfo(applicationInfo.name, applicationInfo.packageName, applicationInfo.loadIcon(mPackageManager));
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return hotAppInfo;

	}
	public void startApp(String name) {
		Intent intent = getPackageManager().getLaunchIntentForPackage(name);
		if (intent != null) {
			startActivity(intent);
		}

	}
	public void addApp(View view) {
		showPopWindow(view);
		Toast.makeText(this, "addapp", Toast.LENGTH_SHORT).show();
	}
	public void showPopWindow(View view) {
		View contentView = LayoutInflater.from(this).inflate(R.layout.popwindow, null);
		PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setWidth(800);
		popupWindow.setHeight(400);
		popupWindow.setContentView(contentView);
		ColorDrawable drawable = new ColorDrawable(0x000000);
		popupWindow.setBackgroundDrawable(drawable);
		RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.pop);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
		getinstallAPP();
		final CommonAdapter<HotAppInfo> commonAdapter = new CommonAdapter<HotAppInfo>(this, R.layout.popolist, mData) {
			@Override
			protected void convert(ViewHolder holder, HotAppInfo hotAppInfo, int position) {
				holder.setImageDrawable(R.id.image, hotAppInfo.getDrawableIcon());
				holder.setText(R.id.appname, hotAppInfo.getName());
				holder.getConvertView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							CommonMethods.zoomOut(v, Constants.SIZE_BIG);
						} else {
							CommonMethods.zoomIn(v, Constants.SIZE_NORMAL);
						}
					}
				});
			}
		};
		commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
				mList.add(mData.get(position));
				mAdapter.notifyDataSetChanged();
				if (mAdapter.getItemCount()==8)
				{
					mButton.setVisibility(View.GONE);
				}
				mAppinfoHandle.addHotAppinfo(mData.get(position));
			}

			@Override
			public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
				return false;
			}
		});
		recyclerView.setAdapter(commonAdapter);

		
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
	}
	
	public void getinstallAPP() {
		List<ApplicationInfo> applicationInfos = mPackageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		Collections.sort(applicationInfos, new ApplicationInfo.DisplayNameComparator(mPackageManager));// 排序
		mData = new ArrayList<>();
		for (ApplicationInfo app : applicationInfos) {
			mData.add(getAppinfo(app));
		}
		
	}
	
	public HotAppInfo getAppinfo(ApplicationInfo applicationInfo) {
		HotAppInfo hotAppInfo = new HotAppInfo();
		hotAppInfo.setName(applicationInfo.loadLabel(mPackageManager).toString());
		hotAppInfo.setPackageName(applicationInfo.packageName);
		hotAppInfo.setDrawableIcon(applicationInfo.loadIcon(mPackageManager));
		return hotAppInfo;
	}
}
