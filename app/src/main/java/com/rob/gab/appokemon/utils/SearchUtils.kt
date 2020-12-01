package com.rob.gab.appokemon.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.whileSelect
import java.util.concurrent.TimeUnit

//Da utilizzare con onTextChanged(): ReceiveChannel
fun <T> ReceiveChannel<T>.debounce(lifecycle: LifecycleCoroutineScope, time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): ReceiveChannel<T> =
    Channel<T>(capacity = Channel.CONFLATED).also { channel ->
        lifecycle.launch {
            var value = receive()
            whileSelect {
                onTimeout(time) {
                    channel.offer(value)
                    value = receive()
                    true
                }
                onReceive {
                    value = it
                    true
                }
            }
        }
    }


public fun EditText.onTextChanged(): ReceiveChannel<String?> =
    Channel<String?>(capacity = Channel.UNLIMITED).also { channel ->
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                editable?.toString().let(channel::offer)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }


//Alternativa utilizzando callbackFlow. In questo modo il .debounce è ereditato. Funzionante ma ancora in fase sperimentale
//public fun EditText.onTextChanged(): Flow<String?> =
//    callbackFlow{
//        addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(editable: Editable?) {
//                offer( editable?.toString() )
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//        }
//        )
//        awaitClose { this.cancel() }
//    }