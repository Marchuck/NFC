package pl.marchuck.nfc.write;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import pl.marchuck.nfc.R;
import pl.marchuck.nfc.utils.SoundUtils;

public class NfcWriteActivity extends AppCompatActivity implements NfcWriteView<NfcWriteActivity> {
    private static final String TAG = "NFCWriteTag";

    EditText editText;
    NfcWritePresenter<NfcWriteActivity> presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        presenter = new NfcWritePresenter<>(this);

        setupBackButton();
        setupTextChanges();
        presenter.setup();
    }

    private void setupTextChanges() {
        editText = (EditText) findViewById(R.id.text_to_write);
        editText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.setTagText(charSequence.toString());
            }

            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setupBackButton() {
        View backBtn = findViewById(R.id.write_back_button);
        if (backBtn != null) {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        presenter.onNewIntent(intent);
    }

    @Override
    public NfcWriteActivity self() {
        return this;
    }

}