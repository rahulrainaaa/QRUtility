package app.uril.qr.qrutility;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRGen extends AppCompatActivity {

    private ImageView imageView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Generate QR");
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        if (Constants.bmp != null) {
            imageView.setImageBitmap(Constants.bmp);
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTextBox();
            }
        });
        //inputTextBox();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_action_scan:
                scanQR();
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * @method inputTextBox
     * @desc Ask for String for QR.
     */
    public void inputTextBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        alertDialogBuilder.setView(input);
        alertDialogBuilder.setTitle("Text");

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("QR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        generateQR(input.getText().toString());
                    }
                })
                .setNegativeButton("BAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        //generateBar(input.getText().toString());
                    }
                });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    /**
     * @method generateQR
     * @desc Generate QR on screen
     */
    private void generateQR(String content) {

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Constants.bmp = null;
            Constants.bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Constants.bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(Constants.bmp);

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @method generateBar
     * @desc Generate Bar on screen
     */
    private void generateBar(String content) {
        CodaBarWriter barcode = new CodaBarWriter();
        try {
            BitMatrix bitMatrix = barcode.encode(content, BarcodeFormat.CODABAR, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Constants.bmp = null;
            Constants.bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Constants.bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(Constants.bmp);

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @method scanQR
     * @desc Scan QR and show content
     */
    private void scanQR() {

        startActivity(new Intent(this, QRScan.class));
    }
}
