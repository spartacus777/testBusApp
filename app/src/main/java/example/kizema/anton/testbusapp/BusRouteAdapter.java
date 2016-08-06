package example.kizema.anton.testbusapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import example.kizema.anton.testbusapp.model.BusModel;

public class BusRouteAdapter extends RecyclerView.Adapter<BusRouteAdapter.BusViewHolder> {

    private List<BusModel> busModels;

    private OnUserClickListener onUserClickListener;

    public interface OnUserClickListener{
        void onUserClicked(BusModel str);
    }

    public BusRouteAdapter(List<BusModel> busModels) {
        this.busModels = busModels;
    }

    public void update(){
        notifyDataSetChanged();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position % 3 == 1){
//            return TYPE_PHOTO_CENTRAL;
//        } else {
//            return TYPE_PHOTO_SIDE;
//        }
//    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId, tvDirection, tvDeparture;

        public BusViewHolder(View itemView) {
            super(itemView);

            tvId = (TextView) itemView.findViewById(R.id.tvId);
            tvDirection = (TextView) itemView.findViewById(R.id.tvDirection);
            tvDeparture = (TextView) itemView.findViewById(R.id.tvDeparture);
        }
    }

    @Override
    public BusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new BusViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(final BusViewHolder holder, final int position) {

        BusModel model = busModels.get(position);
        holder.tvId.setText(model.id);
        holder.tvDeparture.setText(""+model.timestamp);
        holder.tvDirection.setText(model.direction);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserClickListener != null) {
                    onUserClickListener.onUserClicked(busModels.get(holder.getAdapterPosition()));
                }
            }
        });
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
