package com.example.restaurantsmkjaya;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;

import org.json.JSONObject;

import java.util.List;

import okhttp3.Response;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.ViewHolder> {
    Context c;
    List<PesananModel> list;

    public PesananAdapter(Context c, List<PesananModel> list) {
        this.c = c;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(c).inflate(R.layout.rv_layout, viewGroup, false), c);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvTable;
        ImageView ivMenu;
        Context c;
        CardView lyOrder;

        public ViewHolder(@NonNull View itemView, Context c) {
            super(itemView);
            this.c = c;
            tvName = itemView.findViewById(R.id.tv_title);
            tvQty = itemView.findViewById(R.id.tv_qty);
            tvTable = itemView.findViewById(R.id.tv_table);
            ivMenu = itemView.findViewById(R.id.iv_menu);
            lyOrder = itemView.findViewById(R.id.ly_order);
        }

        public void bind(final PesananModel m) {
            tvName.setText(m.getName());
            tvQty.setText("Qty : " + m.getQty());
            tvTable.setText("Table : " + m.getTable());
            byte[] b = m.getPhotoUrl();
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            ivMenu.setImageBitmap(bitmap);

            lyOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                    alertDialog.setMessage("Apakah pesanan ini sudah diantar?");
                    alertDialog.setPositiveButton("Sudah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            AndroidNetworking.post(EndPoint.url + "Delivered").addQueryParameter("idDetail", m.getDetailId()).build().getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                                @Override
                                public void onResponse(Response okHttpResponse, JSONObject response) {

                                    try {
                                        if (okHttpResponse.code() == 200) {
                                            MainActivity.getPesanan();
                                        }
                                        Toast.makeText(c, response.getString("Message"), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                        }
                    });
                    alertDialog.setNegativeButton("Belum", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }

            });

        }
    }
}
