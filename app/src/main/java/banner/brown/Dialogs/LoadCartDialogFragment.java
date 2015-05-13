package banner.brown.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import banner.brown.BannerApplication;
import banner.brown.api.BannerAPI;
import banner.brown.models.Cart;
import banner.brown.ui.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadCartDialogFragment extends DialogFragment {

        private String mCartName;

        public static String CART_NAME_EXTRA = "banner.brown.dialogs.cart.name";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            mCartName = getArguments().getString(CART_NAME_EXTRA);
            builder.setMessage("Make sure to save your current cart. Loading a cart will remove all current unregistered courses.")
                    .setPositiveButton("Load", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BannerAPI.loadNamedCart(mCartName, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.toLowerCase().contains("success")) {
                                        BannerApplication.mCurrentCart = new Cart();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }



