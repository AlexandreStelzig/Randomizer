package alexstelzig.randomizer;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import alexstelzig.randomizer.components.RandomItemDialog;
import alexstelzig.randomizer.model.RandomListItem;

/**
 * Created by alex on 2018-03-15.
 */

public class ListItemAdapter extends DragItemAdapter<Pair<Long, RandomListItem>, ListItemAdapter.ViewHolder> {

    private final static boolean DRAG_ON_LONG_PRESS = false;

    private MainActivity mainActivity;

    ListItemAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        setItemList(mainActivity.fetchItems());
    }

    public void updateAdapterData(ArrayList<Pair<Long, RandomListItem>> data) {
        setItemList(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.custom_list_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final RandomListItem item = mItemList.get(position).second;


        holder.mName.setText(item.getName());

        if (item.isActive()){
            double weight = item.getRandomWeight();
            double percentage = ((((double) weight) / mainActivity.getTotalWeight()) * 100);
            percentage = Double.parseDouble(new DecimalFormat("##.##").format(percentage));

            holder.mWeight.setText(weight + " W");
            holder.mWeightPercentage.setText(percentage + "%");
            holder.mBackground.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.active));
        }else{
            holder.mWeight.setText("N/A");
            holder.mWeightPercentage.setText("Inactive");
            holder.mBackground.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.inactive));
        }

        holder.mBackground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new RandomItemDialog(mainActivity, "Create new Item", true, item.getName(),
                        item.getRandomWeight(), item.isActive()) {
                    @Override
                    public void positiveButtonClicked(String itemName, int weight, boolean isActive) {
                        mainActivity.updateItem(item.getRandomListItemId(), itemName, weight, isActive);
                    }

                    @Override
                    public void deleteButtonClicked() {
                        mainActivity.deleteListItem(item.getRandomListItemId());
                    }
                }.show();
                return true;
            }
        });

        if(position == mItemList.size() - 1){
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.mBackground.getLayoutParams();
            params.setMargins(0, 0, 3, 250);
            holder.mBackground.setLayoutParams(params);
        }else{
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.mBackground.getLayoutParams();
            params.setMargins(0, 0, 3, 3);
            holder.mBackground.setLayoutParams(params);
        }

    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).first;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mName;
        TextView mWeight;
        TextView mWeightPercentage;
        LinearLayout mBackground;

        ViewHolder(final View itemView) {
            super(itemView, R.id.drag_layout, DRAG_ON_LONG_PRESS);
            mName = (TextView) itemView.findViewById(R.id.name_text_view);
            mWeight = (TextView) itemView.findViewById(R.id.weight_text_view);
            mWeightPercentage = (TextView) itemView.findViewById(R.id.weight_percentage_text_view);
            mBackground = (LinearLayout) itemView.findViewById(R.id.list_item_background);
        }
    }
}
