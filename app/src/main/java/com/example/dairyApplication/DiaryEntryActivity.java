package com.example.dairyApplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.smdiary.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import Database.DatabaseHelper;
import Database.DatabaseManager;

public class DiaryEntryActivity extends AppCompatActivity {

    private EditText diaryContent;
    private EditText diaryTitle;
    private Spinner tagSpinner;
    private DatabaseManager databaseManager;
    private long entryId = -1;
    private String[] colors = {"红色", "绿色", "蓝色"};
    private String userID;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Button pickImageButton;
    private String imagePath;
    private static final int PICK_VIDEO_REQUEST = 2; // 视频选择请求码
    private String videoPath; // 保存视频路径
    private Button pickVideoButton; // 视频选择按钮
    private VideoView videoView; // 播放视频的视图




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("SMDiary");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);
        // 获取Intent
        Intent intent = getIntent();

        // 从Intent中获取用户ID
        userID = intent.getStringExtra("USER_ID");


        diaryContent = findViewById(R.id.diaryContent);
        diaryTitle = findViewById(R.id.diaryTitle);
        tagSpinner = findViewById(R.id.tagSpinner);
        Button returnToMain = findViewById(R.id.returnToMain);
        Button customizeButton = findViewById(R.id.customizeButton);
        Button saveEntryButton = findViewById(R.id.saveEntryButton);
        Button deleteEntryButton = findViewById(R.id.deleteEntryButton);
        imageView = findViewById(R.id.imageView);
        pickImageButton = findViewById(R.id.pickImageButton);

        pickVideoButton = findViewById(R.id.pickVideoButton); // 初始化选择视频按钮
        pickVideoButton.setOnClickListener(v -> openVideoChooser()); // 设置点击事件
        videoView = findViewById(R.id.videoView); // 初始化视频视图



        ArrayAdapter<CharSequence> tagAdapter = ArrayAdapter.createFromResource(this,
                R.array.tags_array, android.R.layout.simple_spinner_item);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tagAdapter);

        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                openImageChooser();
            }
        });

        // 初始化数据库管理器
        databaseManager = new DatabaseManager(this);
        databaseManager.open();



        // 获取传递的 entryId 参数并加载内容
        entryId = getIntent().getLongExtra("entryId", -1);
        if (entryId != -1) {
            loadDiaryEntry(entryId);
        }

        // 返回按钮点击事件
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 直接返回上一个页面
            }
        });

        // 自定义按钮点击事件
        customizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomizationDialog(entryId);
            }
        });

        // 保存按钮点击事件
        saveEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDiaryEntry();
            }
        });


        // 删除按钮点击事件
        deleteEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entryId != -1) {
                    new AlertDialog.Builder(DiaryEntryActivity.this)
                            .setTitle("确认删除")
                            .setMessage("确定要删除这篇日记吗？删除后将无法恢复。")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int rowsDeleted = databaseManager.deleteDiaryEntryById(entryId);
                                    if (rowsDeleted > 0) {
                                        Toast.makeText(DiaryEntryActivity.this, "日记删除成功", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK); // 设置返回结果
                                        finish(); // 关闭Activity
                                    } else {
                                        Toast.makeText(DiaryEntryActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            }
        });
    }
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // 图片选择功能
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                imagePath = saveImageToInternalStorage(bitmap); // 保存图像路径
            } catch (IOException e) {
                Log.e("DiaryEntryActivity", "Error loading image", e);
                Toast.makeText(this, "无法加载图片", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri videoUri = data.getData(); // 获取选中的视频URI
            videoPath = saveVideoToInternalStorage(videoUri); // 保存视频
            if (videoPath != null) {
                videoView.setVideoPath(videoPath); // 设置视频路径
                videoView.setVisibility(View.VISIBLE); // 显示VideoView
                videoView.start(); // 播放视频
            } else {
                Toast.makeText(this, "无法保存视频", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String saveVideoToInternalStorage(Uri videoUri) {
        Context context = getApplicationContext();
        File directory = context.getDir("videoDir", Context.MODE_PRIVATE);
        File videoFile = new File(directory, "diaryVideo.mp4"); // 使用统一的视频文件名

        try (InputStream inputStream = getContentResolver().openInputStream(videoUri);
             FileOutputStream fos = new FileOutputStream(videoFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            Log.e("DiaryEntryActivity", "Error saving video", e);
            return null; // 如果保存失败，返回null
        }

        return videoFile.getAbsolutePath();
    }




    private void openVideoChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择视频"), PICK_VIDEO_REQUEST);
    }


    private String saveImageToInternalStorage(Bitmap bitmap) {
        Context context = getApplicationContext();
        File directory = context.getDir("imageDir", Context.MODE_PRIVATE);
        File imageFile = new File(directory, "diaryImage.png");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            Log.e("DiaryEntryActivity", "Error saving image", e);
        }

        return imageFile.getAbsolutePath();
    }

    private void loadDiaryEntry(long entryId) {
        Cursor cursor = databaseManager.queryDiaryEntries("entryId = ?", new String[]{String.valueOf(entryId)});
        if (cursor != null && cursor.moveToFirst()) {
            try {
                String fontSize = databaseManager.queryFontSizeByDiaryEntryId(entryId);
                String fontColor = databaseManager.queryFontColorByDiaryEntryId(entryId);
                diaryTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)));
                diaryContent.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT)));
                tagSpinner.setSelection(((ArrayAdapter) tagSpinner.getAdapter()).getPosition(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TAGS))));
                if (fontSize != null) {
                    switch (fontSize) {
                        case "小":
                            diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            break;
                        case "中":
                            diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                            break;
                        case "大":
                            diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                            break;
                    }
                }
                if (fontColor != null) {
                    // 假设fontColor是以"#RRGGBB"格式存储的
                    int color = Color.parseColor(fontColor);
                    diaryContent.setTextColor(color);
                }
                // 确保 COLUMN_IMAGE_PATH 列存在
                int imagePathIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_PATH);
                if (imagePathIndex != -1) {
                    String path = cursor.getString(imagePathIndex);
                    if (path != null && !path.isEmpty()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        imageView.setImageBitmap(bitmap);
                    }
                }

                // 确保 COLUMN_VIDEO_PATH 列存在
                int videoPathIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_VIDEO_PATH);
                if (videoPathIndex != -1) {
                    String path = cursor.getString(videoPathIndex);
                    if (path != null && !path.isEmpty()) {
                        videoView.setVideoPath(path); // 设定视频路径
                        videoView.setVisibility(View.VISIBLE); // 显示VideoView
                        videoView.start(); // 开始播放
                    }
                }
            } catch (IllegalArgumentException e) {
                Log.e("loadDiaryEntry", "Column not found", e);
            } finally {
                cursor.close();
            }
        }
    }


    private String getRealPathFromURI(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
        }
        return path;
    }



    private void saveDiaryEntry() {
        String title = diaryTitle.getText().toString().trim();
        String content = diaryContent.getText().toString().trim();
        String tag = tagSpinner.getSelectedItem().toString();
        Log.d(userID, "userIBefore " + userID);

        // 非空验证
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "标题和内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        long date = System.currentTimeMillis();

        if (entryId == -1) {
            // 新建日记
            Log.d("DiaryEntry", "ImagePathInsert " + imagePath);

            // 插入新的日记条目
            long newEntryId = databaseManager.insertDiaryEntry(
                    title,
                    content,
                    date,
                    tag,
                    "默认位置",
                    1,
                    userID,
                    imagePath,
                    videoPath
            );

            if (newEntryId != -1) {
                // 插入成功
                Toast.makeText(this, "日记保存成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // 设置返回结果
                finish();
            } else {
                // 插入失败
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 更新现有日记
            Log.d("DiaryEntry", "ImagePathUpdate " + imagePath);
            // 从Intent获取userID
            userID = getIntent().getStringExtra("USER_ID");

            if (imagePath == null || imagePath.isEmpty()) {
                Cursor cursor = null;
                try {
                    cursor = databaseManager.queryDiaryEntries("entryId = ?", new String[]{String.valueOf(entryId)});
                    if (cursor != null && cursor.moveToFirst()) {
                        int imagePathIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_PATH);
                        if (imagePathIndex != -1) {
                            imagePath = cursor.getString(imagePathIndex);
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close(); // 确保关闭 Cursor
                    }
                }
            }

            if (videoPath == null || videoPath.isEmpty()) {
                Cursor cursor1 = null;
                try {
                    cursor1 = databaseManager.queryDiaryEntries("entryId = ?", new String[]{String.valueOf(entryId)});
                    if (cursor1 != null && cursor1.moveToFirst()) {
                        int videoPathIndex = cursor1.getColumnIndex(DatabaseHelper.COLUMN_VIDEO_PATH);
                        if (videoPathIndex != -1) {
                            videoPath = cursor1.getString(videoPathIndex);
                        }
                    }
                } finally {
                    if (cursor1 != null) {
                        cursor1.close(); // 确保关闭 Cursor
                    }
                }
            }

            // 更新日记条目
            int rowsUpdated = databaseManager.updateDiaryEntry(
                    entryId,
                    title,
                    content,
                    date,
                    tag,
                    "默认位置",
                    1,
                    userID,
                    imagePath,
                    videoPath
            );

            if (rowsUpdated > 0) {
                // 更新成功
                Toast.makeText(this, "日记更新成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // 设置返回结果
                finish();
            } else {
                // 更新失败
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // 自定义对话框
    private void showCustomizationDialog(long entryID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] fontSizes = {"小", "中", "大"};

        // 字体大小选择对话框
        builder.setTitle("选择字体大小")
                .setItems(fontSizes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                                databaseManager.updateDiaryEntryFontSize(entryID,"小");
                                break;
                            case 1:
                                diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                databaseManager.updateDiaryEntryFontSize(entryID,"中");
                                break;
                            case 2:
                                diaryContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                                databaseManager.updateDiaryEntryFontSize(entryID,"大");
                                break;
                        }
                        showColorDialog(entryID);
                    }
                });
        builder.create().show();
    }

    // 颜色选择对话框
    private void showColorDialog(long entryID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择文本颜色")
                .setItems(colors, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int color = Color.BLACK;
                        switch (which) {
                            case 0:
                                color = Color.RED;
                                databaseManager.updateDiaryEntryFontColor(entryID,"#FF0000");
                                break;
                            case 1:
                                color = Color.GREEN;
                                databaseManager.updateDiaryEntryFontColor(entryID,"#00FF00");
                                break;
                            case 2:
                                color = Color.BLUE;
                                databaseManager.updateDiaryEntryFontColor(entryID,"#0000FF");
                                break;
                        }
                        applyColorToText(diaryContent.getText(), color);
                    }
                });
        builder.create().show();
    }

    // 应用颜色到所有文本
    private void applyColorToText(Editable text, int color) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(colorSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        diaryContent.setText(spannable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (databaseManager != null) {
            databaseManager.close();  // 关闭数据库连接
        }
    }

}