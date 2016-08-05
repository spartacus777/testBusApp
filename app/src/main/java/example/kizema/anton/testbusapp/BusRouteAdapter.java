package example.kizema.anton.testbusapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BusRouteAdapter extends RecyclerView.Adapter<BusRouteAdapter.UserViewHolder> {

    private List<String> itemText;

    private OnUserClickListener onUserClickListener;

    public interface OnUserClickListener{
        void onUserClicked(String str);
    }

    public BusRouteAdapter(List<String> users) {
        this.itemText = users;
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

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView text;

        public UserViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView;
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView image = new TextView(parent.getContext());

        image.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));

        return new UserViewHolder(image);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {

        holder.text.setText(itemText.get(position));

        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserClickListener != null) {
                    onUserClickListener.onUserClicked(itemText.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (itemText == null){
            return 0;
        }

        return itemText.size();
    }

    public void clear(){
        itemText.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty(){
        if (itemText == null || itemText.size() == 0) {
            return true;
        }

        return false;
    }

    public void setOnUserClickListener(OnUserClickListener onUserClickListener){
        this.onUserClickListener = onUserClickListener;
    }

}
