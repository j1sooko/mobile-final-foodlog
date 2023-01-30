package ddwu.mobile.finalproject.ma01_20190936;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;
    int layout;


    public MyCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
//        viewHolder 생성 직후 findViewById()를 수행하여 view를 바로 저장하는 방법도 가능
        ViewHolder holder = new ViewHolder();
        view.setTag(holder); // viewHolder 저장
        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder.tvFoodName == null) {
            holder.tvFoodTime = view.findViewById(R.id.tvFoodTime);
            holder.tvFoodName = view.findViewById(R.id.tvFoodName);
            holder.tvPrice = view.findViewById(R.id.tvPrice);
            holder.tvCal = view.findViewById(R.id.tvCal);
        }
        holder.tvFoodName.setText(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_FOODNAME))); //0번째항목은 id
        holder.tvFoodTime.setText(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_FOODTIME)));
        holder.tvPrice.setText(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_PRICE)));
        holder.tvCal.setText(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_CALORIE)));

    }

    //    viewHolder 생성
    static class ViewHolder {
        //명시적으로 멤버변수 null로 초기화, 처음 만들어질 때 null이어야 bindView에서 if문에서 걸림
        TextView tvFoodTime;
        TextView tvFoodName;
        TextView tvPrice;
        TextView tvCal;

        public ViewHolder() {
            tvFoodTime = null;
            tvFoodName = null;
            tvPrice = null;
            tvCal = null;
        }
    }
}

