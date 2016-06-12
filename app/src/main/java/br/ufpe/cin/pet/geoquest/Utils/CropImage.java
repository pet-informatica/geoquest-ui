package br.ufpe.cin.pet.geoquest.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 * Created by rbb3 on 12/06/16.
 */
public class CropImage {

    private int raio;

    private Bitmap bmp;

    public CropImage(int raio, Bitmap bmp) {
        this.raio = raio;
        this.bmp = bmp;
    }

    public Bitmap getCroppedBitmap() {
        Bitmap sbmp;
        if(bmp.getWidth() != raio || bmp.getHeight() != raio) {
            sbmp = Bitmap.createScaledBitmap(bmp, raio, raio, false);
        } else {
            sbmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
                sbmp.getWidth() / 2+0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }
}
