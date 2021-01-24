package com.cengcelil.phonenumbersappetstur.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.cengcelil.phonenumbersappetstur.Activities.AddOrEditPersonActivity;
import com.cengcelil.phonenumbersappetstur.Helper.RecyclerClickListener;
import com.cengcelil.phonenumbersappetstur.Models.PersonModel;
import com.cengcelil.phonenumbersappetstur.R;
import com.cengcelil.phonenumbersappetstur.Utils;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyHolder> {
    public ArrayList<PersonModel> getPersonModels() {
        return personModels;
    }

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_NOTE = 1;

    public void setPersonModels(ArrayList<PersonModel> personModels) {
        this.personModels = personModels;

    }

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ArrayList<PersonModel> personModels;
    private Context context;
    private ProgressBar progressBar;
    private RecyclerClickListener listener;

    public PersonAdapter(ArrayList<PersonModel> personModels, ProgressBar progressBar, RecyclerClickListener listener) {
        this.personModels = personModels;
        this.progressBar = progressBar;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;
        if (viewType == TYPE_NORMAL)
            view = LayoutInflater.from(context).inflate(R.layout.single_person_item, parent, false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.single_person_item_with_note, parent, false);
        return new PersonAdapter.MyHolder(view,listener);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeLayout, String.valueOf(personModels.get(position).getName()));
        viewBinderHelper.closeLayout(String.valueOf(personModels.get(position).getName()));
        final PersonModel currentModel = personModels.get(position);
        String name_surname_merge = currentModel.getName() + " " + currentModel.getSurname();
        holder.tvPersonNameSurname.setText(name_surname_merge);
        holder.tvPersonBirthDate.setText(Utils.sdf.format(new Date((currentModel.getBirth_date()))));
        holder.tvPersonEmail.setText(currentModel.getEmail());
        if (getItemViewType(position) == TYPE_NOTE){
            holder.tvPersonNote.setText(currentModel.getNote());
        }

        String number_merge = currentModel.getCountry_code() + " " + currentModel.getPhone_number();
        holder.tvPersonNumber.setText(number_merge);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showProgressBar(progressBar);
                Intent i = new Intent(context, AddOrEditPersonActivity.class);
                i.putExtra("person",currentModel);
                context.startActivity(i);
            }
        });
        holder.btRemove.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;

    }


    @Override
    public int getItemCount() {
        return personModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (personModels.get(position).getNote().trim().length() == 0)
            return TYPE_NORMAL;
        else
            return TYPE_NOTE;
    }

    public static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvPersonNameSurname, tvPersonBirthDate, tvPersonEmail, tvPersonNumber, tvPersonNote, tvRemove;
        CardView cardView;
        RelativeLayout person_note_layout;
        SwipeRevealLayout swipeLayout;
        RecyclerClickListener listener;
        ConstraintLayout btRemove;

        public MyHolder(@NonNull View itemView, RecyclerClickListener listener) {
            super(itemView);
            this.listener = listener;
            tvPersonNameSurname = itemView.findViewById(R.id.person_name_surname_textview);
            tvPersonBirthDate = itemView.findViewById(R.id.person_birth_date_textview);
            tvPersonEmail = itemView.findViewById(R.id.person_email_textview);
            tvPersonNumber = itemView.findViewById(R.id.person_phone_number_textview);
            tvPersonNote = itemView.findViewById(R.id.person_note_textview);
            cardView = itemView.findViewById(R.id.cardView);
            person_note_layout = itemView.findViewById(R.id.person_note_layout);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            tvRemove = itemView.findViewById(R.id.tv_remove);
            btRemove = itemView.findViewById(R.id.layout_remove_person);
            btRemove.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

}
