package com.huertaalexis.civiladvocacy;

import java.io.Serializable;

public class Official implements Serializable {

    private final String oName;
    private final String oOffice;
    private final String oParty;
    private final String oFacebook;
    private final String oTwitter;
    private final String oYoutube;
    private final String oAddress;
    private final String oPhone;
    private final String oEmail;
    private final String oWebsite;
    private final String oPhoto;


    public Official(String oName, String oOffice, String oParty, String oFacebook, String oTwitter, String oYoutube,
                    String oAddress, String oPhone, String oEmail, String oWebsite, String oPhoto) {
        this.oName = oName;
        this.oOffice = oOffice;
        this.oParty = oParty;
        this.oFacebook = oFacebook;
        this.oTwitter = oTwitter;
        this.oYoutube = oYoutube;
        this.oAddress = oAddress;
        this.oPhone = oPhone;
        this.oEmail = oEmail;
        this.oWebsite = oWebsite;
        this.oPhoto = oPhoto;
    }

    String getoName(){
        return oName;
    }
    String getoOffice(){
        return oOffice;
    }
    String getoParty(){
        return oParty;
    }
    String getoFacebook(){
        return oFacebook;
    }
    String getoTwitter(){
        return oTwitter;
    }
    String getoYoutube(){
        return oYoutube;
    }
    String getoAddress(){
        return oAddress;
    }
    String getoPhone(){
        return oPhone;
    }
    String getoEmail(){
        return oEmail;
    }
    String getoWebsite(){
        return oWebsite;
    }
    String getoPhoto(){
        return oPhoto;
    }
}
