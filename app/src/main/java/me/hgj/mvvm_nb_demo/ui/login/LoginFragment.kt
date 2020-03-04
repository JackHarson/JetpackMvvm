package me.hgj.mvvm_nb_demo.ui.login

import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.mvvm_nb.ext.parseState
import me.hgj.mvvm_nb.ext.view.clickNoRepeat
import me.hgj.mvvm_nb.ext.view.visible
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.app.ext.init
import me.hgj.mvvm_nb_demo.app.ext.initClose
import me.hgj.mvvm_nb_demo.app.ext.showMessage
import me.hgj.mvvm_nb_demo.app.util.CacheUtil
import me.hgj.mvvm_nb_demo.app.util.SettingUtil
import me.hgj.mvvm_nb_demo.databinding.FragmentLoginBinding

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:
 */
class LoginFragment : BaseFragment<LoginRegisterViewModel, FragmentLoginBinding>() {

    override fun layoutId() = R.layout.fragment_login

    override fun initView() {
        mDatabind.viewmodel = mViewModel

        toolbar.initClose("登录") { Navigation.findNavController(it).navigateUp()}

        //设置颜色跟主题颜色一致
        appViewModel.appColor.value?.let {
            SettingUtil.setShapColor(login_sub, it)
            login_goregister.setTextColor(it)
            toolbar. setBackgroundColor(it)
        }

        login_clear.setOnClickListener { mViewModel.username.set("") }
        login_key.setOnCheckedChangeListener { buttonView, isChecked ->
            mViewModel.isShowPwd.set(
                isChecked
            )
        }
        login_key.visible()
        login_sub.clickNoRepeat {
            when {
                mViewModel.username.get().isEmpty() -> showMessage("请填写账号")
                mViewModel.password.get().isEmpty() -> showMessage("请填写密码")
                else -> mViewModel.loginReq()
            }
        }

        login_goregister.clickNoRepeat {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFrgment)
        }
    }

    override fun createObserver() {
        //监听请求结果
        mViewModel.loginResult.observe(this, Observer { viewState ->
            parseState(viewState, {
                //登录成功 通知账户数据发生改变鸟
                CacheUtil.setUser(it)
                appViewModel.userinfo.postValue(it)
                Navigation.findNavController(toolbar).navigateUp()
            }, {
                //登录失败
                showMessage(it.errorMsg)
            })
        })
    }

    override fun lazyLoadData() {

    }

}