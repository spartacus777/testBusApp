package example.kizema.anton.testbusapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import example.kizema.anton.testbusapp.R;
import example.kizema.anton.testbusapp.model.BusModel;

public class BusRouteAdapter extends RecyclerView.Adapter<BusRouteAdapter.BusViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_DIVIDER = 2;


    private List<BusModel> busModels;

    private OnUserClickListener onUserClickListener;

    public interface OnUserClickListener{
        void onUserClicked(BusModel str);
    }

    public BusRouteAdapter(List<BusModel> busModels) {
        this.busModels = busModels;
        notifyDataSetChanged();
    }

    public void update(List<BusModel> busModels){
        this.busModels = busModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0){
            return TYPE_DIVIDER;
        }

        BusModel model = busModels.get(position);
        BusModel prevModel = busModels.get(position-1);

        String prevModelTime = getTime(prevModel.timestamp * 1000);
        String modelTime = getTime(model.timestamp * 1000);

        if (modelTime.equalsIgnoreCase(prevModelTime)){
            return TYPE_ITEM;
        }

        return TYPE_DIVIDER;
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId, tvDirection, tvDeparture;
        public ViewGroup parentItem;

        public BusViewHolder(View itemView) {
            super(itemView);

            parentItem = (ViewGroup) itemView.findViewById(R.id.parentItem);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvDirection = (TextView) itemView.findViewById(R.id.tvDirection);
            tvDeparture = (TextView) itemView.findViewById(R.id.tvDeparture);
        }
    }

    public static class BusDividerViewHolder extends BusViewHolder {

        public TextView tvTitleId, tvTitleDirection, tvTitleDeparture, tvTime;

        public BusDividerViewHolder(View itemView) {
            super(itemView);

            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvTitleId = (TextView) itemView.findViewById(R.id.tvTitleId);
            tvTitleDirection = (TextView) itemView.findViewById(R.id.tvTitleDirection);
            tvTitleDeparture = (TextView) itemView.findViewById(R.id.tvTitleDeparture);
        }
    }

    @Override
    public BusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_ITEM: {
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new BusViewHolder(parentView);
            }
            case TYPE_DIVIDER: {
                View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_and_divider, parent, false);
                return new BusDividerViewHolder(parentView);
            }
        }

        return null;
    }

    private String getTime(long timestamp){
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(timestamp);
        return format(cl.get(Calendar.DAY_OF_MONTH)) + ":" + format(cl.get(Calendar.MONTH) + 1) + ":" +
                cl.get(Calendar.YEAR);
    }

    private String getTimeHours(long timestamp){
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(timestamp);
        return format(cl.get(Calendar.HOUR_OF_DAY)) + ":" + format(cl.get(Calendar.MINUTE));
    }

    private String format(int c){
        if (c < 10){
            return "0"+c;
        }

        return ""+c;
    }

    @Override
    public void onBindViewHolder(final BusViewHolder holder, final int position) {

        BusModel model = busModels.get(position);
        holder.tvId.setText(model.lineId);
        holder.tvDeparture.setText(getTimeHours(model.timestamp * 1000));
        holder.tvDirection.setText(model.direction);

        holder.parentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserClickListener != null) {
                    onUserClickListener.onUserClicked(busModels.get(holder.getAdapterPosition()));
                }
            }
        });

        if (holder instanceof BusDividerViewHolder){
            ((BusDividerViewHolder) holder).tvTime.setText(getTime(model.timestamp * 1000));
        }
    }

    @Override
    public int getItemCount() {
        if (busModels == null){
            return 0;
        }

        return busModels.size();
    }

    public void clear(){
        busModels.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty(){
        if (busModels == null || busModels.size() == 0) {
            return true;
        }

        return false;
    }

    public void setOnUserClickListener(OnUserClickListener onUserClickListener){
        this.onUserClickListener = onUserClickListener;
    }

}
