package com.velocitycubed.zohosdk;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;

import com.zoho.commons.InitConfig;
import com.zoho.commons.LauncherModes;
import com.zoho.commons.LauncherProperties;
import com.zoho.commons.OnInitCompleteListener;
import com.zoho.livechat.android.SIQDepartment;
import com.zoho.livechat.android.VisitorChat;
import com.zoho.livechat.android.constants.ConversationType;
import com.zoho.livechat.android.listeners.ConversationListener;
import com.zoho.livechat.android.listeners.DepartmentListener;
import com.zoho.livechat.android.listeners.InitListener;
import com.zoho.livechat.android.listeners.SalesIQChatListener;
import com.zoho.salesiqembed.ZohoSalesIQ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ZohoSDK {

    private static final String EVENT_TOKEN_ChatViewOpen = "ZOHO_ChatViewOpen";
    private static final String EVENT_TOKEN_ChatViewClose = "ZOHO_ChatViewClose";
    private static final String EVENT_TOKEN_ChatOpened = "ZOHO_ChatOpened";
    private static final String EVENT_TOKEN_ChatClosed = "ZOHO_ChatClosed";
    private static final String EVENT_TOKEN_ChatAttended = "ZOHO_ChatAttended";
    private static final String EVENT_TOKEN_ChatMissed = "ZOHO_ChatMissed";
    private static final String EVENT_TOKEN_ChatReOpened = "ZOHO_ChatReOpened";
    private static final String EVENT_TOKEN_Rating = "ZOHO_Rating";
    private static final String EVENT_TOKEN_Feedback = "ZOHO_Feedback";
    private static final String EVENT_TOKEN_QueuePositionChange = "ZOHO_QueuePositionChange";

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ThreadLocal<Activity> activity = new ThreadLocal<>();

    private final List<ZohoEventListener> chatListeners = new ArrayList<>();

    public void addChatViewOpenListener(ZohoEventListener listener) {
        chatListeners.add(listener);
    }

    public void removeChatViewOpenListener(ZohoEventListener listener) {
        chatListeners.remove(listener);
    }

    public void initialize(Application application, Activity activity, String appKey, String accessKey) {
        this.activity.set(activity);
        InitConfig initConfig = new InitConfig();

        if (!ZohoSalesIQ.isSDKEnabled()) {
            ZohoSalesIQ.init(application, appKey, accessKey, activity, new OnInitCompleteListener() {
                @Override
                public void onInitComplete() {
                }

                @Override
                public void onInitError() {
                }
            }, new InitListener() {
                @Override
                public void onInitSuccess() {
                    LauncherProperties launcherProperties = new LauncherProperties(LauncherModes.FLOATING);
                    ZohoSalesIQ.setLauncherProperties(launcherProperties);

                    showZohoLauncher();

                    ZohoSalesIQ.Chat.setListener(new SalesIQChatListener() {
                        @Override
                        public void handleChatViewOpen(String s) {
                            mainHandler.post(() -> {
                                JSONObject data = new JSONObject();
                                try {
                                    data.put("chatId", s);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                for (ZohoEventListener listener : chatListeners) {
                                    listener.onEvent(EVENT_TOKEN_ChatViewOpen, data);
                                }
                            });
                        }

                        @Override
                        public void handleChatViewClose(String s) {
                            mainHandler.post(() -> {
                                JSONObject data = new JSONObject();
                                try {
                                    data.put("chatId", s);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                for (ZohoEventListener listener : chatListeners) {
                                    listener.onEvent(EVENT_TOKEN_ChatViewClose, data);
                                }

                                showZohoLauncher();
                            });
                        }

                        @Override
                        public void handleChatOpened(VisitorChat visitorChat) {
                            mainHandler.post(() -> {
                                JSONObject data = parseVisitorChatToJSONObject(visitorChat);

                                for (ZohoEventListener listener : chatListeners) {
                                    listener.onEvent(EVENT_TOKEN_ChatOpened ,data);
                                }
                            });
                        }

                        @Override
                        public void handleChatClosed(VisitorChat visitorChat) {
                            mainHandler.post(() -> {
                                JSONObject data = parseVisitorChatToJSONObject(visitorChat);

                                for (ZohoEventListener listener : chatListeners) {
                                    listener.onEvent(EVENT_TOKEN_ChatClosed, data);
                                }

                                showZohoLauncher();
                            });
                        }

                        @Override
                        public void handleChatAttended(VisitorChat visitorChat) {
                            mainHandler.post(() -> {
                                JSONObject data = parseVisitorChatToJSONObject(visitorChat);

                                for (ZohoEventListener listener : chatListeners) {
                                    listener.onEvent(EVENT_TOKEN_ChatAttended, data);
                                }
                            });
                        }

                        @Override
                        public void handleChatMissed(VisitorChat visitorChat) {
                            mainHandler.post(() -> {
                                JSONObject data = parseVisitorChatToJSONObject(visitorChat);

                                for (ZohoEventListener listener : chatListeners) {
                                    listener.onEvent(EVENT_TOKEN_ChatMissed,data);
                                }
                            });
                        }

                        @Override
                        public void handleChatReOpened(VisitorChat visitorChat) {
                            mainHandler.post(() -> {
                                JSONObject data = parseVisitorChatToJSONObject(visitorChat);

                                for (ZohoEventListener listener : chatListeners) {
                                    listener.onEvent(EVENT_TOKEN_ChatReOpened, data);
                                }
                            });
                        }

                        @Override
                        public void handleRating(VisitorChat visitorChat) {
                            mainHandler.post(() -> {
                                JSONObject data = parseVisitorChatToJSONObject(visitorChat);

                                for (ZohoEventListener listener : chatListeners) {
                                    listener.onEvent(EVENT_TOKEN_Rating, data);
                                }
                            });
                        }

                        @Override
                        public void handleFeedback(VisitorChat visitorChat) {
                            mainHandler.post(() -> {
                                JSONObject data = parseVisitorChatToJSONObject(visitorChat);

                                for (ZohoEventListener listener : chatListeners) {
                                    listener.onEvent(EVENT_TOKEN_Feedback, data);
                                }
                            });
                        }

                        @Override
                        public void handleQueuePositionChange(VisitorChat visitorChat) {
                            mainHandler.post(() -> {
                                JSONObject data = parseVisitorChatToJSONObject(visitorChat);

                                for (ZohoEventListener listener : chatListeners) {
                                    listener.onEvent(EVENT_TOKEN_QueuePositionChange, data);
                                }
                            });
                            // Call showZohoLauncher
                            showZohoLauncher();
                        }
                    });
                }

                @Override
                public void onInitError(int i, String s) {
                }
            }, initConfig);
        }
    }

    public void open() {
        ZohoSalesIQ.Chat.getList(ConversationType.OPEN, new ConversationListener() {
            @Override
            public void onSuccess(ArrayList<VisitorChat> chats) {
                if (removeWaitingMissed(chats).size() > 0) {
                    ZohoSalesIQ.Chat.open();
                } else {
                    ZohoSalesIQ.Chat.openNewChat();
                }
            }

            @Override
            public void onFailure(int code, String message) {
                // Handle failure
            }
        });
    }

    public void setDepartment(String departmentName) {
        String escapedDepartmentName = Html.escapeHtml(departmentName);
        ZohoSalesIQ.Chat.setDepartment(escapedDepartmentName);
    }

    public void setLanguage(String languageCode) {
        ZohoSalesIQ.Chat.setLanguage(languageCode);
    }

    public void setAdditionalInformation(JSONObject additionalInformation) {
        Iterator<String> keys = additionalInformation.keys();

        while (keys.hasNext()) {
            String currentDynamicKey = keys.next();
            String currentDynamicValue = additionalInformation.optString(currentDynamicKey);
            ZohoSalesIQ.Visitor.addInfo(currentDynamicKey, currentDynamicValue);
        }
    }

    public void setPageTitle(String title) {
        ZohoSalesIQ.Tracking.setPageTitle(title);
    }

    public void getDepartments(final ZohoCallback callback) {
        ZohoSalesIQ.Chat.getDepartments(new DepartmentListener() {
            @Override
            public void onSuccess(ArrayList<SIQDepartment> arrayList) {
                JSONArray jsArray = new JSONArray();
                for (SIQDepartment department : arrayList) {
                    JSONObject jsObjectDepartment = new JSONObject();
                    try {
                        jsObjectDepartment.put("available", department.available);
                        jsObjectDepartment.put("id", department.id);
                        jsObjectDepartment.put("name", department.name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsArray.put(jsObjectDepartment);
                }
                JSONObject ret = new JSONObject();
                try {
                    ret.put("departments", jsArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(ret);
            }

            @Override
            public void onFailure(int i, String s) {
                // Handle failure
            }
        });
    }

    public void setQuestion(String question) {
        ZohoSalesIQ.Visitor.setQuestion(question);
    }

    private void showZohoLauncher() {
        ZohoSalesIQ.Chat.getList(ConversationType.OPEN, new ConversationListener() {
            @Override
            public void onSuccess(ArrayList<VisitorChat> chats) {
                ZohoSalesIQ.showLauncher(removeWaitingMissed(chats).size() > 0);
            }

            @Override
            public void onFailure(int code, String message) {
                // Handle failure
            }
        });
    }

    private JSONObject parseVisitorChatToJSONObject(VisitorChat visitorChat) {
        JSONObject data = new JSONObject();
        try {
            data.put("chatId", visitorChat.getChatID());
            data.put("question", visitorChat.getQuestion());
            data.put("attenderName", visitorChat.getAttenderName());
            data.put("attenderEmail", visitorChat.getAttenderEmail());
            data.put("attenderId", visitorChat.getAttenderId());

            data.put("isBotAttender", visitorChat.isBotAttender());
            data.put("departmentName", visitorChat.getDepartmentName());
            data.put("chatStatus", visitorChat.getChatStatus());
            data.put("unreadCount", visitorChat.getUnreadCount());
            data.put("feedback", visitorChat.getFeedbackMessage());

            data.put("rating", visitorChat.getRating());
            data.put("lastMessage", visitorChat.getLastMessage());
            data.put("lastMessageTime", visitorChat.getLastMessageTime());
            data.put("lastMessageSender", visitorChat.getLastMessageSender());
            data.put("queuePosition", visitorChat.getQueuePosition());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }


    private ArrayList<VisitorChat> removeWaitingMissed(List<VisitorChat> chats) {
        ListIterator<VisitorChat> data = chats.listIterator();
        ArrayList<VisitorChat> filteredChats = new ArrayList<>();
        while (data.hasNext()) {
            VisitorChat currentChat = data.next();
            if (currentChat.getAttenderId() != null && currentChat.getAttenderId().compareTo("") == 0 && currentChat.getChatStatus().compareTo("WAITING") == 0) {
                continue;
            }
            filteredChats.add(currentChat);
        }
        return filteredChats;
    }

    public interface ZohoCallback {
        void onSuccess(JSONObject result);

        void onFailure(String errorMessage);
    }
}
