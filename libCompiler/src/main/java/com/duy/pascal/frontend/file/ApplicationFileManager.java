/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.frontend.file;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.duy.pascal.frontend.setting.PascalPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * File Manager
 * Created by Duy on 10-Feb-17.
 */

public class ApplicationFileManager {
    /**
     * storage path for saveFile code
     */
    private static final String EXTERNAL_DIR_CODE = Environment.getExternalStorageDirectory().getPath()
            + "/PascalCompiler/";
    private final String TAG = ApplicationFileManager.class.getSimpleName();
    private final String FILE_TEMP_NAME = "tmp.pas";
    private int mode = SAVE_MODE.EXTERNAL;
    private Context context;
    private Database mDatabase;
    private PascalPreferences mPascalPreferences;

    public ApplicationFileManager(Context context) {
        this.context = context;
        mDatabase = new Database(context);
        mPascalPreferences = new PascalPreferences(context);
    }

    /**
     * @return path of application
     */
    public static String getApplicationPath() {
        File file = new File(EXTERNAL_DIR_CODE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return EXTERNAL_DIR_CODE;
    }

    public static StringBuilder streamToString(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                result.append(mLine).append("\n");
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return result;
    }

    /**
     * get path from uri
     *
     * @param context
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    public String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getLineError(int position, String mFileName) {
        File file = new File(mFileName);
        if (file.canRead()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                int index = 0;
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    index += line.length();
                    if (index >= position) break;
                }
                if (index >= position) {
                    return line;
                } else
                    return "";
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return "";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }
        return "";
    }

    public boolean createDirectory() {
        File dir = new File(EXTERNAL_DIR_CODE);
        if (dir.exists()) {
            if (dir.isDirectory()) return true;
            if (!dir.delete()) return false;
        }
        return dir.mkdir();
    }

    public StringBuilder fileToString(String path) {
        File file = new File(path);
        if (file.canRead()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                return stringBuilder;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
        return new StringBuilder();
    }

    public String fileToString(File file) {
        if (file.canRead()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line + "\n";
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }
        return "";
    }

    /**
     * get all file in folder
     *
     * @param path - folder path
     * @return list file
     */
    public ArrayList<File> getListFile(String path) {
        ArrayList<File> list = new ArrayList<>();
        File directory = new File(path);
        File[] files = directory.listFiles();
        try {
            if (files == null) return list;
            for (File file : files) {
                if (file.isFile()) list.add(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<File> listFile = mDatabase.getListFile();
        list.addAll(listFile);
        return list;
    }

    /**
     * get all file with filter
     *
     * @param path      - folder path
     * @param extension - extension of file
     * @return - list file
     */
    public ArrayList<File> getListFile(String path, String extension) {
        ArrayList<File> list = new ArrayList<>();
        File f = new File(path);
        File[] files = f.listFiles();
        for (File file : files) {
            int ind = file.getPath().lastIndexOf('.');
            if (ind > 0) {
                String tmp = file.getPath().substring(ind + 1);// this is the extension
                if (tmp.equals(extension)) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * saveFile current project
     *
     * @param file
     */
    public void saveFile(File file, String text) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (text.length() > 0) out.write(text.getBytes());
            out.close();
        } catch (Exception ignored) {
        }
    }

    /**
     * saveFile file
     *
     * @param filePath - name of file
     * @param text     - content of file
     */
    public boolean saveFile(@NonNull String filePath, String text) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            if (text.length() > 0) writer.write(text);
            writer.close();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public String loadInMode(File file) {
        String res = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

            String line;
            while ((line = in.readLine()) != null) {
                res += line;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * read content of file
     *
     * @param filename - file
     * @return - string
     */
    public String loadInMode(String filename) {
        String res = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(getCurrentPath() + filename)), "UTF8"));

            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * create new file in dir of application
     *
     * @param fileName
     * @return - path of file
     */
    public String createNewFileInMode(String fileName) {
        String name = getCurrentPath() + fileName;
        File file = new File(name);
//        Log.i(TAG, "createNewFileInMode: " + name);
        try {
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            return file.getPath();
        } catch (IOException e) {
//            Log.e("", "Could not create file.", e);
            return "";
        }
    }

    /**
     * create new file
     *
     * @param path path to file
     * @return file path
     */
    public String createNewFile(String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            return path;
        } catch (IOException e) {
            return "";
        }
    }

    public boolean deleteFile(File file) {
        try {
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    public String removeFile(File file) {
        try {
            file.delete();
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * set content of file pas for generate, put it in internal storage
     *
     * @param content - Content of file, string
     */
    public String setContentFileTemp(String content) {
        String name = getCurrentPath(SAVE_MODE.INTERNAL) + FILE_TEMP_NAME;
//        Dlog.d(TAG, "setContentFileTemp: " + name);
//        Dlog.d(TAG, "setContentFileTemp: " + content);
        File file = new File(name);
        FileOutputStream outputStream;
        try {
            if (!file.exists()) {
                createNewFile(name);
            }
            outputStream = new FileOutputStream(new File(name));
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    /**
     * return file pas for generate program
     *
     * @return - pascal file
     */
    public File getTempFile() {
        String name = getCurrentPath(SAVE_MODE.INTERNAL) + File.separatorChar + FILE_TEMP_NAME;
//        Dlog.d(TAG, "getTempFile: " + name);
        File file = new File(name);
        if (!file.exists()) {
            createNewFileInMode(name);
        }
        return file;
    }

    public String getCurrentPath() {
        if (mode == SAVE_MODE.INTERNAL) {
            return context.getFilesDir().getPath() + File.separatorChar;
        } else {
            return Environment.getExternalStorageDirectory().getPath() + "/PascalCompiler/";
        }
    }

    private String getCurrentPath(int mode) {
        if (mode == SAVE_MODE.INTERNAL) {
            return context.getFilesDir().getPath() + File.separatorChar;
        } else {
            return Environment.getExternalStorageDirectory().getPath() + "/PascalCompiler/";
        }
    }

    public void addNewPath(String path) {
        mDatabase.addNewFile(new File(path));
    }

    /**
     * set working file path
     *
     * @param path
     */
    public void setWorkingFilePath(String path) {
        mPascalPreferences.put(PascalPreferences.FILE_PATH, path);
    }

    public void removeTabFile(String path) {
        mDatabase.removeFile(path);
    }

    public String createRandomFile() {
        String filePath;
        filePath = getApplicationPath() + Integer.toHexString((int) System.currentTimeMillis())
                + ".pas";
        return createNewFile(filePath);
    }

    /**
     * copy data from in to inType
     */
    public void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();

        out.flush();
        out.close();

    }

    /**
     * copy data from file in to file inType
     */
    public void copy(String pathIn, String pathOut) throws IOException {
        InputStream in = new FileInputStream(new File(pathIn));
        OutputStream out = new FileOutputStream(new File(pathOut));
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();

        out.flush();
        out.close();

    }

    public static class SAVE_MODE {
        static final int INTERNAL = 1;
        static final int EXTERNAL = 2;
    }
}
