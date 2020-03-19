package com.jueze.uri2path;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {


	TextView osView, fileUriView, schemeView, authorityView, pathView, uriTypeView, realPathView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
			if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
			}
		}
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		osView = findViewById(R.id.system_os);
		fileUriView = findViewById(R.id.file_uri);
		schemeView = findViewById(R.id.scheme);
		authorityView = findViewById(R.id.authority);
		pathView = findViewById(R.id.path);
		uriTypeView = findViewById(R.id.uri_type);
		realPathView = findViewById(R.id.real_path);

		osView.setText(Build.VERSION.SDK);
    }

	public void chooseFile(View v) {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 1:
				if (resultCode == RESULT_OK) {
					Uri uri = data.getData();
					String scheme = uri.getScheme();
					String authority = uri.getAuthority();
					String path = uri.getPath();

					String realPath=null;
					try {
						realPath = FileUriUtils.getByUri(MainActivity.this, uri);
					} catch (Exception e) {}

					fileUriView.setText(uri.toString());
					schemeView.setText(scheme);
					authorityView.setText(authority);
					pathView.setText(path);
					String type="";
					if (DocumentsContract.isDocumentUri(this, uri)) {
						type = "documentUri";
					} else {
						type = uri.getScheme();
					}
					uriTypeView.setText(type);

					realPathView.setText(realPath);
				}
				break;
		}
	}

}
