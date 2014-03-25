package com.github.davidmihola.pdffromassetsprovider;

import com.github.davidmihola.pdffromassetsprovider.provider.PDFFromAssetsProvider;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

	// the part after "content://" must be identical to the string defined in your
	// AndroidManifest.xml as android:authorities in the provider tag
	private static final Uri PDF_CONTENT_URI = Uri.parse("content://com.github.davidmihola.PDFFromAssetsProviderExampleApp");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
        openPDF("gpl/quick-guide-gplv3.pdf");
	}

	// filename contains the path of the PDF file (relative to the PDF_ROOT
	// directory as defined in PDFFromAssetsProvider.PDF_ROOT
	// but WITHOUT the mp3 suffix
	protected void openPDF(String filename) {

		Uri pdfUri = Uri.withAppendedPath(PDF_CONTENT_URI, filename);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(pdfUri, "application/pdf");

		startActivity(intent);
	}
}
