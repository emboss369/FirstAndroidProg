package com.example.username.mytodo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

/**
 * Created by user.name on 2017/01/23.
 */

public class InputCategoryDialog extends DialogFragment {
    private static final String ICON = "icon";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private EditText mCategoryText;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(InputCategoryDialog dialog);

        public void onDialogNegativeClick(InputCategoryDialog dialog);
    }

    NoticeDialogListener mListener;

    public static InputCategoryDialog newInstance(int icon, String title,
                                                  String message) {
        // ダイアログフラグメントを生成します
        InputCategoryDialog fragment = new InputCategoryDialog();
        // ダイアログフラグメントに渡すデータはBundleオブジェクト内に格納します
        Bundle args = new Bundle();
        // バンドルにダイアログフラグメントに渡す値をセットします。
        args.putInt(ICON, icon);
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        // ダイアログフラグメントにバンドルをセットします。
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // ダイアログフラグメントに渡されたデータをバンドルから取り出します。
        Bundle args = getArguments();
        int icon = args.getInt(ICON);
        String title = args.getString(TITLE);
        String message = args.getString(MESSAGE);


        mCategoryText = new EditText(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setIcon(icon)
                .setTitle(title)
                .setView(mCategoryText)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // アクティビティに対しpositiveボタンが押されたイベントを送り返します
                                mListener.onDialogPositiveClick(
                                        InputCategoryDialog.this);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // アクティビティに対しnegativeボタンが押されたイベントを送り返します
                                mListener.onDialogNegativeClick(
                                        InputCategoryDialog.this);
                            }
                        });
        // 最後にビルダーでAlertDialogオブジェクトを作成して戻り値とします
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public String getCategory() {
        return this.mCategoryText.getText().toString();
    }

}
