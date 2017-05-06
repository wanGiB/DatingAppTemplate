package com.app.wemeet.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.app.wemeet.R;
import com.app.wemeet.models.NavItem;
import com.app.wemeet.ui.widgets.WeMeetTextView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Wan Clem
 */

public class NavListViewAdapter extends ArrayAdapter<NavItem> {

    private LayoutInflater layoutInflater;
    private SparseBooleanArray selectedPositions;
    private Context context;
    private FirebaseAuth firebaseAuth;

    public NavListViewAdapter(@NonNull Context context, List<NavItem> navItems) {
        super(context, 0, navItems);
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        selectedPositions = new SparseBooleanArray();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final NavItemsHolder navItemsHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.nav_drawer_list_item, parent, false);
            navItemsHolder = new NavItemsHolder(convertView);
            convertView.setTag(navItemsHolder);
        } else {
            navItemsHolder = (NavItemsHolder) convertView.getTag();
        }

        NavItem navItem = getItem(position);
        if (navItem != null) {
            navItemsHolder.navItemIcon.setImageResource(navItem.getNavItemIcon());
            navItemsHolder.navItemTitle.setText(navItem.getNavItemTitle());

            if (position == 0) {
                selectView(navItemsHolder);
            }

            if (position == 3) {
                navItemsHolder.navBadgeIndicator.setVisibility(View.VISIBLE);
                navItemsHolder.navBadgeIndicator.setText(String.valueOf(navItem.getBagde()));
            } else {
                navItemsHolder.navBadgeIndicator.setVisibility(View.GONE);
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPositions.clear();
                selectedPositions.put(position, true);
                selectView(navItemsHolder);
                notifyDataSetChanged();

                if (position == 5) {
                    showSignOutPrompt();
                }
            }
        });

        invalidateViewPositions(navItemsHolder, position);
        return convertView;
    }

    private void showSignOutPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you wish to sign out?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Attempt sign out
                if (firebaseAuth.getCurrentUser() != null) {
                    firebaseAuth.signOut();
                }

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void invalidateViewPositions(NavItemsHolder navItemsHolder, int position) {
        if (selectedPositions.size() != 0) {
            if (selectedPositions.get(position)) {
                navItemsHolder.navItemIcon.getDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
                navItemsHolder.navItemTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            } else {
                navItemsHolder.navItemIcon.getDrawable().setColorFilter(ContextCompat.getColor(context, R.color.icons_color), PorterDuff.Mode.MULTIPLY);
                navItemsHolder.navItemTitle.setTextColor(ContextCompat.getColor(context, R.color.icons_color));
            }
        }
    }

    private void selectView(NavItemsHolder navItemsHolder) {
        navItemsHolder.navItemIcon.getDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
        navItemsHolder.navItemTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }

    static class NavItemsHolder {

        @BindView(R.id.nav_drawer_item_icon)
        ImageView navItemIcon;

        @BindView(R.id.nav_item_title)
        WeMeetTextView navItemTitle;

        @BindView(R.id.nav_bagde_indicator)
        WeMeetTextView navBadgeIndicator;

        NavItemsHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}
