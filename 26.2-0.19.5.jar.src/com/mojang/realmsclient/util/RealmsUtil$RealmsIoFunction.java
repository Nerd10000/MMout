package com.mojang.realmsclient.util;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;

@FunctionalInterface
public interface RealmsIoFunction<T> {
  T apply(RealmsClient paramRealmsClient) throws RealmsServiceException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\RealmsUtil$RealmsIoFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */