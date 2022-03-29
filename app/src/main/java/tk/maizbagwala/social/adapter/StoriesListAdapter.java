package tk.maizbagwala.social.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import tk.maizbagwala.social.R;
import tk.maizbagwala.social.databinding.ItemsInstaViewBinding;
import tk.maizbagwala.social.model.story.ItemModel;
import tk.maizbagwala.social.util.Utils;

import java.util.ArrayList;

public class StoriesListAdapter extends RecyclerView.Adapter<StoriesListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemModel> storyItemModelList;

    public StoriesListAdapter(Context context, ArrayList<ItemModel> list) {
        this.context = context;
        this.storyItemModelList = list;
    }

    @NonNull
    @Override
    public StoriesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.items_insta_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesListAdapter.ViewHolder viewHolder, int position) {
        ItemModel itemModel = storyItemModelList.get(position);
        try {
            if (itemModel.getMedia_type()==2) {
                viewHolder.binding.ivPlay.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.ivPlay.setVisibility(View.GONE);
            }
            Glide.with(context)
                    .load(itemModel.getImage_versions2().getCandidates().get(0).getUrl())
                    .into(viewHolder.binding.pcw);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        viewHolder.binding.tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemModel.getMedia_type()==2) {
                    Utils.startDownload(itemModel.getVideo_versions().get(0).getUrl(),
                            Utils.RootDirectoryInsta, context,"story_"+itemModel.getId()+".mp4" );
                }else {
                    Utils.startDownload(itemModel.getImage_versions2().getCandidates().get(0).getUrl(),
                            Utils.RootDirectoryInsta, context, "story_"+itemModel.getId()+".png");
                }
            }
        });


    }
    @Override
    public int getItemCount() {
        return storyItemModelList == null ? 0 : storyItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         ItemsInstaViewBinding binding;
        public ViewHolder(ItemsInstaViewBinding mbinding) {
            super(mbinding.getRoot());
            this.binding = mbinding;
        }
    }
}