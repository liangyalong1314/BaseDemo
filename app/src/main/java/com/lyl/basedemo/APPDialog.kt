package com.lyl.basedemo

import android.content.Context
import android.os.Bundle
import com.blankj.utilcode.util.*

import com.lyl.basedemo.databinding.DialogAppBinding
import com.lyl.baselibrary.base.BaseVmDialog
import com.lyl.baselibrary.net.DownloadCallback
import com.lyl.baselibrary.net.RetrofitFactory
import com.lyl.baselibrary.net.RxNet
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.math.BigDecimal


/**
 *   作者：梁亚龙
 *   时间：2022/12/2
 *   描述：
 **/
class APPDialog(context: Context, var url: String, var name: String) :
    BaseVmDialog<DialogAppBinding>(context) {

    override fun getLayoutId(): Int = R.layout.dialog_app
    override fun initView(savedInstanceState: Bundle?) {
        setCanceledOnTouchOutside(false)
    }

    override fun initData() {

        downloadFile(url)
    }

    private var mDownloadTask: Disposable? = null
    private fun downloadFile(url: String) {
        val path: String =
            PathUtils.getExternalAppCachePath() + File.separator + TimeUtils.getNowMills() + ".apk"
        RxNet.download(url, path, object : DownloadCallback {
            override fun onStart(d: Disposable) {
                mDownloadTask = d
                LogUtils.d("onStart $d")

            }

            override fun onProgress(totalByte: Long, currentByte: Long, progress: Int) {
                LogUtils.d(byteFormat(totalByte)+  "onProgress $progress%")
                ThreadUtils.runOnUiThread {
                    dataBinding.pbProgress.setProgress(progress)
                    dataBinding.tvProgress.setText("$progress%")
                    dataBinding.tvTotalM.text = byteFormat(totalByte)
                    dataBinding.tvDownloadM.setText(byteFormat(currentByte))
                }

            }

            override fun onFinish(file: File) {
                LogUtils.d("onFinish " + file.absolutePath)
//                productionTesting()
                ThreadUtils.runOnUiThread {
                    dismiss()
                }

                AppUtils.installApp(file)


            }

            override fun onError(msg: String) {
                LogUtils.d("onError $msg")
            }
        })
    }



    fun <T> addDisposable(observable: Observable<T>, observer: DisposableObserver<T>) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
        )
    }

    private var compositeDisposable: CompositeDisposable? = null
    private fun byteFormat(bytes: Long): String? {
        val fileSize = BigDecimal(bytes)
        val megabyte = BigDecimal(1024 * 1024)
        var returnValue = fileSize.divide(megabyte, 2, BigDecimal.ROUND_UP).toFloat()
        if (returnValue > 1) {
            return returnValue.toString() + "MB"
        }
        val kilobyte = BigDecimal(1024)
        returnValue = fileSize.divide(kilobyte, 2, BigDecimal.ROUND_UP).toFloat()
        return returnValue.toString() + "KB"
    }

    override fun dismiss() {
        super.dismiss()
        RxNet.cancel(mDownloadTask)
    }

}