package com.twishe.mymovies.adapters;

import com.twishe.mymovies.R;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twishe.mymovies.data.Movie;
import com.twishe.mymovies.utilities.AppData;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   final private String TAG = MovieAdapter.class.getSimpleName();
    private Context myContext;
    private List<Movie> mList;
    private final MovieAdapterOnClickListener mClickHandler;
    private static final int FOOTER_VIEW = 1;

    public MovieAdapter(Context context,MovieAdapterOnClickListener listener) {

        this.myContext = context;
        this.mClickHandler = listener;
    }

    public interface MovieAdapterOnClickListener{
        void onMovieClicked(Movie movie);
    }
    public void setItems(List<Movie> listData){
        this.mList = listData;
        this.notifyDataSetChanged();
    }

    public class NormalViewHolder extends ViewHolder {
        NormalViewHolder(View itemView) {
            super(itemView);

        }
    }

    public class FooterViewHolder extends ViewHolder {
        ProgressBar progressBar;
        FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);



        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        int layoutForListItem = R.layout.list_item_movie;
        int layoutForFooter = R.layout.footer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        if (viewType == FOOTER_VIEW) {
            view= LayoutInflater.from(parent.getContext()).inflate(layoutForFooter, parent, false);
            return new FooterViewHolder(view);
        }


        boolean shoutAttachToParentImmediately = false;
        view = inflater.inflate(layoutForListItem, parent, shoutAttachToParentImmediately);
        return new NormalViewHolder(view);



    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
              try {
            if (holder1 instanceof NormalViewHolder) {
                final NormalViewHolder holder = (NormalViewHolder) holder1;

                final Movie movie = mList.get(position);

                if (movie != null) {
                    Picasso.with(myContext).load(AppData.POSTER_IMAGE_BASE_URL + "/" + movie.getPosterPath()).into(holder.poster);
                    String titleOfMovie = movie.getOriginalTitle();

                    String[] dateParts = movie.getReleaseDate().split("-");
                    if(dateParts!=null && !dateParts[0].isEmpty() && dateParts[0]!=null)
                      titleOfMovie+="\n("+dateParts[0]+")";


                    holder.moreDetails.setText(titleOfMovie);
                    holder.rating.setText(String.valueOf(movie.getVoteAverage()));




                }

            }
            else  if (holder1 instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) holder1;

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {

        if (mList == null) {
            return 0;
        }

        if (mList.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView poster;
        final TextView moreDetails;
        final TextView rating;


        public ViewHolder(View convertView){
            super(convertView);

            poster =  convertView.findViewById(R.id.iv_poster);
            moreDetails = convertView.findViewById(R.id.tv_more_details);
            rating = convertView.findViewById(R.id.tv_rating);

            convertView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Movie data = mList.get(clickedPosition);
            mClickHandler.onMovieClicked(data);

        }

    }

}

