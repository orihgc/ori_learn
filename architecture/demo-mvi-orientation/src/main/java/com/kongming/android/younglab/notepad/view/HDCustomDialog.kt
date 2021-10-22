package com.kongming.android.younglab.notepad.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kongming.android.younglab.R

open class HDCustomDialog protected constructor() : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    protected lateinit var builder: HDDialogBuilder

    init {
        this.setStyle(STYLE_NO_TITLE, R.style.HDDialogWindowStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.apply {
            window?.setGravity(builder.gravity)
        }

        val layoutId = builder.layoutId
        return when {
            layoutId != View.NO_ID -> {
                inflater.inflate(layoutId, container, false)
            }
            else -> {
                throw IllegalArgumentException("layoutId cannot be NO_ID")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    protected fun initViews() {
        isCancelable = builder.cancelable
        dialog?.apply {
            setCanceledOnTouchOutside(builder.canceledOnTouchOutside)
        }
        builder.onViewInflater?.invoke(this)
    }

    fun listeners(listener: View.OnClickListener, vararg viewIds: Int) {
        for (i in viewIds.indices) {
            val view = getView<View>(viewIds[i])
            view.setOnClickListener(listener)
        }
    }

    fun listeners(listener: View.OnClickListener, vararg listenersViews: View) {
        for (view in listenersViews) {
            view.setOnClickListener(listener)
        }
    }

    fun listeners(listener: (view: View) -> Unit, vararg viewIds: Int) {
        for (i in viewIds.indices) {
            val view = getView<View>(viewIds[i])
            view.setOnClickListener {
                listener(it)
            }
        }
    }

    /**
     * 回调方法包含 dialog 引用，方便直接使其 dimiss
     */
    fun listenrs(listener: (dialog: HDCustomDialog, view: View) -> Unit, vararg viewIds: Int) {
        for (i in viewIds.indices) {
            val view = getView<View>(viewIds[i])
            view.setOnClickListener {
                listener(this, it)
            }
        }
    }

    fun listeners(listener: (view: View) -> Unit, vararg listenersViews: View) {
        for (view in listenersViews) {
            view.setOnClickListener {
                listener(it)
            }
        }
    }

    fun text(@IdRes viewId: Int, text: String?): HDCustomDialog {
        val textView = getView<TextView>(viewId)
        textView.text = text
        return this
    }

    fun <T : View> getView(@IdRes viewId: Int): T {
        return view?.findViewById(viewId) ?: throw IllegalArgumentException("viewId cannot find")
    }

    override fun dismiss() {
        dismissAllowingStateLoss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        builder.dismissCallback?.invoke(this)
    }

    class HDDialogBuilder {
        var layoutId: Int = View.NO_ID
        var gravity: Int = Gravity.CENTER
        var onViewInflater: ((HDCustomDialog: HDCustomDialog) -> Unit)? = null
        var dismissCallback: HDCustomDialogCallback? = null
        var showCallback: HDCustomDialogCallback? = null
        var cancelable: Boolean = false
        var canceledOnTouchOutside = false

        fun show(activity: FragmentActivity): HDCustomDialog {
            return HDCustomDialog().apply {
                builder = this@HDDialogBuilder
                show(activity.supportFragmentManager, null)
                showCallback?.invoke(this)
            }
        }
    }
}

typealias HDCustomDialogCallback = (HDCustomDialog) -> Unit

fun HDCustomDialog(builder: HDCustomDialog.HDDialogBuilder.() -> Unit): HDCustomDialog.HDDialogBuilder =
    HDCustomDialog.HDDialogBuilder().apply(builder)

fun dialogRequest(builder: HDCustomDialog.HDDialogBuilder.() -> Unit) =
    HDCustomDialog.HDDialogBuilder().apply(builder)

fun FragmentActivity.dialogShow(builder: HDCustomDialog.HDDialogBuilder.() -> Unit) =
    dialogRequest(builder).show(this)

fun Fragment.dialogShow(builder: HDCustomDialog.HDDialogBuilder.() -> Unit) =
    dialogRequest(builder).show(this.requireActivity())