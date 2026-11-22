package com.mojang.realmsclient;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;

interface RealmsCall<T> {
  T request(RealmsClient paramRealmsClient) throws RealmsServiceException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen$RealmsCall.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */