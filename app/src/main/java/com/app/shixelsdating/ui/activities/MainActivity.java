package com.app.shixelsdating.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.shixelsdating.R;
import com.app.shixelsdating.models.FeedItem;
import com.app.shixelsdating.models.NavItem;
import com.app.shixelsdating.ui.adapters.FeedAdapter;
import com.app.shixelsdating.ui.adapters.NavListViewAdapter;
import com.app.shixelsdating.ui.widgets.CircleImageView;
import com.app.shixelsdating.ui.widgets.WeMeetTextView;
import com.app.shixelsdating.utils.DummyFeedManager;
import com.app.shixelsdating.utils.EndlessRecyclerViewScrollListener;
import com.app.shixelsdating.utils.HideShowScrollListener;
import com.app.shixelsdating.utils.UiUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_list_view)
    ListView navListView;

    @BindView(R.id.nav_toggle)
    ImageView navToggle;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.main_recycler_view)
    RecyclerView mainRecyclerView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.dummy_button)
    ImageView dummyButton;

    private List<NavItem> navItems = new ArrayList<>();
    private List<FeedItem> feedItems = new ArrayList<>();

    private CircleImageView signedInUserImageView;
    private WeMeetTextView signedInUserNameView;
    private ImageView settingsButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupNavListView();

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser signedInUser = firebaseAuth.getCurrentUser();

                if (signedInUser != null) {

                    Uri signedInUserProfilePhotoUri = signedInUser.getPhotoUrl();

                    if (signedInUserProfilePhotoUri != null) {

                        String signedInUserPhotoUrl = signedInUserProfilePhotoUri.toString();

                        if (!TextUtils.isEmpty(signedInUserPhotoUrl)) {
                            UiUtils.loadImage(MainActivity.this, signedInUserPhotoUrl, signedInUserImageView);
                        }

                    }

                    String signedInUsername = signedInUser.getDisplayName();
                    String signedInUserEmail = signedInUser.getEmail();

                    if (!TextUtils.isEmpty(signedInUsername)) {

                        signedInUserNameView.setText(signedInUsername);

                    } else {

                        if (!TextUtils.isEmpty(signedInUserEmail)) {
                            signedInUserNameView.setText(signedInUserEmail.split("@")[0]);
                        }

                    }


                } else {
                    Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(authIntent);
                    finish();
                }

            }

        };

        navToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.openDrawer(Gravity.START);
                } else {
                    drawerLayout.closeDrawer(Gravity.END);
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiUtils.sorryFontsAndIconsDontTally(MainActivity.this);
            }
        });

        dummyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButton.performClick();
            }
        });

        initFeeds();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void populateNavItems() {
        navItems.add(new NavItem(R.drawable.feed_icon, "Feed", 0));
        navItems.add(new NavItem(R.drawable.events_icon, "Events", 0));
        navItems.add(new NavItem(R.drawable.post_icon, "Post", 0));
        navItems.add(new NavItem(R.drawable.notifications_icon, "Notifications", 3));
        navItems.add(new NavItem(R.drawable.account_icon, "Account", 0));
        navItems.add(new NavItem(R.drawable.log_out_icon, "Log Out", 0));
    }

    private void setupNavListView() {

        populateNavItems();

        //Inflater nav header view
        @SuppressLint("InflateParams")
        View navListViewHeader = getLayoutInflater().inflate(R.layout.nav_header, null);

        //Bind header view children here
        bindNavHeaderViewChildren(navListViewHeader);

        NavListViewAdapter navListViewAdapter = new NavListViewAdapter(this, navItems);
        navListView.addHeaderView(navListViewHeader);
        navListView.setAdapter(navListViewAdapter);


    }

    private void bindNavHeaderViewChildren(View headerView) {
        signedInUserImageView = ButterKnife.findById(headerView, R.id.signed_in_user_image_view);
        signedInUserNameView = ButterKnife.findById(headerView, R.id.signed_in_user_name_view);
        settingsButton = ButterKnife.findById(headerView,R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButton.performClick();
            }
        });
    }

    private void initFeeds() {

        feedItems.addAll(DummyFeedManager.loadDummyFeeds());

        feedAdapter = new FeedAdapter(this, feedItems);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mainRecyclerView.setLayoutManager(linearLayoutManager);
        mainRecyclerView.setAdapter(feedAdapter);

        mainRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mainRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreFeeds();
            }

        });

        mainRecyclerView.addOnScrollListener(new HideShowScrollListener() {
            @Override
            public void onHide() {
                floatingActionButton.hide();
            }

            @Override
            public void onShow() {
                floatingActionButton.show();
            }
        });
    }

    private void loadMoreFeeds() {
        mainRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                feedItems.addAll(DummyFeedManager.loadDummyFeeds());
                feedAdapter.notifyItemInserted(feedItems.size() - 1);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
