package com.android.baselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.NumberKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.android.baselibrary.R;
import com.android.baselibrary.interface_.CommCallBack;
import com.android.baselibrary.util.Util;

/**
 * Created by Administrator on 2018/9/20.
 */

public class Comm_EditView extends LinearLayout implements View.OnClickListener {

    public static int InuptType_none = 0;              //默认不限制
    public static int InuptType_phoneOrEmail = 1;     //手机或者邮箱
    public static int InuptType_password = 2;           //文本密码
    public static int InuptType_number = 3;             //纯数字
    public static int InuptType_numberDecimal = 4;      //带小数点数字
    public static int InuptType_password_number = 5;    //数字密码
    public static int InuptType_numberAndLetters = 6;    //数字和单词

    public Comm_EditView(Context context) {
        super(context);
        this.context = context;
        initView(null);
    }

    public Comm_EditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public Comm_EditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    int inputType = InuptType_none;
    boolean isPwdVisible = false;
    boolean isTextStyle_bold = false;

    Context context;
    public EditText et_input;
    ImageView iv_clear, iv_pwd_eye;
    View view_line;
    LinearLayout ll_editcontainer, ll_allcontainer;

    CommCallBack inputCallBack;

    public void setInputCallBack(CommCallBack inputCallBack) {
        this.inputCallBack = inputCallBack;
    }

    private void initView(AttributeSet attrs) {
        View.inflate(context, R.layout.view_comm_editview, this);
        et_input = findViewById(R.id.et_input);
        iv_clear = findViewById(R.id.iv_clear);
        view_line = findViewById(R.id.view_line);
        iv_pwd_eye = findViewById(R.id.iv_pwd_eye);
        ll_editcontainer = findViewById(R.id.ll_editcontainer);
        ll_allcontainer = findViewById(R.id.ll_allcontainer);


        iv_clear.setOnClickListener(this);
        iv_pwd_eye.setOnClickListener(this);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Comm_EditView);

