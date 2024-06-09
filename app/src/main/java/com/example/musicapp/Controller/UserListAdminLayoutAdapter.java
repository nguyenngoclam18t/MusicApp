package com.example.musicapp.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.OnItemLongClickListenerUser;
import com.example.musicapp.Model.UserModel;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdminLayoutAdapter extends RecyclerView.Adapter<UserListAdminLayoutAdapter.ViewHolder>{
    ArrayList<UserModel>arr=new ArrayList<>();
    private OnItemLongClickListenerUser onItemLongClickListener;

    public UserListAdminLayoutAdapter(ArrayList<UserModel> arr, OnItemLongClickListenerUser listener) {
        this.arr = arr;
        this.onItemLongClickListener = listener;
    }
    @NonNull
    @Override
    public UserListAdminLayoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_userlist,parent,false);
        return new UserListAdminLayoutAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdminLayoutAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UserModel userModel=arr.get(position);
        Picasso.get()
                .load(userModel.getAvatarUrl())
                .into(holder.img);
        holder.title.setText(userModel.getFullName());
        holder.desc.setText(userModel.getEmail());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có muốn xóa mục này không?");

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xóa mục đó khỏi danh sách dữ liệu
                        removeItem(position);
                        onItemLongClickListener.onItemLongClick(position);

                    }
                });


                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng AlertDialog
                        dialog.dismiss();
                    }
                });

                // Hiển thị AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }


        });

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
    public void removeItem(int position) {
        arr.remove(position);
        notifyItemRemoved(position);
    }
    public class ViewHolder  extends RecyclerView.ViewHolder {
        TextView title,desc;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.imgUserAdminLayout);
            title=(TextView) itemView.findViewById(R.id.titleUserAdminLayout);
            desc=(TextView)itemView.findViewById(R.id.descUserAdminLayout);
        }
    }
}
