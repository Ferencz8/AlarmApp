package countingsheep.alarm.ui.shared;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class EmptyAdapterDataObserver extends RecyclerView.AdapterDataObserver {

    private RecyclerView.Adapter<?> adapter;
    private View emptyView;
    private RecyclerView recyclerView;

    public EmptyAdapterDataObserver(RecyclerView.Adapter<?> adapter, View emptyView, RecyclerView recyclerView) {
        this.adapter = adapter;
        this.emptyView = emptyView;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onChanged() {
        if(adapter != null && emptyView != null) {
            if(adapter.getItemCount() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}
