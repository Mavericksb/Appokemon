package com.rob.gab.appokemon.ui.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.StringRes
import com.rob.gab.appokemon.R
import kotlinx.android.synthetic.main.dialog_floating_toast.*
import java.util.*


class FloatingToastDialog(
    context: Context,
    val title: String,
    val message: String?,
    val type: FloatingToastType
) : Dialog(context), View.OnClickListener {

    constructor(
        context: Context,
        @StringRes titleIdRes: Int,
        @StringRes messageIdRes: Int,
        type: FloatingToastType
    ) : this(context, context.getString(titleIdRes), context.getString(messageIdRes), type)

    constructor(
        context: Context,
        message: String?,
        type: FloatingToastType
    ) : this(
        context,
        when (type) {
            FloatingToastType.Alert -> context.getString(R.string.floating_toast_alert_title)
            FloatingToastType.Error -> context.getString(R.string.floating_toast_error_title)
            FloatingToastType.Warning -> context.getString(R.string.floating_toast_warning_title)
        },
        message,
        type
    )

    private var floatingToastAnimation: Int? = null
    private var timerMillis: Long? = null
    private var mCancelListener: DialogInterface.OnCancelListener? = null

    var onCancel: (() -> Unit)? = null

    init {
        slideDown()
    }

    enum class FloatingToastType {
        Alert,
        Warning,
        Error;

        fun toIntType(): Int {
            return ordinal
        }

        companion object {
            fun parseType(intType: Int): FloatingToastType {
                return values()[intType]
            }
        }
    }

    /**
     * NOTE: SET FADE ANIMATION BEFORE Dialog.show()
     */
    fun fade(): FloatingToastDialog {
        floatingToastAnimation = R.style.FloatingToastFadeAnimation
        return this
    }

    fun slideDown(): FloatingToastDialog {
        floatingToastAnimation = R.style.FloatingToastSlideDownAnimation
        return this
    }

    fun noAnim(): FloatingToastDialog {
        floatingToastAnimation = null
        return this
    }

    fun timer(millis: Long): FloatingToastDialog {
        timerMillis = millis
        return this
    }

    fun setCancel(onCancel: () -> Unit): FloatingToastDialog {
        this.onCancel = onCancel
        return this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_floating_toast)

        initComponents()

        this.setOnCancelListener {
            onCancel?.invoke()
        }
    }



    private fun initComponents() {
        window?.attributes?.let { layoutParams ->
            layoutParams.gravity = Gravity.TOP
            layoutParams.flags = layoutParams.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            floatingToastAnimation?.let { layoutParams.windowAnimations = it }
            with(window!!) {
                attributes = layoutParams
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            toastTitle.text = title
            toastBody.text = message
            val idDrawable = when (type) {
                FloatingToastType.Alert -> R.drawable.ic_floating_toast_top_alert
                FloatingToastType.Error -> R.drawable.ic_floating_toast_top_error
                else -> R.drawable.ic_floating_toast_top_warning
            }
            floatingToastTopImage.setImageResource(idDrawable)
        }
    }

    override fun show() {
        super.show()

        timerMillis?.let {
            Timer().schedule(object: TimerTask() {
                override fun run() {
                    dismiss()
                }
            }, it)
        }
    }

    override fun onClick(v: View) {
        dismiss()
    }


}