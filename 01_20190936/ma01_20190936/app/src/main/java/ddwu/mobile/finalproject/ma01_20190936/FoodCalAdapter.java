package ddwu.mobile.finalproject.ma01_20190936;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodCalAdapter extends BaseAdapter  {

    public static final String TAG = "FoodCalAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<FoodCodeDTO> codelist;
    private NetworkManager networkManager = null;


    public FoodCalAdapter(Context context, int resource, ArrayList<FoodCodeDTO> list) {
        this.context = context;
        this.layout = resource;
        this.codelist = list;
        networkManager = new NetworkManager(context);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return codelist.size();
    }


    @Override
    public FoodCodeDTO getItem(int position) {
        return codelist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return codelist.get(position).get_id();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvSearchFoodName = view.findViewById(R.id.tvSearchFoodName);
            viewHolder.tvFoodCode = view.findViewById(R.id.tvFoodCode);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        FoodCodeDTO codeDTO = codelist.get(position);

        viewHolder.tvSearchFoodName.setText(codeDTO.getFoodName());
        viewHolder.tvFoodCode.setText(codeDTO.getFoodCode());

        return view;
    }


    public void setList(ArrayList<FoodCodeDTO> list) {
        this.codelist = list;
        notifyDataSetChanged();
    }

    //    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView tvSearchFoodName = null;
        public TextView tvFoodCode = null;
    }


}
