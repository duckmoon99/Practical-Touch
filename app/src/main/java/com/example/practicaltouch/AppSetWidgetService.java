package com.example.practicaltouch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;

import com.example.practicaltouch.database.AppSet;
import com.example.practicaltouch.database.AppSetDAO;
import com.example.practicaltouch.database.AppSetDatabase;

import java.util.ArrayList;
import java.util.List;

public class AppSetWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppSetWidgetItemFactory(getApplicationContext(), intent);
    }

    class AppSetWidgetItemFactory implements RemoteViewsFactory {
        private Context context;
        private AppSetDAO appSetDAO;
        private List<AppSet> allAppSets;


        AppSetWidgetItemFactory(Context context, Intent intent) {
            this.context = context;
            AppSetDatabase database = AppSetDatabase.getInstance(context);
            this.appSetDAO = database.appSetDAO();
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            allAppSets = appSetDAO.getList();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return allAppSets.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            AppSet curr = allAppSets.get(position);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appset_item_widget);
            views.setTextViewText(R.id.appset_name_widget, curr.getName());

            final ArrayList<String> listOfAppIds = new ArrayList<>(curr.getAppIdsList().getListOfAppIds());
            int index = 0;
            int count = 0;
            while (index < listOfAppIds.size() && count < 3) {
                try {
                    Bitmap icon = getBitmapFromDrawable(context.getPackageManager().getApplicationIcon(listOfAppIds.get(index)));
                    switch (count) {
                        case 0:
                            views.setImageViewBitmap(R.id.appIcon1, icon);
                            count++;
                            index++;
                            break;
                        case 1:
                            views.setImageViewBitmap(R.id.appIcon2, icon);
                            count++;
                            index++;
                            break;
                        case 2:
                            views.setImageViewBitmap(R.id.appIcon3, icon);
                            count++;
                            index++;
                            break;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    index++;
                }
            }

            Intent fillIntent = new Intent();
            fillIntent.putExtra(AppSetWidgetProvider.EXTRA_ITEM_POSITION, position);
            fillIntent.putStringArrayListExtra(AppSetWidgetProvider.EXTRA_ITEM_APPSET,
                    listOfAppIds);
            views.setOnClickFillInIntent(R.id.appset_launchbutton_widget, fillIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @NonNull
        private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
            final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bmp);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bmp;
        }
    }
}
