package com.djordjeratkovic.checked.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.ui.home.ui.home.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwipeToDeleteAndEditCallback extends ItemTouchHelper.Callback {
    //TODO: moras pasujes listu producta i da vidis koji od 4 drawable backgrounds da stavis onlu, first, middle i last
    // takodje moraces da izracunas da oduzme naslov ako bude bio vidljiv

    private Context context;
    private ItemTouchHelperDelete delete;
    private ItemTouchHelperEdit edit;

    private Paint mClearPaint;
    private Drawable deleteDrawable;
    private Drawable editDrawable;
    private Drawable deleteDrawableBackground;
    private Drawable editDrawableBackground;
    private int deleteIntrinsicWidth;
    private int deleteIntrinsicHeight;
    private int editIntrinsicWidth;
    private int editIntrinsicHeight;

    private List<Product> products = new ArrayList<>();

    public SwipeToDeleteAndEditCallback(Context context, ItemTouchHelperDelete delete, ItemTouchHelperEdit edit, List<Product> productList) {
        this.context = context;
        this.delete = delete;
        this.edit = edit;
        this.products = productList;

        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        deleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete);
        deleteDrawable.setTint(ContextCompat.getColor(context, R.color.cream));
        deleteIntrinsicWidth = deleteDrawable.getIntrinsicWidth();
        deleteIntrinsicHeight = deleteDrawable.getIntrinsicHeight();
//        deleteDrawableBackground = ContextCompat.getDrawable(context, R.drawable.delete_background);


        editDrawable = ContextCompat.getDrawable(context, R.drawable.ic_edit);
        editDrawable.setTint(ContextCompat.getColor(context, R.color.white));
        editIntrinsicWidth = editDrawable.getIntrinsicWidth();
        editIntrinsicHeight = editDrawable.getIntrinsicHeight();
//        editDrawableBackground = ContextCompat.getDrawable(context, R.drawable.edit_background);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }


    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof HomeAdapter.ViewHolder) {
//            ConstraintLayout itemView = viewHolder.itemView.findViewById(R.id.cl);
//            recyclerView.getChildAdapterPosition(viewHolder.itemView);
            //TODO: icons left to set and radius for background

            View item = viewHolder.itemView.findViewById(R.id.cl);
            View itemView = viewHolder.itemView;
            int itemHeight = item.getHeight();

            boolean isCancelled = dX == 0 && !isCurrentlyActive;

            if (isCancelled) {
                clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                return;
            }
            if (dX < 0) {

                switch (new BackgroundAndCategory(viewHolder.getAdapterPosition(), products, context).getPosition()) {
                    case 1:
                        deleteDrawableBackground = ContextCompat.getDrawable(context, R.drawable.delete_background_first);
                        break;
                    case 2:
                        deleteDrawableBackground = ContextCompat.getDrawable(context, R.drawable.delete_background_middle);
                        break;
                    case 3:
                        deleteDrawableBackground = ContextCompat.getDrawable(context, R.drawable.delete_background_last);
                        break;
                    case 4:
                        deleteDrawableBackground = ContextCompat.getDrawable(context, R.drawable.delete_background_only);
                        break;
                }

                deleteDrawableBackground.setBounds(itemView.getRight() + (int) dX - 500, itemView.getTop() + item.getTop(), itemView.getRight(), itemView.getBottom());
                deleteDrawableBackground.draw(c);


                int deleteIconTop = itemView.getTop() + item.getTop() + (itemHeight - deleteIntrinsicHeight) / 2;
//                int deleteIconMargin = (itemHeight - deleteIntrinsicHeight) / 2;
                int deleteIconMargin = 100;
                int deleteIconLeft = itemView.getRight() - deleteIconMargin - deleteIntrinsicWidth;
                int deleteIconRight = itemView.getRight() - deleteIconMargin;
                int deleteIconBottom = deleteIconTop + deleteIntrinsicHeight;


                deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                deleteDrawable.draw(c);
            }
            if (dX > 0) {

                switch (new BackgroundAndCategory(viewHolder.getAdapterPosition(), products, context).getPosition()) {
                    case 1:
                        editDrawableBackground = ContextCompat.getDrawable(context, R.drawable.edit_background_first);
                        break;
                    case 2:
                        editDrawableBackground = ContextCompat.getDrawable(context, R.drawable.edit_background_middle);
                        break;
                    case 3:
                        editDrawableBackground = ContextCompat.getDrawable(context, R.drawable.edit_background_last);
                        break;
                    case 4:
                        editDrawableBackground = ContextCompat.getDrawable(context, R.drawable.edit_background_only);
                        break;
                }


                editDrawableBackground.setBounds(itemView.getLeft(), itemView.getTop() + item.getTop(), itemView.getLeft() + (int) dX + 500, itemView.getBottom());
                editDrawableBackground.draw(c);

                int editIconTop = itemView.getTop() + item.getTop() + (itemHeight - editIntrinsicHeight) / 2;
//                int editIconMargin = (itemHeight - editIntrinsicHeight) / 2;
                int editIconMargin = 100;
                int editIconLeft = itemView.getLeft() + editIconMargin;
                int editIconRight = itemView.getLeft() + editIconMargin + editIntrinsicWidth;
                int editIconBottom = editIconTop + editIntrinsicHeight;

                editDrawable.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom);
                editDrawable.draw(c);
            }

        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        switch (direction) {
            case ItemTouchHelper.START:
                delete.onItemDelete(viewHolder.getAdapterPosition());
                break;
            case ItemTouchHelper.END:
                edit.onItemEdit(viewHolder.getAdapterPosition());
                break;
        }
    }
}
