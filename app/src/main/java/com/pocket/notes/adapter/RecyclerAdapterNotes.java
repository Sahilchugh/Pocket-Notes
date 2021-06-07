package com.pocket.notes.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.pocket.notes.R;
import com.pocket.notes.database.NotesDataDB;
import com.pocket.notes.database.NotesDataDao;
import com.pocket.notes.databaseEntity.NotesData;
import com.pocket.notes.homeStructure.MainFragment;
import com.pocket.notes.models.NotesModel;
import com.pocket.notes.networkingStructure.NetworkingCalls;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterNotes extends RecyclerView.Adapter<RecyclerAdapterNotes.ViewHolder> implements Filterable {

    Context context;
    ArrayList<NotesData>notesModelList = new ArrayList<>();
    private ArrayList<NotesData> dataFull;
    Activity activity;

    public RecyclerAdapterNotes(Context context, ArrayList<NotesData> notesModelList, Activity activity) {
        this.context = context;
        this.notesModelList = notesModelList;
        dataFull = new ArrayList<>(notesModelList);
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list, parent, false);
        return new RecyclerAdapterNotes.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(notesModelList.get(position).getTitle());
        holder.description.setText(notesModelList.get(position).getDescription());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Warning!!")
                        .setMessage("Do you really want to delete the note")
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new NetworkingCalls(context).deleteNotes(notesModelList.get(position).getId());
                                NotesDataDao notesDataDao = NotesDataDB.getInstance(context).notesDataDao();
                                notesDataDao.removeNotesById(notesModelList.get(position).getId());
                                notesModelList.remove(notesModelList.get(position));
                                MainFragment.notesViewModel.getNotesLiveData().setValue(notesModelList);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new NetworkingCalls(context).updateNotes(notesModelList.get(position).getTitle(),notesModelList.get(position).getDescription());
                /*NotesDataDao notesDataDao = NotesDataDB.getInstance(context).notesDataDao();
                notesDataDao.updateNote(notesModelList.get(position).getTitle(),notesModelList.get(position).getDescription(),notesModelList.get(position).getId());
                MainFragment.notesViewModel.getNotesLiveData().setValue(notesModelList);*/

                Bundle bundle = new Bundle();
                bundle.putString("title",notesModelList.get(position).getTitle());
                bundle.putString("description",notesModelList.get(position).getDescription());
                bundle.putInt("id",notesModelList.get(position).getId());
                Log.e("id needs to sent", ""+notesModelList.get(position).getId());
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_editNotesFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesModelList.size();
    }

    @Override
    public Filter getFilter() {
        return noteFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,description;
        ImageView delete,edit;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }


    private Filter noteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<NotesData> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length()==0)
            {
                filteredList.addAll(dataFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (NotesData item : dataFull)
                {
                    if (item.getTitle().toLowerCase().startsWith(filterPattern))
                    {
                        filteredList.add(item);

                    }
                    else if (item.getDescription().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                    else {

                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notesModelList.clear();
            notesModelList.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };
}
