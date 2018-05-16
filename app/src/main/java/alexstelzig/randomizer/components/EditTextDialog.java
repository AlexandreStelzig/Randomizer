package alexstelzig.randomizer.components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import alexstelzig.randomizer.R;

public abstract class EditTextDialog {

    private AlertDialog alertDialog;
    private Context mContext;
    private EditText editText;

    public EditTextDialog(Context context, String title, String message, String initialText) {

        this.mContext = context;

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null)
            builder.setTitle(title);
        if (message != null)
            builder.setMessage(message);

        builder.setCancelable(false);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.edit_text_dialog, null);

        builder.setView(view);

        editText = (EditText) view.findViewById(R.id.list_name_edit_text);

        if (initialText != null)
            editText.setText(initialText);

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
                boolean handled = false;
                if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {
                    handled = true;
                    validateEditText(editText.getText().toString());
                }
                return handled;
            }
        });

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // validate stuff here
                        validateEditText(editText.getText().toString());
                    }
                });

            }
        });

    }

    public void show() {
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();
        editText.setSelection(editText.getText().length());
    }


    private void validateEditText(String listName) {

        if (listName.trim().isEmpty()) {
            Toast.makeText(mContext, "The list name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        alertDialog.dismiss();
        positiveButtonClicked(listName);

    }

    public abstract void positiveButtonClicked(String listName);

}
