package com.example.dlv4119.datetransferwithftp2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    /** 進捗ダイアログ */
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // タスク
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            /**
             * 準備
             */
            @Override
            protected void onPreExecute() {

                // 進捗ダイアログを開始
                Main.this.progressDialog = new ProgressDialog(Main.this);
                Main.this.progressDialog.setMessage("Now Loading ...");
                Main.this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                Main.this.progressDialog.setCancelable(true);
                Main.this.progressDialog.show();
            }

            /**
             * 実行
             */
            @Override
            protected String doInBackground(Void... params) {

                FTPClient ftp = null;
                FileInputStream fis = null;
                FileOutputStream fos = null;

                try {

                    ftp = new FTPClient();

                    // エンコーディング
                    ftp.setControlEncoding("UTF8");

                    // 接続前タイムアウト：10秒
                    ftp.setDefaultTimeout(10000);
                    // 接続
                    ftp.connect("www13.plala.or.jp", 21);

                    // 接続エラーの場合
                    if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {

                        return "サーバーに接続できません";
                    }

                    // 接続後タイムアウト：10秒
                    ftp.setSoTimeout(10000);

                    // ログイン
                    if (!ftp.login("id", "password")) {

                        return "サーバーの認証に失敗しました";
                    }

                    // ファイル種別：アスキーモード
                    ftp.setFileType(FTP.ASCII_FILE_TYPE);
                    // PASVモード
                    ftp.enterLocalPassiveMode();
                    // タイムアウト：10秒
                    ftp.setDataTimeout(10000);

                    // 受信元のディレクトリを作成
                    String path = Environment.getExternalStorageDirectory().getPath() + "/SAMPLE/";
                    new File(path).mkdir();

                    // 受信
                    fos = new FileOutputStream(path + "hoge2.txt");
                    if (!ftp.retrieveFile("/TEST/hoge1.txt", fos)) {

                        return "ファイルの受信に失敗しました";
                    }

                    // 送信
                    fis = new FileInputStream(path + "hoge2.txt");
                    if (!ftp.storeFile("/TEST/hoge3.txt", fis)) {

                        return "ファイルの送信に失敗しました";
                    }

                } catch (SocketException e) {

                    return "FTP通信に失敗しました（１）";

                } catch (IOException e) {

                    return "FTP通信に失敗しました（２）";

                } finally {

                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                        }
                    }

                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                        }
                    }

                    if (ftp != null) {
                        try {
                            // ログアウト
                            ftp.logout();
                        } catch (IOException e) {
                        }
                        try {
                            // 切断
                            ftp.disconnect();
                        } catch (IOException e) {
                        }
                    }
                }

                return "送受信に成功しました";
            }

            /**
             * 完了
             */
            @Override
            protected void onPostExecute(String param) {

                if (Main.this.progressDialog.isShowing()) {

                    // 進捗ダイアログを終了
                    Main.this.progressDialog.dismiss();
                }

                Toast.makeText(Main.this, param, Toast.LENGTH_LONG).show();
            }
        };

        // タスクを実行
        task.execute();
    }
}
