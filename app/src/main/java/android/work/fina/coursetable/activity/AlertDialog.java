package android.work.fina.coursetable.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.work.fina.coursetable.R;


public class AlertDialog extends Dialog {

    private Activity context;
    private ImageView imageView;
    private EditText editText;
    private Button button;

    public ImageView getImageView() {
        return imageView;
    }

    public EditText getEditText() {
        return editText;
    }

    private View.OnClickListener mClickListener;

    public AlertDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public AlertDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        imageView = (ImageView) findViewById(R.id.valcode);
        editText = (EditText) findViewById(R.id.inputcode);
        button = (Button) findViewById(R.id.sendcode);

        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.height = (int) (d.getHeight() * 0.36);
        p.width = (int) (d.getWidth() * 0.7);
        dialogWindow.setAttributes(p);

        button.setOnClickListener(mClickListener);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);

    }

}
