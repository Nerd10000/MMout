/*    */ package com.mojang.realmsclient.client;
/*    */ 
/*    */ enum Region {
/* 61 */   US_EAST_1("us-east-1", "ec2.us-east-1.amazonaws.com"),
/* 62 */   US_WEST_2("us-west-2", "ec2.us-west-2.amazonaws.com"),
/* 63 */   US_WEST_1("us-west-1", "ec2.us-west-1.amazonaws.com"),
/* 64 */   EU_WEST_1("eu-west-1", "ec2.eu-west-1.amazonaws.com"),
/* 65 */   AP_SOUTHEAST_1("ap-southeast-1", "ec2.ap-southeast-1.amazonaws.com"),
/* 66 */   AP_SOUTHEAST_2("ap-southeast-2", "ec2.ap-southeast-2.amazonaws.com"),
/* 67 */   AP_NORTHEAST_1("ap-northeast-1", "ec2.ap-northeast-1.amazonaws.com"),
/* 68 */   SA_EAST_1("sa-east-1", "ec2.sa-east-1.amazonaws.com");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final String endpoint;
/*    */   
/*    */   Region(String name, String endpoint) {
/* 71 */     this.name = name;
/* 72 */     this.endpoint = endpoint;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\Ping$Region.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */