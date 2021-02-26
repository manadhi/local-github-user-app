package com.udhipe.githubuserex.widget

import android.content.Intent
import android.widget.RemoteViewsService

class FavoriteUserService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return FavoriteUserRemoteViewsFactory(this.applicationContext)
    }
}