package pl.marchuck.nfc;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import pl.marchuck.nfc.scan.NfcScanActivity;
import pl.marchuck.nfc.utils.SoundUtils;
import pl.marchuck.nfc.write.NfcWriteActivity;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view1 = findViewById(R.id.write_example);
        View view2 = findViewById(R.id.scan_example);
        if (view1 != null) view1.setOnClickListener(this);
        if (view2 != null) view2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        SoundUtils.get(this).play(false);
        Class<? extends Activity> klazz;

        switch (view.getId()) {
            case R.id.write_example:
                klazz = NfcWriteActivity.class;
                break;
            default:
            case R.id.scan_example:
                klazz = NfcScanActivity.class;
                break;
        }

        Intent intent = new Intent(this, klazz);
        startActivity(intent);
    }
}
