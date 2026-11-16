package cn.edu.sicnu.cardgame;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    private static final String TAB = "selectedTab";
    private int selectedTab = R.id.tab_profile;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tab_profile).setOnClickListener(v -> select(v.getId()));
        findViewById(R.id.tab_pair).setOnClickListener(v -> select(v.getId()));
        findViewById(R.id.tab_triple).setOnClickListener(v -> select(v.getId()));
        if (state == null) {
            select(selectedTab);
        } else {
            selectedTab = state.getInt(TAB, R.id.tab_profile);
            markTab();
        }
    }

    private void select(int tab) {
        if (tab == selectedTab && getFragmentManager().findFragmentById(R.id.fragment_container) != null) {
            return;
        }
        selectedTab = tab;
        Fragment page;
        if (tab == R.id.tab_pair) {
            page = new TwoCardGameFragment();
        } else if (tab == R.id.tab_triple) {
            page = new ThreeCardGameFragment();
        } else {
            page = new ProfileFragment();
        }
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, page).commit();
        markTab();
    }

    private void markTab() {
        int[] tabs = {R.id.tab_profile, R.id.tab_pair, R.id.tab_triple};
        for (int tab : tabs) {
            View button = findViewById(tab);
            button.setSelected(tab == selectedTab);
            button.setEnabled(tab != selectedTab);
        }
    }

    @Override protected void onSaveInstanceState(Bundle state) {
        state.putInt(TAB, selectedTab);
        super.onSaveInstanceState(state);
    }

    @Override public boolean onCreateOptionsMenu(android.view.Menu menu) {
        menu.add(0,1,0,R.string.about).setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }
    @Override public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if(item.getItemId()==1){new android.app.AlertDialog.Builder(this).setTitle(R.string.about).setMessage(R.string.about_message).setPositiveButton(R.string.ok,null).show();return true;}
        return super.onOptionsItemSelected(item);
    }
}