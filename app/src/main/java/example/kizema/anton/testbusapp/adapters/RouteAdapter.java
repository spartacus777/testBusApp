package example.kizema.anton.testbusapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import example.kizema.anton.testbusapp.R;
import example.kizema.anton.testbusapp.model.RouteModel;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<RouteModel> routes;

    public RouteAdapter(List<RouteModel> busModels) {
        this.routes = busModels;
    }

    public void update(List<RouteModel> busModels){
        this.routes = busModels;
        notifyDataSetChanged();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvAddress;

        public RouteViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
        }
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_item, parent, false);

        return new RouteViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(final RouteViewHolder holder, final int position) {
        RouteModel model = routes.get(position);
        holder.tvName.setText(model.name);
        holder.tvAddress.setText(model.address);
    }

    @Override
    public int getItemCount() {
        if (routes == null){
            return 0;
        }

        return routes.size();
    }

    public void clear(){
        routes.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty(){
        if (routes == null || routes.size() == 0) {
            return true;
        }

        return false;
    }

}
