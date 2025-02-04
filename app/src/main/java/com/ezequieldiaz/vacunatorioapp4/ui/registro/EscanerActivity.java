package com.ezequieldiaz.vacunatorioapp4.ui.registro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class EscanerActivity extends AppCompatActivity {

    private PreviewView previewView;
    private BarcodeScanner barcodeScanner;
    private ProcessCameraProvider cameraProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner);

        previewView = findViewById(R.id.previewView);
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_PDF417)
                .build();
        barcodeScanner = BarcodeScanning.getClient(options);

        // Verificar y solicitar permisos de la cámara
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            // Iniciar la cámara si los permisos ya están concedidos
            startCamera();
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                // Configurar la vista previa de la cámara
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Configurar el análisis de códigos de barras
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
                    @SuppressLint("UnsafeOptInUsageError")
                    Image mediaImage = imageProxy.getImage();
                    if (mediaImage != null) {
                        InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                        barcodeScanner.process(image)
                                .addOnSuccessListener(barcodes -> {
                                    if (barcodes.isEmpty()) {
                                        // Log para saber que no se detectó nada en este frame
                                        System.out.println("No se detectaron códigos en este frame.");
                                    }
                                    for (Barcode barcode : barcodes) {
                                        try {
                                            String rawValue = barcode.getRawValue();
                                            assert rawValue != null;
                                            // Convertir usando windows-1252 a UTF-8
                                            String correctedValue = new String(rawValue.getBytes("windows-1252"), "UTF-8");
                                            System.out.println("Código detectado: " + correctedValue);
                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("DATOS_ESCANEADOS", correctedValue);
                                            setResult(RESULT_OK, resultIntent);
                                            finish();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                })
                                .addOnFailureListener(e -> {
                                    e.printStackTrace();
                                })
                                .addOnCompleteListener(task -> {
                                    imageProxy.close();
                                });
                    } else {
                        imageProxy.close();
                    }
                });


                // Enlazar la cámara al ciclo de vida
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                // Limpiar cualquier uso previo de la cámara
                cameraProvider.unbindAll();

                // Enlazar la cámara al ciclo de vida de la actividad
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al iniciar la cámara", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar la cámara cuando la actividad se destruya
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }
}