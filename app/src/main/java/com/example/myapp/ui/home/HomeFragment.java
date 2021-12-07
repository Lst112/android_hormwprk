package com.example.myapp.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.example.myapp.databinding.FragmentHomeBinding;
import com.example.myapp.db.domain.Result;
import com.example.myapp.db.viewModel.ResultViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import share.ShareViewModel;


public class HomeFragment extends Fragment {
    private ResultViewModel resultViewModel;
    private ShareViewModel shareViewModel;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String APP_ID = "25253617";
    private String API_KEY = "MWZQSiubTL87K7NK3iFGDiVe";
    private String SECRET_KEY = "x7hxURn95ioQBwSu90QqZKw8XmcDmuor";
    private String imagePath;
    private String keyWord; //关键字
    private final int takePhoto = 1;
    private final int fromAlbum = 2;
    private Uri imageUri;
    private File outputImage;
    private JSONObject res;
    private int index;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        index = 0;
        shareViewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        shareViewModel.getLog().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.commit.setEnabled(aBoolean);
            }
        });
        binding.textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
//        摄像头获取
        binding.takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputImage = new File(getContext().getExternalCacheDir(), "output_image.jpg");
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(getContext(), "com.example.myapp.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, takePhoto);
            }
        });
        binding.fromAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, fromAlbum);
            }
        });
        binding.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> options = new HashMap<String, String>();
                options.put("baike_num", "5");
                AipImageClassify client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
                if (imagePath != null) {
                    //开启子线程异步上传图片，使用关键字请在子线程末尾，如果在主线程中使用关键字会产生不同步
                    new Thread(() -> {
                        res = client.advancedGeneral(imagePath, options);
                        try {
                            keyWord = res.getJSONArray("result").getJSONObject(0).getString("keyword");
                            System.out.println(res.toString());
                            binding.key.setText(keyWord);
                            String detail = res.getJSONArray("result").getJSONObject(0).getJSONObject("baike_info").getString("description");
                            binding.textView.setText(detail);
                            index++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (res == null) {
                    Toast.makeText(getContext(), "未检索信息", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    try {
                        int sum = res.getJSONArray("result").length();
                        if (index < sum) {
                            keyWord = res.getJSONArray("result").getJSONObject(index).getString("keyword");
                            System.out.println(res.toString());
                            binding.key.setText(keyWord);
                            String detail = res.getJSONArray("result").getJSONObject(index).getJSONObject("baike_info").getString("description");
                            binding.textView.setText(detail);
                            index++;
                        } else {
                            index = 0;
                            Toast.makeText(getContext(), "已输出所有信息", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Result result = new Result(binding.key.getText().toString(), binding.textView.getText().toString());
                resultViewModel.insert(result);
            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        switch (requestCode) {
            case takePhoto:
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来并存入相册
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap rotatedBitmap = rotateIfRequired(bitmap);
                    Date now = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
                    saveImage("IMG_" + ft.format(now), rotatedBitmap);
                    binding.imageView.setImageBitmap(rotatedBitmap);
                    imagePath = outputImage.getPath();
                }
                break;
            case fromAlbum:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    if (DocumentsContract.isDocumentUri(getContext(), uri)) {
                        String docId = DocumentsContract.getDocumentId(uri);
                        if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                            String id = docId.split(":")[1];
                            String selection = MediaStore.Images.Media._ID + "=" + id;
                            imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                        } else if ("com.android.providers.downloads.documents" == uri.getAuthority()) {
                            Uri contentUri = ContentUris.withAppendedId((Uri.parse("content://downloads/public_downloads")), Long.parseLong(docId));
                            imagePath = getImagePath(contentUri, null);
                        }
                    } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                        imagePath = getImagePath(uri, null);
                    }
                    binding.imageView.setImageBitmap(getBitmapFromUri(uri));
                }
        }
    }

//    根据照片uri获取
    private Bitmap getBitmapFromUri(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFileDescriptor(contentResolver.openFileDescriptor(uri, "r").getFileDescriptor());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap rotateIfRequired(Bitmap bitmap) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(outputImage.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Bitmap rotatedBitmap = null;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateBitmap(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateBitmap(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateBitmap(bitmap, 270);
                break;
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degree);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return rotatedBitmap;
    }

    private void saveImage(String fileName, Bitmap bitmap) {
        try {
            //设置保存参数到ContentValues中
            ContentValues contentValues = new ContentValues();
            //设置文件名
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            //兼容Android Q和以下版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                //RELATIVE_PATH是相对路径不是绝对路径
                //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera");
                //contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Music/signImage");
            } else {
                contentValues.put(MediaStore.Images.Media.DATA, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
            }
            //设置文件类型
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
            //执行insert操作，向系统文件夹中添加文件
            //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
            Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                //若生成了uri，则表示该文件添加成功
                //使用流将内容写入该uri中即可
                OutputStream outputStream = getContext().getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
            }
        } catch (Exception e) {
        }
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}