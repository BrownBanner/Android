package banner.brown.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import banner.brown.BannerApplication;
import banner.brown.api.BannerAPI;
import banner.brown.models.Cart;
import banner.brown.ui.MainActivity;

/**
 * Created by Andy on 5/14/15.
 */
public class DeleteCartDialog extends DialogFragment{



        private String mCartName;

        public static String CART_NAME_EXTRA = "banner.brown.dialogs.cart.name.delete";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            mCartName = getArguments().getString(CART_NAME_EXTRA);
            final MainActivity activity = (MainActivity) getActivity();

            builder.setMessage("Are you sure you want to delete \"" + mCartName + "\"?" )
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BannerApplication.showLoadingIcon(activity);
                            BannerAPI.deleteNameCart(mCartName, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (!response.toLowerCase().contains("success")) {
                                        BannerApplication.hideLoadingIcon();
                                        BannerApplication.showToast(activity, "Error saving: " + response);
                                    } else {
                                        BannerAPI.getNamedCarts(new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                BannerApplication.hideLoadingIcon();
                                                BannerApplication.updateNamedCarts(response);
                                                activity.getNavigationDrawerFragment().updateSavedCarts();
                                                BannerApplication.showToast(activity, "cart named \"" + mCartName + "\" successfully deleted");
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                BannerApplication.hideLoadingIcon();
                                                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
