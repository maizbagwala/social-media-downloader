package tk.maizbagwala.social.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tk.maizbagwala.social.R
import tk.maizbagwala.social.activity.YoutubeActivity
import tk.maizbagwala.social.interfaces.YoutubeInterface

class YoutubeQuelityListAdapter(private val list:ArrayList<YoutubeActivity.YtFragmentedVideo>, private val YtInter: YoutubeInterface) :
    RecyclerView.Adapter<YoutubeQuelityListAdapter.YoutubeQuelityListAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YoutubeQuelityListAdapterViewHolder {
        return YoutubeQuelityListAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ytq_item,parent,false))
    }

    override fun onBindViewHolder(holder: YoutubeQuelityListAdapterViewHolder, position: Int) {
        val model=list[position] as YoutubeActivity.YtFragmentedVideo

        val btnText: String = when {
            model.height == -1 -> "Audio Only" + model.audioFile.format
                .audioBitrate + " kbit/s"
            model.videoFile.format
                .fps == 60 -> model.height.toString() + "p60"
            else -> model.height.toString() + "p"
        }
        holder.tv.text=btnText
        holder.tv.setOnClickListener {
            YtInter.onDownloadClick(model)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class YoutubeQuelityListAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv: TextView =itemView.findViewById(R.id.tv)
    }
}