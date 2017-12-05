package RollCallTelBot;

import org.telegram.telegrambots.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBoti extends TelegramLongPollingBot {
    Map<Integer,String> step = new HashMap<>();
    Map<Integer,ArrayList<String>> mutual = new HashMap<>();
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            Message message = update.getMessage();
            if (message.getText().equals("/start")){
//                SendMessage messageToPer = new SendMessage();
//                messageToPer.setChatId(message.getChatId());
//                messageToPer.setText("به ربیاب خوش آمدید . برای استفاده از این ربات ابتدا با دستورات حضور غیاب خوش آمدید . برای استفاده از این ربات ابتدا با دستور /new  اطلاعات خود را ذخیره کنید.");
//                messageToPer.setReplyToMessageId(message.getMessageId());
//                try {
//                    sendMessage(messageToPer);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
                sendToPerson(message,"به ربات ما خوش آمدید . برایاستفاده از آن از دستور /new  استفاده کنید.",true);
            }
            if (message.getText().startsWith("/new")){
                step.put(message.getFrom().getId(),"getChannel");
                sendToPerson(message,"حالا ایدی کانال خود را بفرستید . دقت کنید ربات را باید در کانال خود ادمین کنید.",true);

            }

            if (step.get(message.getFrom().getId())!=null){
                String value = step.get(message.getFrom().getId());

                if (value.equals("getChannel")){
                    if (message.getText().contains("@")){
                        GetChat getChat = new GetChat(message.getText());
                        try {
                            getChat(getChat);
                            ArrayList<String> channels = mutual.get(message.getFrom().getId());
                            if (channels==null){
                                channels = new ArrayList<>();
                            }
                            channels.add(message.getText());
                            mutual.put(message.getFrom().getId(),channels);
                            step.remove(message.getFrom().getId());
                        } catch (TelegramApiException e) {
                            sendToPerson(message,,"این کانال موجود نیست",true);
                        }
                    }
                }
            }
        }


    }

    @Override
    public String getBotUsername() {
        return "حضور غیاب";
    }

    @Override
    public String getBotToken() {
        return null;
    }

    public void sendToPerson(Message message,String text,Boolean reply){
        SendMessage messageToPer = new SendMessage();
        messageToPer.setChatId(message.getChatId());
        messageToPer.setText(text);
        if (reply)
        messageToPer.setReplyToMessageId(message.getMessageId());
        try {
            sendMessage(messageToPer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
