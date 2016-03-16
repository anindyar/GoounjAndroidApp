package com.bvocal.goounj.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.pojo.ContactItem;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

/**
 * Created by nandagopal on 25/10/15.
 */
public class ContactGridviewAdapter extends RecyclerView.Adapter<ContactGridviewAdapter.ContactViewHolder> {

    Context mContext;
    List<ContactItem> itemList;
    AdapterView.OnItemClickListener mOnItemClickListener;
    InputStream is;
    int[] user_image;
    Random random = new Random();

    public ContactGridviewAdapter(Context context, List<ContactItem> itemList) {
        super();
        this.mContext = context;
        this.itemList = itemList;
        user_image = new int[]{R.drawable.ic_usericon_blue, R.drawable.ic_usericon_green, R.drawable.ic_usericon_olive,
                R.drawable.ic_usericon_pink, R.drawable.ic_usericon_orange,};
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_contact_gridview, parent, false);
        return new ContactViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        final ContactItem item = itemList.get(position);
        Uri uri = Uri.parse("android.resource://" + mContext.getPackageName() + "/drawable/menu_profile");
        holder.mContactName.setText("" + item.mcontactName);
//        holder.mcontactNumber.setText("" + item.mContactNumber);
        holder.mCheckbox.setChecked(item.mChkSelected);
        holder.mCheckbox.setTag(itemList.get(position));

        if (item.mContactImage != null) {
            try {
                is = new FileInputStream(item.mContactImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bitmap bmImg = BitmapFactory.decodeStream(is);
            BitmapDrawable background = new BitmapDrawable(bmImg);
            holder.mImageCheck.setBackgroundDrawable(background);
        } else {
            holder.mImageCheck.setBackgroundDrawable(mContext.getResources().getDrawable(user_image[random.nextInt(4)]));
        }


        holder.mCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ContactItem contact = (ContactItem) cb.getTag();

                contact.mChkSelected = cb.isChecked();
//                stList.get(pos).setSelected(cb.isChecked());
                itemList.get(position).mChkSelected = cb.isChecked();
            }
        });

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(ContactViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public void selectAllList() {
        for (int i = 0; i < itemList.size(); i++) {
            itemList.get(i).mChkSelected = true;
        }
        notifyDataSetChanged();
    }

    public void deSelectAllList() {
        for (int i = 0; i < itemList.size(); i++) {
            itemList.get(i).mChkSelected = false;
        }
        notifyDataSetChanged();
    }

    // method to access in activity after updating selection
    public List<ContactItem> getStudentist() {
        return itemList;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mContactName, mcontactNumber;
        ImageView mContactimage;
        LinearLayout mImageCheck;
        ContactGridviewAdapter mAdapter;
        CheckBox mCheckbox;

        public ContactViewHolder(View itemView, ContactGridviewAdapter mAdapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mAdapter = mAdapter;

            mContactName = (TextView) itemView.findViewById(R.id.contactName);
            mcontactNumber = (TextView) itemView.findViewById(R.id.contactNumber);
//            mContactimage = (ImageView) itemView.findViewById(R.id.contactPicture);
            mImageCheck = (LinearLayout) itemView.findViewById(R.id.layout_image);
            mCheckbox = (CheckBox) itemView.findViewById(R.id.checkboxContact);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}
