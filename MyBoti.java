package RollCallTelBot;

import org.telegram.telegrambots.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBoti extends TelegramLongPollingBot {
    Map<Integer,String> step = new HashMap<>();
    Map<Integer,ArrayList<ChanGroup>> mutual = new HashMap<>();
    Map<Student,String> allStudents  = new HashMap<>();
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            ArrayList<ChanGroup> chanGroups=mutual.get(callbackQuery.getFrom().getId());

            for (int i = 0; i <chanGroups.size() ; i++) {
                if (chanGroups.get(i).getId().equals(callbackQuery.getData())){
                    sendToChanGroup(callbackQuery.getData());
                }
            }
        }




        if (update.hasMessage()){
            Message message = update.getMessage();



            if (message.getText().startsWith("/start") && !message.getText().equals("/start"));
            {
                String step = allStudents.get(new Student(message.getChatId()));
                if (step==null){
                    allStudents.put(new Student(message.getChatId()),"set");
                    sendToPerson(message,"please send us your student id",true);
                }
            }

            if (allStudents.get(new Student(message.getChatId())).equals("set")){
                if (message.getText().matches("\\d+"))
                {
                    allStudents.remove(new Student(message.getChatId()));
                    allStudents.put(new Student(message.getChatId(),Integer.parseInt(message.getText()),message.getFrom().getFirstName()),"ok");
                    sendToPerson(message,"successfully confirmed",false);
                }
                else {
                    sendToPerson(message,"please enter valid student id",true);

                }

            }





            if(message.getText().startsWith("/call")){
                ArrayList<ChanGroup> chanGroup = mutual.get(message.getFrom().getId());
                if (chanGroup!=null){
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> raws = new ArrayList<>();
                    for (int i = 0; i <chanGroup.size() ; i++) {
                        List<InlineKeyboardButton> buttons = new ArrayList<>();
                        InlineKeyboardButton button = new InlineKeyboardButton(chanGroup.get(i).getName());
                        button.setCallbackData(chanGroup.get(i).getId());
                        buttons.add(button);
                        raws.add(buttons);

                    }
                    inlineKeyboardMarkup.setKeyboard(raws);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("در کجا قرار دهیم ؟");
                    sendMessage.setChatId((message.getChatId()));
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        sendMessage(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }





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
                            ArrayList<ChanGroup> channels = mutual.get(message.getFrom().getId());
                            if (channels==null){
                                channels = new ArrayList<>();
                            }
                            channels.add(new ChanGroup(message.getText().substring(1),message.getText()));
                            mutual.put(message.getFrom().getId(),channels);
                            step.remove(message.getFrom().getId());
                            sendToPerson(message,"با موفقیت اضافه شد",true);
                        } catch (TelegramApiException e) {
                            sendToPerson(message,"این کانال موجود نیست",true);
                        }
                    }
                }
            }
        }


    }

    private void sendToChanGroup(String data) {
        String text = "";
        SendMessage sendMessage = new SendMessage(data,text);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(new InlineKeyboardButton("حاضر").setUrl(""));
        rows.add(row);
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "Callrollbot";
    }

    @Override
    public String getBotToken() {
        return "481448295:AAE1Zuevp8EVxc16X6vHIfppBsUF2OaMNWA";
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