            //线
            view_line.setVisibility(typedArray.getBoolean(R.styleable.Comm_EditView_cev_showLine, false) ? VISIBLE : GONE);
            //内容
            String content = typedArray.getString(R.styleable.Comm_EditView_cev_content);
            String hint = typedArray.getString(R.styleable.Comm_EditView_cev_hint);
            et_input.setText(content);   //内容
            et_input.setHint(hint);        //默认文案
            et_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(typedArray.getInteger(R.styleable.Comm_EditView_cev_maxlength, 100))});   //输入长度
            et_input.setTextSize(typedArray.getInt(R.styleable.Comm_EditView_cev_textSize, 18));   //文字大小
            isTextStyle_bold = typedArray.getBoolean(R.styleable.Comm_EditView_cev_textStyle_bold, false);   //文字加粗
            et_input.setGravity(typedArray.getInt(R.styleable.Comm_EditView_cev_gravity, 0) == 0 ? Gravity.LEFT | Gravity.CENTER_VERTICAL : Gravity.RIGHT | Gravity.CENTER_VERTICAL);   //文字居中
            //设置输入类型
            setInputType(typedArray.getInt(R.styleable.Comm_EditView_cev_inputType, 0));

            //删除按钮
            iv_clear.setVisibility(TextUtils.isEmpty(content) ? GONE : VISIBLE);

            //是否是多行
            boolean multipleLines = typedArray.getBoolean(R.styleable.Comm_EditView_cev_multipleLines, false);
            if (multipleLines) {
                LayoutParams params_et_input = (LayoutParams) et_input.getLayoutParams();
                params_et_input.height = LayoutParams.MATCH_PARENT;
                params_et_input.width = LayoutParams.MATCH_PARENT;
//                params_et_input.topMargin = Util.dip2px(getContext(), 9);
//                params_et_input.bottomMargin = Util.dip2px(getContext(), 9);
                et_input.setLayoutParams(params_et_input);
                et_input.setSingleLine(false);
                et_input.setGravity(Gravity.TOP);

                ll_editcontainer.setGravity(Gravity.TOP);
//                LayoutParams params_ll_edit = (LayoutParams) ll_editcontainer.getLayoutParams();
//                params_ll_edit.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                ll_editcontainer.setLayoutParams(params_ll_edit);
//
//                ll_allcontainer.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {

            }

            typedArray.recycle();
        }

        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    iv_clear.setVisibility(View.GONE);
                } else {
                    if (et_input.hasFocus()) {
                        iv_clear.setVisibility(View.VISIBLE);
                    }
                }
                if (inputCallBack != null) {
                    inputCallBack.onResult(null);
                }
            }
        });

        et_input.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (et_input.getText().length() > 0) {
                        iv_clear.setVisibility(View.VISIBLE);
                    }
                } else {
                    iv_clear.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
        if (inputType == InuptType_phoneOrEmail) {
            setInuptType_phoneOrEmail();
        } else if (inputType == InuptType_number) {
            et_input.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
//            et_input.setInputType(InputType.TYPE_CLASS_NUMBER);
//            et_input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
        } else if (inputType == InuptType_numberDecimal) {
//            et_input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            et_input.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
//            et_input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else if (inputType == InuptType_password) {
            iv_pwd_eye.setVisibility(VISIBLE);
            setPwdVisible();
        } else if (inputType == InuptType_password_number) {
            iv_pwd_eye.setVisibility(VISIBLE);
            setPwdVisible();
        } else if (inputType == InuptType_numberAndLetters) {
            setInuptType_numberAndLetters();
//            et_input.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        }
        et_input.getPaint().setFakeBoldText(isTextStyle_bold);   //文字加粗
    }

    private void setPwdVisible() {
        if (isPwdVisible) {
            iv_pwd_eye.setImageResource(R.drawable.ico_eyes_open_grey);
            if (inputType == InuptType_password) {  //文本密码
                et_input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_TEXT);
            } else if (inputType == InuptType_password_number) {   //数字密码
                et_input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_NUMBER);
            }
            et_input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            iv_pwd_eye.setImageResource(R.drawable.ico_eyes_close_grey);
            if (inputType == InuptType_password) {
                et_input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            } else if (inputType == InuptType_password_number) {
                et_input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
            }
            et_input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        et_input.setSelection(getText().length());
        et_input.getPaint().setFakeBoldText(isTextStyle_bold);   //文字加粗
    }

    public String getText() {
        return et_input.getText().toString();
    }

    public void setText(String text) {
        try {
            et_input.setText(text);
            if (!TextUtils.isEmpty(text)) {
                et_input.setSelection(text.length());
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_clear) {
            et_input.setText("");
        } else if (id == R.id.iv_pwd_eye) {
            isPwdVisible = !isPwdVisible;
            setPwdVisible();
        }
    }

    //默认文案
    public void setHint(String hint) {
        et_input.setHint(hint);
    }

    //输入长度
    public void setMaxLength(int length) {
        et_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});   //输入长度
    }

    //获取当前输入法类名
    private String getDefaultInputMethodClassName(Context context) {
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.DEFAULT_INPUT_METHOD);
    }

    //设置输入方式是手机或者邮箱
    private void setInuptType_phoneOrEmail() {
        String className = getDefaultInputMethodClassName(context);
        if (!TextUtils.isEmpty(className) && (className.contains("LatinIME") || className.contains("google"))) {//google输入法
            et_input.setKeyListener(new NumberKeyListener() {
                // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
                @Override
                public int getInputType() {
                    return  InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                }

                // 返回允许输入的字符
                @Override
                protected char[] getAcceptedChars() {
                    // TODO Auto-generated method stub
                    char[] c = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                            '@', '.'};//0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@.
                    return c;
                }
            });
        } else {
            et_input.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@."));
        }
    }

    //设置输入方式是数字 和字母
    private void setInuptType_numberAndLetters() {
        String className = getDefaultInputMethodClassName(context);
        if (!TextUtils.isEmpty(className) && (className.contains("LatinIME") || className.contains("google"))) {//google输入法
            et_input.setKeyListener(new NumberKeyListener() {
                // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
                @Override
                public int getInputType() {
                    return  InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                }

                // 返回允许输入的字符
                @Override
                protected char[] getAcceptedChars() {
                    // TODO Auto-generated method stub
                    char[] c = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};//0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
                    return c;
                }
            });
        } else {
            et_input.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        }
    }
}
