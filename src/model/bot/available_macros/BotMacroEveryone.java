package model.bot.available_macros;

import com.google.gson.Gson;
import core.CustomBot;
import http.TelegramSyncHTTPSRequester;
import model.telegram.available_methods.TelegramMethodGetChatAdmins;
import model.telegram.available_types.TelegramChatMembersInfoResponse;
import model.telegram.available_types.TelegramUpdate;

public class BotMacroEveryone extends BotBaseMacro {

    private static final int macroId=0;
    private static final boolean macroTakesArgs=false;
    private static final String macroName=BotBaseMacro.MACRO_SIGNAL+"everyone";
    private String[] macroArgs;

    public BotMacroEveryone() {
        super(macroId, macroTakesArgs, macroName);
    }

    @Override
    public String applyMacro(CustomBot botInfo, TelegramUpdate telegramUpdate) {
        String result=null;
        TelegramMethodGetChatAdmins telegramMethodGetChatAdmins=new TelegramMethodGetChatAdmins();
        telegramMethodGetChatAdmins.updateFieldValue(TelegramMethodGetChatAdmins.CHAT_ID,telegramUpdate.getMessage().getChat().getId());
        TelegramSyncHTTPSRequester requester=new TelegramSyncHTTPSRequester(botInfo,telegramMethodGetChatAdmins);
        try {
            String s=requester.sendRequest();
            //System.out.println(s);
            Gson gson=new Gson();
            TelegramChatMembersInfoResponse chatMembersInfo=gson.fromJson(s, TelegramChatMembersInfoResponse.class);
            result="";
            for (int i=0;i<chatMembersInfo.getResult().size();i++){
                if (!chatMembersInfo.getResult().get(i).getUser().isBot()){
                    result+="@"+chatMembersInfo.getResult().get(i).getUser().getFirstName()+" ";
                }
            }
            result=telegramUpdate.getMessage().getText().replaceAll(macroName,result);
            //System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
