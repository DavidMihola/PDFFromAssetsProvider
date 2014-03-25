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
public class PDFFromAssetsProvider extends ContentProvider {

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

	// note that all pdfs must have the suffix .mp3 to prevent them from
	// being compressed; if you store them uncompressed (i.e. without the
	// mp3 suffix), you get a FileNotFoundException when you try to open it
	@Override
	public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {

		AssetManager assetManager = getContext().getAssets();
		String filename = uri.getPath();

		if (filename == null) {
			throw new FileNotFoundException();
		}

		File file = new File(PDF_ROOT, filename + ".mp3");

		AssetFileDescriptor assetFileDescriptor = null;

		try {
			assetFileDescriptor = assetManager.openFd(file.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return assetFileDescriptor;
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
