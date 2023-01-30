package ddwu.mobile.finalproject.ma01_20190936;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MonthlyDataAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<MonthlyFood> monthlyFoods;
    private LayoutInflater layoutInflater;

    public MonthlyDataAdapter(Context context, int layout, ArrayList<MonthlyFood> monthlyFoods) {
        this.context = context;
        this.layout = layout;
        this.monthlyFoods = monthlyFoods;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return monthlyFoods.size();
    }

    @Override
    public Object getItem(int position) {
        return monthlyFoods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvYearMonth = convertView.findViewById(R.id.tvYearMonth);
            viewHolder.tvTotalMoney = convertView.findViewById(R.id.tvPrice);
            viewHolder.tvTotalCal = convertView.findViewById(R.id.tvCal);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("monthlyDataAdapter", "monthlyDataAdapter: " + monthlyFoods.get(position).getYearMonth());
        viewHolder.tvYearMonth.setText(monthlyFoods.get(position).getYearMonth());
        viewHolder.tvTotalMoney.setText(monthlyFoods.get(position).getTotalMoney());
        viewHolder.tvTotalCal.setText(monthlyFoods.get(position).getTotalCal());



        return convertView;
    }

    static class ViewHolder {
        TextView tvYearMonth;
        TextView tvTotalMoney;
        TextView tvTotalCal;

    }
}


