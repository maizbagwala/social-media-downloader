package tk.maizbagwala.social.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import tk.maizbagwala.social.R
import tk.maizbagwala.social.activity.FullViewActivity
import tk.maizbagwala.social.activity.GalleryActivity
import tk.maizbagwala.social.adapter.FileListAdapter
import tk.maizbagwala.social.databinding.FragmentHistoryBinding
import tk.maizbagwala.social.interfaces.FileListClickInterface
import tk.maizbagwala.social.util.Utils
import java.io.File
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "m"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [YoutubeDownloadedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YoutubeDownloadedFragment : Fragment(), FileListClickInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentHistoryBinding? = null
    private var fileListAdapter: FileListAdapter? = null
    private var fileArrayList: ArrayList<File>? = null
    private var activity: GalleryActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onResume() {
        super.onResume()
        activity = getActivity() as GalleryActivity?
        getAllFiles()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        initViews()
        return binding!!.root
    }
    override fun onAttach(_context: Context) {
        super.onAttach(_context)
        activity = _context as GalleryActivity
    }
    private fun initViews() {
        binding!!.swiperefresh.setOnRefreshListener {
            getAllFiles()
            binding!!.swiperefresh.isRefreshing = false
        }
    }
    private fun getAllFiles() {
        fileArrayList = ArrayList()
        val files = Utils.RootDirectoryYouTubeShow.listFiles()
        if (files != null) {
            for (file in files) {
                fileArrayList!!.add(file)
            }
            fileListAdapter = FileListAdapter(activity, fileArrayList, this@YoutubeDownloadedFragment)
            binding!!.rvFileList.adapter = fileListAdapter
        }
    }
    override fun getPosition(position: Int, file: File?) {
        val inNext = Intent(activity, FullViewActivity::class.java)
        inNext.putExtra("ImageDataFile", fileArrayList)
        inNext.putExtra("Position", position)
        requireActivity().startActivity(inNext)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment YoutubeDownloadedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            YoutubeDownloadedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}