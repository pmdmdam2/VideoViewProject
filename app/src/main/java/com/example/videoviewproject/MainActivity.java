package com.example.videoviewproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Ejemplo de VideoView para reproducir un vídeo almacenado en la
 * carpeta /mnt/sdcard/Download. Es necesario controlar los permisos Android
 * de manera completa (API superior a la 23)
 */
public class MainActivity extends AppCompatActivity {
    private VideoView mVideoView;
    private final int READ_EXTERNAL=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView =(VideoView)findViewById(R.id.surface_view);
        this.getPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                READ_EXTERNAL);
    }
    private void reproducirVideo(){
        //mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() +
        //        "/" + R.raw.calamardo));
        //acceso a memoria interna controlado por los permisos Android API > 23
        mVideoView.setVideoPath("/mnt/sdcard/Download/calamardo.3gp");
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.start();
        mVideoView.requestFocus();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL: {
                // si la solicitud del permiso se ha cancelado el array de resultados estará vacío
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    // permission concedido, se puede realizar la operación relacionada
                    //intent = new Intent(Intent.ACTION_INSERT);
                    //se comprueba si el dispositivo tiene posibilidad de realizar la acción solicitada por el intent
                    this.reproducirVideo();
                } else {
                    // permiso denegado
                    Toast.makeText(this,
                            R.string.permiso_denegado,Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    public void getPermission(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            /*2. Se comprueba si el permiso fue rechazado, devuelve true
            si el usuario rechazó la solicitud anteriormente y muestra
            false si un usuario rechazó un permiso y seleccionó la opción
            No volver a preguntar en el diálogo de solicitud de permiso, o
            si una política de dispositivo lo prohíbe*/
            if (ActivityCompat.
                    shouldShowRequestPermissionRationale(this,
                            permission)) {
                //Se informa al usuario de que no es posible ejecuta la acción solicitada
                // porque lo prohibe el dispositivo o porque se ha rechazado la solicitud.
                Toast.makeText(this, "explicación del permiso",
                        Toast.LENGTH_LONG).show();
            } else {
                //3. se requiere el permiso
                ActivityCompat.requestPermissions((AppCompatActivity) this,
                        new String[]{permission},
                        requestCode);
            }
        } else {
            //el permiso ya ha sido concedido, se llama al intent implícito
            // encargado de la marcación telefónica.
            //se comprueba si el dispositivo tiene posibilidad de realizar
            // la acción solicitada por el intent
            this.reproducirVideo();
        }
    }
}