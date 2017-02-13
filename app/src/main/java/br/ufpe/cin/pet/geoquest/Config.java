package br.ufpe.cin.pet.geoquest;

import com.facebook.AccessToken;

/**
 * Created by mras on 12/06/16.
 */
public class Config {

    public static String getKey() {
        return AccessToken.getCurrentAccessToken().getToken();
    }
}
