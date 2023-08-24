package com.krypt.bluecoin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krypt.bluecoin.Models.TransModel;
import com.krypt.bluecoin.R;
import com.krypt.bluecoin.User.UserModel;
import com.krypt.bluecoin.utils.SessionHandler;

import java.util.List;

public class AdaptersTransactions extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final String TAG = "Trans adapter";
    private Context ctx;
    private SessionHandler session;
    private UserModel user;
    private List<TransModel> items;

    public AdaptersTransactions(Context ctx, List<TransModel> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_trans_layout, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final TransModel o = items.get(position);


            view.txv_transdet.setText("#"+items.get(position)+ "You transacted "+o.getAmount()+" "+o.getCurrency()+" on "+o.getDate());
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_transdet;


        public OriginalViewHolder(View v) {
            super(v);


            txv_transdet = v.findViewById(R.id.trans_view);


        }
    }
}
