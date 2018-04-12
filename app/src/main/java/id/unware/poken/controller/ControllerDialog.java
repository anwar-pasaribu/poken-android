package id.unware.poken.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import id.unware.poken.R;
import id.unware.poken.interfaces.InputDialogListener;

/**
 * Created by IT11 on 6/9/2015.
 * Various dialog box implementation.
 * (29 Juli) - Externalize all string.
 */
public class ControllerDialog {

    private static ControllerDialog instance;

    public static ControllerDialog getInstance() {
        if (instance == null) {
            instance = new ControllerDialog();
        }
        return instance;
    }

    public ProgressDialog showLoading(Context context) {
        ProgressDialog progressDialog = ProgressDialog.show(
                context,
                context.getString(R.string.loading),
                context.getString(R.string.msg_loading_data));
        progressDialog.setCancelable(true);

        return progressDialog;
    }

    /**
     * Generate progress dialog with specific message and title.
     *
     * @param context Context to display progress dialog.
     * @param title Title to show on proress dialog
     * @param message Message to show.
     *
     * @return Progress dialog object.
     */
    public ProgressDialog showLoading(
            Context context,
            @Nullable CharSequence title,
            @Nullable CharSequence message) {

        CharSequence
                charSequenceTitle = title == null
                ? context.getString(R.string.loading)
                : title,

                charSequenceMessage = message == null
                        ? context.getString(R.string.msg_loading_data)
                        : message;

        ProgressDialog progressDialog = ProgressDialog.show(
                context,
                charSequenceTitle,
                charSequenceMessage);

        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        return progressDialog;
    }

    public ProgressDialog showLoadingNotCancelable(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(null);
        progressDialog.setMessage(context.getString(R.string.msg_loading_data));
        progressDialog.show();
        return progressDialog;
    }

    public AlertDialog showYesNoDialog(String message, Context context, DialogInterface.OnClickListener listener) {
        return showYesNoDialog(message, context, listener,
                context.getString(R.string.btn_positive_yes).toUpperCase(),
                context.getString(R.string.btn_negative_no).toUpperCase());
    }

    public AlertDialog showYesNoDialog(String message, Context context,
                                       DialogInterface.OnClickListener listener,
                                       String yesOption, String noOption) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message)
                .setPositiveButton(yesOption.toUpperCase(), listener)
                .setNegativeButton(noOption.toUpperCase(), listener);

        return builder.show();
    }

    public AlertDialog showDialogInfo(String title, String message, Context context) {
        return showDialogInfo(title, message, context, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public AlertDialog showDialogInfo(String title, String message, Context context, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        builder.setMessage(Html.fromHtml(message)).setPositiveButton(
                context.getString(R.string.btn_positive_ok),
                listener);

        return builder.show();
    }

    public AlertDialog showDialogTwoOption(
            String message, Context context, String op1, String op2, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton(op1, listener)
                .setNegativeButton(op2, listener);
        return builder.show();
    }

    public AlertDialog showInputDialog(String message, Context context, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);

        // Set up the input
        EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        // input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(context.getString(R.string.btn_positive_ok), listener);
        builder.setNegativeButton(context.getString(R.string.btn_negative_cancel), listener);

        return builder.show();
    }

    public AlertDialog showInputDialog(
            CharSequence dialogTitle,
            CharSequence inputText,
            CharSequence inputTextHint,
            int inputType,
            final Context context, final InputDialogListener listener) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_input_text, null);

        final TextView title = mView.findViewById(R.id.title);
        final TextView textViewHelper = mView.findViewById(R.id.textViewHelper);
        final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);

        title.setText(dialogTitle);

        if (inputType == InputType.TYPE_CLASS_NUMBER) {
            userInputDialogEditText.setSelectAllOnFocus(true);
        }

        userInputDialogEditText.setText(inputText);
        userInputDialogEditText.requestFocusFromTouch();
        userInputDialogEditText.requestFocus();
        userInputDialogEditText.setRawInputType(inputType);
        textViewHelper.setText(inputTextHint);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(null);
        builder.setView(mView);

        // Set up the buttons
        builder.setPositiveButton(context.getString(R.string.btn_positive_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onInputTextDone(userInputDialogEditText.getText());

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userInputDialogEditText.getWindowToken(), 0);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(context.getString(R.string.btn_negative_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userInputDialogEditText.getWindowToken(), 0);

                dialog.dismiss();
            }
        });

        // Force keyboard to show
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


        return builder.show();
    }
}
