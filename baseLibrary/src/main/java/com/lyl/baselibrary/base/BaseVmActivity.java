package com.lyl.baselibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;

import me.jessyan.autosize.AutoSizeCompat;

public abstract class BaseVmActivity<VM extends BaseViewModel, DB extends ViewDataBinding> extends AppCompatActivity {


    protected boolean needOnBackPressed = false;

    /**
     * 当前 activity 的实例
     */
    protected Activity currentActivity;

    public VM viewModel;

    public DB dataBinding;
    private Utils.ActivityLifecycleCallbacks activityLifecycleCallbacks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setWindowParams();
        super.onCreate(savedInstanceState);
        AutoSizeCompat.autoConvertDensityOfGlobal(getResources());
        EventBus.getDefault().register(this);

        currentActivity = this;

        activityLifecycleCallbacks = new Utils.ActivityLifecycleCallbacks();
        ActivityUtils.addActivityLifecycleCallbacks(this, activityLifecycleCallbacks);
        viewModel = new ViewModelProvider(this).get(getVMClass());
        dataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        dataBinding.setLifecycleOwner(this);
        initView(savedInstanceState);
        initView();
        initLiveData();
        initData();

    }

    public void initView() {

    }

    /**
     * 获取泛型对相应的 Class 对象
     */
    public Class<VM> getVMClass() {
        //  返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        //  返回表示此类型实际类型参数的 Type 对象的数组，想要获取第一泛型的Class，索引写 0
        return (Class) type.getActualTypeArguments()[0];//<VM>
    }


    /**
     * 获取资源 ID
     */
    protected abstract int getLayoutId();


    protected abstract void initView(Bundle savedInstanceState);


    /**
     * 初始化数据
     */
    protected abstract void initData();


    @CallSuper
    protected void initLiveData() {
        if (viewModel == null) return;
        viewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });
    }


    public void setWindowParams() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        window.setAttributes(params);
    }


    public void toActivity(Class<? extends Activity> clazz) {
        toActivity(clazz, null);
    }


    public void toActivity(Class<? extends Activity> clazz, Bundle args) {
        Intent intent = new Intent(this, clazz);
        if (null != args) {
            intent.putExtras(args);
        }
        startActivity(intent);
    }


    public void showLoading() {
        hideLoading();

    }


    public void hideLoading() {

    }


    protected void toast(@StringRes int resId) {
        ToastUtils.showShort(resId);
    }


    protected void toast(String msg) {
        ToastUtils.showShort(msg);
    }

    protected void toastLong(@StringRes int resId) {
        ToastUtils.showLong(resId);
    }


    protected void toastLong(String msg) {
        ToastUtils.showLong(msg);
    }


    @Override
    public void onBackPressed() {
        if (needOnBackPressed) {
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        currentActivity = null;
        ActivityUtils.removeActivityLifecycleCallbacks(this, activityLifecycleCallbacks);
        if (dataBinding != null) {
            dataBinding.unbind();
            dataBinding = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessageEvent(EmptyEvent event) {

    }

}
