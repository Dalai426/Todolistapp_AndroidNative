package com.example.todoapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItems extends ItemTouchHelper.SimpleCallback {
    RecyclerAdapter adapter;


    public RecyclerItems(RecyclerAdapter adapter) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter=adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position=viewHolder.getAdapterPosition();
        if(direction==ItemTouchHelper.LEFT){
            AlertDialog.Builder builder=new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Устгах");
            builder.setMessage("Устгахдаа итгэлтэй байна уу ??");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.deleteItem(position);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });


            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();

        }else{
            adapter.editItem(position);

        }
        adapter.notifyItemChanged(viewHolder.getAdapterPosition());

    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX,
                           float dY, int actionState, boolean isActive){

        super.onChildDraw(c, recyclerView,viewHolder, dX, dY, actionState, isActive);
        Drawable icon;
        ColorDrawable backr;
        View item=viewHolder.itemView;

        int offset=20;
        if(dX>0){
            icon= ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_mode_edit_24);
            backr=new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.edit));
        }else{
            icon= ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_delete_forever_24);

            backr=new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.delete));
        }
        int margin=(item.getHeight()-icon.getIntrinsicHeight())/2;
        int iconTop=item.getTop()+margin;
        int bottom=item.getBottom()-margin;


        if (dX > 0) { // Swiping to the right
            int iconLeft = item.getLeft() + margin;
            int iconRight = item.getLeft() + margin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, bottom);
            backr.setBounds(item.getLeft(), item.getTop(),
                    item.getRight(), item.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = item.getRight() - margin - icon.getIntrinsicWidth();
            int iconRight = item.getRight() - margin;
            icon.setBounds(iconLeft, iconTop, iconRight, bottom);

            backr.setBounds(item.getLeft(),
                    item.getTop(), item.getRight(), item.getBottom());
        } else { // view is unSwiped
            backr.setBounds(0, 0, 0, 0);
        }

        backr.draw(c);
        icon.draw(c);


    }
}
