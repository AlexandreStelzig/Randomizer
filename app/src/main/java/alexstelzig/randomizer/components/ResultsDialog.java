package alexstelzig.randomizer.components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import alexstelzig.randomizer.MainActivity;
import alexstelzig.randomizer.R;
import alexstelzig.randomizer.model.RandomListItem;

public abstract class ResultsDialog {

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private MainActivity mainActivity;

    private List<RandomListItem> results;

    public ResultsDialog(MainActivity mainActivity, List<RandomListItem> results) {
        this.mainActivity = mainActivity;
        this.results = results;

        builder = new AlertDialog.Builder(mainActivity);
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_listview_results, null);
        builder.setView(convertView);
        builder.setTitle("Results");
        ListView lv = (ListView) convertView.findViewById(R.id.listview);

        lv.setAdapter(new DeckCustomListAdapter());

        builder.setCancelable(false);

        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNeutralButton("Randomize", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                onNeutralButtonClicked();
            }
        });

        alertDialog = builder.create();

    }

    public abstract void onNeutralButtonClicked();

    public void show(){
        alertDialog.show();

    }

    public class DeckCustomListAdapter extends BaseAdapter {

        private LayoutInflater inflater = null;

        public DeckCustomListAdapter() {
            super();
            inflater = (LayoutInflater) mainActivity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Holder {
            TextView result;
            TextView name;
            TextView weight;
            TextView percentage;
            LinearLayout background;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Holder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.custom_listview_results_item, null);
                holder = new Holder();

                holder.result = (TextView) convertView.findViewById(R.id.result_textview);
                holder.name = (TextView) convertView.findViewById(R.id.name_text_view);
                holder.weight = (TextView) convertView.findViewById(R.id.weight_text_view);
                holder.percentage = (TextView) convertView.findViewById(R.id.weight_percentage_text_view);
                holder.background = (LinearLayout) convertView.findViewById(R.id.list_item_background);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            RandomListItem randomListItem = results.get(position);

            holder.result.setText(position + 1 + "");
            holder.name.setText(randomListItem.getName());



            if (randomListItem.isActive()){
                double weight = randomListItem.getRandomWeight();
                double percentage = ((((double) weight) / mainActivity.getTotalWeight()) * 100);
                percentage = Double.parseDouble(new DecimalFormat("##.##").format(percentage));

                holder.weight.setText(randomListItem.getRandomWeight() + " W");
                holder.percentage.setText(percentage + "%");

                holder.background.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.active));
            }else{
                holder.weight.setText("N/A");
                holder.percentage.setText("Inactive");
                holder.background.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.inactive));
            }

            // all

            return convertView;

        }


    }

}
