package codepathandroid.todotracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by s0195620 on 2/18/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener{

    private List<String> dataSet;
    private RecycleViewItemCLickListener recycleViewItemCLickListener;

    public RecyclerViewAdapter(List<String> dataSet, Context context) {
        this.dataSet = dataSet;
        recycleViewItemCLickListener = (RecycleViewItemCLickListener) context;
    }

    public interface RecycleViewItemCLickListener{
        void onItemClicked(View v);
        void onItemLongClicked(View v);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String listItem = dataSet.get(position);
        holder.textView.setText(listItem);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public void taskDeleted(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(String item, int position){
        dataSet.remove(position);
        dataSet.add(position, item);
    }
    public void addItem(String listItem) {
        dataSet.add(listItem);
        notifyItemInserted(dataSet.size() - 1);
    }

    public Collection<String> getAllItems() {
        LinkedHashSet<String> allItems = new LinkedHashSet<String>(dataSet);
        return allItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_text);
        }
    }

    @Override
    public void onClick(final View v) {
        recycleViewItemCLickListener.onItemClicked(v);
    }

    @Override
    public boolean onLongClick(View v){
        recycleViewItemCLickListener.onItemLongClicked(v);
        return true ;
    }
}
