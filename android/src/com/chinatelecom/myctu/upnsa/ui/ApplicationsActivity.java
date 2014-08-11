package com.chinatelecom.myctu.upnsa.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.chinatelecom.myctu.upnsa.R;
import com.chinatelecom.myctu.upnsa.core.utils.BitmapUtils;
import com.chinatelecom.myctu.upnsa.manager.UpnsAgentManager;
import com.chinatelecom.myctu.upnsa.model.Application;

/**
 * 应用列表
 * <p/>
 * User: snowway
 * Date: 11/18/13
 * Time: 4:27 PM
 */
public class ApplicationsActivity extends AbstractUpnsAgentActivity {

    private UpnsAgentManager upnsAgentManager = factory.getUpnsManager();

    private ListView applicationsListView;

    public final int TAG_VIEW_HOLDER = -1;

    public final int TAG_APPLICATION = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applications_activity);
        setTitle("已注册的应用列表");
        applicationsListView = (ListView) findViewById(R.id.applicationsListView);
        applicationsListView.setAdapter(new ApplicationsCursorAdapter(upnsAgentManager.findRegisteredApplications()));
    }

    /**
     * ListView ViewHolder
     */
    public static final class ViewHolder {
        public ImageView applicationIconView;
        public TextView titleView;
        public TextView contentView;
        public TextView applicationIdView;
        public View view;
    }

    /**
     * MessageCursorAdapter
     */
    private class ApplicationsCursorAdapter extends CursorAdapter {


        public ApplicationsCursorAdapter(Cursor cursor) {
            super(getContext(), cursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.application_item, null, false);
            ViewHolder holder = new ViewHolder();
            holder.view = view;
            holder.applicationIconView = (ImageView) view.findViewById(R.id.applicationIconView);
            holder.titleView = (TextView) view.findViewById(R.id.titleView);
            holder.contentView = (TextView) view.findViewById(R.id.contentView);
            holder.applicationIdView = (TextView) view.findViewById(R.id.applicationIdView);
            view.setTag(TAG_VIEW_HOLDER, holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag(TAG_VIEW_HOLDER);
            Application application = upnsAgentManager.createApplication(cursor);
            view.setTag(TAG_APPLICATION, application);
            holder.applicationIconView.setImageBitmap(BitmapUtils.fromByteArray(application.getNotificationIcon()));
            holder.titleView.setText(application.getApplicationName());
            holder.contentView.setText(application.getNotificationIntent());
            holder.applicationIdView.setText(application.getApplicationId());
        }
    }
}

