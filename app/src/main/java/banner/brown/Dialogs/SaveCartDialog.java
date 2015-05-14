package banner.brown.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import banner.brown.BannerApplication;
import banner.brown.api.BannerAPI;
import banner.brown.models.Cart;
import banner.brown.ui.R;

/**
 * Created by Andy on 5/13/15.
 */
public class SaveCartDialog extends DialogFragment implements DialogInterface.OnClickListener{



        public static String SAVE_CART_NAME_EXTRA = "banner.brown.dialogs.cart.save";

        private EditText mEditText;

        public SaveCartDialog() {
            // Empty constructor required for DialogFragment
        }


        public static SaveCartDialog newInstance() {
            SaveCartDialog frag = new SaveCartDialog();

            return frag;
        }


        @Override
        public void onClick(DialogInterface d, int which) {
            String text = mEditText.getText().toString();
            text = text.replaceAll("\\s", "");
            BannerApplication.mostRecentNamedCart = text;
            BannerAPI.saveNamedCart(text, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    Object x = response;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });


        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Use an EditText view to get user input.
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View toAdd = inflater.inflate(R.layout.comment_dialog, null);
            mEditText = (EditText)toAdd.findViewById(R.id.comment_dialog);
            mEditText.setText(BannerApplication.mostRecentNamedCart);
            mEditText.setSelectAllOnFocus(true);
            mEditText.requestFocusFromTouch();
            builder.setView(toAdd);

            builder.setTitle("Save Cart")
                    .setPositiveButton("Save", this)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        @Override
        public void onActivityCreated(Bundle savedInstance) {
            super.onActivityCreated(savedInstance);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }





