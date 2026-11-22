package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.gui.task.DataFetcher;

interface DataFetcherConfiguration {
  DataFetcher.Subscription initDataFetcher(RealmsDataFetcher paramRealmsDataFetcher);
  
  boolean showOldNotifications();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsNotificationsScreen$DataFetcherConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */