package indi.nevd.dingdinghunter;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by nevd on 8/4/2017.
 */

public class DingDingMsg {

    public static String MSG_RECALL = "Msg has been recalled.";

    public static void setMsgText(Object msg, String newText) {
        Object innerContent = XposedHelpers.getObjectField(msg, "mMessageContent");
        int type = (int) XposedHelpers.callMethod(innerContent, "type");
        if (type == MessageContentType.TEXT) {
            XposedHelpers.callMethod(innerContent, "setText", newText);
        }
    }

    public static int getMsgType(Object msg){
        Object innerContent = XposedHelpers.getObjectField(msg, "mMessageContent");
        return (int) XposedHelpers.callMethod(innerContent, "type");
    }

    public static String getMsgContent(Object msg, int type) {
        String content = null;

        Object innerContent = XposedHelpers.getObjectField(msg, "mMessageContent");

        if (type == MessageContentType.TEXT) {
            if (type == MessageContentType.TEXT) {
                content = (String) XposedHelpers.callMethod(innerContent, "text");
            }else if(type == MessageContentType.IMAGE){
                content = (String)XposedHelpers.callMethod(innerContent, "url");          // http://static.dingtalk.com/media/xxx.jpg
            }
        }
        return content;
    }

    public static String getMsgSenderName(Object msg) {
        return (String) XposedHelpers.getObjectField(msg, "senderName");
    }

    public static long getMsgId(Object msg){
        return XposedHelpers.getLongField(msg, "mMid");
    }

    public static long getMsgSenderId(Object msg){
        return XposedHelpers.getLongField(msg, "mSenderId");
    }

    public static String getConversationId(Object msg){
        Object conversation = XposedHelpers.getObjectField(msg, "mConversation");
        return (String)XposedHelpers.callMethod(conversation, "conversationId");
    }

    public static long getMsgTime(Object msg){
        return XposedHelpers.getLongField(msg, "mCreatedAt");
    }


    public static class MessageContentType
    {
        public static final int AUDIO = 3;
        public static final int AUTH_AUDIO = 252;
        public static final int AUTH_COMMON_VIDEO = 254;
        public static final int AUTH_IMAGE = 251;
        public static final int AUTH_VIDEO = 253;
        public static final int COMMENT = 1400;
        public static final int COMMON_VIDEO = 202;
        public static final int DING_CARD = 1600;
        public static final int ENCRYPTED_SPACE_FILE = 502;
        public static final int ENCRYPT_AUDIO = 204;
        public static final int ENCRYPT_COMMON_VIDEO = 206;
        public static final int ENCRYPT_IMAGE = 203;
        public static final int ENCRYPT_VIDEO = 205;
        public static final int FILE = 4;
        public static final int FORWARD_COMBINE = 1500;
        public static final int GEO = 104;
        public static final int IMAGE = 2;
        public static final int LINKED = 102;
        public static final int PERSONAL_SPACE_FILE = 501;
        public static final int ROBOT_MARKDOWN = 1200;
        public static final int ROBOT_MARKDOWN_EX = 1201;
        public static final int SPACE_FILE = 500;
        public static final int TEXT = 1;
        public static final int UNKNOWN = -1;
        public static final int VIDEO = 103;
    }

}
