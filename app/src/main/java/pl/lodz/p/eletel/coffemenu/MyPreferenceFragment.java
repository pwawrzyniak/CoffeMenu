package pl.lodz.p.eletel.coffemenu;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by student_pl on 07.05.17.
 */

public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
