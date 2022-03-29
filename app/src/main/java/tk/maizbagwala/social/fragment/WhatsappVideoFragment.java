package tk.maizbagwala.social.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import tk.maizbagwala.social.R;
import tk.maizbagwala.social.activity.WhatsappActivity;
import tk.maizbagwala.social.adapter.VideoAdapter;
import tk.maizbagwala.social.adapter.WhatsappStatusAdapter;
import tk.maizbagwala.social.databinding.FragmentWhatsappImageBinding;
import tk.maizbagwala.social.model.Status;
import tk.maizbagwala.social.model.WhatsappStatusModel;
import tk.maizbagwala.social.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static androidx.databinding.DataBindingUtil.inflate;

public class WhatsappVideoFragment extends Fragment {
    FragmentWhatsappImageBinding binding;

    private File[] allfiles;
    ArrayList<WhatsappStatusModel> statusModelArrayList;
    ArrayList<Status> statusList = new ArrayList<>();
    private VideoAdapter videoAdapter;
    private WhatsappStatusAdapter whatsappStatusAdapter;
    public static final String STATUS_DIRECTORY_NEW = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, R.layout.fragment_whatsapp_image, container, false);
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        statusModelArrayList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            getData11();
            getVideosApi29();
        } else {
            getData();
        }
        binding.swiperefresh.setOnRefreshListener(() -> {
            statusModelArrayList = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                getData11();
                getVideosApi29();
            } else {
                getData();
            }
            binding.swiperefresh.setRefreshing(false);
        });

    }

    private void getData() {
        WhatsappStatusModel whatsappStatusModel;
        String targetPath1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
        String targetPath2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
        String targetPath = "";
        if (Utils.dir_exists(targetPath2)) {
            targetPath = targetPath2;
        } else {
            targetPath = targetPath1;
        }
        File targetDirector = new File(targetPath);
        allfiles = targetDirector.listFiles();

        String targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses";
        File targetDirectorBusiness = new File(targetPathBusiness);
        File[] allfilesBusiness = targetDirectorBusiness.listFiles();


        try {
            Arrays.sort(allfiles, (Comparator) (o1, o2) -> {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            });

            for (int i = 0; i < allfiles.length; i++) {
                File file = allfiles[i];
                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                    whatsappStatusModel = new WhatsappStatusModel("WhatsStatus: " + (i + 1),
                            Uri.fromFile(file),
                            allfiles[i].getAbsolutePath(),
                            file.getName());
                    statusModelArrayList.add(whatsappStatusModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Arrays.sort(allfilesBusiness, (Comparator) (o1, o2) -> {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            });

            for (int i = 0; i < allfilesBusiness.length; i++) {
                File file = allfilesBusiness[i];
                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                    whatsappStatusModel = new WhatsappStatusModel("WhatsStatusB: " + (i + 1),
                            Uri.fromFile(file),
                            allfilesBusiness[i].getAbsolutePath(),
                            file.getName());
                    statusModelArrayList.add(whatsappStatusModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (statusModelArrayList.size() != 0) {
            binding.tvNoResult.setVisibility(View.GONE);
        } else {
            binding.tvNoResult.setVisibility(View.VISIBLE);
        }
        whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), statusModelArrayList);
        binding.rvFileList.setAdapter(whatsappStatusAdapter);

    }

    private void getData11() {

        WhatsappStatusModel whatsappStatusModel;

        Uri uri = Uri.parse(STATUS_DIRECTORY_NEW);

        DocumentFile doc = DocumentFile.fromTreeUri(requireActivity(), uri);

        if (doc == null) {
            return;
        }

        DocumentFile doc2 = doc.findFile("Media");

        assert doc2 != null;
        DocumentFile doc3 = doc2.findFile(".Statuses");

        DocumentFile[] statusFiles;

        assert doc3 != null;
        statusFiles = doc3.listFiles();
        try {
//                Arrays.sort(statusFiles, (Comparator) (o1, o2) -> {
//                    if (((DocumentFile) o1).lastModified() > ((DocumentFile) o2).lastModified()) {
//                        return -1;
//                    } else if (((DocumentFile) o1).lastModified() < ((DocumentFile) o2).lastModified()) {
//                        return +1;
//                    } else {
//                        return 0;
//                    }
//                });

            for (int i = 0; i < statusFiles.length; i++) {
                DocumentFile file = statusFiles[i];
                if (Uri.fromFile(new File(file.getUri().getPath())).toString().endsWith(".mp4")) {
                    File tem = new File(file.getUri().getPath());
                    whatsappStatusModel = new WhatsappStatusModel("WhatsStatus: " + (i + 1),
                            Uri.fromFile(tem),
                            tem.getAbsolutePath(),
                            tem.getName());
                    statusModelArrayList.add(whatsappStatusModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (statusModelArrayList.size() != 0) {
            binding.tvNoResult.setVisibility(View.GONE);
        } else {
            binding.tvNoResult.setVisibility(View.VISIBLE);
        }
        whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), statusModelArrayList);
        binding.rvFileList.setAdapter(whatsappStatusAdapter);
//            imagesList.clear();
//
//            if (statusFiles.length > 0) {
//
//                for (DocumentFile file : statusFiles) {
//                    Status status = new Status(new File(file.getUri().getPath()), file.getName(),
//                            file.getUri().getPath(),
//                            file.getUri());
//
//                    if (!status.isVideo() && status.getTitle().endsWith(".jpg")) {
//                        imagesList.add(status);
//                    }
//                }
//
//                handler.post(() -> {
//
//                    if (imagesList.size() <= 0) {
//                        messageTextView.setVisibility(View.VISIBLE);
//                        messageTextView.setText(R.string.no_files_found);
//                    } else {
//                        messageTextView.setVisibility(View.GONE);
//                        messageTextView.setText("");
//                    }
//
//                    imageAdapter = new ImageAdapter(imagesList, container);
//                    recyclerView.setAdapter(imageAdapter);
//                    imageAdapter.notifyDataSetChanged();
//                    progressBar.setVisibility(View.GONE);
//                });
//
//            } else {
//
//                handler.post(() -> {
//                    progressBar.setVisibility(View.GONE);
//                    messageTextView.setVisibility(View.VISIBLE);
//                    messageTextView.setText(R.string.no_files_found);
//                    Toast.makeText(getActivity(), getString(R.string.no_files_found), Toast.LENGTH_SHORT).show();
//                });
//
//            }
//            swipeRefreshLayout.setRefreshing(false);

    }

    private void getVideosApi29() {
        new Thread(() -> {

            Uri uri = Uri.parse(STATUS_DIRECTORY_NEW);

            DocumentFile doc = DocumentFile.fromTreeUri(requireActivity(), uri);

            if (doc == null) {
                binding.tvNoResult.setVisibility(View.VISIBLE);
                binding.tvNoResult.setText("Can not find whatsapp directory");
                return;
            }

            DocumentFile doc2 = doc.findFile("Media");

            assert doc2 != null;
            DocumentFile doc3 = doc2.findFile(".Statuses");

            DocumentFile[] statusFiles;

            assert doc3 != null;
            statusFiles = doc3.listFiles();

            statusList.clear();

            if (statusFiles.length > 0) {

                for (DocumentFile file : statusFiles) {
                    Status status = new Status(new File(file.getUri().getPath()), file.getName(),
                            file.getUri().getPath(),
                            file.getUri());

                    if (status.isVideo() && status.getTitle().endsWith(".mp4")) {
                        statusList.add(status);
                    }
                }

                new Handler(Looper.getMainLooper()).post(() -> {

                    if (statusList.size() <= 0) {
                        binding.tvNoResult.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvNoResult.setVisibility(View.GONE);
                    }

                    videoAdapter = new VideoAdapter(statusList, binding.container);
                    binding.rvFileList.setAdapter(videoAdapter);
                    videoAdapter.notifyDataSetChanged();
                });

            } else {

                new Handler(Looper.getMainLooper()).post(() -> {
                    binding.tvNoResult.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "no files found", Toast.LENGTH_SHORT).show();
                });

            }
            binding.swiperefresh.setRefreshing(false);
        }).start();
    }
}
