package com.example.mycommon.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by yaos on 2016/6/16 0016.
 */
public class FileUtils {
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    /**
     * 保存用户信息
     * @param context
     * @return
     */
    public static String configPath(Context context) {
        return context.getCacheDir().getPath() + '/' + "person";
    }

    /**
     * 保存首页缓存数据
     * @param context
     * @return
     */
    public static String homePath(Context context) {
        return context.getCacheDir().getPath() + '/' + "hone";
    }


    /**
     * 保存
     * @param context
     * @return
     */
    public static String publicPath(Context context) {
        return context.getCacheDir().getPath() + '/' + "public";
    }

    //获取SD卡存储路径
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath
     *            文件路径
     * @param sizeType
     *            获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize, sizeType);
    }

    public static File createTmpFile(Context context) throws IOException{
        File dir = null;
        if(TextUtils.equals(Environment.getExternalStorageState(), MEDIA_MOUNTED)) {
            dir = new File(context.getCacheDir().getPath());
            if (!dir.exists()) {
                dir =  new File(context.getCacheDir().getPath());
                if (!dir.exists()) {
                    dir = getCacheDirectory(context, true);
                }
            }
        }else{
            dir = getCacheDirectory(context, true);
        }
        return File.createTempFile(JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, dir);
    }

    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }


    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath
     *            文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize);
    }

    public static String saveBitmap(Bitmap bitmap, String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }

        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG,80, fOut);
            fOut.flush();

            return path;
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }
    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }
    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double formatFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
    /**
     * 根据文件后缀名获得对应的MIME类型。
     * @param file
     */
    public static String getMIMEType(File file) {
        return getMIMEType(file.getName());
    }

    public static String getMIMEType(String fileName) {
        String type="*/*";
        String fName = fileName;
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
        /* 获取文件的后缀名*/
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="") {
            return type;
        }
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if(end.equals(MIME_MapTable[i][0])) {
                type = MIME_MapTable[i][1];
            }
        }
        return type;
    }


    /**
     * 根据图片Uri得到一张图片的路径
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if ( null == uri ) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 通过uri获取文件路径
     *
     * @param mUri
     * @return
     */
    public static String getFilePath(Context context, Uri mUri) {
        try {
            if ("file".equals(mUri.getScheme())) {
                return mUri.getPath();
            } else {
                return getFilePathByUri(context,mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    // 获取文件路径通过url
    private static String getFilePathByUri(Context context, Uri mUri) throws FileNotFoundException {
        Cursor cursor = context.getContentResolver()
                .query(mUri, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    private static final String[][] MIME_MapTable={//{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-JavaScript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    public static File scal(String path){
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize = 400 * 424;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }else{
                File tempFile = outputFile;
                outputFile = new File(createImageFile().getPath());
               copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile;

    }

    public static Uri createImageFile(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp +".png";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image =getFilePath(storageDir.getAbsolutePath(),imageFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }
    public static void copyFileUsingFileChannels(File source, File dest){
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static File getFilePath(String filePath,
                                       String fileName) {
                 File file = null;
                 makeRootDirectory(filePath);
                try {
                         file = new File(filePath + fileName);
                     } catch (Exception e) {
            // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                 return file;
             }

    public static void makeRootDirectory(String filePath) {
        File file = null;
                try {
            file = new File(filePath);
                        if (!file.exists()) {
                                file.mkdir();
                           }
                     } catch (Exception e) {

                     }
            }



              /**
  79      * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
  80      */

              @SuppressLint("NewApi")
      public static String getPath(final Context context, final Uri uri) {

                 final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

                 // DocumentProvider
                if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                         // ExternalStorageProvider
                         if (isExternalStorageDocument(uri)) {
                                 final String docId = DocumentsContract.getDocumentId(uri);
                                 final String[] split = docId.split(":");
                                 final String type = split[0];

                                 if ("primary".equalsIgnoreCase(type)) {
                                         return Environment.getExternalStorageDirectory() + "/" + split[1];
                                     }
                             }
                         // DownloadsProvider
                         else if (isDownloadsDocument(uri)) {

                                 final String id = DocumentsContract.getDocumentId(uri);
                                 final Uri contentUri = ContentUris.withAppendedId(
                                                 Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                                 return getDataColumn(context, contentUri, null, null);
                             }
                         // MediaProvider
                         else if (isMediaDocument(uri)) {
                                 final String docId = DocumentsContract.getDocumentId(uri);
                                 final String[] split = docId.split(":");
                                 final String type = split[0];

                                 Uri contentUri = null;
                                 if ("image".equals(type)) {
                                         contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                                     } else if ("video".equals(type)) {
                                         contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                     } else if ("audio".equals(type)) {
                                         contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                                     }

                                 final String selection = "_id=?";
                                 final String[] selectionArgs = new String[]{split[1]};

                                 return getDataColumn(context, contentUri, selection, selectionArgs);
                             }
                     }
                 // MediaStore (and general)
                 else if ("content".equalsIgnoreCase(uri.getScheme())) {
                         return getDataColumn(context, uri, null, null);
                     }
                 // File
                 else if ("file".equalsIgnoreCase(uri.getScheme())) {
                         return uri.getPath();
                     }
                 return null;
             }

             /**
 140      * Get the value of the data column for this Uri. This is useful for
 141      * MediaStore Uris, and other file-based ContentProviders.
 142      *
 143      * @param context       The context.
 144      * @param uri           The Uri to query.
 145      * @param selection     (Optional) Filter used in the query.
 146      * @param selectionArgs (Optional) Selection arguments used in the query.
 147      * @return The value of the _data column, which is typically a file path.
 148      */
             public static String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

                 Cursor cursor = null;
                 final String column = "_data";
                 final String[] projection = {column};

                 try {
                         cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                                         null);
                         if (cursor != null && cursor.moveToFirst()) {
                                 final int column_index = cursor.getColumnIndexOrThrow(column);
                                 return cursor.getString(column_index);
                             }
                     } finally {
                         if (cursor != null) {
                             cursor.close();
                         }
                     }
                 return null;
             }

             /**
       * @param uri The Uri to check.
       * @return Whether the Uri authority is ExternalStorageProvider.
       */
             public static boolean isExternalStorageDocument(Uri uri) {
                 return "com.android.externalstorage.documents".equals(uri.getAuthority());
             }

             /**
 179      * @param uri The Uri to check.
 180      * @return Whether the Uri authority is DownloadsProvider.
 181      */
             public static boolean isDownloadsDocument(Uri uri) {
                 return "com.android.providers.downloads.documents".equals(uri.getAuthority());
             }
             /**
 187      * @param uri The Uri to check.
 188      * @return Whether the Uri authority is MediaProvider.
 189      */
             public static boolean isMediaDocument(Uri uri) {
                 return "com.android.providers.media.documents".equals(uri.getAuthority());
            }

    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }
}
