package alexstelzig.randomizer.components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import alexstelzig.randomizer.R;

public abstract class RandomItemDialog {

    private AlertDialog alertDialog;
    private Context mContext;
    private EditText editText;
    private NumberPicker numberPicker;
    private Switch activeSwitch;

    private final int MAX_WEIGHT = 100;

    public RandomItemDialog(Context context, String title, final boolean isEdit, String initialMessage, int initialWeight, boolean initialActive) {

        this.mContext = context;

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null)
            builder.setTitle(title);

        builder.setCancelable(false);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.item_dialog, null);

        builder.setView(view);

        editText = (EditText) view.findViewById(R.id.edit_text);
        numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        activeSwitch = (Switch) view.findViewById(R.id.is_active_switch);

        if(initialMessage != null)
            editText.setText(initialMessage);

        numberPicker.setMaxValue(MAX_WEIGHT);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);

        if(initialWeight != -1)
            numberPicker.setValue(initialWeight);

        activeSwitch.setChecked(initialActive);

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

        String neutralButtonText;

        if(isEdit)
            neutralButtonText = "Delete";
        else
            neutralButtonText = "Next";

        builder.setNeutralButton(neutralButtonText, new DialogInterface.OnClickListener() {
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

                        String itemName = editText.getText().toString();
                        int weight = numberPicker.getValue();
                        boolean active = activeSwitch.isChecked();
                        // validate stuff here
                        if(validateEditText(itemName)){
                            alertDialog.dismiss();
                            positiveButtonClicked(itemName, weight, active);
                        }
                    }
                });

                Button neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                neutralButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isEdit){
                            String itemName = editText.getText().toString();
                            int weight = numberPicker.getValue();
                            boolean active = activeSwitch.isChecked();
                            // validate stuff here
                            if(validateEditText(itemName)){
                                positiveButtonClicked(itemName, weight,active);
                                editText.setText("");
                                numberPicker.setValue(0);
                            }
                        }else{
                            deleteButtonClicked();
                            alertDialog.dismiss();
                        }
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


    private boolean validateEditText(String itemName) {

        if (itemName.trim().isEmpty()) {
            Toast.makeText(mContext, "The item name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }

    }

    public abstract void positiveButtonClicked(String listName, int weight, boolean active);

    public abstract void deleteButtonClicked();

}
