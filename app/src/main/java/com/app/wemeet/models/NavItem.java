package com.app.wemeet.models;

/**
 * Created by wan on 3/13/17.
 */

public class NavItem {

    public int bagde;

    public int navItemIcon;

    public String navItemTitle;

    public NavItem(int navItemIcon, String navItemTitle, int bagde) {
        this.navItemIcon = navItemIcon;
        this.navItemTitle = navItemTitle;
        this.bagde = bagde;
    }

    public void setBagde(int bagde) {
        this.bagde = bagde;
    }

    public void setNavItemIcon(int navItemIcon) {
        this.navItemIcon = navItemIcon;
    }

    public void setNavItemTitle(String navItemTitle) {
        this.navItemTitle = navItemTitle;
    }

    public int getBagde() {
        return bagde;
    }

    public int getNavItemIcon() {
        return navItemIcon;
    }

    public String getNavItemTitle() {
        return navItemTitle;
    }

}
