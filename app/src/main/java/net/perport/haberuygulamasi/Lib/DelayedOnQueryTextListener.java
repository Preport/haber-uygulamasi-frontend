package net.perport.haberuygulamasi.Lib;

import android.os.Handler;
import android.widget.SearchView;

public abstract class DelayedOnQueryTextListener implements SearchView.OnQueryTextListener {

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public boolean onQueryTextSubmit(String s) {
        handler.removeCallbacks(runnable);
        onDelayedQueryTextChange(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        handler.removeCallbacks(runnable);
        runnable = () -> onDelayedQueryTextChange(s);
        handler.postDelayed(runnable, 200);
        return true;
    }

    public abstract void onDelayedQueryTextChange(String query);
}
