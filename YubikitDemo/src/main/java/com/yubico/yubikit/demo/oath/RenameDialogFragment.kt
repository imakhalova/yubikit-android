/*
 * Copyright (C) 2019 Yubico.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yubico.yubikit.demo.oath

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.yubico.yubikit.demo.R
import kotlinx.android.synthetic.main.oath_rename_dialog.view.*

class RenameDialogFragment : DialogFragment() {

    private var listener: DialogListener? = null

    private var selectedItemPosition: Int = 0
    private var defaultName = ""
    private var defaultIssuer = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        require(arguments != null) {"Use RenameDialogFragment.newInstance() method to create this fragment"}
        selectedItemPosition = arguments!!.getInt(ARG_POSITION)
        defaultName = arguments!!.getString(ARG_NAME, "")
        defaultIssuer = arguments!!.getString(ARG_ISSUER, "")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alert = AlertDialog.Builder(context)

        alert.setTitle("Set new name and issuer")

        val input = activity!!.layoutInflater.inflate(R.layout.oath_rename_dialog, null)

        input.name.setText(defaultName)
        input.new_name.setText(defaultIssuer)

        alert.setView(input)
        alert.setPositiveButton(android.R.string.ok) { _, _ ->
            val newName = input.name.editableText.toString()
            val newIssuer = input.new_name.editableText.toString()
            listener?.onNameChanged(selectedItemPosition, newName, newIssuer)
        }

        alert.setNegativeButton(android.R.string.cancel) {
            dialog, _ -> dialog.cancel()
        }
        return alert.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        listener = if (parent != null) {
            parent as DialogListener
        } else {
            context as DialogListener
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    interface DialogListener {
        fun onNameChanged(position: Int, name: String, issuer: String)
    }

    companion object {
        const val ARG_NAME = "name"
        const val ARG_ISSUER = "issuer"
        const val ARG_POSITION = "issuer"

        @JvmStatic
        fun newInstance(itemPosition: Int, currentName: String, currentIssuer: String) =
                RenameDialogFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_POSITION, itemPosition)
                        putString(ARG_NAME, currentName)
                        putString(ARG_ISSUER, currentIssuer)
                    }
                }
    }
}