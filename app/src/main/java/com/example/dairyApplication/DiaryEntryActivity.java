package com.example.dairyApplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smdiary.R;

// 日记条目活动
public class DiaryEntryActivity extends AppCompatActivity {

    private EditText diaryContent; // 日记内容编辑框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry); // 设置布局
        diaryContent = findViewById(R.id.diaryContent); // 获取日记内容编辑框
        Button returnToMain = findViewById(R.id.returnToMain); // 获取返回按钮
        Button customizeButton = findViewById(R.id.customizeButton); // 获取自定义按钮

        // 处理返回按钮点击事件
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryEntryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 处理自定义样式按钮点击事件
        customizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomizationDialog(); // 显示自定义样式对话框
            }
        });
    }

    // 显示自定义样式对话框
    private void showCustomizationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] fontSizes = {"小", "中", "大"};
        String[] colors = {"红色", "绿色", "蓝色"};

        // 字体大小选择
        builder.setTitle("选择字体大小")
                .setItems(fontSizes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                diaryContent.setTextSize(14); // 小
                                break;
                            case 1:
                                diaryContent.setTextSize(18); // 中
                                break;
                            case 2:
                                diaryContent.setTextSize(22); // 大
                                break;
                        }
                    }
                });

        // 颜色选择
        builder.setPositiveButton("选择颜色", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showColorDialog(colors); // 显示颜色选择对话框
            }
        });

        builder.create().show();
    }

    // 显示颜色选择对话框
    private void showColorDialog(String[] colors) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择文本颜色")
                .setItems(colors, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int color = Color.BLACK; // 默认颜色
                        switch (which) {
                            case 0:
                                color = Color.RED; // 红色
                                break;
                            case 1:
                                color = Color.GREEN; // 绿色
                                break;
                            case 2:
                                color = Color.BLUE; // 蓝色
                                break;
                        }
                        // 应用选择的颜色
                        applyColorToText(diaryContent.getText(), color);
                    }
                });
        builder.create().show();
    }

    // 应用选择的颜色到文本
    private void applyColorToText(Editable text, int color) {
        int start = diaryContent.getSelectionStart(); // 获取文本选择开始位置
        int end = diaryContent.getSelectionEnd(); // 获取文本选择结束位置
        if (start >= 0 && end > start) {
            // 使用Spannable设置颜色
            Spannable spannable = new SpannableString(text);
            spannable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            diaryContent.setText(spannable); // 更新文本
        }
    }
}
