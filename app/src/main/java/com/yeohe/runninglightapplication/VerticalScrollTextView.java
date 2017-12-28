package com.yeohe.runninglightapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/26.
 */

public class VerticalScrollTextView extends TextView {

    int perLineCount = 20;
    int currentNum = 0;  // currentNum*perLineCount =一行（为 了慢慢移动把一行分成了perLineCount份）
    int totalLine = 0;
    int lineHeight;
    int totalHeight;
    int i=0;
    boolean canScroll = true;
    Handler mhander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //循环滚动
            scrollTo(0, lineHeight * currentNum);
        }
    };

    public VerticalScrollTextView(Context context) {
        super(context);
        init();
    }

    public VerticalScrollTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalScrollTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
//                    if (!canScroll) {
//                        continue;
//                    }
                    if (totalHeight != 0) {
                        currentNum = currentNum + 1;
                        if (currentNum == (totalLine-3)*perLineCount) {
                            currentNum = 0;
                        }
                        Message message = mhander.obtainMessage();
                        message.arg1 = currentNum;
                        message.sendToTarget();
                        try {
                            if (currentNum==0){
                                Thread.sleep(1500);
                            }else
                                Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        currentNum = 0;
        postInvalidate();
        //获取行高
        lineHeight = getLineHeight() / perLineCount;
        //获取总共的行数
        totalLine = getLineCount();
        //获取总共的高度
        totalHeight = getLineCount() * lineHeight*perLineCount;
        //获取控件高度
        int height =getMeasuredHeight();
        i = (int) (height / getTextSize());
        if (totalLine <= i) {
            canScroll = false;
        }else {
            canScroll=true;
        }
    }

}
