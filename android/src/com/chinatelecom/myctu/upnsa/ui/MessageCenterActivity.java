/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.chinatelecom.myctu.upnsa.ui;

import android.app.AlertDialog;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.*;
import android.widget.*;
import com.chinatelecom.myctu.upnsa.R;
import com.chinatelecom.myctu.upnsa.core.utils.DateUtils;
import com.chinatelecom.myctu.upnsa.core.utils.Logger;
import com.chinatelecom.myctu.upnsa.core.utils.StringUtils;
import com.chinatelecom.myctu.upnsa.manager.UpnsAgentManager;
import com.chinatelecom.myctu.upnsa.model.Configuration;
import com.chinatelecom.myctu.upnsa.model.Message;
import com.chinatelecom.myctu.upnsa.remote.UpnsAgentApi;
import com.chinatelecom.myctu.upnsa.remote.UpnsAgentListener;
import com.chinatelecom.myctu.upnsa.service.UpnsAgentService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息中心界面
 * <p/>
 * User: snowway
 * Date: 9/9/13
 * Time: 2:49 PM
 */
public class MessageCenterActivity extends AbstractUpnsAgentActivity {

    public final int TAG_VIEW_HOLDER = -1;

    public final int TAG_MESSAGE = -2;

    private UpnsAgentManager upnsAgentManager = factory.getUpnsManager();

    private TextView statusTextView;

    private ListView messageListView;

    private Handler handler = new Handler();

    private UpnsAgentApi upnsAgentApi;

    private static boolean logined = false;

    private static String lastUserId;

    /**
     * service回调接口实现
     */
    private UpnsAgentListener upnsAgentListener = new UpnsAgentListener.Stub() {

        @Override
        public void onConnected(final String userId) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String text = String.format(getResources().getString(R.string.connected), userId);
                    statusTextView.setText(text);
                    logined = true;
                }
            });
        }

        @Override
        public void onDisconnected() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    statusTextView.setText(R.string.disconnected);
                    logined = false;
                }
            });
        }

        @Override
        public void onFinishHistory(String applicationId) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyListViewDataChanged();
                }
            });
        }

        @Override
        public void onMessage(String applicationId, String messageId) throws RemoteException {
            onFinishHistory(applicationId);
        }

        @Override
        public void onFault(final String errorCode, final String errorMessage) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), String.format("发生错误,错误码:%s,错误说明:%s",
                            errorCode, errorMessage), Toast.LENGTH_LONG).show();
                }
            });
        }
    };


    /**
     * 连接后台Service回调
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            upnsAgentApi = UpnsAgentApi.Stub.asInterface(iBinder);
            try {
                String text = null;
                if (upnsAgentApi.isConnected()) {
                    logined = true;
                    text = String.format(getResources().getString(R.string.connected), upnsAgentApi.getCurrentUserId());
                } else {
                    text = getResources().getString(R.string.disconnected);
                }
                statusTextView.setText(text);
                upnsAgentApi.addListener(upnsAgentListener);
            } catch (RemoteException ex) {
                Toast.makeText(getContext(), "远程调用错误:" + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_center_activity);
        setTitle("消息中心");
        if(StringUtils.isBlank(lastUserId)){
            Configuration configuration = factory.getUpnsManager().getConfiguration(null);
            lastUserId = configuration != null ? configuration.getUserId() : null;
        }
        statusTextView = (TextView) findViewById(R.id.statusTextVew);
        messageListView = (ListView) findViewById(R.id.messageListView);
        messageListView.setAdapter(new MessageCursorAdapter(factory.getUpnsManager().findMessages()));
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message message = (Message) view.getTag(TAG_MESSAGE);
                if (message.getExtension() != null) {
                    Toast.makeText(getContext(), message.getExtension().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        Intent intent = new Intent(UpnsAgentService.INTENT_UPNS_AGENT_API);
        startService(intent);
        bindService(intent, serviceConnection, 0);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.getItem(2).setVisible(!logined);
        menu.getItem(3).setVisible(logined);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.message_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.registered_applications:
                showRegisteredApplications();
                break;
            case R.id.message_menu_clear:
                factory.getUpnsManager().clearMessages();
                notifyListViewDataChanged();
                break;
            case R.id.login:
                showLoginDialog();
                break;
            case R.id.disconnect:
                try {
                    upnsAgentApi.disconnect();
                } catch (RemoteException ex) {
                    Logger.error(ex.getMessage(), ex);
                }
        }
        return true;
    }

    //显示登录对话框
    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("用户登录");
        LinearLayout layout = new LinearLayout(getContext());
        final EditText editText = new EditText(getContext());
        editText.setHint("请输入用户Id");
        editText.setText(StringUtils.trim(lastUserId));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = params.rightMargin = 8;
        layout.addView(editText, params);
        builder.setView(layout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    lastUserId = StringUtils.trim(editText.getText().toString());
                    upnsAgentApi.userAuthenticated(lastUserId);
                } catch (RemoteException ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.show();
    }

    private void showRegisteredApplications() {
        startActivity(new Intent(this, ApplicationsActivity.class));
    }

    private void notifyListViewDataChanged() {
        CursorAdapter adapter = (CursorAdapter) messageListView.getAdapter();
        adapter.changeCursor(upnsAgentManager.findMessages());
    }


    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    /**
     * ListView ViewHolder
     */
    public static final class ViewHolder {
        public TextView applicationView;
        public TextView titleView;
        public TextView contentView;
        public TextView receiveTimeView;
        public View view;
    }

    /**
     * MessageCursorAdapter
     */
    private class MessageCursorAdapter extends CursorAdapter {

        public final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yy/MM/dd");

        public final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

        public MessageCursorAdapter(Cursor cursor) {
            super(getContext(), cursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.message_item, null, false);
            ViewHolder holder = new ViewHolder();
            holder.view = view;
            holder.applicationView = (TextView) view.findViewById(R.id.applicationView);
            holder.titleView = (TextView) view.findViewById(R.id.titleView);
            holder.contentView = (TextView) view.findViewById(R.id.contentView);
            holder.receiveTimeView = (TextView) view.findViewById(R.id.receiveTimeView);
            view.setTag(TAG_VIEW_HOLDER, holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag(TAG_VIEW_HOLDER);
            Message message = factory.getUpnsManager().createMessage(cursor);
            view.setTag(TAG_MESSAGE, message);
            holder.applicationView.setText(message.getApplicationId());
            holder.titleView.setText(message.getTitle() + (message.getExtension() == null ? "" : "(x)"));
            holder.contentView.setText(message.getContent());
            Date now = new Date();
            Date createTime = new Date(message.getCreateTime());
            if (DateUtils.isSameDay(now, createTime)) {
                holder.receiveTimeView.setText(TIME_FORMAT.format(createTime));
            } else {
                holder.receiveTimeView.setText(DATE_FORMAT.format(createTime));
            }
        }
    }
}
