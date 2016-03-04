package com.example.dlv4119.datatransferwithftp;

/**
 * Created by dlv4119 on 2016/01/08.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.IOException;

public class DataSend extends AsyncTask implements DialogInterface.OnCancelListener {

    private Context myContext;
    private ProgressDialog myProgressDialog;
    private FTPClient myFTPClient;

    public DataSend(Context context) {
        myContext = context;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    // 非同期処理開始
    @Override
    protected void onPreExecute() {
        myProgressDialog = new ProgressDialog(myContext);
        myProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        myProgressDialog.setCancelable(true);
        myProgressDialog.setOnCancelListener(this);
        myProgressDialog.setTitle("データ送信");
        myProgressDialog.setMessage("アップロード中");
        myProgressDialog.show();
    }

    // 非同期処理
    //@Override
    protected String doInBackground(String... params) {
        String remoteserver = params[0];                 //FTPサーバーアドレス
        int remoteport = Integer.parseInt(params[1]);    //FTPサーバーポート
        String remotefile = params[2];                   //サーバーフォルダ
        String userid = params[3];                       //ログインユーザID
        String passwd = params[4];                       //ログインパスワード
        boolean passive = Boolean.valueOf(params[5]);    //パッシブモード使用

        //ＦＴＰファイル送信
        FTP ftp = new FTP(myContext);
        String result = ftp.putData(remoteserver, remoteport, userid, passwd, passive, remotefile);
        ftp = null;

        return result;
    }

    // 非同期処理終了
    // @Override
    protected void onPostExecute(String result) {
        myProgressDialog.dismiss();
        if (result == null) {
            Toast.makeText(myContext, "データ送信終了", Toast.LENGTH_SHORT ).show();
        } else {
            Toast.makeText(myContext, "データ送信 エラー発生", Toast.LENGTH_SHORT ).show();
        }
    }

    // キャンセル処理
    @Override
    public void onCancel(DialogInterface dialog) {
        this.cancel(true);
    }
    @Override
    protected void onCancelled() {
        try {myFTPClient.abort();} catch(Exception e) {}
        Toast.makeText(myContext, "データ送信 キャンセル", Toast.LENGTH_SHORT ).show();
    }

    // インナークラス　ＦＴＰクライアント commons net使用
    private class FTP extends ContextWrapper {
        public FTP(Context base) {
            super(base);
        }
        private String putData (String remoteserver, int remoteport,
                                String userid, String passwd, boolean passive, String remotefile) {
            int reply = 0;
            boolean isLogin = false;
            myFTPClient = new FTPClient();

            try {
                myFTPClient.setConnectTimeout(5000);
                //接続
                myFTPClient.connect(remoteserver, remoteport);
                reply = myFTPClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    throw new Exception("Connect Status:" + String.valueOf(reply));
                }
                //ログイン
                if (!myFTPClient.login(userid, passwd)) {
                    throw new Exception("Invalid user/password");
                }
                isLogin = true;
                //転送モード
                if (passive) {
                    myFTPClient.enterLocalPassiveMode(); //パッシブモード
                } else {
                    myFTPClient.enterLocalActiveMode();  //アクティブモード
                }
                //ファイル送信
                myFTPClient.setDataTimeout(15000);
                myFTPClient.setSoTimeout(15000);
                FileInputStream fileInputStream = this.openFileInput("送信ファイル名");
                myFTPClient.storeFile(remotefile, fileInputStream);
                reply = myFTPClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    throw new Exception("Send Status:" + String.valueOf(reply));
                }
                fileInputStream.close();
                fileInputStream = null;
                //ログアウト
                myFTPClient.logout();
                isLogin = false;
                //切断
                myFTPClient.disconnect();
            } catch(Exception e) {
                return e.getMessage();
            } finally {
                if (isLogin) {
                    try {myFTPClient.logout();} catch (IOException e) {}
                }
                if (myFTPClient.isConnected()) {
                    try {myFTPClient.disconnect();} catch (IOException e) {}
                }
                myFTPClient = null;
            }
            return null;
        }
    }
}