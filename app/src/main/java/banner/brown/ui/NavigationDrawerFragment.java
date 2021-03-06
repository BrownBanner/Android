package banner.brown.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Set;

import banner.brown.BannerApplication;
import banner.brown.Dialogs.DeleteCartDialog;
import banner.brown.Dialogs.LoadCartDialogFragment;
import banner.brown.Dialogs.SaveCartDialog;
import banner.brown.adapters.SemesterSpinnerAdapter;
import banner.brown.api.BannerAPI;
import banner.brown.models.Semester;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements View.OnClickListener{

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ScrollView mDrawerViews;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private Spinner mSpinner;

    private LinearLayout mNamedCartsOuter;
    private LinearLayout mNamedCarts;


    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerViews = (ScrollView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerViews.findViewById(R.id.save_cart_drawer).setOnClickListener(this);

        mDrawerViews.findViewById(R.id.logout_drawer).setOnClickListener(this);


        mSpinner = (Spinner) mDrawerViews.findViewById(R.id.semester_spinner);

        ArrayList<Semester> spinnerList = Semester.getSemestersToChooseFrom();
        // Create an ArrayAdapter using the string array and a default spinner layout
        SemesterSpinnerAdapter adapter = new SemesterSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,
                spinnerList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
        int spot = spinnerList.indexOf(BannerApplication.curSelectedSemester);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Semester selected = (Semester)parent.getItemAtPosition(position);
                BannerApplication.getInstance().setCurSelectedSemester(selected);
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    mDrawerLayout.closeDrawers();
                    ((MainActivity)getActivity()).updateCalendar(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner.setSelection(spinnerList.indexOf(BannerApplication.curSelectedSemester));


        mNamedCarts = (LinearLayout) mDrawerViews.findViewById(R.id.named_carts_container);
        mNamedCartsOuter = (LinearLayout) mDrawerViews.findViewById(R.id.named_carts_container_outer);

        return mDrawerViews;
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void updateSavedCarts() {
        Set<String> keySet = BannerApplication.mNamedCarts.keySet();
        String [] carts = keySet.toArray(new String[keySet.size()]);
        if (carts.length == 0) {
            mNamedCartsOuter.setVisibility(View.GONE);
        } else {
            mNamedCartsOuter.setVisibility(View.VISIBLE);
        }
        mNamedCarts.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();

        for (final String s : carts) {
            View toAdd = inflater.inflate(R.layout.name_cart_row, null);
            TextView name = (TextView) toAdd.findViewById(R.id.name_cart_text);
            name.setText(s);
            mNamedCarts.addView(toAdd);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadCartDialogFragment dialog = new LoadCartDialogFragment();
                    Bundle args = new Bundle();
                    args.putString(LoadCartDialogFragment.CART_NAME_EXTRA, s);
                    dialog.setArguments(args);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "load_cart_dialog");
                }
            });

            ImageView delete = (ImageView) toAdd.findViewById(R.id.delete_cart);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteCartDialog dialog = new DeleteCartDialog();
                    Bundle args = new Bundle();
                    args.putString(DeleteCartDialog.CART_NAME_EXTRA, s);
                    dialog.setArguments(args);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "load_cart_dialog");
                }
            });
        }

    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                updateSavedCarts();
                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

//        if (item.getItemId() == R.id.action_example) {
//            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(BannerApplication.curSelectedSemester.toString());
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);

    }

    @Override
    public void onClick(View v) {
//        if (mDrawerLayout != null) {
//            mDrawerLayout.closeDrawer(mFragmentContainerView);
//        }
//        if (v.getId() == R.id.department_drawer) {
//            Intent myIntent = new Intent(getActivity(), DepartmentsActivity.class);
//            //myIntent.putExtra("key", value); //Optional parameters
//            getActivity().startActivity(myIntent);
////        } else if (v.getId() == 1) {
////            Intent myIntent = new Intent(getActivity(), LoginActivity.class);
////
////            getActivity().startActivity(myIntent);
////        }
////        if (mDrawerListView != null) {
////            mDrawerListView.setItemChecked(position, true);
////        }
//            if (mDrawerLayout != null) {
//                mDrawerLayout.closeDrawer(mFragmentContainerView);
//            }
////        if (mCallbacks != null) {
////            mCallbacks.onNavigationDrawerItemSelected(position);
////        }
//        } else
        if (v.getId() == R.id.logout_drawer) {
            BannerBaseLogoutTimerActivity.logUserOut(getActivity());
            getActivity().finish();
        } else if (v.getId() == R.id.save_cart_drawer) {
            if (BannerApplication.mCurrentCart.getCourses().size() == 0) {
                Toast.makeText(getActivity(), "You have no courses currently in your cart", Toast.LENGTH_SHORT).show();
            } else {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                final SaveCartDialog commentDialog = SaveCartDialog.newInstance();
                commentDialog.show(fm, "fragment_save");
            }
        }
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
