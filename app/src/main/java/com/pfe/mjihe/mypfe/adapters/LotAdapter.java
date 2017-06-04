package com.pfe.mjihe.mypfe.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pfe.mjihe.mypfe.R;
import com.pfe.mjihe.mypfe.models.Lot;

import java.util.List;

/**
 * Created by Mjihe on 02/06/2017.
 */

public class LotAdapter extends RecyclerView.Adapter<LotAdapter.MyViewHolder> {
    private List<Lot> loList;

    public LotAdapter(List<Lot> loList) {
        this.loList = loList;
    }

    public LotAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_lot, parent, false);
        return new LotAdapter.MyViewHolder(itemView);
    }


    public int getItemCount() {
        return loList.size();
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        Lot lot = loList.get(position);
        holder.numcin.setText(lot.getCin().toString());
        holder.numlot.setText(lot.getNumlot().toString());
        holder.tax.setText(lot.getTaxe().toString());
        if (lot.getPayment()) {
            holder.lotView.setBackgroundColor(Color.GREEN);
        } else {
            holder.lotView.setBackgroundColor(Color.RED);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView numlot, numcin, tax;
        private FrameLayout lotView;

        public MyViewHolder(View View) {
            super(View);
            lotView = (FrameLayout) View.findViewById(R.id.viewLot);
            numcin = (TextView) View.findViewById(R.id.numCin);
            numlot = (TextView) View.findViewById(R.id.numlot);
            tax = (TextView) View.findViewById(R.id.ataxe);

        }
    }
}
