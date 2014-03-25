package com.github.davidmihola.pdffromassetsprovider.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;

// this version of the provider is based on an answer on stackoverflow
// by user Cristan:
// http://stackoverflow.com/a/19819321
// 
// but this version does not require all PDFs be directly under PDF_PATH,
// instead they can also be in sub-directories of that directory
//
// this version also uses the openAssetFile and writehDataToPipe methods from
// Lars Vogel's tutorial at:
// http://www.vogella.com/code/ApiDemos/src/com/example/android/apis/content/FileProvider.html
// to deal with compressed files
//
// this removes the need to append the ugly mp3 suffix to your PDFs
public class PDFFromAssetsProvider extends ContentProvider implements ContentProvider.PipeDataWriter<InputStream>{

	// this is the path to your pdf root folder (relative to assets)
	private static final String PDF_ROOT = "shared_pdfs/";

	// there's nothing to set up; just return true
	@Override
	public boolean onCreate() {
		return true;
	}

	// all content provided by this class has MIME type "application/pdf"
	@Override
	public String getType(Uri uri) {
		return "application/pdf";
	}

	// note that (in contrast to the previous version of the provider) this version does
	// need an mp3 suffix on your PDF file name
	@Override
	public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {

		String filename = uri.getPath();

		if (filename == null) {
			throw new FileNotFoundException();
		}

		File file = new File(PDF_ROOT, filename);

		try {

			InputStream is = getContext().getAssets().open(file.toString());
			// Start a new thread that pipes the stream data back to the caller.

			return new AssetFileDescriptor(
					openPipeHelper(uri, null, null, is, this), 0,
					AssetFileDescriptor.UNKNOWN_LENGTH);
		} catch (IOException e) {
			e.printStackTrace();
			FileNotFoundException fnf = new FileNotFoundException("Unable to open " + uri);
			throw fnf;
		}
	}

	@Override
	public void writeDataToPipe(ParcelFileDescriptor output, Uri uri, String mimeType,
			Bundle opts, InputStream args) {
		// Transfer data from the asset to the pipe the client is reading.
		byte[] buffer = new byte[8192];
		int n;
		FileOutputStream fout = new FileOutputStream(output.getFileDescriptor());
		try {
			while ((n=args.read(buffer)) >= 0) {
				fout.write(buffer, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				args.close();
			} catch (IOException e) {
			}
			try {
				fout.close();
			} catch (IOException e) {
			}
		}
	}

	// the remaining methods should not actually be called (I think)
	// but have to be implemented anyway
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

}
