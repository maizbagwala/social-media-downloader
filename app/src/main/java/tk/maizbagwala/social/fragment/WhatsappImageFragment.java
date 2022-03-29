package tk.maizbagwala.social.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import tk.maizbagwala.social.R;
import tk.maizbagwala.social.adapter.ImageAdapter;
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

public class WhatsappImageFragment extends Fragment {
    FragmentWhatsappImageBinding binding;

    private File[] allfiles;
    ArrayList<WhatsappStatusModel> statusModelArrayList;
    ArrayList<Status> statusList = new ArrayList<>();
    private ImageAdapter imageAdapter;


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
            getImagesApi29();
        } else {
            getData();
        }
        binding.swiperefresh.setOnRefreshListener(() -> {
            statusModelArrayList = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                getImagesApi29();
            } else {
                getData();
            }
            binding.swiperefresh.setRefreshing(false);
        });

    }

    private void getImagesApi29() {
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

                    if (!status.isVideo() && status.getTitle().endsWith(".jpg")) {
                        statusList.add(status);
                    }
                }

                new Handler(Looper.getMainLooper()).post(() -> {

                    if (statusList.size() <= 0) {
                        binding.tvNoResult.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvNoResult.setVisibility(View.GONE);
                    }

                    imageAdapter = new ImageAdapter(statusList, binding.container);
                    binding.rvFileList.setAdapter(imageAdapter);
                    imageAdapter.notifyDataSetChanged();
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
                if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg")) {
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
                if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg")) {
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
}
