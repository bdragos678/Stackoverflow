package com.example.bdrag.stackoverflow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item>{

    public ItemAdapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Item item = getItem(position);

        ImageView profileImage = (ImageView)convertView.findViewById(R.id.profile_image);
        TextView displayNameUser = (TextView)convertView.findViewById(R.id.display_name_user);

        Picasso.with(getContext()).load(item.getProfileImage()).into(profileImage);
        displayNameUser.setText(item.getDisplayName());

        return convertView;
    }
}
